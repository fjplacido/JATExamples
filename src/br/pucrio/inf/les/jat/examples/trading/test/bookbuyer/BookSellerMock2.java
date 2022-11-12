package br.pucrio.inf.les.jat.examples.trading.test.bookbuyer;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.Hashtable;

import br.pucrio.inf.les.jat.core.JadeMockAgent;

public class BookSellerMock2 extends JadeMockAgent {

	private Hashtable catalogue;
	private AID[] sellerAgents;


	// Put agent initializations here
	protected void setup() {

		catalogue = new Hashtable();

		String targetBookTitle = null;
		int targetBookPrice = 0;

		Object[] args = getArguments();

		if (args != null && args.length >= 2) {
			try{
				targetBookTitle = (String) args[0];
				targetBookPrice = Integer.parseInt((String) args[1]);
				catalogue.put(targetBookTitle, new Integer(targetBookPrice));
			}catch(NumberFormatException ex){
				ex.printStackTrace(); 
			}
		}

		addBehaviour(new RequestPerformer());
	}

	protected void takeDown() {

	}

	private class RequestPerformer extends OneShotBehaviour {


		public void action(){

			try {

				sendMessage(ACLMessage.PROPOSE, new AID("comprador",false), String.valueOf(catalogue.get("terra")));

				ACLMessage reply = blockingReceive(10000);

				asserts.assertNull(reply);

				setTestResult("OK");

			} catch(NumberFormatException ee) {
				setTestResult(prepareMessageResult(ee));
				return;
			} catch (Exception e) {
				setTestResult(prepareMessageResult(e));
				e.printStackTrace();
				return;
			}
		}
	}
}