package restaurant.restaurant_tranac.roles;

import java.util.*;
import java.util.concurrent.Semaphore;

import market.MarketInvoice;
import market.MarketOrder;
import market.interfaces.MarketCashier;
import base.Item;
import base.Item.EnumItemType;
import restaurant.intermediate.*;
import restaurant.restaurant_tranac.gui.TranacCookGui;
import restaurant.restaurant_tranac.interfaces.*;

/**
 * Restaurant Cook Agent
 */
public class TranacRestaurantCookRole extends RestaurantCookRole implements TranacCook {
        private TranacCookGui cookGui;
        
        public enum OrderState {Pending, Cooking, Plated, PickedUp, Done, Finished};
        public enum FoodState {Good, LowStock, Ordered, NoStock};
        
//        private List<Market> markets = Collections.synchronizedList(new ArrayList<Market>());
        private List<Order> orders = Collections.synchronizedList(new ArrayList<Order>());
//        private List<Food> inventory = Collections.synchronizedList(new ArrayList<Food>());
        
        private Map<EnumItemType,Integer> mCookTimes = new HashMap<EnumItemType,Integer>();
        
        private final int NGRILLS = 3;
        private final int NPLATES = 4;
        private Map<Integer, Boolean> grills = new HashMap<Integer, Boolean>();
        private Map<Integer, Boolean> plates = new HashMap<Integer, Boolean>();
        
        private Timer timer = new Timer();
        
        private final int baseTime = 5000;
        private final int baseNeed = 3;
        private final int stockThreshold = 1;
        
        private Semaphore inTransit = new Semaphore(0, true);

        public TranacRestaurantCookRole() {
                super();
                /*
                //create inventory
                inventory.add(new Food("Steak",(int)(baseTime*2),DEFAULT_FOOD_QTY));
                inventory.add(new Food("Chicken",(int)(baseTime*1.75),DEFAULT_FOOD_QTY));
                inventory.add(new Food("Salad",baseTime,DEFAULT_FOOD_QTY));
                inventory.add(new Food("Pizza",(int)(baseTime*1.5),DEFAULT_FOOD_QTY));
                */
                //inventory created in restaurantCookRole
                mItemInventory.put(EnumItemType.STEAK,DEFAULT_FOOD_QTY);
                mItemInventory.put(EnumItemType.CHICKEN,DEFAULT_FOOD_QTY);
                mItemInventory.put(EnumItemType.SALAD,DEFAULT_FOOD_QTY);
                mItemInventory.put(EnumItemType.PIZZA,DEFAULT_FOOD_QTY);

                //create cook times
                mCookTimes.put(EnumItemType.STEAK,(int)(baseTime*2));
                mCookTimes.put(EnumItemType.CHICKEN,(int)(baseTime*1.75));
                mCookTimes.put(EnumItemType.SALAD,(int)(baseTime));
                mCookTimes.put(EnumItemType.PIZZA,(int)(baseTime));
                
                //initialize grills and plates
                for(int i=1;i<=NGRILLS;i++)
                        grills.put(i,false);
                for(int i=1;i<=NPLATES;i++)
                        plates.put(i, false);
        }

        /** Messages */

        public void msgHereIsOrder(TranacWaiter w, String c, int t) {
                orders.add(new Order(w,c,t));
                stateChanged();
        }
        
        public void msgOrderPickedUp(TranacWaiter w, String c) {
                synchronized(orders) {
                        for(Order o : orders) {
                                if(o.waiter == w && o.choice.toString().equalsIgnoreCase(c)) {
                                        o.s = OrderState.PickedUp;
                                }
                        }
                }
                stateChanged();
        }
        
        public void msgOrderDone(Order o) {
                o.s = OrderState.Done;
                stateChanged();
        }
        /*
        public void msgCanFulfillInventory(String f, int n) {
                Food food = null;
                synchronized(inventory) {
                        for(Food i : inventory) {
                                if(i.name == f)
                                        food = i;
                        }
                }
                food.s = FoodState.Ordered;
                if(n < food.numNeeded) {
                        food.s = FoodState.LowStock;
                        food.numNeeded = food.numNeeded - n;
                }
                else {
                        food.numNeeded = 0;
                }
                stateChanged();
        }
        
        public void msgHereIsInventory(String f, int n) {
                Food food = null;
                synchronized(inventory) {
                        for(Food i : inventory) {
                                if(i.name == f)
                                        food = i;
                        }
                }
                food.s = FoodState.Good;
                food.stock = food.stock + n;
                stateChanged();
        }
        
        public void msgOutOfInventory(Market m, String f) {
                Food food = null;
                synchronized(inventory) {
                        for(Food i : inventory) {
                                if(i.name == f)
                                        food = i;
                        }
                }
                food.s = FoodState.LowStock;
                food.addMarketOutOfItem(m);
                stateChanged();
        }
        */
        /** Animation Messages */
        public void msgAnimationAtGrill() {
                inTransit.release();
                stateChanged();
        }
        
        public void msgAnimationAtPlate() {
                inTransit.release();
                stateChanged();
        }
        
        /**
         * Scheduler.  Determine what action is called for, and do it.
         */
        public boolean pickAndExecuteAnAction() {
        /*        for(Food f : inventory) {
                        if(f.s == FoodState.LowStock) {
                                orderFood();
                                return true;
                        }
                }
        */
            for(Order o : orders) {
                if(o.s == OrderState.PickedUp) {
                        removeOrder(o);
                        return true;
                }
            }
            for(Order o : orders) {
                if(o.s == OrderState.Done) {
                        plateIt(o);
                        return true;
                }
            }for(Order o : orders) {
                        if(o.s == OrderState.Pending) {
                                tryToCookIt(o);
                                return true;
                        }
                }

                //ordering food items
 //               if(marketPickAndExecuteAnAction())
 //                       return true;
                DoGoToHome();
                return false;
        }

        /** Actions */

        private void tryToCookIt(final Order o) {
                EnumItemType food = o.choice;
                /*
                synchronized(inventory) {
                        for(Food i : inventory) {
                                if(i.name == o.choice)
                                        food = i;
                        }
                }
                //ANGELICA: add in functionality to order if low stock
                if(food.stock == stockThreshold && food.s != FoodState.Ordered) {
                        food.s = FoodState.LowStock;
                        Do("Low on food!");
                        food.numNeeded = baseNeed;
                }
                
                if(food.stock == 0) {                                //handles if out of food item
                        Do("Out of " + food.name);
                        o.waiter.msgOutOfFood(o.choice, o.table);
                        orders.remove(o);
                        if(food.s != FoodState.NoStock && food.s != FoodState.Ordered) {
                                food.s = FoodState.LowStock;
                                food.numNeeded = baseNeed;
                        }
                        return;
                }
                */
                
                if(mItemInventory.keySet().contains(food))
                	System.out.println("Cool.");
            
                if(mItemInventory.get(food) == 0) {
                        Do("Out of choice " + food);
                        o.waiter.msgOutOfFood(o.choice.toString(), o.table);
                        orders.remove(o);
                        mItemsDesired.put(food,baseNeed);
                        return;
                }
                
                Do("Cooking " + o.choice);
                o.s = OrderState.Cooking;

                for(int i=1;i<=NGRILLS;i++) {
                        if(!grills.get(i)) {
                                o.n = i;
                                grills.put(i, true);
                                break;
                        }
                }
                if(o.n == 0)
                        o.n = 1;
                
                DoGoToGrill(o);
                DoAddFoodItem(o);
                timer.schedule(new TimerTask() {        //runs a new timer to "cook" the food
                        public void run() {
                                msgOrderDone(o);
                        }
                },mCookTimes.get(food));
                mItemInventory.put(food,mItemInventory.get(food)-1);
        }
        
        private void plateIt(Order o) {
                Do("Plating " + o.choice.toString());
                o.s = OrderState.Plated;
                DoGoToGrill(o);
                grills.put(o.n, false);
                for(int i=1;i<=NPLATES;i++) {
                        o.n = 0;
                        if(!plates.get(i)) {
                                o.n = i;
                                plates.put(i, true);
                                break;
                        }
                }
                if(o.n == 0)
                        o.n = 1;
                
                DoGoToPlate(o);
                o.waiter.msgOrderDone(o.choice.toString(), o.table, o.n);        //messages waiter order is done
        }

        private void removeOrder(Order o) {
                Do("Removing " + o.choice);
                o.s = OrderState.Finished;
                DoRemoveOrder(o);
                plates.put(o.n, false);
                o.n = 0;
        }
        /*
        private void orderFood() {                                //order all food items that are lowStock
                Do("Ordering food");
                synchronized(inventory) {
                        for(Food f : inventory) {
                                if(f.s == FoodState.LowStock) {
                                synchronized(markets) {
                                        for(Market m : markets) {
                                                if(!f.outOfItem.isEmpty()) {
                                                        for(Market out : f.outOfItem) {
                                                                if(m != out) {
                                                                        f.s = FoodState.Ordered;
                                                                        m.msgOrderFood(this,f.name,f.numNeeded);
                                                                        break;
                                                                }
                                                        }
                                                if(f.s == FoodState.Ordered)
                                                        break;
                                                }
                                                else {
                                                        f.s = FoodState.Ordered;
                                                        m.msgOrderFood(this,f.name,f.numNeeded);
                                                        break;
                                                }
                                        }
                                }
                                if(f.s != FoodState.Ordered)
                                        f.s = FoodState.NoStock;                        //if all markets are out of stock, cannot order more
                                }
                        }
                }
        }
        */
        /** Animation Actions */
        private void DoGoToHome() {
                cookGui.DoGoToHome();
        }
        
        private void DoAddFoodItem(Order o) {
                cookGui.DoAddFoodItem(o.choice.toString(), o.n);
        }
        
        private void DoGoToGrill(Order o) {
                cookGui.DoGoToGrill(o.n);
                try {
                        inTransit.acquire();
                }
                catch (InterruptedException e) {
                        e.printStackTrace();
                }
        }
        
        private void DoGoToPlate(Order o) {
                cookGui.DoGoToPlate(o.n, o.choice.toString());                        //change to choose correct plate
                try {
                        inTransit.acquire();
                }
                catch (InterruptedException e) {
                        e.printStackTrace();
                }
        }
        
        private void DoRemoveOrder(Order o) {
                cookGui.FoodPickedUp(o.n, o.choice.toString());
        }
        
        /** Utilities */

        public String getName() {
                return mPerson.getName();
        }
        
        public void setGui(TranacCookGui c) {
                setCookGui(c);
        }
        
        public TranacCookGui getCookGui() {
                return cookGui;
        }

        public void setCookGui(TranacCookGui cookGui) {
                this.cookGui = cookGui;
        }
        /*
        public void addMarket(Market m) {
                markets.add(m);
        }
        
        public void setInventory(int n) {
                synchronized(inventory) {
                        for(Food f : inventory) {
                                f.stock = n;
                                if(n <= stockThreshold) {
                                        f.s = FoodState.LowStock;
                                        f.numNeeded = baseNeed;
                                }
                        }
                }
        }
        
        public void setInventory(String f, int n) {
                synchronized(inventory) {
                        for(Food food : inventory) {
                                if(food.name.equals(f)) {
                                        food.stock = n;
                                        if(n <= stockThreshold) {
                                                food.s = FoodState.LowStock;
                                                food.numNeeded = baseNeed;
                                        }
                                }
                        }
                }
        }
*/
        /** Classes */
        
        private class Order {                //holds all relevant information for the order
                TranacWaiter waiter;
//                String choice;
                EnumItemType choice;
                int table;
                int n;
                OrderState s;
                
                Order(TranacWaiter w, String c, int t) {
                        this.waiter = w;
                        choice = Item.stringToEnum(c);
                        table = t;
                        n = 0;
                        s = OrderState.Pending;
                }
        }
        
        private class Food {
                String name;
                int cookingTime;
                int stock;
                int numNeeded;
                FoodState s;
                List<TranacMarket> outOfItem = new ArrayList<TranacMarket> ();
                
                Food(String n, int c, int s) {
                        name = n;
                        cookingTime = c;
                        stock = s;
                        this.s = FoodState.Good;
                        numNeeded = 0;
                }
                
                public void addMarketOutOfItem(TranacMarket m) {
                        outOfItem.add(m);
                }
        } 
}