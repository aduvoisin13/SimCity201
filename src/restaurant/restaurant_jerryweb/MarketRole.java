package restaurant.restaurant_jerryweb;

import restaurant.restaurant_jerryweb.agents.Agent;
import restaurant.restaurant_jerryweb.CashierRole.mBillState;
import restaurant.restaurant_jerryweb.CookRole;
import restaurant.restaurant_jerryweb.CookRole.Food;
import restaurant.restaurant_jerryweb.CookRole.FoodState;
import restaurant.restaurant_jerryweb.CookRole.OrderState;
import restaurant.restaurant_jerryweb.CustomerRole.AgentEvent;
import restaurant.restaurant_jerryweb.WaiterRole.CustomerState;
import restaurant.restaurant_jerryweb.gui.HostGui;
import restaurant.restaurant_jerryweb.gui.Menu;
import restaurant.restaurant_jerryweb.interfaces.Market;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Market Agent
 */

public class MarketRole extends Agent implements Market {
	static final int semaphoreCerts = 0;
	public List<Food> foodItems= Collections.synchronizedList(new ArrayList<Food>());
	public List<Order> restockOrder = Collections.synchronizedList(new ArrayList<Order>());
	//public List<Bill>
	public Menu m = new Menu();
	public Map<String,Food> stockMap = new HashMap<String,Food>(4);	
	private CookRole cook;	
	private CashierRole cashier;
	double bill = 0;
	
	public class Order{
		OrderState s;
		String name;
		double cost;
		
		Map<String,CookRole.Food> cookInventory;
		public Order(String nm,Map<String, CookRole.Food> foodMap, OrderState orderS, double price){
			cookInventory = foodMap;
			
			name = nm;
			s = orderS;
			cost = price;
		}
	}

	public enum OrderState
	{pending, sent, Paid, Done}
	
	public class Food {
		String type;
		int amount; 
		double price;
		public Food(String foodType,  int quantity, double cost){
			type = foodType;
			amount = quantity; 
			price = cost; 
		}
	}
	
	public enum FoodState
	{idle,ordered, delivered}
	
	private String name;
	//private Semaphore atTable = new Semaphore(semaphoreCerts,true);

	//public HostGui hostGui = null;

	public MarketRole(String name) {
		super();
		this.name = name;
		//if(name.equals())
		stockMap.put("steak",new Food("steak", 15, 7));
		stockMap.put("chicken",new Food("chicken", 21, 5));
		stockMap.put("salad",new Food("salad", 30, 3));
		stockMap.put("pizza",new Food("pizza", 24, 4));
	}
	
	public void setCook(CookRole myCook) {
		this.cook = myCook;

	}
	
	public void setCashier(CashierRole myCashier) {
		this.cashier = myCashier;

	}
	
	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}
	// Messages
	
	public void msgGiveMeOrder(String choice, Map<String, CookRole.Food> foodMap){
		print("Ok cook here is your order.");
		restockOrder.add(new Order(choice, foodMap, OrderState.pending,0));
		stateChanged();
		
	}
	
	public void msgPayment(double income, int BN){
		synchronized(restockOrder){
		for(int i=0; i<restockOrder.size(); i++){
			//if(restockOrder.get(i).cost == income){
			if(i == BN && income == restockOrder.get(i).cost){
				restockOrder.get(i).s = OrderState.Paid;
				stateChanged();
			}
		}
		}
	}
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		try{
		for(int i=0; i<restockOrder.size(); i++){
			if(restockOrder.get(i).s == OrderState.pending){
				//restockOrder.get(i).
				processRequest(restockOrder.get(i), i);
				
				return true;
			}
		}
		
		for(int i=0; i<restockOrder.size(); i++){
			if(restockOrder.get(i).s == OrderState.Paid){
				CompleteTransaction(restockOrder.get(i));
				return true;
			}
		
		}
		
		return false;}
		catch (ConcurrentModificationException e) {
		
		return false;
		}
	}

	// Actions
	public void CompleteTransaction(Order order){
		cashier.msgMarketPaid(this);
		order.s = OrderState.Done;
	}
	
	public void processRequest(Order order, int x){
		int orderSize = 0;
	
		/*if(stockMap.get("steak").amount > 0){
			//if(stockMap.get("steak").amount >= order.cookInventory.get("steak").capacity){
			orderSize = order.cookInventory.get("steak").capacity - order.cookInventory.get("steak").amount;
			
			if((stockMap.get("steak").amount - orderSize) >= 0){
				order.cookInventory.get("steak").amount = orderSize;
				stockMap.get("steak").amount = stockMap.get("steak").amount - orderSize;
				print("We will give you " + orderSize + " steaks");
				cook.msgHereIsOrder(order.name, order.cookInventory);
				order.s = OrderState.sent;
				stateChanged();
			}
			else{
				order.cookInventory.get("steak").amount = stockMap.get("steak").amount;
				stockMap.get("steak").amount = 0;
				cook.msgHereIsOrder(order.name, order.cookInventory);
				order.s = OrderState.sent;
				print("We will give you " + orderSize + " steaks");
				stateChanged();
			}
		}
		else{
			print("Steak is out of Stock!");
				cook.msgOutOfStock("steak");
				order.s = OrderState.out;
				stateChanged();
		}*/
		
		for(int i=0; i<4; i++){
			String foodItem = "";
 			if( i == 0){
 				foodItem = "steak";
 			}
 		
 			if(i == 1){
 				foodItem = "chicken";
 			}
 			
 			if(i == 2){
 				foodItem = "salad";
 			}
 		
 			if(i == 3){
 				foodItem = "pizza";
 			}
 		
 			if(stockMap.get(foodItem).amount > 0){
			//if(stockMap.get(foodItem).amount >= order.cookInventory.get(foodItem).capacity){
				orderSize = order.cookInventory.get(foodItem).capacity - order.cookInventory.get(foodItem).amount;
			
				if((stockMap.get(foodItem).amount - orderSize) >= 0){
					order.cookInventory.get(foodItem).amount = order.cookInventory.get(foodItem).amount + orderSize;
					stockMap.get(foodItem).amount = stockMap.get(foodItem).amount - orderSize;
					order.cost = order.cost + stockMap.get(foodItem).price*orderSize;
					print("We will give you " + orderSize + " " + foodItem);
					
				}
				else{
					order.cookInventory.get(foodItem).amount = stockMap.get(foodItem).amount;
					stockMap.get(foodItem).amount = 0;
					order.cost = order.cost + stockMap.get(foodItem).price*orderSize;
					print("We will give you " + orderSize + " " + foodItem);
				}
			}
			else{
				print("" + foodItem + " is out of Stock!");
					cook.msgOutOfStock(this, foodItem);
					//order.s = OrderState.out;
					stateChanged();
			}
		}
		cook.msgHereIsOrder(order.name, order.cookInventory);
		cashier.msgPayMarket(this, order.cost, x);
		order.s = OrderState.sent;
		stateChanged();
	}
	//utilities
	public List<Order> getRestockList(){
		return restockOrder;
	}


}