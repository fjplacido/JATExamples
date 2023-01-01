package br.pucrio.inf.les.jat.examples.bargainer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;


public class BargainerAgent extends Agent {

	private static final long serialVersionUID = 1L;	
	private List<Componente> componentes = null;
	private Map<String,String> companyList;
	private String mock;

	public BargainerAgent() {		
		this.componentes = new ArrayList<Componente>();		
	}

	protected void setup() {

		Object[] args = getArguments();

		this.mock = (String)args[0];

		Componente component = new Componente();
		component.setIdComponente(0);
		component.setNome("Milk");

		this.componentes.add(component);

		component = new Componente();
		component.setIdComponente(1);
		component.setNome("Flour");

		this.componentes.add(component);

		component = new Componente();
		component.setIdComponente(2);
		component.setNome("Egg");

		this.componentes.add(component);

		addBehaviour(new MainBehaviour(this));
	}

	protected void takeDown() {
		System.out.println(getName() + " exiting ...");		
	}

	private class MainBehaviour extends OneShotBehaviour {

		private Agent agent = null;

		public MainBehaviour(Agent agent) {
			super(agent);
			this.agent = agent;
		}

		public void action() {

			try {

				//Obtem todos os agentes empresas registrados.
				DFAgentDescription template = new DFAgentDescription();
				ServiceDescription templateSd = new ServiceDescription();
				templateSd.setType("agenteEmpresa");
				template.addServices(templateSd);	  		

				DFAgentDescription[] agentsDescription = DFService.search(agent, template);

				//Envia uma proposta para todos os agentes cadastrados
				sendProposal(agentsDescription);
				
				//Recebe as respostas.
				Map<String, List<Response>> respostasAnuncio = readAcceptProposal();

				//Inicia os acutionAgents
				startAcutions(respostasAnuncio);

				//Recebe as respostas dos acutionAgents
				companyList = receiveAuctionsInform(respostasAnuncio);

				System.out.println(companyList.values());				
			} catch (FIPAException e) {
				e.printStackTrace();
			}			
		}		
	}

	//Se agentsDescription for null pode ocorrer um erro aqui
	private void sendProposal(DFAgentDescription[] agentsDescription)  {
		
		ACLMessage msg = new ACLMessage(ACLMessage.PROPOSE);
		msg.setSender(getAID());

		for ( int x = 0; x < agentsDescription.length; x++ ) {
			msg.addReceiver(agentsDescription[x].getName());
		}

		for ( Componente componente: this.componentes ) {
			msg.setContent("COMPONENTE:" + componente.getNome());
			msg.setReplyWith("CFP" + System.currentTimeMillis());
			send(msg);
		}
	}

	private Map<String,List<Response>> readAcceptProposal() {
		
		Map<String,List<Response>> respostasAnuncio = new HashMap<String, List<Response>>();

		ACLMessage msgAcceptProposal = this.blockingReceive(MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL),10000);

		while ( msgAcceptProposal != null ) {
			
			//Se o conte�do da mensagem estiver errado pode ocorrer um erro n�o verificado aqui.
			//PARTICIPO: SENDAS;COMPONENTE:REFRIGERANTE COCA-COLA
			System.out.println(getAID().getLocalName() + " -> De: " + msgAcceptProposal.getSender().getLocalName() + " - Conte�do: " + msgAcceptProposal.getContent());

			StringTokenizer st = new StringTokenizer(msgAcceptProposal.getContent(),";");

			String enterprise = st.nextToken();
			enterprise = enterprise.substring(enterprise.indexOf(":") + 1,enterprise.length());

			String component = st.nextToken();
			component = component.substring(component.indexOf(":") + 1,component.length());
			
			Response response = new Response();
			response.setAgent(msgAcceptProposal.getSender());
			response.setReplayWith(msgAcceptProposal.getReplyWith());

			if ( respostasAnuncio.containsKey(component) ) {
				respostasAnuncio.get(component).add(response);				
			}
			else {
				List<Response> enterprises = new ArrayList<Response>();
				enterprises.add(response);
				respostasAnuncio.put(component,enterprises);
			}
			
			msgAcceptProposal = this.blockingReceive(MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL),10000);
		}
		
		return respostasAnuncio;
	}

	//Se respostasAnuncio for null pode ocorrer um erro aqui.
	private void startAcutions(Map<String,List<Response>> respostasAnuncio) {
		
		for ( String component : respostasAnuncio.keySet() ) {

			List<Response> enterprises = respostasAnuncio.get(component);
			String acutionAgentName = "auction_" + this.getLocalName() + "_" + component;
			
			try {
				//Se a variavel mock for null pode ocorrer um erro aqui.
				AgentController agentController = this.getContainerController().createNewAgent(acutionAgentName, mock , new Object[] {this.getAID(),enterprises,component,100.0f});
				agentController.start();
			} catch (StaleProxyException e) {
				e.printStackTrace();
			}			
		}
	}

	//Se respostasAnuncio for null pode ocorrer um erro aqui.
	private Map<String,String> receiveAuctionsInform(Map<String,List<Response>> respostasAnuncio) {
				
		Map<String,String> bestSellers = new HashMap<String, String>();
		
		for ( int x = 0; x < respostasAnuncio.size(); x++ ) {

			MessageTemplate messageTemplate = MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.INFORM), MessageTemplate.MatchPerformative(ACLMessage.FAILURE));
			ACLMessage msg = blockingReceive(messageTemplate);

			if ( msg != null ) {				
				if ( msg.getPerformative() == ACLMessage.INFORM) {
					
//					Se o conte�do da mensagem estiver errado pode ocorrer um erro n�o verificado aqui.
					StringTokenizer st = new StringTokenizer(msg.getContent(),";");

					String component = st.nextToken();
					component = component.substring(component.indexOf(":") + 1,component.length());

					String enterprise = st.nextToken();
					enterprise = enterprise.substring(enterprise.indexOf(":") + 1,enterprise.length());

					String price = st.nextToken();
					price = price.substring(price.indexOf(":") + 1 ,price.length());

					bestSellers.put(component, enterprise);
				}
			}	
		}
		
		return bestSellers;
	}
}
