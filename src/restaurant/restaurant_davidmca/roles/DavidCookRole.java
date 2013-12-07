package restaurant.restaurant_davidmca.roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import restaurant.intermediate.RestaurantCookRole;
import restaurant.restaurant_davidmca.Order;
import restaurant.restaurant_davidmca.Table;
import restaurant.restaurant_davidmca.gui.CookGui;
import restaurant.restaurant_davidmca.interfaces.Cook;
import restaurant.restaurant_davidmca.interfaces.Market;
import restaurant.restaurant_davidmca.interfaces.Waiter;
import base.BaseRole;
import base.Item;
import base.Location;
import base.interfaces.Person;
import base.reference.ContactList;

/**
 * Restaurant customer restaurant_davidmca.agent.
 */
public class DavidCookRole extends BaseRole implements Cook {
	private RestaurantCookRole mRole;
	private CookGui cookGui;
	private Semaphore isAnimating = new Semaphore(1, true);
	private boolean ordering;
	private boolean reorder;
	private Request currentRequest;
	private final static int orderThreshold = 1;

	private class Request {
		Map<String, Integer> stuffToBuy;
		List<Market> askedMarkets;

		private Request() {
			stuffToBuy = Collections
					.synchronizedMap(new HashMap<String, Integer>());
			askedMarkets = Collections
					.synchronizedList(new ArrayList<Market>());
		}
	}

	public List<Order> pendingOrders = Collections
			.synchronizedList(new ArrayList<Order>());
	public List<Order> revolvingStand = Collections
			.synchronizedList(new ArrayList<Order>());
//	List<MarketAgent> marketList = Collections
//			.synchronizedList(new ArrayList<MarketAgent>());
	int cooktime = 5000; // hack

	public enum OrderState {
		Received, Cooking, Finished
	};

//	Map<String, Stock> foodList = new HashMap<String, Stock>();

	private String name;

	Timer cookTimer = new Timer();
	Timer standTimer = new Timer();
	boolean timeToCheckRevolvingStand = false;
	TimerTask standTimerTask = new TimerTask() {
		public void run() {
			if (mPerson != null) {
				stateChanged();
			}
		}
	};

	// restaurant_davidmca.agent correspondents
	/**
	 * Constructor for CookAgent class
	 * 
	 * @param name
	 *            name of the customer
	 */
	public DavidCookRole(Person p, RestaurantCookRole r) {
		super(p);
		mRole = r;
		this.name = "DavidCook";
		ordering = false;
		reorder = false;
//		foodList.put("Steak", new Stock("Steak", qty));
//		foodList.put("Salad", new Stock("Salad", qty));
//		foodList.put("Chicken", new Stock("Chicken", qty));
//		foodList.put("Pizza", new Stock("Pizza", qty));
		/*ANGELICA:
		mItemInventory.put(EnumItemType.STEAK,DEFAULT_FOOD_QTY);
        mItemInventory.put(EnumItemType.CHICKEN,DEFAULT_FOOD_QTY);
        mItemInventory.put(EnumItemType.SALAD,DEFAULT_FOOD_QTY);
        mItemInventory.put(EnumItemType.PIZZA,DEFAULT_FOOD_QTY);
        */
		standTimer.scheduleAtFixedRate(standTimerTask, new Date( System.currentTimeMillis() + 10000), 10000);
	}

	public String getName() {
		return name;
	}

	public void setGui(CookGui g) {
		cookGui = g;
	}

//	public void addMarket(MarketAgent mkt) {
//		marketList.add(mkt);
//	}

	// Messages

	public void msgHereIsAnOrder(Waiter w, String choice, Table t) {
		pendingOrders.add(new Order(w, choice, t));
		stateChanged();
	}

	public void msgDoneAnimating() {
		isAnimating.release();
		stateChanged();
	}

//	public void msgOrderFullFillment(Market mkt, List<Stock> recieved) {
//		synchronized (recieved) {
//			for (Stock stock : recieved) {
//				Iterator<Entry<String, Integer>> it = currentRequest.stuffToBuy
//						.entrySet().iterator();
//				while (it.hasNext()) {
//					Entry<String, Integer> r = it.next();
//					if (stock.getChoice() == r.getKey()) {
//						if (stock.getQuantity() == 0) {
//							print(mkt.getName()
//									+ " didn't have "
//									+ stock.getChoice()
//									+ ", adding this market to blacklist so we order elsewhere");
//							currentRequest.askedMarkets.add(mkt);
//						} else {
//							foodList.get(stock.getChoice()).setQuantity(
//									stock.getQuantity());
//							print("Quantity in stock of " + stock.getChoice()
//									+ " is now " + stock.getQuantity());
//							it.remove();
//						}
//					}
//				}
//			}
//		}
//		if (currentRequest.stuffToBuy.size() > 0) {
//			reorder = true;
//		} else {
//			ordering = false;
//			reorder = false;
//			currentRequest = null;
//		}
//		stateChanged();
//	}

	// Scheduler

	public boolean pickAndExecuteAnAction() {
		if (revolvingStand.size() > 0) {
			CheckStand();
			return true;
		}
//		if (reorder) {
//			reorder = false;
//			DoOrderFood();
//			return true;
//		}
//		if (!ordering) {
//			ordering = true;
//			currentRequest = new Request();
//			for (Map.Entry<String, Stock> f : foodList.entrySet()) {
//				if (f.getValue().getQuantity() == 0) {
//					currentRequest.stuffToBuy.put(f.getKey(), defaultqty);
//				}
//			}
//			if (currentRequest.stuffToBuy.size() > 0) {
//				DoOrderFood();
//			}
//			return true;
//		}
		if (!pendingOrders.isEmpty()) {
			if (pendingOrders.get(0).status == OrderState.Finished) {
				Notify(pendingOrders.get(0));
				return true;
			}
			if (pendingOrders.get(0).status == OrderState.Received) {
				pendingOrders.get(0).status = OrderState.Cooking;
				Cook(pendingOrders.get(0));
				print("cooking order...");
				return true;
			}
		}
		return false;
	}

	// Actions

//	private void DoOrderFood() {
//		for (Market mkt : marketList) {
//			if (!currentRequest.askedMarkets.contains(mkt)) {
//				print("Ordering from " + mkt.getName());
//				mkt.msgWantToBuy(this, currentRequest.stuffToBuy);
//				break;
//			}
//		}
//	}

	private void Cook(Order order) {
		cookGui.setLabelText("Picking up order");
		cookGui.DoGoToPlating();
		try {
			isAnimating.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		cookGui.setLabelText("Going to Fridge");
		cookGui.DoGoToFridge();
		try {
			isAnimating.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		cookGui.setFood();
		Random rand = new Random();
		int randomNum = rand.nextInt(4) + 1;
		cookGui.setLabelText("Going to Grill " + randomNum);
		cookGui.DoGoToGrill(randomNum);
		try {
			isAnimating.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		final String thischoice = order.choice;
		if (mRole.mItemInventory.get(Item.stringToEnum(thischoice)) == 0) {
			order.waiter.msgOutOfFood(thischoice);
			pendingOrders.remove(order);
			return;
		}
		cookGui.setLabelText("Cooking");
		cookTimer.schedule(new TimerTask() {
			public void run() {
				try {
					isAnimating.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				cookGui.setLabelText("Going to Plating");
				cookGui.DoGoToPlating();
				try {
					isAnimating.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				pendingOrders.get(0).status = OrderState.Finished;
//				foodList.get(thischoice).decrementQuantity();
				mRole.mItemInventory.put(Item.stringToEnum(thischoice), mRole.mItemInventory.get(Item.stringToEnum(thischoice))-1);
				if (mRole.mItemInventory.get(Item.stringToEnum(thischoice)) < orderThreshold) {
					mRole.mItemsDesired.put(Item.stringToEnum(thischoice), 5);
				}
				cookGui.setLabelText("");
				cookGui.removeFood();
				cookGui.DoGoToHome();
				try {
					isAnimating.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (isAnimating.availablePermits() == 0) {
					isAnimating.release();
				}
			}
		}, cooktime);

	}

	private void Notify(Order order) {
		print("Notifying");
		order.waiter.msgOrderIsReady(order);
		pendingOrders.remove(order);
	}

	private void CheckStand() {
		cookGui.setLabelText("Checking Revolving Stand");
		cookGui.DoGoToPlating();
		try {
			isAnimating.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		synchronized (revolvingStand) {
			synchronized (pendingOrders) {
				Iterator<Order> itr = revolvingStand.iterator();
				while (itr.hasNext()) {
					Order order = itr.next();
					pendingOrders.add(order);
					itr.remove();
				}
			}
		}
		cookGui.setLabelText("");
		cookGui.DoGoToHome();
		try {
			isAnimating.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

//	public Collection<MarketAgent> getMarketList() {
//		return marketList;
//	}

	@Override
	public List<Order> getRevolvingStand() {
		return revolvingStand;
	}
	
	@Override
	public Location getLocation() {
		return ContactList.cRESTAURANT_LOCATIONS.get(4);
	}

}