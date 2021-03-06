package market.roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import market.MarketInvoice;
import market.MarketOrder;
import market.MarketOrder.EnumOrderEvent;
import market.MarketOrder.EnumOrderStatus;
import market.gui.MarketCustomerGui;
import market.interfaces.MarketCashier;
import market.interfaces.MarketCustomer;
import base.BaseRole;
import base.ContactList;
import base.Item;
import base.Item.EnumItemType;
import base.Location;
import base.PersonAgent;
import base.interfaces.Person;
import city.gui.trace.AlertTag;

public class MarketCustomerRole extends BaseRole implements MarketCustomer {
	int mMarketID;
	//DATA
	private MarketCustomerGui mGui;
	private Semaphore inTransit = new Semaphore(0,true);
	
	List<MarketOrder> mOrders = Collections.synchronizedList(new ArrayList<MarketOrder>());
	List<MarketInvoice> mInvoices	= Collections.synchronizedList(new ArrayList<MarketInvoice>());

	public Map<EnumItemType, Integer> mItemInventory;// = Collections.synchronizedMap(new HashMap<EnumItemType, Integer>());
	public Map<EnumItemType, Integer> mItemsDesired;// = Collections.synchronizedMap(new HashMap<EnumItemType,Integer>());
	
	Map<EnumItemType, Integer> mCannotFulfill;// = new HashMap<EnumItemType, Integer>();

	public MarketCashier mCashier;
	
	public MarketCustomerRole(Person person, int marketID) {
		super(person);
		mMarketID = marketID;
		
		//Add guis
		mGui = new MarketCustomerGui(this);
		ContactList.sMarketList.get(mMarketID).mGuis.add(mGui);
		
		mCashier = ContactList.sMarketList.get(mMarketID).mCashier;
		mItemInventory = person.getItemInventory();
		mItemsDesired = person.getItemsDesired();
	}
	
	//MESSAGES
	public void msgInvoiceToPerson(Map<EnumItemType, Integer> cannotFulfill, MarketInvoice invoice) {
		mInvoices.add(invoice);
		mCannotFulfill = cannotFulfill;
		invoice.mOrder.mEvent = EnumOrderEvent.RECEIVED_INVOICE;
		stateChanged();
	}


	public void msgHereIsCustomerOrder(MarketOrder order){
		order.mEvent = EnumOrderEvent.RECEIVED_ORDER;
		stateChanged();
	}
	
/* Animation Messages */
	public void msgAnimationAtMarket() {
		inTransit.release();
	}
	
	public void msgAnimationAtWaitingArea() {
		inTransit.release();
	}
	
	public void msgAnimationLeftMarket() {
		inTransit.release();
	}
	
	//SCHEDULER
	public boolean pickAndExecuteAnAction(){
		for(MarketInvoice invoice : mInvoices) {
			MarketOrder order = invoice.mOrder;//ANGLE: The the status is never set to paying? -Jerry
			if(order.mStatus == EnumOrderStatus.PAYING && order.mEvent == EnumOrderEvent.RECEIVED_INVOICE) {
				order.mStatus = EnumOrderStatus.PAID;
				payAndProcessOrder(invoice);
				return true;
			}
		}
		for(MarketOrder order : mOrders) {
			if(order.mStatus == EnumOrderStatus.FULFILLING && order.mEvent == EnumOrderEvent.RECEIVED_ORDER) {
				order.mStatus = EnumOrderStatus.DONE;
				completeOrder(order);
				return true;
			}
		}
		for(MarketOrder order : mOrders) {
			if(order.mStatus == EnumOrderStatus.CARTED) {
				order.mStatus = EnumOrderStatus.PLACED;
				placeOrder(order);
				return true;
			}
		}
		//check efficiency of method
		synchronized(mItemsDesired) {
			for(EnumItemType iType : mItemsDesired.keySet()) {
			if(mItemsDesired.get(iType) != 0) {
				createOrder();
				return true;
			}
		}
		}

		return false;
	}
	
	
	//ACTIONS
	private void createOrder(){
		print("Creating an order.");
		HashMap<EnumItemType,Integer> items = new HashMap<EnumItemType,Integer>();
		for(EnumItemType iItemType : mItemsDesired.keySet()) {
			items.put(iItemType,mItemsDesired.get(iItemType));
			mItemsDesired.put(iItemType,0);
		}
		MarketOrder order = new MarketOrder(items, this);		
		mOrders.add(order);
	}

	private void placeOrder(MarketOrder order){
		print("Placing order with " + mCashier.toString());
		DoGoToMarket();
		mCashier.msgOrderPlacement(order);
	}

	private void payAndProcessOrder(MarketInvoice invoice) {
		print("Paying for order.");
		invoice.mPayment += invoice.mTotal;
		
		ContactList.SendPayment(mPerson.getSSN(), invoice.mMarketBankNumber, invoice.mPayment);
		
		synchronized(mItemsDesired) {
			for(EnumItemType item : mCannotFulfill.keySet()) {
			mItemsDesired.put(item, mItemsDesired.get(item)+mCannotFulfill.get(item));
		}
		}
		
		mCashier.msgPayingForOrder(invoice);
		mInvoices.remove(invoice);
		DoWaitForOrder();
	}

	private void completeOrder(MarketOrder order) {
		print("Completing order!");
		for(EnumItemType item : order.mItems.keySet()) {
			int n = mItemInventory.get(item);
			n += order.mItems.get(item);
			mItemInventory.put(item, n);
			if(item.equals(EnumItemType.CAR)) {
				PersonAgent p = (PersonAgent) mPerson;
				p.setHasCar(true);
			}
		}
		DoLeaveMarket();
	}
	
/* Animation Actions */
	private void DoGoToMarket() {
		mGui.DoGoToMarket();
		try {
			inTransit.acquire();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void DoWaitForOrder() {
		mGui.DoWaitForOrder();
		try {
			inTransit.acquire();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void DoLeaveMarket() {
		mGui.DoLeaveMarket();
		try {
			inTransit.acquire();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		mPerson.msgRoleFinished();
		mPerson.assignNextEvent();
	}
	
/* Utilities */
	public Location getLocation() {
		if (mMarketID == 0) {
			return ContactList.cMARKET1_LOCATION;
		}
		else if (mMarketID == 1) {
			return ContactList.cMARKET2_LOCATION;
		}
		return null;
	}
	
	public void Do(String msg) {
		super.Do(msg, AlertTag.MARKET);
	}
	
	public void print(String msg) {
		super.print(msg, AlertTag.MARKET);
	}
	
	public void print(String msg, Throwable e) {
		super.print(msg, AlertTag.MARKET, e);
	}
	
	public List<MarketOrder> getOrderList(){
		return mOrders;
	}
	
	public List<MarketInvoice> getInvoiceList(){
		return mInvoices;
	}
	
	public Map<Item.EnumItemType,Integer> getCannotFulFillMap(){
		return mCannotFulfill;
	}
}
