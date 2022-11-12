package br.pucrio.inf.les.jat.examples.trading.test.bookbuyer;

import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

import java.util.Hashtable;

import br.pucrio.inf.les.jat.core.JadeMockAgent;

public class BookSellerMock32 extends JadeMockAgent {

	private Hashtable catalogue;

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
		
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("book-selling");
		sd.setName(this.getLocalName());
		dfd.addServices(sd);

		try {
			DFService.register(this, dfd);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
		
		addBehaviour(new RequestPerformer());
	}

	protected void takeDown() {
		try {
			DFService.deregister(this);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
	}

	private class RequestPerformer extends OneShotBehaviour {

		
		public void action(){

			try {
				
				ACLMessage msg = blockReceiveMessage(30000,ACLMessage.CFP);
				
				asserts.assertEquals("terra", msg.getContent());

				ACLMessage reply = msg.createReply();
				reply.setPerformative(ACLMessage.PROPOSE);
				reply.setContent(String.valueOf(catalogue.get(msg.getContent())));

				send(reply);
				
				msg = blockReceiveMessage(30000,ACLMessage.REFUSE);
								
				setTestResult("OK");
				
			} catch(NumberFormatException e) {
				setTestResult(prepareMessageResult(e));
			} catch (Exception e) {
				setTestResult(prepareMessageResult(e));
			}
		}
	}
}