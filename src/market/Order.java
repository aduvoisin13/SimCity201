package market;

import java.util.Map;

import base.Item.EnumMarketItemType;
import base.interfaces.Role;
import market.interfaces.Cashier;
import market.interfaces.DeliveryTruck;
import market.interfaces.Worker;

public class Order {
	public static enum EnumOrderStatus {CARTED, PLACED, PAYING, PAID, ORDERING, DELIVERING, FULFILLING, DONE};
	public EnumOrderStatus mStatus;
	public static enum EnumOrderEvent {ORDER_PLACED, RECEIVED_INVOICE, ORDER_PAID, TOLD_TO_FULFILL, TOLD_TO_SEND, TOLD_TO_DELIVER, RECEIVED_ORDER, NONE};
	public EnumOrderEvent mEvent;
	public Map<String, Integer> mItems;
	public Role mPersonRole;
	public Worker mWorker;
	public Cashier mCashier;
	public DeliveryTruck mDeliveryTruck;
	
	public Order(Map<String, Integer> items, Role person) {
		mItems = items;
		mPersonRole = person;
		mStatus = EnumOrderStatus.CARTED;
		mEvent = EnumOrderEvent.NONE;
	}
}
