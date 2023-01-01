package br.pucrio.inf.les.jat.examples.bargainer.test.enterprise;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import br.pucrio.inf.les.jat.core.JadeMockAgent;
import br.pucrio.inf.les.jat.core.exception.ComparisonContentFailure;
import br.pucrio.inf.les.jat.core.exception.ReplyReceptionFailed;
import br.pucrio.inf.les.jat.examples.bargainer.Componente;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class BargainerMock2 extends JadeMockAgent {

	private List<Componente> componentes = null;
		
	public BargainerMock2() {
		this.componentes = new ArrayList<Componente>();		
	}
	
	protected void setup() {
		
		Object[] args = getArguments();
		
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
		
		addBehaviour(new TestBehaviour());
	}

	protected void takeDown() {

	}

	private class TestBehaviour extends OneShotBehaviour {

		public TestBehaviour() {

		}

		public void action() {
			ACLMessage msg;
			
			try {
				
				DFAgentDescription template = new DFAgentDescription();
				ServiceDescription templateSd = new ServiceDescription();
				templateSd.setType("agenteEmpresa");
				template.addServices(templateSd);	  		

				DFAgentDescription[] agentsDescription = DFService.search(myAgent, template);
				
				msg = new ACLMessage(ACLMessage.PROPOSE);
				msg.setSender(getAID());
				
				for ( int x = 0; x < agentsDescription.length; x++ ) {
					msg.addReceiver(agentsDescription[x].getName());
				}

				for ( Componente componente: componentes ) {
					msg.setContent("COMPONENTE:" + componente.getNome());
					msg.setReplyWith("CFP" + System.currentTimeMillis());
					send(msg);
				}
				
				for ( int x = 0; x < 3; x++ ) {
					msg = blockReceiveMessage(6000,ACLMessage.ACCEPT_PROPOSAL);
					
					StringTokenizer st = new StringTokenizer(msg.getContent(),";");

					String enterprise = st.nextToken();
					enterprise = enterprise.substring(enterprise.indexOf(":") + 1,enterprise.length());

					String component = st.nextToken();
					component = component.substring(component.indexOf(":") + 1,component.length());
					
					if ( msg.getSender().getLocalName().equalsIgnoreCase("enterpriseOne") ) {
						asserts.assertEquals("Milk",component);
					} else if ( msg.getSender().getLocalName().equalsIgnoreCase("enterpriseTwo") ) {
						asserts.assertEquals("Flour", component);
					} else if ( msg.getSender().getLocalName().equalsIgnoreCase("enterpriseTree") ) {
						asserts.assertEquals("Egg",component);
					}
					
					AgentController agentController = getContainerController().createNewAgent("auction_" + component, "br.pucrio.inf.les.jat.examples.bargainer.test.enterprise.AuctionMock2" , new Object[] {getAID(),enterprise,component,100.0f,msg.getReplyWith()});
					agentController.start();
				}
								
				setTestResult("OK");
								
			} catch (ReplyReceptionFailed e) {
				setTestResult(prepareMessageResult(e));
			} catch (FIPAException e) {
				setTestResult(prepareMessageResult(e));
			} catch (ComparisonContentFailure e) {
				setTestResult(prepareMessageResult(e));
			} catch (StaleProxyException e) {
				setTestResult(prepareMessageResult(e));
			}		
		}
	}
}
