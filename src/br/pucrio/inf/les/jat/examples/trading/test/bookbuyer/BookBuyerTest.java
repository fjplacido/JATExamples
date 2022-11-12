package br.pucrio.inf.les.jat.examples.trading.test.bookbuyer;

import br.pucrio.inf.les.jat.aspects.monitor.AgentMonitorServices;
import br.pucrio.inf.les.jat.core.JadeTestCase;

public class BookBuyerTest extends JadeTestCase {
	
	//Caso normal de compra de um livro
	public void testBookBuyer1() {		
		registerAndStartMockAgent("BookSellerMock1","br.pucrio.inf.les.jat.examples.trading.test.bookbuyer.BookSellerMock11",new Object[]{"terra","2"});		
		registerAndStartMockAgent("BookSellerMock2","br.pucrio.inf.les.jat.examples.trading.test.bookbuyer.BookSellerMock12",new Object[]{"terra","3"});
		registerAndStartAgent("comprador","br.pucrio.inf.les.jat.examples.trading.BookBuyerAgent", new String[]{"terra"});
		
		AgentMonitorServices.waitUntilTestHasFinished("BookSellerMock1");
		AgentMonitorServices.waitUntilTestHasFinished("BookSellerMock2");
		
		assertMockAgent("BookSellerMock1");
		assertMockAgent("BookSellerMock2");
		
		String res = (String)getAgentBelief("comprador","best");
		String expected = "BookSellerMock1";
		
		assertEquals(expected, res);
	}
	
	//Envia um PROPOSE sem o BookBuyer ter requisitado um livro
	public void testBookBuyer2() {
		registerAndStartAgent("comprador","br.pucrio.inf.les.jat.examples.trading.BookBuyerAgent", new String[]{"terra"});
		registerAndStartMockAgent("BookSellerMock","br.pucrio.inf.les.jat.examples.trading.test.bookbuyer.BookSellerMock2",new Object[]{"terra","2"});
		
		AgentMonitorServices.waitUntilTestHasFinished("BookSellerMock");
		
		assertMockAgent("BookSellerMock");
		
		String res = (String)getAgentBelief("comprador","best");
		String expected = "";
		
		assertEquals(expected, res);
	}
	
	//Os dois primeiros enviam o PROPOSE e o terceior envia um REFUSE
	public void testBookBuyer3() {		
		registerAndStartMockAgent("BookSellerMock1","br.pucrio.inf.les.jat.examples.trading.test.bookbuyer.BookSellerMock31",new Object[]{"terra","2"});
		registerAndStartMockAgent("BookSellerMock2","br.pucrio.inf.les.jat.examples.trading.test.bookbuyer.BookSellerMock32",new Object[]{"terra","5"});
		registerAndStartMockAgent("BookSellerMock3","br.pucrio.inf.les.jat.examples.trading.test.bookbuyer.BookSellerMock33",new Object[]{"terra","7"});
		registerAndStartAgent("comprador","br.pucrio.inf.les.jat.examples.trading.BookBuyerAgent", new String[]{"terra"});

		AgentMonitorServices.waitUntilTestHasFinished("BookSellerMock1");
		AgentMonitorServices.waitUntilTestHasFinished("BookSellerMock2");
		AgentMonitorServices.waitUntilTestHasFinished("BookSellerMock3");
		
		assertMockAgent("BookSellerMock1");
		assertMockAgent("BookSellerMock2");
		assertMockAgent("BookSellerMock3");
		
		String res = (String)getAgentBelief("comprador","best");
		String expected = "BookSellerMock1";
		
		assertEquals(expected, res);
	}
	
	//Somente dois dos três Mocks respondem ao CFP e um outro Mock envia a responta sem ter recebido um CFP
	public void testBookBuyer4() {		
		registerAndStartMockAgent("BookSellerMock1","br.pucrio.inf.les.jat.examples.trading.test.bookbuyer.BookSellerMock41",new Object[]{"terra","2"});
		registerAndStartMockAgent("BookSellerMock2","br.pucrio.inf.les.jat.examples.trading.test.bookbuyer.BookSellerMock42",new Object[]{"terra","5"});
		registerAndStartMockAgent("BookSellerMock3","br.pucrio.inf.les.jat.examples.trading.test.bookbuyer.BookSellerMock43",new Object[]{"terra","7"});		
		registerAndStartAgent("comprador","br.pucrio.inf.les.jat.examples.trading.BookBuyerAgent", new String[]{"terra"});
		registerAndStartMockAgent("BookSellerMock4","br.pucrio.inf.les.jat.examples.trading.test.bookbuyer.BookSellerMock44",new Object[]{"terra","1"});

		AgentMonitorServices.waitUntilTestHasFinished("BookSellerMock1");
		AgentMonitorServices.waitUntilTestHasFinished("BookSellerMock2");
		AgentMonitorServices.waitUntilTestHasFinished("BookSellerMock3");
		AgentMonitorServices.waitUntilTestHasFinished("BookSellerMock4");
		
		assertMockAgent("BookSellerMock1");
		assertMockAgent("BookSellerMock2");
		assertMockAgent("BookSellerMock3");
		assertMockAgent("BookSellerMock4");
		
		String res = (String)getAgentBelief("comprador","best");
		String expected = "BookSellerMock4";
		
		assertEquals(expected, res);
	}
		
	//Disputa entre dois vendedores por um livro
	/*public void testBookBuyer5() {		
		registerAndStartMockAgent("BookSellerMock1","br.pucrio.inf.les.jat.examples.trading.test.bookbuyer.BookSellerMock51",new Object[]{"terra","2"});
		registerAndStartMockAgent("BookSellerMock2","br.pucrio.inf.les.jat.examples.trading.test.bookbuyer.BookSellerMock52",new Object[]{"terra","2"});
		registerAndStartAgent("comprador","br.pucrio.inf.les.jat.examples.trading.BookBuyerAgent", new String[]{"terra"});
		
		AgentMonitorServices.waitUntilTestHasFinished("BookSellerMock1");
		AgentMonitorServices.waitUntilTestHasFinished("BookSellerMock2");
		
		assertMockAgent("BookSellerMock1");
		assertMockAgent("BookSellerMock2");
	}*/
	
	//Responde com uma performativa diferente de PROPOSE
	public void testBookBuyer6() {		
		registerAndStartMockAgent("BookSellerMock1","br.pucrio.inf.les.jat.examples.trading.test.bookbuyer.BookSellerMock6",new Object[]{"terra","2"});
		registerAndStartMockAgent("BookSellerMock2","br.pucrio.inf.les.jat.examples.trading.test.bookbuyer.BookSellerMock6",new Object[]{"terra","1"});
		registerAndStartAgent("comprador","br.pucrio.inf.les.jat.examples.trading.BookBuyerAgent", new String[]{"terra"});
		
		AgentMonitorServices.waitUntilTestHasFinished("BookSellerMock1");
		AgentMonitorServices.waitUntilTestHasFinished("BookSellerMock2");
		
		assertMockAgent("BookSellerMock1");
		assertMockAgent("BookSellerMock2");
	}
}
