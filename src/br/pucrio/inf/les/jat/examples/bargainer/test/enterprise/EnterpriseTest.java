package br.pucrio.inf.les.jat.examples.bargainer.test.enterprise;

import br.pucrio.inf.les.jat.aspects.monitor.AgentMonitorServices;
import br.pucrio.inf.les.jat.core.JadeTestCase;

public class EnterpriseTest extends JadeTestCase {
	
	public void testEnterprise1() {		
		registerAndStartAgent("enterprise1","br.pucrio.inf.les.jat.examples.bargainer.EnterpriseAgent", new String[]{"enterpriseOne","Milk","15","8"});
		registerAndStartAgent("enterprise2","br.pucrio.inf.les.jat.examples.bargainer.EnterpriseAgent", new String[]{"enterpriseTwo","Flour","20","15"});
		registerAndStartAgent("enterprise3","br.pucrio.inf.les.jat.examples.bargainer.EnterpriseAgent", new String[]{"enterpriseTree","Egg","10","8"});		
		registerAndStartMockAgent("bargainer","br.pucrio.inf.les.jat.examples.bargainer.test.enterprise.BargainerMock1",new Object[0]);
		
		AgentMonitorServices.setTimeOut(60000);
		AgentMonitorServices.waitUntilTestHasFinished("bargainer");
		
		assertMockAgent("bargainer");		
	}	
	
	public void testEnterprise2() {		
		registerAndStartAgent("enterprise1","br.pucrio.inf.les.jat.examples.bargainer.EnterpriseAgent", new String[]{"enterpriseOne","Milk","15","8"});
		registerAndStartAgent("enterprise2","br.pucrio.inf.les.jat.examples.bargainer.EnterpriseAgent", new String[]{"enterpriseTwo","Flour","20","15"});
		registerAndStartAgent("enterprise3","br.pucrio.inf.les.jat.examples.bargainer.EnterpriseAgent", new String[]{"enterpriseTree","Egg","10","8"});		
		registerMockAgent("auction_Milk", "br.pucrio.inf.les.jat.examples.bargainer.test.enterprise.AuctionMock2");
		registerMockAgent("auction_Flour", "br.pucrio.inf.les.jat.examples.bargainer.test.enterprise.AuctionMock2");
		registerMockAgent("auction_Egg", "br.pucrio.inf.les.jat.examples.bargainer.test.enterprise.AuctionMock2");
		registerAndStartMockAgent("bargainer","br.pucrio.inf.les.jat.examples.bargainer.test.enterprise.BargainerMock2",new Object[0]);
		
		AgentMonitorServices.setTimeOut(60000);
		AgentMonitorServices.waitUntilTestHasFinished("bargainer");
		AgentMonitorServices.waitUntilTestHasFinished("auction_Milk");
		AgentMonitorServices.waitUntilTestHasFinished("auction_Flour");
		AgentMonitorServices.waitUntilTestHasFinished("auction_Egg");
		
		assertMockAgent("bargainer");
		assertMockAgent("auction_Milk");
		assertMockAgent("auction_Flour");
		assertMockAgent("auction_Egg");
	}
	
	public void testEnterprise3() {		
		registerAndStartAgent("enterprise1","br.pucrio.inf.les.jat.examples.bargainer.EnterpriseAgent", new String[]{"enterpriseOne","Milk","15","8"});
		registerAndStartAgent("enterprise2","br.pucrio.inf.les.jat.examples.bargainer.EnterpriseAgent", new String[]{"enterpriseTwo","Flour","20","15"});
		registerAndStartAgent("enterprise3","br.pucrio.inf.les.jat.examples.bargainer.EnterpriseAgent", new String[]{"enterpriseTree","Egg","10","8"});		
		registerMockAgent("auction_Milk", "br.pucrio.inf.les.jat.examples.bargainer.test.enterprise.AuctionMock3");
		registerMockAgent("auction_Flour", "br.pucrio.inf.les.jat.examples.bargainer.test.enterprise.AuctionMock3");
		registerMockAgent("auction_Egg", "br.pucrio.inf.les.jat.examples.bargainer.test.enterprise.AuctionMock3");
		registerAndStartMockAgent("bargainer","br.pucrio.inf.les.jat.examples.bargainer.test.enterprise.BargainerMock3",new Object[0]);
		
		AgentMonitorServices.setTimeOut(60000);
		AgentMonitorServices.waitUntilTestHasFinished("bargainer");
		AgentMonitorServices.waitUntilTestHasFinished("auction_Milk");
		AgentMonitorServices.waitUntilTestHasFinished("auction_Flour");
		AgentMonitorServices.waitUntilTestHasFinished("auction_Egg");
		
		assertMockAgent("bargainer");
		assertMockAgent("auction_Milk");
		assertMockAgent("auction_Flour");
		assertMockAgent("auction_Egg");
	}
	
	public void testEnterprise4() {		
		registerAndStartAgent("enterpriseOne","br.pucrio.inf.les.jat.examples.bargainer.EnterpriseAgent", new String[]{"enterpriseOne","Milk","15","8"});
		registerAndStartAgent("enterpriseTwo","br.pucrio.inf.les.jat.examples.bargainer.EnterpriseAgent", new String[]{"enterpriseTwo","Flour","20","15"});
		registerAndStartAgent("enterpriseThree","br.pucrio.inf.les.jat.examples.bargainer.EnterpriseAgent", new String[]{"enterpriseThree","Egg","10","8"});		
		registerMockAgent("auction_Milk", "br.pucrio.inf.les.jat.examples.bargainer.test.enterprise.AuctionMock4");
		registerMockAgent("auction_Flour", "br.pucrio.inf.les.jat.examples.bargainer.test.enterprise.AuctionMock4");
		registerMockAgent("auction_Egg", "br.pucrio.inf.les.jat.examples.bargainer.test.enterprise.AuctionMock4");		
		registerAndStartMockAgent("bargainer","br.pucrio.inf.les.jat.examples.bargainer.test.enterprise.BargainerMock4",new Object[0]);
		
		AgentMonitorServices.setTimeOut(60000);
		AgentMonitorServices.waitUntilTestHasFinished("bargainer");
		AgentMonitorServices.waitUntilTestHasFinished("auction_Milk");
		AgentMonitorServices.waitUntilTestHasFinished("auction_Flour");
		AgentMonitorServices.waitUntilTestHasFinished("auction_Egg");
				
		assertMockAgent("bargainer");
		assertMockAgent("auction_Milk");
		assertMockAgent("auction_Flour");
		assertMockAgent("auction_Egg");
	}
}