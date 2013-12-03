package restaurant.restaurant_cwagoner.roles;

import base.Item;
import base.Item.EnumItemType;
import base.interfaces.Person;
import restaurant.intermediate.RestaurantCookRole;
import restaurant.restaurant_cwagoner.gui.*;
import restaurant.restaurant_cwagoner.interfaces.*;

import java.util.*;
import java.util.concurrent.Semaphore;


public class CwagonerCookRole extends RestaurantCookRole implements CwagonerCook {
	
	public CwagonerCookRole(Person person) {
		super(person);

		mItemInventory.put(EnumItemType.STEAK,DEFAULT_FOOD_QTY);
        mItemInventory.put(EnumItemType.CHICKEN,DEFAULT_FOOD_QTY);
        mItemInventory.put(EnumItemType.SALAD,DEFAULT_FOOD_QTY);
        mItemInventory.put(EnumItemType.PIZZA,DEFAULT_FOOD_QTY);
	}
	
	public CwagonerCookRole() {
		mItemInventory.put(EnumItemType.STEAK,DEFAULT_FOOD_QTY);
        mItemInventory.put(EnumItemType.CHICKEN,DEFAULT_FOOD_QTY);
        mItemInventory.put(EnumItemType.SALAD,DEFAULT_FOOD_QTY);
        mItemInventory.put(EnumItemType.PIZZA,DEFAULT_FOOD_QTY);
	}

	public String getName() {
		return "CwagonerCook " + mPerson.getName();
	}
	
	CwagonerCashier cwagonerCashier;
	CwagonerCookGui gui;
	private Semaphore animationFinished = new Semaphore(0, true);
	
	// Remembers which market was previously ordered from
	
	// Remembers if currently ordering
	boolean ordering = false;
	
	
	// DATA

	// Orders uses try-catch (method 2) instead of 'synchronized'
	public List<Order> Orders = new ArrayList<Order>();
	
	Timer cookingTimer = new Timer();
	
	
	// MESSAGES
	
	// From GUI
	public void msgAnimationFinished() {
		animationFinished.release();
	}
	
	// From waiter delivering customer's order
	public void msgHeresAnOrder(CwagonerWaiter w, int tableNum, String food) {
		print("Received msgHeresAnOrder(" + w.getName() + ", table " + tableNum + ", " + food + ")");
		
		Orders.add(new Order(w, tableNum, food));
		stateChanged();
	}

	
	// SCHEDULER
	
	public boolean pickAndExecuteAnAction() {
		
		try {
			for (Order o : Orders) {
				if (o.state.equals(Order.State.readyToDeliver)) {
					AlertWaiter(o);
					return true;
				}
			}
		
			for (Order o : Orders) {
				if (o.state.equals(Order.State.received)) {
					Prepare(o);
					return true;
				}
			}
		} catch (ConcurrentModificationException e) {
			return false;
		}
			
		return false;
	}
	
	
	// ACTIONS
	
	private void AlertWaiter(Order o) {
		print("AlertWaiter() " + o.waiter.getName() + ", table " + o.tableNum + ", " + o.food);

		
		// Move food from cooking to plating
		gui.DoGoToCooking();
		try { animationFinished.acquire(); } catch (InterruptedException e) {}
		gui.DoDrawFood(o.food.substring(0, 2));
		gui.DoGoToPlating();
		try { animationFinished.acquire(); } catch (InterruptedException e) {}
		
		gui.DoClearFood();
		gui.DoGoToHomePosition();
		
		o.waiter.msgOrderReady(o.tableNum);
		Orders.remove(o);
		stateChanged();
	}
	
	private void Prepare(final Order o) {
		print("Prepare(" + o.waiter.getName() + ", table " + o.tableNum + ", " + o.food +")");
		
		o.state = Order.State.cooking;
		
		
		// Check if food exists in fridge
		gui.DoDrawFood(o.food.substring(0, 2) + "?");
		gui.DoGoToFridge();
		try { animationFinished.acquire(); } catch (InterruptedException e) {}

		
		if (mItemInventory.get(Item.stringToEnum(o.food)) == 0) {
			o.waiter.msgOutOfFood(o.tableNum);
			gui.DoClearFood();
			gui.DoGoToHomePosition();
			Orders.remove(o);
			return;
		}
		decreaseInventory(Item.stringToEnum(o.food));
		
		
		// Take food to cooking area
		gui.DoDrawFood(o.food.substring(0, 2));
		gui.DoGoToCooking();
		try { animationFinished.acquire(); } catch (InterruptedException e) {}
		gui.DoClearFood();
		gui.DoGoToHomePosition();
		
		// Schedule timer task based on correct hashmap times
		cookingTimer.schedule(new TimerTask() {
			public void run() {
				o.state = Order.State.readyToDeliver;
				stateChanged();
			}
		}, cookTimeOf(o.food));
		// Timer finishes, changing order state to ready

		stateChanged();
	}
	
	
	// CLASSES
	
	private int cookTimeOf(String food) {
		if (food.equals("Steak")) return 8000;
		else if (food.equals("Chicken")) return 6000;
		else if (food.equals("Pizza")) return 4000;
		else return 2000; // salad
	}


	public static class Order {
		private CwagonerWaiter waiter;
		private int tableNum;
		private String food;
		public enum State { received, cooking, readyToDeliver };
		private State state;
		
		public Order(CwagonerWaiter w, int table, String choice) {
			waiter = w;
			tableNum = table;
			food = choice;
			state = State.received;
		}
	}
	
	// MenuItems accessors
	
	public void setCashier(CwagonerCashier c) {
		cwagonerCashier = c;
	}
	
	public void setGui(CwagonerCookGui g) {
		gui = g;
	}
}
