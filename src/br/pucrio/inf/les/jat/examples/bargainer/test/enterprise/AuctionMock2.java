package br.pucrio.inf.les.jat.examples.bargainer.test.enterprise;

import java.util.StringTokenizer;

import br.pucrio.inf.les.jat.core.JadeMockAgent;
import br.pucrio.inf.les.jat.core.exception.ComparisonContentFailure;
import br.pucrio.inf.les.jat.core.exception.ReplyReceptionFailed;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class AuctionMock2 extends JadeMockAgent {

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
				
				msg = blockReceiveMessage(6000, ACLMessage.INFORM);
				
				StringTokenizer st = new StringTokenizer(msg.getContent(),";");

				String nomeEmpresa = st.nextToken();
				nomeEmpresa = nomeEmpresa.substring(nomeEmpresa.indexOf(":") + 1,nomeEmpresa.length());

				String componente = st.nextToken();
				componente = componente.substring(componente.indexOf(":") + 1,componente.length());

				String preco = st.nextToken();
				preco = preco.substring(preco.indexOf(":") + 1,preco.length());
				
				asserts.assertEquals(enterprises, nomeEmpresa);
				asserts.assertEquals(componentName, componente);
								
				setTestResult("OK");
			} catch (ReplyReceptionFailed e) {
				setTestResult(prepareMessageResult(e));
			} catch (ComparisonContentFailure e) {
				setTestResult(prepareMessageResult(e));
			}
		}

	}
}
