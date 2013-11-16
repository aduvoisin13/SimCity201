package market.other;

import java.util.Map;

import base.Role;
import market.interfaces.Cashier;
import market.interfaces.DeliveryTruck;
import market.interfaces.Worker;
import market.other.Item.EnumMarketItemType;

public class Order {
	public static enum EnumOrderStatus {CARTED, PLACED, PAYING, PAID, ORDERING, DELIVERING, FULFILLING, DONE};
	public EnumOrderStatus mStatus;
	public static enum EnumOrderEvent {ORDER_PLACED, RECEIVED_INVOICE, ORDER_PAID, TOLD_TO_FULFILL, TOLD_TO_SEND, TOLD_TO_DELIVER, RECEIVED_ORDER};
	public EnumOrderEvent mEvent;
	public Map<EnumMarketItemType, Integer> mItems;
	public Role mPersonRole;
	public Worker mWorker;
	public Cashier mCashier;
	public DeliveryTruck mDeliveryTruck;
}
