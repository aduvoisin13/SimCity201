package restaurant.restaurant_cwagoner.gui;

import restaurant.restaurant_cwagoner.roles.*;
import base.PersonAgent;
import base.PersonAgent.EnumJobType;
import base.interfaces.Person;
import base.interfaces.Role;

import javax.swing.*;

import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class CwagonerRestaurantPanel extends JPanel {
	// THIS!
	static CwagonerRestaurantPanel instance;

    private CwagonerRestaurantGui mainGui; // Reference to main GUI
    
    public CwagonerHostRole host = new CwagonerHostRole();
    public CwagonerCashierRole cashier = new CwagonerCashierRole();
    public CwagonerCookRole cook = new CwagonerCookRole();
    private List<CwagonerCustomerRole> Customers = new ArrayList<CwagonerCustomerRole>();
    private List<CwagonerWaiterRole> Waiters = new ArrayList<CwagonerWaiterRole>();

    @SuppressWarnings("static-access")
	public CwagonerRestaurantPanel(CwagonerRestaurantGui g, int numTables) {
    	super();

        this.instance = this;

        mainGui = g;

        host.setNumTables(numTables);
        
        CwagonerCookGui cg = new CwagonerCookGui(cook, mainGui);
        mainGui.animationPanel.addGui(cg);
        cook.setGui(cg);
        
        cashier.setCook(cook);
        cook.setCashier(cashier);
    }

    public static CwagonerRestaurantPanel getInstance() {
		return instance;
	}

    /**
     * Adds a customer or waiter to the appropriate list
     *
     * @param name name of person
     */
    public void addPerson(Role subRole) {

    	if (subRole instanceof CwagonerCustomerRole) {
    		((CwagonerCustomerRole) subRole).setHost(host);
    		((CwagonerCustomerRole) subRole).setCashier(cashier);
    		((CwagonerCustomerRole) subRole).setGui(new CwagonerCustomerGui((CwagonerCustomerRole) subRole, mainGui));	
    		((CwagonerCustomerRole) subRole).getGui().setPresent(true);
    		Customers.add((CwagonerCustomerRole) subRole);
    		mainGui.animationPanel.addGui(((CwagonerCustomerRole) subRole).getGui());
    	}
    	else if (subRole instanceof CwagonerWaiterRole) {
    		((CwagonerWaiterRole) subRole).setHost(host);
    		((CwagonerWaiterRole) subRole).setCashier(cashier);
    		((CwagonerWaiterRole) subRole).setGui(new CwagonerWaiterGui((CwagonerWaiterRole) subRole, mainGui));	
    		Waiters.add((CwagonerWaiterRole) subRole);
    		mainGui.animationPanel.addGui(((CwagonerWaiterRole) subRole).getGui());
    	}
    	// CHASE add other roles
    }
}