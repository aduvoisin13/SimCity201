package restaurant.restaurant_maggiyan.test.mock;


import restaurant.restaurant_maggiyan.Check;
import restaurant.restaurant_maggiyan.Menu;
import restaurant.restaurant_maggiyan.interfaces.MaggiyanCashier;
import restaurant.restaurant_maggiyan.interfaces.MaggiyanCustomer;
import restaurant.restaurant_maggiyan.interfaces.MaggiyanWaiter;
import restaurant.restaurant_maggiyan.roles.MaggiyanSharedWaiterRole;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class MockCustomer extends Mock implements MaggiyanCustomer {

	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public MaggiyanCashier cashier;
	public MaggiyanSharedWaiterRole waiter; 

	public MockCustomer(String name) {
		super(name);

	}

//	@Override
//	public void HereIsYourTotal(double total) {
//		log.add(new LoggedEvent("Received HereIsYourTotal from cashier. Total = "+ total));
//
//		if(this.name.toLowerCase().contains("thief")){
//			//test the non-normative scenario where the customer has no money if their name contains the string "theif"
//			cashier.IAmShort(this, 0);
//
//		}else if (this.name.toLowerCase().contains("rich")){
//			//test the non-normative scenario where the customer overpays if their name contains the string "rich"
//			cashier.HereIsMyPayment(this, Math.ceil(total));
//
//		}else{
//			//test the normative scenario
//			cashier.HereIsMyPayment(this, total);
//		}
//	}
//
//	@Override
//	public void HereIsYourChange(double total) {
//		log.add(new LoggedEvent("Received HereIsYourChange from cashier. Change = "+ total));
//	}
//
//	@Override
//	public void YouOweUs(double remaining_cost) {
//		log.add(new LoggedEvent("Received YouOweUs from cashier. Debt = "+ remaining_cost));
//	}

	@Override
	public void msgRestaurantFull() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgFollowMe(MaggiyanWaiter w, Menu m, int tableNumber) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgWhatDoYouWant() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsYourFood() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsCheck(Check c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOutOfYourChoice() {
		// TODO Auto-generated method stub
		
	}

	
}
