package restaurant_all.interfaces.Host;

import restaurant_all.interfaces.Customer.Customer_cw;
import restaurant_cwagoner.interfaces.Waiter;

public interface Host_cw {

	public void msgIWantFood(Customer_cw cust);

	public void msgCustomerGoneTableEmpty(Customer_cw c, int tableNum);

	public void msgCanIGoOnBreak(Waiter w);

	public void msgOffBreak(Waiter w);
}