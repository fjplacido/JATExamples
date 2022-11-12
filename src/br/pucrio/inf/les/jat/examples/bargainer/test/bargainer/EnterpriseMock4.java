package br.pucrio.inf.les.jat.examples.bargainer.test.bargainer;

import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import br.pucrio.inf.les.jat.core.JadeMockAgent;
import br.pucrio.inf.les.jat.core.exception.ReplyReceptionFailed;

public class EnterpriseMock4 extends JadeMockAgent {
	
	private String nome;
	private float preco = 0;
	private String componente;
	private DFAgentDescription dfd = null;

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

		nome = (String)args[0];
		componente = (String)args[1];
		preco = Float.valueOf((String)args[2]);

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

	private class MainBehaviour extends OneShotBehaviour {

		private EnterpriseMock4 agent;

		public MainBehaviour(EnterpriseMock4 agent) {
			this.agent = agent;
		}

		@Override
		public void action() {
			
			try {
				for ( int x = 0; x < 3; x++ ) {
					ACLMessage msgPropose = blockReceiveMessage(60000, ACLMessage.PROPOSE);

					String componenteReceived = msgPropose.getContent().substring(msgPropose.getContent().indexOf(":") + 1,msgPropose.getContent().length());

					if ( componenteReceived.equalsIgnoreCase(agent.componente) ) {
						String content  = "PARTICIPO:" + agent.nome + ";COMPONENTE:" + agent.componente;
						ACLMessage reply = msgPropose.createReply();
						reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
						reply.setContent(content);
						
						send(reply);						
					}
				}
															
				setTestResult("OK");
				
			} catch (ReplyReceptionFailed e) {
				setTestResult(prepareMessageResult(e));
			}
		}
	}
}
