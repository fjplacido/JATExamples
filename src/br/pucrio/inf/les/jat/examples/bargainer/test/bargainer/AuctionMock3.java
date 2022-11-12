package br.pucrio.inf.les.jat.examples.bargainer.test.bargainer;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.List;

import br.pucrio.inf.les.jat.core.JadeMockAgent;

public class AuctionMock3 extends JadeMockAgent {
	
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
		
		private AuctionMock3 agent;

		public MainBehaviour(AuctionMock3 agent) {
			this.agent = agent;
		}

		@Override
		public void action() {
			
			ACLMessage msgInform = new ACLMessage(ACLMessage.INFORM);
			msgInform.setSender(agent.getAID());			
			msgInform.addReceiver(bargainer);
			
			if ( componentName.equalsIgnoreCase("Milk") ) {
				bestPrice = 100;
				bestEnterprise = "enterprise1";
			} else if ( componentName.equalsIgnoreCase("Flour") ) {
				bestPrice = 50;
				bestEnterprise = "enterprise2";
			} else if ( componentName.equalsIgnoreCase("Egg") ) {
				setTestResult("Erro");
			}
			
			msgInform.setContent("COMPONENTE:" + componentName + ";EMPRESA:" + bestEnterprise + ";PRECO:" + bestPrice);
						
			send(msgInform);
			
			setTestResult("OK");
		}
	}
}
