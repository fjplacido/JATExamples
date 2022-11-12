package br.pucrio.inf.les.jat.examples.trading.test.bookseller;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import br.pucrio.inf.les.jat.core.JadeSynchronizedMockAgent;

public class BookBuyerSynchronizerMock1 extends JadeSynchronizedMockAgent {
	
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
		
		public void action() {

			try {
				sendMessage(ACLMessage.CFP, sellerAgents[0], targetBookTitle );
				ACLMessage reply = blockReceiveMessage(10000, ACLMessage.PROPOSE);
				
				//verifica conteudo da mensagem
				int price = Integer.parseInt(reply.getContent());
				asserts.assertEquals(2, price);
				sendMessage(ACLMessage.ACCEPT_PROPOSAL, sellerAgents[0] , targetBookTitle);
				
				reply = blockReceiveMessage(10000, ACLMessage.FAILURE);
						
				setTestResult("OK");
			} catch(NumberFormatException e){
				setTestResult(prepareMessageResult(e));
			} catch (Exception e) {
				setTestResult(prepareMessageResult(e));
				e.printStackTrace();
			}
		}
	}
}
