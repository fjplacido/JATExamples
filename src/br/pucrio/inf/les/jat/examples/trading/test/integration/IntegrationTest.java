package br.pucrio.inf.les.jat.examples.trading.test.integration;
import br.pucrio.inf.les.jat.aspects.monitor.AgentMonitorServices;
import br.pucrio.inf.les.jat.core.JadeTestCase;

/**
 * Classe de Testes que testa o comportamento de um afente Vendedor a partir
 * da execucao do behavior de um mock comprador.
 * 
 * @author roberta
 * 
 */
public class IntegrationTest extends JadeTestCase {
		
	//Teste de integração
	public void testIntegration() {
		registerAndStartAgent("vendedor1","org.jadeunit.examples.trading.BookSellerAgent", new String[]{"terra","2"});
		registerAndStartAgent("vendedor2","org.jadeunit.examples.trading.BookSellerAgent", new String[]{"terra","4"});
		registerAndStartAgent("vendedor3","org.jadeunit.examples.trading.BookSellerAgent", new String[]{"terra","1"});
		registerAndStartAgent("vendedor4","org.jadeunit.examples.trading.BookSellerAgent", new String[]{"terra","5"});
		registerAndStartAgent("comprador","org.jadeunit.examples.trading.BookBuyerAgent", new String[]{"terra"});		

		AgentMonitorServices.setTimeOut(60000);
		AgentMonitorServices.waitUntilTestHasFinished("vendedor1");
		AgentMonitorServices.waitUntilTestHasFinished("vendedor2");
		AgentMonitorServices.waitUntilTestHasFinished("vendedor3");
		AgentMonitorServices.waitUntilTestHasFinished("vendedor4");
		AgentMonitorServices.waitUntilTestHasFinished("comprador");
		
		String res = (String)getAgentBelief("vendedor1","buyer");
		String expected = "";
		
		assertEquals(expected, res);
		
		res = (String)getAgentBelief("vendedor2","buyer");
		expected = "";
		
		assertEquals(expected, res);
		
		res = (String)getAgentBelief("vendedor3","buyer");
		expected = "comprador";
		
		assertEquals(expected, res);
		
		res = (String)getAgentBelief("vendedor4","buyer");
		expected = "";
		
		assertEquals(expected, res);
		
		res = (String)getAgentBelief("comprador","best");
		expected = "vendedor3";
		
		assertEquals(expected, res);
	}
}