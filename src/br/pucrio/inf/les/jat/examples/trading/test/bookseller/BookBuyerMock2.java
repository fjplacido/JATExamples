package br.pucrio.inf.les.jat.examples.trading.test.bookseller;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import br.pucrio.inf.les.jat.core.JadeMockAgent;
import br.pucrio.inf.les.jat.core.exception.ReplyReceptionFailed;

@SuppressWarnings("serial")
public class BookBuyerMock2 extends JadeMockAgent {

	private String targetBookTitle;
	private AID[] sellerAgents;

	protected void setup() {

		Object[] args = getArguments();

		targetBookTitle = (String) args[0];
		sellerAgents = new AID[1];
		sellerAgents[0] = new AID("vendedor", AID.ISLOCALNAME);
		addBehaviour(new RequestPerformer());
	}

	protected void takeDown() {
		System.out.println("Buyer-agent " + getAID().getName()
				+ " terminating.");
	}

	private class RequestPerformer extends OneShotBehaviour {

		public void action() {

			try {

				sendMessage(ACLMessage.CFP, sellerAgents[0], targetBookTitle);
				blockReceiveMessage(30000, ACLMessage.REFUSE);

				setTestResult("OK");
			} catch (ReplyReceptionFailed e) {
				setTestResult(prepareMessageResult(e));
			}
		}
	}
}
