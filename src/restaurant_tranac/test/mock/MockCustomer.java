package restaurant_tranac.test.mock;

import restaurant_tranac.Check;
import restaurant_tranac.Menu;
import restaurant_tranac.gui.CustomerGui;
import restaurant_tranac.interfaces.*;

/**
 * MockCustomer built for unit testing.
 *
 * @author Angelica Huyen Tran
 */

public class MockCustomer extends Mock implements Customer {
	public MockCustomer(String name) {
		super(name);
	}
	
	public void msgHereIsChange(Check c) {
		log.add(new LoggedEvent("Received msgHereIsChange from cashier. Change = " + c.getChange()));
	}
	
	public void msgPayNextTime() {
		log.add(new LoggedEvent("Received msgPayNextTime from cashier."));
	}

//empty messages; not necessary to test cashier
	public String getName() { return getName(); }
	
	public CustomerGui getGui() { return null; }
	
	public void msgGotHungry() {	}
	
	public void msgPleaseWaitHere(int n) {	}
	
	public void msgRestaurantFull() {	}
	
	public void msgFollowMe(Menu m, Waiter w) {	}
	
	public void msgWhatDoYouWant() {	}
	
	public void msgOutOfChoice() {	}
	
	public void msgHereIsFood() {	}
	
	public void msgDoneEating() {	}
	
	public void msgHereIsCheck(Check c) {	}
}