package restaurant_smileham.test.mock;


import restaurant_smileham.Food.EnumFoodOptions;
import restaurant_smileham.Menu;
import restaurant_smileham.gui.CustomerGui;
import restaurant_smileham.interfaces.Cashier;
import restaurant_smileham.interfaces.Customer;
import restaurant_smileham.interfaces.Host;
import restaurant_smileham.interfaces.Waiter;
import restaurant_smileham.agent.Check;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class MockCustomer extends Mock implements Customer {

	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public Cashier mCashier;

	public MockCustomer(String name) {
		super(name);

	}

	@Override
	public void msgGotHungry() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgRestaurantFull() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgSitAtTable(Waiter waiter, int tableNum, Menu menu) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAnimationFinishedGoToSeat() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgWhatWouldYouLike(Menu menu) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsYourFood(EnumFoodOptions choice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgCheckDelivered(Check check) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgGoodToGo(int change) {
		log.add(new LoggedEvent("msgGoodToGo(" + change + ")"));
	}

	@Override
	public void msgAnimationFinishedLeaveRestaurant() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setHost(Host host) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getCustomerName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getHungerLevel() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getCash() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setHungerLevel(int hungerLevel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGui(CustomerGui g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CustomerGui getGui() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isHungry() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void msgAnimationPickedUp() {
		// TODO Auto-generated method stub
		
	}

}
