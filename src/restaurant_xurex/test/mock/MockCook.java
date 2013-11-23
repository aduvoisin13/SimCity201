package restaurant_xurex.test.mock;


import java.util.Map;

import restaurant_xurex.gui.RestaurantGui;
import restaurant_xurex.interfaces.Cook;
import restaurant_xurex.interfaces.Market;
import restaurant_xurex.interfaces.Waiter;


/**
 * MockCook built to test waiter
 *
 * @author Rex Xu
 *
 */
public class MockCook extends Mock implements Cook {

	public MockCook(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void HereIsOrder(Waiter w, String choice, int table) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void MarketCanFulfill(Market market, Map<String, Integer> provided) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void MarketCannotFulfill(Market market, Map<String, Integer> provided) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OrderIsReady(Market market) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addMarket(Market market) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getQuantity(String food) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setGui(RestaurantGui gui) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtLocation() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void PickedUp(int kitchen) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addToStand(Waiter w, String choice, int table) {
		// TODO Auto-generated method stub
		
	}


}
