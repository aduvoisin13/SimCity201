package market.roles;

import java.util.*;
import java.util.concurrent.Semaphore;

import market.*;
import market.MarketOrder.EnumOrderEvent;
import market.MarketOrder.EnumOrderStatus;
import market.gui.MarketCashierGui;
import market.interfaces.*;
import base.*;
import base.interfaces.Role;

/*
 	SHANE ANGELICA: Check to make sure all of these apply
 	1) Each market has its own owner/cashier who handles money.
	2) Each market can have any restaurant/person as a client. 
	3) Restaurants are delivered to, persons must go to the market.
	4) Markets can run out of inventory. They can be resupplied from the gui.
 */
public class MarketCashierRole extends BaseRole implements MarketCashier{
	PersonAgent mPerson;
	Market mMarket;
	
	MarketCashierGui mGui;
	Semaphore inTransit = new Semaphore(0,true);
	
	int mNumWorkers = 0;
	
	Map<String, Integer> mInventory;
	
	List<MarketWorker> mWorkers = Collections.synchronizedList(new ArrayList<MarketWorker>());
	static int mWorkerIndex;
	
	List<MarketDeliveryTruck> mDeliveryTrucks = Collections.synchronizedList(new ArrayList<MarketDeliveryTruck>());
	
	int mCash;

	List<MarketOrder> mOrders = Collections.synchronizedList(new ArrayList<MarketOrder>());
	List<MarketInvoice> mInvoices = Collections.synchronizedList(new ArrayList<MarketInvoice>());
	
	public MarketCashierRole(PersonAgent person, Market m) {
		mPerson = person;
		mMarket = m;
	}
	
//	Messages
	public void msgOrderPlacement(MarketOrder order){
		mOrders.add(order);
		order.mEvent = EnumOrderEvent.ORDER_PLACED;
		stateChanged();
	}

	public void msgPayingForOrder(MarketInvoice invoice){
		if (invoice.mTotal == invoice.mPayment){
			invoice.mOrder.mEvent = EnumOrderEvent.ORDER_PAID;
		}
		else{
			//SHANE ANGELICA: What do we do if they can't pay? throw error?
		}
		stateChanged();
	}
	
/* Animation Messages */
	public void msgAnimationLeftRestaurant() {
		inTransit.release();
	}
	
	public void msgAnimationAtPosition() {
		inTransit.release();
	}
	
//	Scheduler
	public boolean pickAndExecuteAnAction(){
		/*
		 * if cashier has just started, go to position
		 */
		if (mOrders.size() > 0){
			for (MarketOrder iOrder : mOrders){
				//notify customer if an order has been placed
				if ((iOrder.mStatus == EnumOrderStatus.PLACED) && (iOrder.mEvent == EnumOrderEvent.ORDER_PLACED)){
					iOrder.mStatus = EnumOrderStatus.PAYING;
					processOrderAndNotifyPerson(iOrder);
					return true;
				}
			}
			for (MarketOrder iOrder : mOrders){
				if ((iOrder.mStatus == EnumOrderStatus.PAID) && (iOrder.mEvent == EnumOrderEvent.ORDER_PAID)){
					iOrder.mStatus = EnumOrderStatus.ORDERING;
					fulfillOrder(iOrder);
					return true;
				}
			}
		}
		/*
		 * if time for role change
		 * 	DoLeaveMarket();
		 */
		return false;
	}
	
//	Actions
	private void processOrderAndNotifyPerson(MarketOrder order){
		Map<String, Integer> cannotFulfill = new HashMap<String, Integer>();
		int cost = 0;

		for(String item : order.mItems.keySet()) {
			if(mMarket.getInventory(item) < order.mItems.get(item)) {
				cannotFulfill.put(item,order.mItems.get(item)-mMarket.getInventory(item));
				mMarket.setInventory(item,0);
				cost += mMarket.getCost(item) * mMarket.getInventory(item);
			}
			else {
				mMarket.setInventory(item, mMarket.getInventory(item)-order.mItems.get(item));
				cost += mMarket.getCost(item) * order.mItems.get(item);
			}
		}
		
		Role personRole = order.mPersonRole;
		MarketInvoice invoice = new MarketInvoice(order, cost);

		//if a cook
		if (personRole instanceof MarketCook){
			MarketCook cook = (MarketCook) order.mPersonRole;
			cook.msgInvoiceToPerson(cannotFulfill, invoice);
		}

		//if a customer
		else if (personRole instanceof MarketCustomer){
			MarketCustomer customer = (MarketCustomer) order.mPersonRole;
			customer.msgInvoiceToPerson(cannotFulfill, invoice);
		}
	}

	void fulfillOrder(MarketOrder order){
		order.mWorker = mWorkers.get(mWorkerIndex++ % mNumWorkers);
		order.mWorker.msgFulfillOrder(order);
	}
	
/* Animation Actions */
	private void DoLeaveMarket() {
		mGui.DoLeaveMarket();
		try {
			inTransit.acquire();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void DoGoToPosition() {
		mGui.DoGoToPosition();
		try {
			inTransit.acquire();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
/* Utilities */
	public void setGui(MarketCashierGui gui) {
		mGui = gui;
	}
	
	public int getNumWorkers(){
		return mNumWorkers;
	}
	
	public void addWorker(MarketWorker w) {
		mWorkers.add(w);
	}
}
