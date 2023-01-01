package br.pucrio.teste;

import br.pucrio.inf.les.jat.aspects.monitor.AgentMonitorServices;
import br.pucrio.inf.les.jat.core.JadeTestCase;

public class TestCase2 extends JadeTestCase {

	public void testMethod1() {

		registerAndStartMockAgent("BookSeller1",
				"br.pucrio.inf.les.jat.examples.trading.test.bookbuyer.BookSellerMock11", new Object[] {});
		registerAndStartMockAgent("BookSeller1", "br.pucrio.inf.les.jat.examples.trading.test.bookbuyer",
				new Object[] {});
		registerAndStartAgent("BookBuyer", "br.pucrio.inf.les.jat.examples.trading.BookBuyerAgent", new Object[] {});

		AgentMonitorServices.waitUntilTestHasFinished("BookSeller1");
		AgentMonitorServices.waitUntilTestHasFinished("BookSeller1");

		assertMockAgent("BookSeller1");
		assertMockAgent("BookSeller1");
	}
}