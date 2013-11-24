package restaurant.interfaces;

import restaurant.restaurant_davidmca.roles.CookRole;
import base.BaseRole;
import base.interfaces.Person;
import base.interfaces.Role;

public class RestaurantCookRole extends BaseRole {
	
	Role subRole = null;
	static int DEFAULT_FOOD_QTY = 5;
	
	public RestaurantCookRole(Person person){
		mPerson = person;
	}
	
	public void setRestaurant(int restaurantID) {
		if (restaurantID == 1) {
			subRole = new CookRole(mPerson.getName(), DEFAULT_FOOD_QTY);
		}
		//TODO DAVID add if statements for all the other restaurants
	}
	
	public boolean pickAndExecuteAnAction() {
		return subRole.pickAndExecuteAnAction();
	}

}
