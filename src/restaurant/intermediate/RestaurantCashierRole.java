package restaurant.intermediate;

import restaurant.intermediate.interfaces.RestaurantBaseInterface;
import restaurant.restaurant_davidmca.gui.DavidAnimationPanel;
import restaurant.restaurant_davidmca.roles.DavidCashierRole;
import restaurant.restaurant_duvoisin.gui.AndreRestaurantPanel;
import restaurant.restaurant_duvoisin.roles.AndreCashierRole;
import restaurant.restaurant_maggiyan.gui.MaggiyanAnimationPanel;
import restaurant.restaurant_maggiyan.roles.MaggiyanCashierRole;
import restaurant.restaurant_smileham.gui.SmilehamAnimationPanel;
import restaurant.restaurant_smileham.roles.SmilehamCashierRole;
import restaurant.restaurant_tranac.gui.TranacAnimationPanel;
import restaurant.restaurant_tranac.roles.TranacCashierRole;
import restaurant.restaurant_xurex.RexCashierRole;
import restaurant.restaurant_xurex.gui.RexAnimationPanel;
import base.BaseRole;
import base.Location;
import base.interfaces.Person;
import base.interfaces.Role;
import base.reference.ContactList;

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
			case 0: //andre
				subRole = new AndreCashierRole(super.mPerson);
				AndreRestaurantPanel.instance.cashier = (AndreCashierRole) subRole;
				break;
//			case 1: //chase
//				subRole = ((CwagonerRestaurantPanel) SimCityGui.getInstance().citypanel.masterRestaurantList.get(1)).cashier;
//				subRole.setPerson(super.mPerson);
//				break;
//			case 2: //jerry
//				subRole = ((JerrywebRestaurantPanel) SimCityGui.getInstance().citypanel.masterRestaurantList.get(2)).cashier;
//				subRole.setPerson(super.mPerson);
//				break;
			case 3: //maggi
				subRole = new MaggiyanCashierRole(super.mPerson);
				MaggiyanAnimationPanel.addPerson((MaggiyanCashierRole) subRole);
				break;
			case 4: //david
				subRole = new DavidCashierRole(super.mPerson);
				DavidAnimationPanel.cashier = (DavidCashierRole) subRole;
				break;
			case 5: //shane
				subRole = new SmilehamCashierRole(super.mPerson);
				SmilehamAnimationPanel.addPerson((SmilehamCashierRole) subRole);
				break;
			case 6: //angelica
				subRole = new TranacCashierRole(mPerson);
				TranacAnimationPanel.addPerson((TranacCashierRole)subRole);
				break;
			case 7: //rex
				subRole =  new RexCashierRole(super.mPerson);
				RexAnimationPanel.addPerson((RexCashierRole) subRole);
				break;
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
