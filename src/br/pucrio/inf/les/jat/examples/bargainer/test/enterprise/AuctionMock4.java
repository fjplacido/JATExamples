package br.pucrio.inf.les.jat.examples.bargainer.test.enterprise;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.StringTokenizer;

import br.pucrio.inf.les.jat.core.JadeMockAgent;
import br.pucrio.inf.les.jat.core.exception.ComparisonContentFailure;
import br.pucrio.inf.les.jat.core.exception.ReplyReceptionFailed;

public class AuctionMock4 extends JadeMockAgent {

	private AID bargainer;
	private String enterprises;
	private String componentName;
	private float initialPrice;
	private String replayWith;
	private float bestPrice;
	private String bestEnterprise = "";

	protected void setup() {

		Object[] args = getArguments();
		this.bargainer = (AID)args[0];
		this.enterprises = (String)args[1];
		this.componentName = (String)args[2];
		this.initialPrice = (Float)args[3];
		this.replayWith = (String)args[4];
		this.bestPrice = this.initialPrice;
		
		System.out.println(this.getAID().getName());

		addBehaviour(new TestBehaviour());
	}

	protected void takeDown() {

	}

	private class TestBehaviour extends OneShotBehaviour {

		public TestBehaviour() {

		}

		@Override
		public void action() {

			ACLMessage msg;
			
			try {
				ACLMessage msgCFP = new ACLMessage(ACLMessage.CFP);
				msgCFP.setSender(getAID());
				msgCFP.setContent("COMPONENTE:" + componentName + ";PRECO" + bestPrice);
				msgCFP.addReceiver(new AID(enterprises,AID.ISLOCALNAME));
				msgCFP.setReplyWith(replayWith);
				send(msgCFP);
				
				msg = blockReceiveMessage(10000, ACLMessage.INFORM);
				
				StringTokenizer st = new StringTokenizer(msg.getContent(),";");

				String nomeEmpresa = st.nextToken();
				nomeEmpresa = nomeEmpresa.substring(nomeEmpresa.indexOf(":") + 1,nomeEmpresa.length());

				String componente = st.nextToken();
				componente = componente.substring(componente.indexOf(":") + 1,componente.length());

				String preco = st.nextToken();
				preco = preco.substring(preco.indexOf(":") + 1,preco.length());
				
				asserts.assertEquals(enterprises, nomeEmpresa);
								
				if ( msg.getSender().getLocalName().equalsIgnoreCase("enterpriseOne") ) {
					asserts.assertEquals("15.0", preco);					
				} else if ( msg.getSender().getLocalName().equalsIgnoreCase("enterpriseTwo") ) {
					asserts.assertEquals("20.0", preco);
				} else if ( msg.getSender().getLocalName().equalsIgnoreCase("enterpriseThree") ) {
					asserts.assertEquals("10.0", preco);
				}
				
				msgCFP = new ACLMessage(ACLMessage.CFP);
				msgCFP.setSender(getAID());
				msgCFP.setContent("COMPONENTE:" + componentName + ";PRECO:" + 2);
				msgCFP.addReceiver(new AID(enterprises,AID.ISLOCALNAME));
				msgCFP.setReplyWith(replayWith);
				send(msgCFP);
				
				msg = blockReceiveMessage(10000, ACLMessage.INFORM);
				
				st = new StringTokenizer(msg.getContent(),";");

				nomeEmpresa = st.nextToken();
				nomeEmpresa = nomeEmpresa.substring(nomeEmpresa.indexOf(":") + 1,nomeEmpresa.length());

				componente = st.nextToken();
				componente = componente.substring(componente.indexOf(":") + 1,componente.length());

				preco = st.nextToken();
				preco = preco.substring(preco.indexOf(":") + 1,preco.length());
				
				asserts.assertEquals(enterprises, nomeEmpresa);
				
				if ( msg.getSender().getLocalName().equalsIgnoreCase("enterpriseOne") ) {
					asserts.assertEquals("13.5", preco);
				} else if ( msg.getSender().getLocalName().equalsIgnoreCase("enterpriseTwo") ) {
					asserts.assertEquals("18.0", preco);
				} else if ( msg.getSender().getLocalName().equalsIgnoreCase("enterpriseThree") ) {					
					asserts.assertEquals("9.0", preco);
				}
				
				ACLMessage msgCANCEL = new ACLMessage(ACLMessage.CANCEL);
				msgCANCEL.setSender(getAID());
				msgCANCEL.addReceiver(new AID(enterprises,AID.ISLOCALNAME));
				msgCANCEL.setReplyWith(replayWith);
				send(msgCANCEL);
								
				setTestResult("OK");
			} catch (ReplyReceptionFailed e) {
				setTestResult(prepareMessageResult(e));
			} catch (ComparisonContentFailure e) {
				setTestResult(prepareMessageResult(e));
			}
		}

	}
}
