package restaurant.intermediate;

import restaurant.intermediate.interfaces.RestaurantBaseInterface;
import restaurant.restaurant_smileham.gui.SmilehamAnimationPanel;
import restaurant.restaurant_smileham.roles.SmilehamCashierRole;
import base.BaseRole;
import base.ContactList;
import base.Location;
import base.interfaces.Person;
import base.interfaces.Role;

public class RestaurantCashierRole extends BaseRole implements RestaurantBaseInterface {
	
	static int totalCashiers = 0;
	
	Role subRole = null;
	int mRestaurantID;

	public RestaurantCashierRole(Person person, int restaurantID){
		super(person); 
		this.mRestaurantID = restaurantID;
	}
	
	public void setPerson(Person person) {
		super.mPerson = person;
		switch(mRestaurantID){
//			case 0: //andre
//				subRole = ((AndreRestaurantPanel) SimCityGui.getInstance().citypanel.masterRestaurantList.get(0)).cashier;
//				subRole.setPerson(super.mPerson);
//				break;
//			case 1: //chase
//				subRole = ((CwagonerRestaurantPanel) SimCityGui.getInstance().citypanel.masterRestaurantList.get(1)).cashier;
//				subRole.setPerson(super.mPerson);
//				break;
//			case 2: //jerry
//				subRole = ((JerrywebRestaurantPanel) SimCityGui.getInstance().citypanel.masterRestaurantList.get(2)).cashier;
//				subRole.setPerson(super.mPerson);
//				break;
//			case 3: //maggi
//				subRole = ((MaggiyanRestaurantPanel) SimCityGui.getInstance().citypanel.masterRestaurantList.get(3)).cashier;
//				subRole.setPerson(super.mPerson);
//				break;
//			case 4: //david
//				subRole = ((DavidRestaurantPanel) SimCityGui.getInstance().citypanel.masterRestaurantList.get(4)).cash;
//				subRole.setPerson(super.mPerson);
//				break;
			case 5: //shane
				subRole = new SmilehamCashierRole(super.mPerson);
				SmilehamAnimationPanel.addPerson((SmilehamCashierRole) subRole);
				break;
//			case 6: //angelica
//				subRole = ((TranacRestaurantPanel) SimCityGui.getInstance().citypanel.masterRestaurantList.get(6)).mCashier;
//				subRole.setPerson(mPerson);
//				break;
//			case 7: //rex
//				subRole =  RexAnimationPanel.cashier;
//				subRole.setPerson(super.mPerson);
//				break;
		}
	}
	
	public boolean pickAndExecuteAnAction() {
		return subRole.pickAndExecuteAnAction();
	}
	
	@Override
	public Location getLocation() {
		return ContactList.cRESTAURANT_LOCATIONS.get(mRestaurantID);
	}

}
