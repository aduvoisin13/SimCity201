package restaurant_smileham.test.mock;

import java.util.List;

import restaurant_smileham.agent.Check;
import restaurant_smileham.Order;
import restaurant_smileham.interfaces.Cashier;
import restaurant_smileham.interfaces.Market;

public class MockCashier extends Mock implements Cashier{

	public MockCashier(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgMakeCheck(Order order) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgPayingCheck(Check check) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void makeCheck(Order order) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Order> getOrders() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void msgMarketBill(Market market, int amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void giveChange(Check check) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void payMarket(Market market, int amount) {
		// TODO Auto-generated method stub
		
	}
}
