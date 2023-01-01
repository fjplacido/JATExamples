package br.pucrio.inf.les.jat.examples.trading.test.bookseller;

import br.pucrio.inf.les.jat.core.JadeMockAgent;
import br.pucrio.inf.les.jat.core.exception.ReplyReceptionFailed;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class BookBuyerMock4 extends JadeMockAgent {

	private String targetBookTitle;
	private AID[] sellerAgents;

	protected void setup() {	
		targetBookTitle = "terra";	
		sellerAgents = new AID[1];
		sellerAgents[0] = new AID("vendedor",AID.ISLOCALNAME);
		addBehaviour(new RequestPerformer());
	}

	protected void takeDown() {
		System.out.println("Buyer-agent " + getAID().getName() + " terminating.");
	}

	private class RequestPerformer extends OneShotBehaviour {

		public void action(){

			try {
				sendMessage(ACLMessage.ACCEPT_PROPOSAL, sellerAgents[0], targetBookTitle);
				blockReceiveMessage(10000,ACLMessage.INFORM);
				
				setTestResult("OK");
			} catch (ReplyReceptionFailed e) {
				setTestResult(prepareMessageResult(e));
			}
		}
	}
}
