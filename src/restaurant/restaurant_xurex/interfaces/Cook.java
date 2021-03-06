package restaurant.restaurant_xurex.interfaces;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import restaurant.restaurant_xurex.agents.CookAgent.CookOrder;
import restaurant.restaurant_xurex.gui.CookGui;


public interface Cook {

	public List<CookOrder> revolvingStand = Collections.synchronizedList(new ArrayList<CookOrder>());

	// MESSAGES
	public abstract void HereIsOrder(Waiter w, String choice, int table);

	//public abstract void MarketCanFulfill(Market market,Map<String, Integer> provided);

	//public abstract void MarketCannotFulfill(Market market,Map<String, Integer> provided);

	//public abstract void OrderIsReady(Market market);

	//public abstract void addMarket(Market market);

	public abstract String getName();

	void setGui(CookGui cookGui);
	
	public abstract void msgAtLocation();
	
	public abstract void PickedUp(int kitchen);

	void addToStand(Waiter w, String choice, int table);

}