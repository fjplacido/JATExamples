package br.pucrio.inf.les.jat.examples.bargainer;

import jade.core.AID;

public class Response {
	
	private AID agent;
	private String replayWith;
	
	public AID getAgent() {
		return agent;
	}
	
	public void setAgent(AID agent) {
		this.agent = agent;
	}
	
	public String getReplayWith() {
		return replayWith;
	}
	
	public void setReplayWith(String replayWith) {
		this.replayWith = replayWith;
	}		
}