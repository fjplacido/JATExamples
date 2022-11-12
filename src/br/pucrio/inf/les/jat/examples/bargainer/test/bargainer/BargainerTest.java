package br.pucrio.inf.les.jat.examples.bargainer.test.bargainer;

import java.util.HashMap;
import java.util.Map;

import br.pucrio.inf.les.jat.aspects.monitor.AgentMonitorServices;
import br.pucrio.inf.les.jat.core.JadeTestCase;

public class BargainerTest extends JadeTestCase {

	//Verifica se o bargainer executa sua tarefa corretamente.
	public void testBargainer1(){

		registerAndStartMockAgent("enterprise1","br.pucrio.inf.les.jat.examples.bargainer.test.bargainer.EnterpriseMock1", new String[]{"enterpriseOne","Milk","15","13"});
		registerAndStartMockAgent("enterprise2","br.pucrio.inf.les.jat.examples.bargainer.test.bargainer.EnterpriseMock1", new String[]{"enterpriseTwo","Flour","20","15"});
		registerAndStartMockAgent("enterprise3","br.pucrio.inf.les.jat.examples.bargainer.test.bargainer.EnterpriseMock1", new String[]{"enterpriseTree","Egg","10","8"});
		registerMockAgent("auction_bargainer_Milk", "br.pucrio.inf.les.jat.examples.bargainer.bargainer.test.AuctionMock1");
		registerMockAgent("auction_bargainer_Flour", "br.pucrio.inf.les.jat.examples.bargainer.bargainer.test.AuctionMock1");
		registerMockAgent("auction_bargainer_Egg", "br.pucrio.inf.les.jat.examples.bargainer.bargainer.test.AuctioneMock1");
		registerAndStartAgent("bargainer","br.pucrio.inf.les.jat.examples.bargainer.BargainerAgent",new String[]{ "br.pucrio.inf.les.jat.examples.bargainer.test.bargainer.AuctionMock1" });

		//TODO Configurar um TimeOut
		AgentMonitorServices.setTimeOut(60000);
		AgentMonitorServices.waitUntilTestHasFinished("enterprise1");
		AgentMonitorServices.waitUntilTestHasFinished("enterprise2");
		AgentMonitorServices.waitUntilTestHasFinished("enterprise3");
		AgentMonitorServices.waitUntilTestHasFinished("auction_bargainer_Milk");
		AgentMonitorServices.waitUntilTestHasFinished("auction_bargainer_Flour");
		AgentMonitorServices.waitUntilTestHasFinished("auction_bargainer_Egg");

		assertMockAgent("enterprise1");
		assertMockAgent("enterprise2");
		assertMockAgent("enterprise3");
		assertMockAgent("auction_bargainer_Milk");
		assertMockAgent("auction_bargainer_Flour");
		assertMockAgent("auction_bargainer_Egg");
		
		Map<String,String> res = (Map<String,String>)getAgentBelief("bargainer","companyList");

		Map<String,String> expected = new HashMap<String, String>();
		expected.put("Milk", "enterprise1");
		expected.put("Flour", "enterprise2");
		expected.put("Egg", "enterprise3");

		assertEquals(expected, res);
	}

	//Os EnterprisesMockAgents não respondem ao PROPOSE do Bargainer
	public void testBargainer2() {

		registerAndStartMockAgent("enterprise1","br.pucrio.inf.les.jat.examples.bargainer.test.bargainer.EnterpriseMock2", new String[]{"enterpriseOne","Milk","15","13"});
		registerAndStartMockAgent("enterprise2","br.pucrio.inf.les.jat.examples.bargainer.test.bargainer.EnterpriseMock2", new String[]{"enterpriseTwo","Flour","20","15"});
		registerAndStartMockAgent("enterprise3","br.pucrio.inf.les.jat.examples.bargainer.test.bargainer.EnterpriseMock2", new String[]{"enterpriseTree","Egg","10","8"});
		registerAndStartAgent("bargainer","br.pucrio.inf.les.jat.examples.bargainer.BargainerAgent",new String[]{ "br.pucrio.inf.les.jat.examples.bargainer.test.bargainer.AuctionMock2" });

		//TODO Configurar um TimeOut
		//AgentMonitorServices.setTimeOut(60000);
		AgentMonitorServices.waitUntilTestHasFinished("enterprise1");
		AgentMonitorServices.waitUntilTestHasFinished("enterprise2");
		AgentMonitorServices.waitUntilTestHasFinished("enterprise3");
		
		assertMockAgent("enterprise1");
		assertMockAgent("enterprise2");
		assertMockAgent("enterprise3");

		Map<String,String> res = (Map<String,String>)getAgentBelief("bargainer","companyList");

		Map<String,String> expected = new HashMap<String, String>();

		assertEquals(expected, res);
	}

	//Ficam faltando alguns produtos da lista para os quais nenhuma empresa respondeu.
	public void testBargainer3(){

		registerAndStartMockAgent("enterprise1","br.pucrio.inf.les.jat.examples.bargainer.test.bargainer.EnterpriseMock3", new String[]{"enterpriseOne","Milk","15","13"});
		registerAndStartMockAgent("enterprise2","br.pucrio.inf.les.jat.examples.bargainer.test.bargainer.EnterpriseMock3", new String[]{"enterpriseTwo","Flour","20","15"});
		registerAndStartMockAgent("enterprise3","br.pucrio.inf.les.jat.examples.bargainer.test.bargainer.EnterpriseMock3", new String[]{"enterpriseTree","Egg","10","8"});
		registerMockAgent("auction_bargainer_Milk", "br.pucrio.inf.les.jat.examples.bargainer.test.bargainer.AuctionMock3");
		registerMockAgent("auction_bargainer_Flour", "br.pucrio.inf.les.jat.examples.bargainer.test.bargainer.AuctionMock3");
		registerAndStartAgent("bargainer","br.pucrio.inf.les.jat.examples.bargainer.BargainerAgent",new String[]{ "br.pucrio.inf.les.jat.examples.bargainer.test.bargainer.AuctionMock3" });

		//TODO Configurar um TimeOut
		//AgentMonitorServices.setTimeOut(60000);
		AgentMonitorServices.waitUntilTestHasFinished("enterprise1");
		AgentMonitorServices.waitUntilTestHasFinished("enterprise2");
		AgentMonitorServices.waitUntilTestHasFinished("enterprise3");
		AgentMonitorServices.waitUntilTestHasFinished("auction_bargainer_Milk");
		AgentMonitorServices.waitUntilTestHasFinished("auction_bargainer_Flour");

		assertMockAgent("enterprise1");
		assertMockAgent("enterprise2");
		assertMockAgent("enterprise3");
		assertMockAgent("auction_bargainer_Milk");
		assertMockAgent("auction_bargainer_Flour");

		Map<String,String> res = (Map<String,String>)getAgentBelief("bargainer","companyList");

		Map<String,String> expected = new HashMap<String, String>();
		expected.put("Milk", "enterprise1");
		expected.put("Flour", "enterprise2");

		assertEquals(expected, res);
	}

	//As empresas aceitam o PROPOSE e nunca mandam a proposta.
	public void testBargainer4(){

		registerAndStartMockAgent("enterprise1","br.pucrio.inf.les.jat.examples.bargainer.test.bargainer.EnterpriseMock4", new String[]{"enterpriseOne","Milk","15","13"});
		registerAndStartMockAgent("enterprise2","br.pucrio.inf.les.jat.examples.bargainer.test.bargainer.EnterpriseMock4", new String[]{"enterpriseTwo","Flour","20","15"});
		registerAndStartMockAgent("enterprise3","br.pucrio.inf.les.jat.examples.bargainer.test.bargainer.EnterpriseMock4", new String[]{"enterpriseTree","Egg","10","8"});
		registerMockAgent("auction_bargainer_Milk", "br.pucrio.inf.les.jat.examples.bargainer.test.bargainer.AuctionMock4");
		registerMockAgent("auction_bargainer_Flour", "br.pucrio.inf.les.jat.examples.bargainer.test.bargainer.AuctionMock4");
		registerMockAgent("auction_bargainer_Egg", "br.pucrio.inf.les.jat.examples.bargainer.test.bargainer.AuctionMock4");
		registerAndStartAgent("bargainer","br.pucrio.inf.les.jat.examples.bargainer.BargainerAgent",new String[]{ "br.pucrio.inf.les.jat.examples.bargainer.test.bargainer.AuctionMock4" });

		//TODO Configurar um TimeOut
		//AgentMonitorServices.setTimeOut(60000);
		AgentMonitorServices.waitUntilTestHasFinished("enterprise1");
		AgentMonitorServices.waitUntilTestHasFinished("enterprise2");
		AgentMonitorServices.waitUntilTestHasFinished("enterprise3");
		AgentMonitorServices.waitUntilTestHasFinished("auction_bargainer_Milk");
		AgentMonitorServices.waitUntilTestHasFinished("auction_bargainer_Flour");
		AgentMonitorServices.waitUntilTestHasFinished("auction_bargainer_Egg");

		assertMockAgent("enterprise1");
		assertMockAgent("enterprise2");
		assertMockAgent("enterprise3");
		assertMockAgent("auction_bargainer_Milk");
		assertMockAgent("auction_bargainer_Flour");
		assertMockAgent("auction_bargainer_Egg");

		Map<String,String> res = (Map<String,String>)getAgentBelief("bargainer","companyList");

		Map<String,String> expected = new HashMap<String, String>();
		
		assertEquals(expected, res);
	}
	
//	As empresas aceitam o PROPOSE e nunca mandam a proposta.
	public void testBargainer5(){

		registerAndStartMockAgent("enterprise1","br.pucrio.inf.les.jat.examples.bargainer.test.bargainer.EnterpriseMock5", new String[]{"enterpriseOne","Milk","15","13"});
		registerAndStartMockAgent("enterprise2","br.pucrio.inf.les.jat.examples.bargainer.test.bargainer.EnterpriseMock5", new String[]{"enterpriseTwo","Flour","20","15"});
		registerAndStartMockAgent("enterprise3","br.pucrio.inf.les.jat.examples.bargainer.test.bargainer.EnterpriseMock5", new String[]{"enterpriseTree","Egg","10","8"});
		registerMockAgent("auction_bargainer_Milk", "br.pucrio.inf.les.jat.examples.bargainer.test.bargainer.AuctionMock5");
		registerMockAgent("auction_bargainer_Flour", "br.pucrio.inf.les.jat.examples.bargainer.test.bargainer.AuctionMock5");
		registerMockAgent("auction_bargainer_Egg", "br.pucrio.inf.les.jat.examples.bargainer.test.bargainer.AuctionMock5");
		registerAndStartAgent("bargainer","br.pucrio.inf.les.jat.examples.bargainer.BargainerAgent",new String[]{ "br.pucrio.inf.les.jat.examples.bargainer.test.bargainer.AuctionMock5" });

		//TODO Configurar um TimeOut
		//AgentMonitorServices.setTimeOut(60000);
		AgentMonitorServices.waitUntilTestHasFinished("enterprise1");
		AgentMonitorServices.waitUntilTestHasFinished("enterprise2");
		AgentMonitorServices.waitUntilTestHasFinished("enterprise3");
		AgentMonitorServices.waitUntilTestHasFinished("auction_bargainer_Milk");
		AgentMonitorServices.waitUntilTestHasFinished("auction_bargainer_Flour");
		AgentMonitorServices.waitUntilTestHasFinished("auction_bargainer_Egg");
		
		assertMockAgent("enterprise1");
		assertMockAgent("enterprise2");
		assertMockAgent("enterprise3");
		assertMockAgent("auction_bargainer_Milk");
		assertMockAgent("auction_bargainer_Flour");
		assertMockAgent("auction_bargainer_Egg");

		Map<String,String> res = (Map<String,String>)getAgentBelief("bargainer","companyList");

		Map<String,String> expected = new HashMap<String, String>();
		expected.put("Milk", "enterprise1");
		expected.put("Flour", "enterprise2");
		
		assertEquals(expected, res);
	}
	
	//Uma empresa aceita o primeiro CFP e nunca manda a proposta.
	public void testBargainer6(){

		registerAndStartMockAgent("enterprise1","br.pucrio.inf.les.jat.examples.bargainer.test.bargainer.EnterpriseMock6", new String[]{"enterpriseOne","Milk","15"});
		registerAndStartMockAgent("enterprise11","br.pucrio.inf.les.jat.examples.bargainer.test.bargainer.EnterpriseMock6", new String[]{"enterpriseOneOne","Milk","10"});
		registerAndStartMockAgent("enterprise2","br.pucrio.inf.les.jat.examples.bargainer.test.bargainer.EnterpriseMock6", new String[]{"enterpriseTwo","Flour","20"});
		registerAndStartMockAgent("enterprise3","br.pucrio.inf.les.jat.examples.bargainer.test.bargainer.EnterpriseMock6", new String[]{"enterpriseTree","Egg","10"});
		registerAndStartAgent("bargainer","br.pucrio.inf.les.jat.examples.bargainer.BargainerAgent",new String[]{ "br.pucrio.inf.les.jat.examples.bargainer.AuctionAgent" });
		registerAgent("auction_bargainer_Milk", "br.pucrio.inf.les.jat.examples.bargainer.AuctionAgent");
		registerAgent("auction_bargainer_Flour", "br.pucrio.inf.les.jat.examples.bargainer.AuctionAgent");
		registerAgent("auction_bargainer_Egg", "br.pucrio.inf.les.jat.examples.bargainer.AuctionAgent");

		//TODO Configurar um TimeOut
		AgentMonitorServices.setTimeOut(60000);
		AgentMonitorServices.waitUntilTestHasFinished("enterprise1");
		AgentMonitorServices.waitUntilTestHasFinished("enterprise11");
		AgentMonitorServices.waitUntilTestHasFinished("enterprise2");
		AgentMonitorServices.waitUntilTestHasFinished("enterprise3");
		AgentMonitorServices.waitUntilTestHasFinished("bargainer");
		AgentMonitorServices.waitUntilTestHasFinished("auction_bargainer_Milk");
		AgentMonitorServices.waitUntilTestHasFinished("auction_bargainer_Flour");
		AgentMonitorServices.waitUntilTestHasFinished("auction_bargainer_Egg");
		
				
		assertMockAgent("enterprise1");
		assertMockAgent("enterprise11");		
		assertMockAgent("enterprise2");
		assertMockAgent("enterprise3");
				
		Map<String,String> respBargainer = (Map<String,String>)getAgentBelief("bargainer","companyList");

		Map<String,String> expectedBargainer = new HashMap<String, String>();
		
		assertEquals(expectedBargainer, respBargainer);
		
		String respAuction = (String)getAgentBelief("auction_bargainer_Milk","bestEnterprise");
		String expectedAuction = "";
		
		assertEquals(expectedAuction, respAuction);
		
		respAuction = (String)getAgentBelief("auction_bargainer_Flour","bestEnterprise");
		expectedAuction = "";
		
		assertEquals(expectedAuction, respAuction);
		
		respAuction = (String)getAgentBelief("auction_bargainer_Egg","bestEnterprise");
		expectedAuction = "";
		
		assertEquals(expectedAuction, respAuction);
	}
	
	//Curso de execução normal dos agentes.
	public void testBargainer7() {

		registerAndStartAgent("enterprise1","br.pucrio.inf.les.jat.examples.bargainer.EnterpriseAgent", new String[]{"enterpriseOne","Milk","15","8"});
		registerAndStartAgent("enterprise11","br.pucrio.inf.les.jat.examples.bargainer.EnterpriseAgent", new String[]{"enterpriseOneOne","Milk","10","7"});
		registerAndStartAgent("enterprise2","br.pucrio.inf.les.jat.examples.bargainer.EnterpriseAgent", new String[]{"enterpriseTwo","Flour","20","15"});
		registerAndStartAgent("enterprise3","br.pucrio.inf.les.jat.examples.bargainer.EnterpriseAgent", new String[]{"enterpriseTree","Egg","10","8"});
		registerAndStartAgent("bargainer","br.pucrio.inf.les.jat.examples.bargainer.BargainerAgent",new String[]{ "br.pucrio.inf.les.jat.examples.bargainer.AuctionAgent" });
		registerAgent("auction_bargainer_Milk", "br.pucrio.inf.les.jat.examples.bargainer.AuctionAgent");
		registerAgent("auction_bargainer_Flour", "br.pucrio.inf.les.jat.examples.bargainer.AuctionAgent");
		registerAgent("auction_bargainer_Egg", "br.pucrio.inf.les.jat.examples.bargainer.AuctionAgent");

		//TODO Configurar um TimeOut
		AgentMonitorServices.setTimeOut(300000);
		AgentMonitorServices.waitUntilTestHasFinished("enterprise1");
		AgentMonitorServices.waitUntilTestHasFinished("enterprise11");
		AgentMonitorServices.waitUntilTestHasFinished("enterprise2");
		AgentMonitorServices.waitUntilTestHasFinished("enterprise3");
		AgentMonitorServices.waitUntilTestHasFinished("bargainer");
		AgentMonitorServices.waitUntilTestHasFinished("auction_bargainer_Milk");
		AgentMonitorServices.waitUntilTestHasFinished("auction_bargainer_Flour");
		AgentMonitorServices.waitUntilTestHasFinished("auction_bargainer_Egg");
								
		Map<String,String> respBargainer = (Map<String,String>)getAgentBelief("bargainer","companyList");

		Map<String,String> expectedBargainer = new HashMap<String, String>();
		expectedBargainer.put("Milk", "enterprise11");
		expectedBargainer.put("Flour", "enterprise2");
		expectedBargainer.put("Egg", "enterprise3");
		
		assertEquals(expectedBargainer, respBargainer);
		
		String respAuction = (String)getAgentBelief("auction_bargainer_Milk","bestEnterprise");
		String expectedAuction = "enterprise11";
		
		assertEquals(expectedAuction, respAuction);
		
		respAuction = (String)getAgentBelief("auction_bargainer_Flour","bestEnterprise");
		expectedAuction = "enterprise2";
		
		assertEquals(expectedAuction, respAuction);
		
		respAuction = (String)getAgentBelief("auction_bargainer_Egg","bestEnterprise");
		expectedAuction = "enterprise3";
		
		assertEquals(expectedAuction, respAuction);
		
		String respEnterprise = (String)getAgentBelief("enterprise1","bestEnterprise");
		String expectedEnterprise = "";
		
		assertEquals(expectedEnterprise, respEnterprise);
	}
}