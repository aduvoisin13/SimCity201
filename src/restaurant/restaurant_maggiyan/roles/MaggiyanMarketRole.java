package restaurant.restaurant_maggiyan.roles;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import restaurant.restaurant_maggiyan.interfaces.MaggiyanCashier;
import restaurant.restaurant_maggiyan.interfaces.MaggiyanCook;
import restaurant.restaurant_maggiyan.interfaces.MaggiyanMarket;
import base.BaseRole;
import base.ContactList;
import base.Location;
import city.gui.trace.AlertTag;


/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class MaggiyanMarketRole extends BaseRole implements MaggiyanMarket{
	private String n; 
	private int maxINVENTORY = 4; 
	@SuppressWarnings("unused")
	private MaggiyanCashier cashier; 
	@SuppressWarnings("unused")
	private MaggiyanCook cook; 
	//private List<MarketOrder> marketOrders = Collections.synchronizedList(new ArrayList<MarketOrder>()); 
	public enum state {idle, fulfillingOrder};
	
	public Timer timer = new Timer(); 
	
	private Map<String, Integer> marketInventory = new HashMap<String, Integer>(); 
	public Map<String, Integer> orderToFulfill = new HashMap<String, Integer>();
	
	public MaggiyanMarketRole(String name){
		super(null);
		this.n = name;
		
		marketInventory.put("Steak", maxINVENTORY);
		marketInventory.put("Chicken", maxINVENTORY);
		marketInventory.put("Salad", maxINVENTORY);
		marketInventory.put("Pizza", maxINVENTORY);

	}
	
	public String getName(){
		return n; 
	}
	
	public void setCashier(MaggiyanCashier c){
		cashier = c; 
	}

	@Override
	public void msgRequestItems(MaggiyanCook c, Map<String, Integer> itemRequest) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsPayment(Double total) {
		// TODO Auto-generated method stub
		
	}
	
	// Messages
	
	//From Cook
//	public void msgRequestItems(MaggiyanCook c, Map<String, Integer> itemRequest)
//	{
//		print("Ordering items"); 
//		cook = c; 
//		MarketOrder order = new MarketOrder(itemRequest); 
//		marketOrders.add(order); 
//		stateChanged(); 
//		
//	}
//	
//	//From Cashier
//	public void msgHereIsPayment(Double amount){
//		print("Received payment for delivery"); 
//		stateChanged(); 
//	}
//	
//	/**
//	 * Scheduler.  Determine what action is called for, and do it.
//	 */
//	public boolean pickAndExecuteAnAction() {
//
//		if(!marketOrders.isEmpty()){
//			synchronized(marketOrders){
//				for(MarketOrder order: marketOrders){
//					FulfillOrder(order);
//					return true; 
//				}
//			}
//		}
//		return false; 
//	}
//
//	// Actions
//	public void FulfillOrder(MarketOrder order){
//		int emptyCounter = 0; 
//		for(Map.Entry<String, Integer> food : marketInventory.entrySet()){
//			if(food.getValue() == 0){
//				emptyCounter++; 
//			}
//			
//		}
//		if(emptyCounter == marketInventory.size()){
//			print("Closing market. Out of stock for all items"); 
//			cook.msgOutOfAllInventory(this); 
//			marketOrders.remove(order); 
//			return; 
//		}
//		int counter = 0; 
//		//makes new map to pass back
//		final Map<String, Integer> deliveryMap = new HashMap<String, Integer>(); 
//		final List<String> deliveryBill = new ArrayList<String>();
//		//checks each item on the order list
//		for(Map.Entry<String, Integer> orderItem : order.foodRequestMap.entrySet()){
//			//checks if requested qty is less than or equal to the market inventory the specified food item
//			if(marketInventory.get(orderItem.getKey()) == 0){
//				order.partialOrder = true;
//				counter++; 
//			}
//			else if(orderItem.getValue().compareTo(marketInventory.get(orderItem.getKey())) <= 0){
//				int updatedQty = marketInventory.get(orderItem.getKey()) - orderItem.getValue();  
//				//updates market inventory
//				marketInventory.put(orderItem.getKey(), updatedQty);
//				print(orderItem.getKey() + " Inventory: " + marketInventory.get(orderItem.getKey())); 
//				//adds items to map to pass back
//				deliveryMap.put(orderItem.getKey(), orderItem.getValue()); 
//				for(int i=0; i < orderItem.getValue(); i++){
//					deliveryBill.add(orderItem.getKey()); 
//				}
//			}
//			else{
//				//identifies order as partially complete
//				order.partialOrder = true; 
//				//stores qty available for delivery
//				int availableQty = marketInventory.get(orderItem.getKey());
//				//decrements market inventory for food item
//				marketInventory.put(orderItem.getKey(), 0);
//				//adds available item qty to map 
//				deliveryMap.put(orderItem.getKey(), availableQty);
//				for(int i = 0; i<availableQty; i++){
//					deliveryBill.add(orderItem.getKey()); 
//				}
//			}
//		}
//		if(counter == order.foodRequestMap.size()){
//			print("Item out of stock, cannot fulfill order"); 
//			cook.msgCannotFulfillOrder();
//			marketOrders.remove(order); 
//			return; 
//		}
//		if(order.partialOrder){
//			print("Fulfilling partial order");
//			cook.msgFulfillingPartialOrder();
//			order.partialOrder = false; 
//		}
//		else{
//			print("Fulfilling complete order");
//			cook.msgFulfillingOrder();
//		}
//		final MaggiyanMarket me = this; 
//		timer.schedule(new TimerTask() {
//			public void run() {
//				print("Delivering inventory order"); 
//				cook.msgDeliverOrder(deliveryMap); 
//				cashier.msgDeliverBill(me, deliveryBill); 
//			}
//		},
//		deliveryMap.size() * 10000); 
//		marketOrders.remove(order); 
//	}
//	
//	//Utilities
//	private class MarketOrder{
//		Map<String, Integer> foodRequestMap;
//		boolean partialOrder;
//		
//		MarketOrder(Map<String, Integer> o){
//			foodRequestMap = o; 
//			partialOrder = false; 
//		}
//	}

	@Override
	public Location getLocation() {
		return ContactList.cRESTAURANT_LOCATIONS.get(3);
	}
	
	public void Do(String msg) {
		super.Do(msg, AlertTag.R3);
	}
	
	public void print(String msg) {
		super.print(msg, AlertTag.R3);
	}
	
	public void print(String msg, Throwable e) {
		super.print(msg, AlertTag.R3, e);
	}
}
