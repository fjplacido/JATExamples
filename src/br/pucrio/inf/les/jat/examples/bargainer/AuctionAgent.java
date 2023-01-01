package br.pucrio.inf.les.jat.examples.bargainer;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class AuctionAgent extends Agent {

	private AID bargainer;
	private List<Response> enterprises;
	private List<Response> respEnterprises;
	private String componentName;
	private float initialPrice;
	private float bestPrice;
	private String bestEnterprise = "";
	public AuctionAgent() {
		this.respEnterprises = new ArrayList<Response>();
	}

	protected void setup() {

		Object[] args = getArguments();
		this.bargainer = (AID)args[0];
		this.enterprises = (List<Response>)args[1];
		this.componentName = (String)args[2];
		this.initialPrice = (Float)args[3];
		this.bestPrice = this.initialPrice;
		this.respEnterprises.addAll(this.enterprises);
		
		addBehaviour(new MainBehaviour(this));
	}

	protected void  takeDown() {
		System.out.println(getName() + " exiting ...");		
	}

	public String getEnterprise() {
		return this.bestEnterprise;
	}

	private class MainBehaviour extends SimpleBehaviour {

		private AuctionAgent agent = null;
		private boolean done = false;

		public MainBehaviour(AuctionAgent agent) {
			super(agent);
			this.agent = agent;
		}

		public void action() {

			//Response pode ser null
						
			for ( Response response : respEnterprises ) {
				ACLMessage msgCFP = new ACLMessage(ACLMessage.CFP);
				msgCFP.setSender(agent.getAID());
				msgCFP.setContent("COMPONENTE:" + componentName + ";PRECO:" + bestPrice);
				msgCFP.addReceiver(response.getAgent());
				msgCFP.setReplyWith(response.getReplayWith());
				
				System.out.println("CFP: " + response.getReplayWith());
				
				send(msgCFP);
			}

			int resps = respEnterprises.size();
			respEnterprises.clear();

			for ( int x = 0; x < resps; x++ ) {

				MessageTemplate informTamplate = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
				ACLMessage msgInform = blockingReceive(informTamplate,60000);

				if ( msgInform == null ) {
					break;
				}

				System.out.println(getAID().getLocalName() + " -> De: " + msgInform.getSender().getLocalName() + " - Conte�do: " + msgInform.getContent());
				
				//Se o conte�do da mesagem estiver errado podemos encontrar erros n�o resolvidos aqui.
				StringTokenizer st = new StringTokenizer(msgInform.getContent(),";");

				String nomeEmpresa = st.nextToken();
				nomeEmpresa = nomeEmpresa.substring(nomeEmpresa.indexOf(":") + 1,nomeEmpresa.length());

				String componente = st.nextToken();
				componente = componente.substring(componente.indexOf(":") + 1,componente.length());

				String preco = st.nextToken();
				preco = preco.substring(preco.indexOf(":") + 1,preco.length());

				if ( Float.valueOf(preco) < bestPrice ) {
					bestPrice = Float.valueOf(preco);
					bestEnterprise = nomeEmpresa;
				}
				
				Response response = new Response();
				response.setAgent(msgInform.getSender());
				response.setReplayWith(msgInform.getReplyWith());
				respEnterprises.add(response);
			}

			if ( respEnterprises.size() == 0 ) {
				
				for ( Response response : enterprises ) {
					ACLMessage msgCancel = new ACLMessage(ACLMessage.CANCEL);
					msgCancel.setSender(getAID());
					msgCancel.setReplyWith(response.getReplayWith());
					msgCancel.addReceiver(response.getAgent());
									
					send(msgCancel);
				}
				
				if ( bestEnterprise.length() > 0 ) {
					ACLMessage msgInformBargainer = new ACLMessage(ACLMessage.INFORM);
					msgInformBargainer.setSender(agent.getAID());
					msgInformBargainer.setContent("COMPONENTE:" + componentName + ";EMPRESA:" + bestEnterprise + ";PRECO:" + bestPrice);
					msgInformBargainer.addReceiver(bargainer);

					send(msgInformBargainer);
				} else {
					ACLMessage msgFailure = new ACLMessage(ACLMessage.FAILURE);
					msgFailure.setSender(agent.getAID());
					msgFailure.setContent("COMPONENTE:" + componentName);
					msgFailure.addReceiver(bargainer);

					send(msgFailure);
				}

				done = true;
			}
		}

		public boolean done() {
			return done;
		}
	}
}
