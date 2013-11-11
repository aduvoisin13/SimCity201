package restaurant_davidmca.interfaces;

import java.util.Map;

import restaurant_davidmca.CashierAgent;
import restaurant_davidmca.CookAgent;

public interface Market {

	public abstract String getName();

	public abstract void setCashier(CashierAgent cash);

	public abstract void msgWantToBuy(CookAgent c,
			Map<String, Integer> stuffToBuy);

	public abstract void msgPayInvoice(double total);

}