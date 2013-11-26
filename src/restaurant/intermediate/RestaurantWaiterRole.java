package restaurant.intermediate;

import java.util.Random;

import restaurant.intermediate.interfaces.RestaurantBaseInterface;
import restaurant.restaurant_maggiyan.gui.MaggiyanRestaurantPanel;
import restaurant.restaurant_maggiyan.roles.MaggiyanSharedWaiterRole;
import restaurant.restaurant_maggiyan.roles.MaggiyanWaiterRole;
import restaurant.restaurant_davidmca.gui.DavidRestaurantPanel;
import restaurant.restaurant_davidmca.roles.DavidWaiterRole;
import restaurant.restaurant_davidmca.roles.DavidWaiterRoleShared;
import restaurant.restaurant_smileham.gui.SmilehamAnimationPanel;
import restaurant.restaurant_smileham.roles.SmilehamWaiterRole;
import restaurant.restaurant_xurex.RexWaiterRole1;
import restaurant.restaurant_xurex.gui.RexAnimationPanel;
import restaurant.restaurant_tranac.gui.RestaurantPanel_at;
import restaurant.restaurant_tranac.roles.RestaurantWaiterRole_at;
import base.BaseRole;
import base.interfaces.Person;
import base.interfaces.Role;

public class RestaurantWaiterRole extends BaseRole implements RestaurantBaseInterface {

	Role subRole = null;
	int restaurantID;

	public RestaurantWaiterRole(Person person) {
		super(person);
	}

	public void setRestaurant(int restaurantID) {
		switch(restaurantID){
			case 0:
				break;
			case 1: 
				break;
			case 2:
				break;
			case 3: //maggi
				int rn1 = new Random().nextInt();
				
				if (rn1 % 2 == 0) {
					subRole = new MaggiyanWaiterRole(super.mPerson);
					MaggiyanRestaurantPanel.getRestPanel().addWaiter((MaggiyanWaiterRole) subRole);
				}else {
					subRole = new MaggiyanSharedWaiterRole(super.mPerson);
					MaggiyanRestaurantPanel.getRestPanel().addSharedWaiter((MaggiyanSharedWaiterRole) subRole);
				}
				break;
			case 4: //david
				int rn = new Random().nextInt();
				if (rn % 2 == 0) {
					subRole = new DavidWaiterRole(super.mPerson);
					DavidRestaurantPanel.getInstance().addWaiter((DavidWaiterRole) subRole);
				} else {
					subRole = new DavidWaiterRoleShared(super.mPerson);
					DavidRestaurantPanel.getInstance().addSharedWaiter(
							(DavidWaiterRoleShared) subRole);
				}
				break;
			case 5: //shane
				subRole = new SmilehamWaiterRole(mPerson);
				SmilehamAnimationPanel.addPerson((SmilehamWaiterRole) subRole);
				break;
			case 6: //angelica
				subRole = new RestaurantWaiterRole_at(mPerson);
				RestaurantPanel_at.getInstance().addWaiter((RestaurantWaiterRole_at)subRole);
				break;
			case 7: //rex
				subRole = new RexWaiterRole1();
				subRole.setPerson(super.mPerson);
				RexAnimationPanel.addPerson((RexWaiterRole1)subRole);
				break;
		}
		
		// TODO DAVID add if statements for all the other restaurants

	}
	
	public void setPerson(Person person){
		super.mPerson = person;	
	}

	public boolean pickAndExecuteAnAction() {
		//System.out.println("RestaurantWaiterRole pAEA run");
		return subRole.pickAndExecuteAnAction();
	}
}
