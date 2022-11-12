package br.pucrio.inf.les.jat.examples.trading.test.bookseller;

import br.pucrio.inf.les.jat.aspects.monitor.AgentMonitorServices;
import br.pucrio.inf.les.jat.core.JadeTestCase;

/**
 * Classe de Testes que testa o comportamento de um afente Vendedor a partir
 * da execucao do behavior de um mock comprador.
 * 
 * @author roberta
 * 
 */
public class BookSellerTest extends JadeTestCase {
	
	//Disputa entre dois BookBuyerMock por um livro
	/*public void testBookSeller1() {
		registerAndStartAgent("vendedor","br.pucrio.inf.les.jat.examples.trading.BookSellerAgent", new String[]{"terra","2"});
		registerAndStartMockAgent("BookBuyer1","br.pucrio.inf.les.jat.examples.trading.test.bookseller.BookBuyerSynchronizerMock1",new Object[]{"terra"});
		registerAndStartMockAgent("BookBuyer2","br.pucrio.inf.les.jat.examples.trading.test.bookseller.BookBuyerSynchronizerMock2",new Object[]{"terra"});
		registerAndStartMockAgent("BookBuyer3","br.pucrio.inf.les.jat.examples.trading.test.bookseller.BookBuyerSynchronizerMock3",new Object[]{"terra"});
		
		AgentMonitorServices.setTimeOut(60000);
		AgentMonitorServices.waitUntilTestHasFinished("BookBuyer1");
		AgentMonitorServices.waitUntilTestHasFinished("BookBuyer2");
		AgentMonitorServices.waitUntilTestHasFinished("BookBuyer3");

		assertMockAgent("BookBuyer1");
		assertMockAgent("BookBuyer2");
		assertMockAgent("BookBuyer3");
				
		String res = (String)getAgentBelief("vendedor");
		String expected = "BookBuyer2";
		
		assertEquals(expected, res);
	}*/
	
	//BookBuyerMock requisita um livro que não tem no catálogo
	public void testBookSeller2() {
		registerAndStartAgent("vendedor","br.pucrio.inf.les.jat.examples.trading.BookSellerAgent", new String[]{"terra","2"});
		registerAndStartMockAgent("BookBuyerMock","br.pucrio.inf.les.jat.examples.trading.test.bookseller.BookBuyerMock2",new Object[]{"lua"});		

		AgentMonitorServices.waitUntilTestHasFinished("BookBuyerMock");
		
		assertMockAgent("BookBuyerMock");					
	}
	
	//BookBuyerMock tenta comprar um livro que não tem no catálogo 
	public void testBookSeller3() {
		
		registerAndStartAgent("vendedor","br.pucrio.inf.les.jat.examples.trading.BookSellerAgent", new String[]{"terra","10"});
		registerAndStartMockAgent("BookBuyerMock","br.pucrio.inf.les.jat.examples.trading.test.bookseller.BookBuyerMock3",new Object[0]);		

		AgentMonitorServices.waitUntilTestHasFinished("BookBuyerMock");
		
		assertMockAgent("BookBuyerMock");
	}
	
	//BookBuyeMock envia um ACCEPT sem ter enviado um propose
	public void testBookSeller4() {
		registerAndStartAgent("vendedor","br.pucrio.inf.les.jat.examples.trading.BookSellerAgent", new String[]{"terra","2"});
		registerAndStartMockAgent("BookBuyerMock2","br.pucrio.inf.les.jat.examples.trading.test.bookseller.BookBuyerMock4",new Object[0]);		

		AgentMonitorServices.waitUntilTestHasFinished("BookBuyerMock2");
		
		assertMockAgent("BookBuyerMock2");
		
		String res = (String)getAgentBelief("vendedor","buyer");
		String expected = "BookBuyerMock2";
		
		assertEquals(expected, res);
	}
	
	//Execução normal do protocolo de interação entre o BookSeller e um BookBuyer
	public void testBookSeller5() {
		registerAndStartAgent("vendedor","br.pucrio.inf.les.jat.examples.trading.BookSellerAgent", new String[]{"terra","2"});
		registerAndStartMockAgent("BookBuyerMock2","br.pucrio.inf.les.jat.examples.trading.test.bookseller.BookBuyerMock5",new Object[0]);		

		AgentMonitorServices.waitUntilTestHasFinished("BookBuyerMock2");
		
		assertMockAgent("BookBuyerMock2");
		
		String res = (String)getAgentBelief("vendedor","buyer");
		String expected = "BookBuyerMock2";
		
		assertEquals(expected, res);
	}
}