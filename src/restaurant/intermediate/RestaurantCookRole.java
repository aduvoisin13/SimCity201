package restaurant.intermediate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import market.MarketInvoice;
import market.MarketOrder;
import market.MarketOrder.EnumOrderEvent;
import market.MarketOrder.EnumOrderStatus;
import market.interfaces.MarketCashier;
import restaurant.intermediate.interfaces.RestaurantBaseInterface;
import restaurant.intermediate.interfaces.RestaurantCashierInterface;
import restaurant.intermediate.interfaces.RestaurantCookInterface;
import restaurant.restaurant_cwagoner.CwagonerRestaurant;
import restaurant.restaurant_cwagoner.roles.CwagonerCookRole;
import restaurant.restaurant_davidmca.DavidRestaurant;
import restaurant.restaurant_davidmca.roles.DavidCookRole;
import restaurant.restaurant_duvoisin.AndreRestaurant;
import restaurant.restaurant_duvoisin.roles.AndreCookRole;
import restaurant.restaurant_jerryweb.JerrywebCookRole;
import restaurant.restaurant_jerryweb.JerrywebRestaurant;
import restaurant.restaurant_maggiyan.MaggiyanRestaurant;
import restaurant.restaurant_maggiyan.roles.MaggiyanCookRole;
import restaurant.restaurant_smileham.SmilehamRestaurant;
import restaurant.restaurant_smileham.roles.SmilehamCookRole;
import restaurant.restaurant_tranac.TranacRestaurant;
import restaurant.restaurant_tranac.roles.TranacCashierRole;
import restaurant.restaurant_tranac.roles.TranacCookRole;
import restaurant.restaurant_xurex.RexCookRole;
import restaurant.restaurant_xurex.gui.RexAnimationPanel;
import base.BaseRole;
import base.ContactList;
import base.Item.EnumItemType;
import base.Location;
import base.interfaces.Person;
import base.interfaces.Role;
import city.gui.trace.AlertTag;

public class RestaurantCookRole extends BaseRole implements RestaurantCookInterface, RestaurantBaseInterface {
        
        static int totalCooks = 0;
        
        public Role subRole = null;
        private RestaurantCashierInterface mRestaurantCashier;

        int mRestaurantID;
        public int DEFAULT_FOOD_QTY = 2;
        private AlertTag mAlertTag;
        
        public RestaurantCookRole(Person person, int restaurantID){
                super(person); 
                this.mRestaurantID = restaurantID;
                
                //populate maps
        		mItemInventory.put(EnumItemType.STEAK,DEFAULT_FOOD_QTY);
        		mItemInventory.put(EnumItemType.CHICKEN,DEFAULT_FOOD_QTY);
        		mItemInventory.put(EnumItemType.SALAD,DEFAULT_FOOD_QTY);
        		mItemInventory.put(EnumItemType.PIZZA,DEFAULT_FOOD_QTY);
        		
        		mHasCreatedOrder.put(EnumItemType.STEAK,false);
        		mHasCreatedOrder.put(EnumItemType.CHICKEN,false);
        		mHasCreatedOrder.put(EnumItemType.SALAD,false);
        		mHasCreatedOrder.put(EnumItemType.PIZZA,false);
        		
        		mItemsDesired.put(EnumItemType.STEAK,0);
        		mItemsDesired.put(EnumItemType.CHICKEN,0);
        		mItemsDesired.put(EnumItemType.SALAD,0);
        		mItemsDesired.put(EnumItemType.PIZZA, 0);
        		
               	mRestaurantCashier = null;
        
        }
        
        public void setPerson(Person person){
            super.mPerson = person;
        	switch(mRestaurantID){
				case 0: //andre
					mAlertTag = AlertTag.R0;
					subRole = new AndreCookRole(super.mPerson, this);
					if(AndreRestaurant.cook == null) {
						AndreRestaurant.addCook((AndreCookRole) subRole);
					} else {
						subRole = AndreRestaurant.cook;
					}
					break;
				case 1: //chase
					mAlertTag = AlertTag.R1;
					subRole = new CwagonerCookRole(super.mPerson, this);
					if (CwagonerRestaurant.cook == null) {
						CwagonerRestaurant.addPerson((CwagonerCookRole) subRole);
					}
					else {
						subRole = CwagonerRestaurant.cook;
					}
					break;
				case 2: //jerry
					mAlertTag = AlertTag.R2;
					subRole = new JerrywebCookRole(super.mPerson, this);
					if (JerrywebRestaurant.cook == null) {
						JerrywebRestaurant.addPerson((JerrywebCookRole) subRole);
					} else {
						subRole = JerrywebRestaurant.cook;
					}
					break;
				case 3: //maggi
					mAlertTag = AlertTag.R3;
					subRole = new MaggiyanCookRole(super.mPerson, this);
					MaggiyanRestaurant.addCook((MaggiyanCookRole) subRole);
					break;
				case 4: //david
					mAlertTag = AlertTag.R4;
					subRole = new DavidCookRole(super.mPerson, this);
					if (DavidRestaurant.cook == null) {
						DavidRestaurant.addCook((DavidCookRole) subRole);
					} else {
						subRole = DavidRestaurant.cook;
					}
					break;
				case 5: //shane
					mAlertTag = AlertTag.R5;
					subRole = new SmilehamCookRole(super.mPerson, this);
					if (SmilehamRestaurant.mCook == null) {
						SmilehamRestaurant.addPerson((SmilehamCookRole) subRole);
					} else {
						subRole = SmilehamRestaurant.mCook;
					}
					break;
				case 6: //angelica
					mAlertTag = AlertTag.R6;
					subRole = new TranacCookRole(mPerson, this);
					TranacRestaurant.addPerson((TranacCookRole)subRole);
					break;
				case 7: //rex
					mAlertTag = AlertTag.R7;
					subRole = new RexCookRole(super.mPerson, this);
					RexAnimationPanel.addPerson((RexCookRole) subRole);
					break;
			}
       }
        
        public boolean pickAndExecuteAnAction() {
    		if(marketPickAndExecuteAnAction())
    			return true;
        	if(subRole != null) {
        		if(subRole.pickAndExecuteAnAction())
        			return true;
        	}
        	return false;
        }

/** MarketCookCustomerRole Data, Actions, Scheduler, etc **/

        public Map<EnumItemType, Integer> mItemInventory = new HashMap<EnumItemType, Integer>();
    	public Map<EnumItemType, Integer> mItemsDesired = new HashMap<EnumItemType, Integer>();
        
        public Map<EnumItemType, Integer> mCannotFulfill = new HashMap<EnumItemType, Integer>();
        
        public Map<EnumItemType, Boolean> mHasCreatedOrder = new HashMap<EnumItemType, Boolean>();
        
        public List<MarketOrder> mOrders = Collections.synchronizedList(new ArrayList<MarketOrder>());
        public List<MarketInvoice> mInvoices = Collections.synchronizedList(new ArrayList<MarketInvoice>());
        
        protected static final int sBaseNeed = 5;
        
        MarketCashier mMarketCashier;
        
/* Messages */
        public void msgCannotFulfillItems(MarketOrder o, Map<EnumItemType,Integer> cannotFulfill) {
        	mCannotFulfill = cannotFulfill;
        	for(MarketOrder io : mOrders) {
        		if(io == o) {
        			io.mEvent = EnumOrderEvent.RECEIVED_INVOICE;
        			break;
        		}
        	}
        	stateChanged();
        }
        
        public void msgHereIsCookOrder(MarketOrder o) {
        	o.mEvent = EnumOrderEvent.RECEIVED_ORDER;
        	stateChanged();
        }
        
/* Scheduler */
        public boolean marketPickAndExecuteAnAction() {
                for(MarketInvoice invoice : mInvoices) {
                        MarketOrder order = invoice.mOrder;
                        if(order.mStatus == EnumOrderStatus.PAYING && order.mEvent == EnumOrderEvent.RECEIVED_INVOICE) {
                                order.mStatus = EnumOrderStatus.PAID;
                                processOrder(invoice);
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
                for(EnumItemType i : mItemsDesired.keySet()) {
                        if(mItemsDesired.get(i) != 0) {
                                createOrder();
                                return true;
                        }
                }
                return false;
        }

/* Actions */
        private void createOrder() {
        		print("Creating a market order.",mAlertTag);
        		Map<EnumItemType,Integer> items = new HashMap<EnumItemType,Integer>();
                
                for(EnumItemType item : mItemsDesired.keySet()) {
                		items.put(item, mItemsDesired.get(item));
                        mItemsDesired.put(item,0);
                        mHasCreatedOrder.put(item,true);
                }
                
                MarketOrder o = new MarketOrder(items, this);
                o.setRestaurantNumber(mRestaurantID);
                mOrders.add(o);
        }
        
        private void placeOrder(MarketOrder o) {
        		print("Placing a market order.", mAlertTag);
        		int m;
        		if(mMarketCashier == null) {
        			m = (int) (Math.random() % 2);
        			mMarketCashier = ContactList.sMarketList.get(m).mCashier;
        		}
        		
                if(mRestaurantCashier == null) {switch(mRestaurantID) {
                case 0:	//andre
                	mRestaurantCashier = AndreRestaurant.cashier.mRole;
                	break;
                case 1: //chase
                	mRestaurantCashier = CwagonerRestaurant.cashier.mRole;
                	break;
                case 2: //jerry
                	mRestaurantCashier = JerrywebRestaurant.cashier.mRole;
                	break;
                case 3: //maggi
                	mRestaurantCashier = MaggiyanRestaurant.mCashier.mRole;
                	break;
                case 4: //david
                	mRestaurantCashier = DavidRestaurant.cashier.mRole;
                	break;
                case 5: //shane
                	mRestaurantCashier = SmilehamRestaurant.mCashier.mRole;
                	break;
                case 6: //angel
                	TranacCashierRole c = (TranacCashierRole)TranacRestaurant.mCashier;
                	mRestaurantCashier = c.mRole;
                	break;
                case 7: //rex
                	 mRestaurantCashier = RexAnimationPanel.cashier.mRole;
                	break;
                }}
                mMarketCashier.msgOrderPlacement(o);
                mRestaurantCashier.msgPlacedMarketOrder(o,mMarketCashier);
        }
        
        private void processOrder(MarketInvoice i) {   
        		print("Processing market order.", mAlertTag);
                for(EnumItemType item : mCannotFulfill.keySet()) {
                        mItemsDesired.put(item, mItemsDesired.get(item)+mCannotFulfill.get(item));
                        mHasCreatedOrder.put(item,false);
                }
        }
        
        private void completeOrder(MarketOrder o) {
        		print("Complete market order.", mAlertTag);
                for(EnumItemType item : o.mItems.keySet()) {
                	print(item.toString() + " " + o.mItems.get(item), mAlertTag);
                	print(o.mItems.get(item) + " " + mItemInventory.get(item), mAlertTag);
                        mItemInventory.put(item, mItemInventory.get(item)+o.mItems.get(item));
                }
                mOrders.remove(o);
        }
        
/* Utilities */
        public void setMarketCashier(int n) {
        		mMarketCashier = ContactList.sMarketList.get(n).mCashier;
        }
        
        public void setMarketCashier(MarketCashier m) {
        	mMarketCashier = m;
        }
        
        public void decreaseInventory(EnumItemType i) {
        	mItemInventory.put(i,mItemInventory.get(i)-1);
        }

        public void setInventory(EnumItemType i, int n) {
        	mItemInventory.put(i,n);
        }
        
        public void setRestaurantCashier(RestaurantCashierInterface r) {
        	mRestaurantCashier = r;
        }
        
        @Override
    	public Location getLocation() {
    		return ContactList.cRESTAURANT_LOCATIONS.get(mRestaurantID);
    	}

		public int getInventory(EnumItemType e) {
			return mItemInventory.get(e);
		}
}
