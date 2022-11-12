package br.pucrio.inf.les.jat.examples.trading.test.bookseller;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import br.pucrio.inf.les.jat.core.JadeMockAgent;

public class BookBuyerMock5 extends JadeMockAgent {

	private String targetBookTitle;
	private AID[] sellerAgents;

	protected void setup() {
		targetBookTitle = "terra";	
		sellerAgents = new AID[1];
		sellerAgents[0] = new AID("vendedor",AID.ISLOCALNAME);
		addBehaviour(new RequestPerformer());
	}

	protected void takeDown() {
		System.out.println("Buyer-agent "+getAID().getName()+" terminating.");
	}

	private class RequestPerformer extends OneShotBehaviour {

		public void action(){

			try {

				sendMessage(ACLMessage.CFP, sellerAgents[0], targetBookTitle);
				ACLMessage reply = blockReceiveMessage(6000, ACLMessage.PROPOSE);

				int price = Integer.parseInt(reply.getContent());
				asserts.assertEquals(2, price);

				sendMessage(ACLMessage.ACCEPT_PROPOSAL, sellerAgents[0] , targetBookTitle);

				reply = blockReceiveMessage(6000, ACLMessage.INFORM);

				setTestResult("OK");

			} catch(NumberFormatException e) {
				setTestResult(prepareMessageResult(e));
			} catch (Exception e) {
				setTestResult(prepareMessageResult(e));
			}
		}
	}
}