package br.pucrio.inf.les.jat.examples.bargainer;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.StringTokenizer;

public class EnterpriseAgent extends Agent {

	private static final long serialVersionUID = 1L;
	private DFAgentDescription dfd = null;
	private float initialComponentPrice;
	private float minComponentPrice = 5f;
	private String name;
	private String component;
	
	public EnterpriseAgent() {
		
	}

	public float getInitialComponentPrice() {
		return initialComponentPrice;
	}

	public float getMinComponentPrice() {
		return minComponentPrice;
	}

	protected void setup() {

		Object[] args = getArguments();

		try {
			this.dfd = new DFAgentDescription();
			this.dfd.setName(getAID());
			ServiceDescription sd = new ServiceDescription();
			sd.setName((String)args[0]);
			sd.setType("agenteEmpresa");
			this.dfd.addServices(sd);

			DFService.register(this, this.dfd);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}

		this.name = (String)args[0];
		this.component = (String)args[1];
		this.initialComponentPrice = Float.valueOf((String)args[2]);
		this.minComponentPrice = Float.valueOf((String)args[3]);
		
		System.out.println(this.getAID().getName());

		addBehaviour(new MainBehaviour(this));
	}

	protected void takeDown() {
		try {
			DFService.deregister(this);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}		
	}

	private boolean offer(String component) {

		if (this.component.equalsIgnoreCase(component)) {
			return true;
		}

		return false;
	}
	
	private class MainBehaviour extends CyclicBehaviour {

		private EnterpriseAgent agent = null;

		public MainBehaviour(EnterpriseAgent agent) {
			super(agent);
			this.agent = agent;
		}

		public void action() {

			ACLMessage msgPropose = receive(MessageTemplate.MatchPerformative(ACLMessage.PROPOSE));
			
			if ( msgPropose != null ) {
				String content = msgPropose.getContent();
				String componentReceived = content.substring(content.indexOf(":") + 1,content.length());

				//TODO Verificar se a empresa oferece esse produto
				if ( offer(componentReceived) ) {
					
					String replyWith = msgPropose.getReplyWith();
					
					ACLMessage msgACCEPT = msgPropose.createReply();
					msgACCEPT.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
					msgACCEPT.setSender(getAID());
					msgACCEPT.setContent("PARTICIPO:" + agent.getLocalName() + ";COMPONENTE:" + componentReceived);
										
					send(msgACCEPT);
					
					addBehaviour(new NegociationBehaviour(agent,replyWith,componentReceived));
				}			

			} else { 
				block();
			}
		}
	}

	private class NegociationBehaviour extends SimpleBehaviour {

		private EnterpriseAgent agent = null;
		private String component;
		private boolean replay = false;
		private String replyWith = "";
		private double actualPrice = 0f; 

		private boolean done = false;

		public NegociationBehaviour(EnterpriseAgent agent,String replyWith,String component) {
			super(agent);
			this.agent = agent;
			this.component = component;
			this.replyWith = replyWith;
			this.actualPrice = initialComponentPrice;
		}

		public void action() {

			MessageTemplate performativeTemplate = MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.CFP), MessageTemplate.MatchPerformative(ACLMessage.CANCEL));
			MessageTemplate template = MessageTemplate.and(MessageTemplate.MatchReplyWith(replyWith), performativeTemplate);
			ACLMessage msg = receive(performativeTemplate);
					
			if ( msg != null ) {
				if ( msg.getPerformative() == ACLMessage.CFP) {
					
					if ( !replay ) {
						
						//Se o conteúdo da mensagem estiver errado pode ocorrer um erro aqui.
						StringTokenizer st = new StringTokenizer(msg.getContent(),";");

						String componentName = st.nextToken();
						componentName = componentName.substring(componentName.indexOf(":") + 1,componentName.length());

						String price = st.nextToken();
						price = price.substring(price.indexOf(":") + 1,price.length());
						
						if ( Float.valueOf(price) > this.agent.getInitialComponentPrice() ) {
							String content = "EMPRESA:" + agent.getLocalName() + ";COMPONENTE:" + this.component + ";PRECO:" + this.agent.getInitialComponentPrice();
							ACLMessage msgInform = msg.createReply();
							msgInform.setPerformative(ACLMessage.INFORM);
							msgInform.setContent(content);
							msgInform.setSender(getAID());
							
							send(msgInform);
						}

						replay = true;
						
					} else {
						//TODO Tratar uma segunda requisição de preço de Bargainer para um produto.
						
						double temp = actualPrice;
						temp = temp * 0.1;
						actualPrice -= temp;
						
						//Se o conteúdo da mensagem estiver errado pode ocorrer um erro aqui.
						StringTokenizer st = new StringTokenizer(msg.getContent(),";");

						String componentName = st.nextToken();
						componentName = componentName.substring(componentName.indexOf(":") + 1,componentName.length());

						String price = st.nextToken();
						price = price.substring(price.indexOf(":") + 1,price.length());
						
						if ( actualPrice > this.agent.getMinComponentPrice() && actualPrice < Double.valueOf(price)) {						
							String content = "EMPRESA:" + agent.getLocalName() + ";COMPONENTE:" + this.component + ";PRECO:" + actualPrice;
							ACLMessage msgInform = msg.createReply();
							msgInform.setPerformative(ACLMessage.INFORM);
							msgInform.setContent(content);
							msgInform.setSender(getAID());

							send(msgInform);
						}
					}
				} else if ( msg.getPerformative() == ACLMessage.CANCEL ) {
					done = true;
				}
			} else {
				block();
			}			
		}

		public boolean done() {
			return done;
		}
	}
}
