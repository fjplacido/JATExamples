package br.pucrio.inf.les.jat.examples.bargainer.test.bargainer;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.List;

import br.pucrio.inf.les.jat.core.JadeMockAgent;

public class AuctionMock4 extends JadeMockAgent {
	
	private AID bargainer;
	private List<AID> entreprises;
	private String componentName;
	private float initialPrice;
	private String replayWith;
	private float bestPrice;
	private String bestEnterprise = "";
	
	protected void setup() {
		
		Object[] args = getArguments();
		this.bargainer = (AID)args[0];
		this.entreprises = (List<AID>)args[1];
		this.componentName = (String)args[2];
		this.initialPrice = (Float)args[3];
		//this.replayWith = (String)args[4];
		this.bestPrice = this.initialPrice;
		
		addBehaviour(new MainBehaviour(this));
	}

	protected void takeDown() {
		
	}

	private class MainBehaviour extends OneShotBehaviour {
		
		private AuctionMock4 agent;

		public MainBehaviour(AuctionMock4 agent) {
			this.agent = agent;
		}

		@Override
		public void action() {
									
			ACLMessage msgFailure = new ACLMessage(ACLMessage.REFUSE);
			msgFailure.setSender(agent.getAID());
			msgFailure.setContent("COMPONENTE:" + componentName);			
			msgFailure.addReceiver(bargainer);

			send(msgFailure);
									
			setTestResult("OK");
		}
	}
}
