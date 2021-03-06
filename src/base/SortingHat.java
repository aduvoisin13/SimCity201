package base;

import housing.House;
import housing.roles.HousingBaseRole;
import housing.roles.HousingLandlordRole;
import housing.roles.HousingOwnerRole;
import housing.roles.HousingRenterRole;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import market.roles.MarketCashierRole;
import market.roles.MarketDeliveryTruckRole;
import market.roles.MarketWorkerRole;
import restaurant.intermediate.RestaurantCashierRole;
import restaurant.intermediate.RestaurantCookRole;
import restaurant.intermediate.RestaurantHostRole;
import restaurant.intermediate.RestaurantWaiterRole;
import bank.roles.BankGuardRole;
import bank.roles.BankMasterTellerRole;
import bank.roles.BankTellerRole;
import base.interfaces.Person;
import base.interfaces.Role;
import city.gui.SimCityGui;

public class SortingHat {
	
	//list of all (non-ubiquitous) roles, accessed and instantiated 
	private static List<Role> sRoles; //list of roles
	static List<Map<Role, Boolean>> sRolesFilled;
	
	public static int sNumBankTellers = 1;
	public static int sNumMarketWorkers = 2;
	public static int sNumRestaurantWaiters = 3;
	
	public static void InstantiateBaseRoles(){
		sRoles = new ArrayList<Role>();
		sRolesFilled = new ArrayList<Map<Role, Boolean>>();
		
		//Bank
		BankMasterTellerRole masterTeller = new BankMasterTellerRole(null);
		sRoles.add(masterTeller);
		ContactList.masterTeller = masterTeller;
		for (int iBankNumber = 0; iBankNumber < ContactList.cNumBanks; iBankNumber++){
			sRoles.add(new BankGuardRole(null, iBankNumber));
			for (int iNumBankTellers = 0; iNumBankTellers < sNumBankTellers; iNumBankTellers++){
				BankTellerRole bankTeller = new BankTellerRole(null, iBankNumber);
				sRoles.add(bankTeller);
			}
		}
		
		//Market
		for (int iMarketNumber = 0; iMarketNumber < ContactList.cNumMarkets; iMarketNumber++){
			sRoles.add(new MarketCashierRole(null, iMarketNumber));
			sRoles.add(new MarketDeliveryTruckRole(null, iMarketNumber));
			for (int iNumMarketWorkers = 0; iNumMarketWorkers < sNumMarketWorkers; iNumMarketWorkers++){
				MarketWorkerRole marketWorker = new MarketWorkerRole(null, iMarketNumber);
				sRoles.add(marketWorker);
			}
		}
		
		//Housing
		HousingLandlordRole masterLandLord = new HousingLandlordRole(null);
		masterLandLord.mHousesList.add(getNextHouse());
		masterLandLord.mHousesList.add(getNextHouse());
		masterLandLord.mHousesList.add(getNextHouse());
		masterLandLord.mHousesList.add(getNextHouse());
		masterLandLord.mHousesList.add(getNextHouse());
		masterLandLord.mHousesList.add(getNextHouse());
		masterLandLord.mHousesList.add(getNextHouse());
		masterLandLord.mHousesList.add(getNextHouse());
		sRoles.add(masterLandLord);
		ContactList.masterLandlord = masterLandLord;
		
		//Restaurants
		int numRestaurants = ContactList.cNumRestaurants;
		int numStart = 0;
		if(SimCityGui.TESTING) {
			if (SimCityGui.TESTNUM >= 0) {
				numStart = SimCityGui.TESTNUM;
				numRestaurants = numStart + 1;
			}
		}
		for (int iRestaurantNum = numStart; iRestaurantNum < numRestaurants; iRestaurantNum++){
			sRoles.add(new RestaurantHostRole(null, iRestaurantNum));
			sRoles.add(new RestaurantCashierRole(null, iRestaurantNum));
			sRoles.add(new RestaurantCookRole(null, iRestaurantNum));
			for (int iNumRestaurantWaiters = 0; iNumRestaurantWaiters < sNumRestaurantWaiters; iNumRestaurantWaiters++){
				sRoles.add(new RestaurantWaiterRole(null, iRestaurantNum, iNumRestaurantWaiters%2));
			}
		}
		
		//Create roles filled matrix
		for (int i = 0; i < ContactList.cNumTimeShifts; i++){
			Map<Role, Boolean> shiftRoles = new HashMap<Role, Boolean>();
			for (Role iRole : sRoles){
				shiftRoles.put(iRole, false);
			}
			sRolesFilled.add(shiftRoles);
		}
		
	}
	
	//BANK
	public static Role getBankRole(int shift) {
		Map<Role, Boolean> shiftRoles = sRolesFilled.get(shift);
		
		//Master Teller (1) - first priority
		for (Role iRole : shiftRoles.keySet()){
			if (iRole instanceof BankMasterTellerRole){ //find role
				if (shiftRoles.get(iRole) == false){ //if role not filled
					shiftRoles.put(iRole, true); //fill it
					return (BankMasterTellerRole) iRole; //return role
				}
			}
		}
		//Guard (1) - second priority
		for (Role iRole : shiftRoles.keySet()){
			if (iRole instanceof BankGuardRole){
				if (shiftRoles.get(iRole) == false){ //if role not filled
					shiftRoles.put(iRole, true);
					return (BankGuardRole) iRole;
				}
			}
		}
		//Teller (limited) - third priority
		for (Role iRole : shiftRoles.keySet()){
			if (iRole instanceof BankTellerRole){
				if (shiftRoles.get(iRole) == false){ //if role not filled
					shiftRoles.put(iRole, true);
					return (BankTellerRole) iRole;
				}
			}
		}
		
		return null;
	}
	
	
	//MARKET
	public static Role getMarketRole(int shift){
		Map<Role, Boolean> shiftRoles = sRolesFilled.get(shift);

		
		//MarketCashierRole (1) - first priority
		for (Role iRole : shiftRoles.keySet()){
			if (iRole instanceof MarketCashierRole){
				if (shiftRoles.get(iRole) == false){ //if role not filled
					shiftRoles.put(iRole, true);
					return (MarketCashierRole) iRole;
				}
			}
		}
		
		//MarketWorkerRole
		for (Role iRole : shiftRoles.keySet()){
			if (iRole instanceof MarketWorkerRole){
				if (shiftRoles.get(iRole) == false){ //if role not filled
					shiftRoles.put(iRole, true);
					return (MarketWorkerRole) iRole;
				}
			}
		}
		
		//MarketDeliveryTruckRole
		for (Role iRole : shiftRoles.keySet()){
			if (iRole instanceof MarketDeliveryTruckRole){
				if (shiftRoles.get(iRole) == false){ //if role not filled
					shiftRoles.put(iRole, true);
					return (MarketDeliveryTruckRole) iRole;
				}
			}
		}
		return null;
	}
	
	//RESTAURANTS
	public static Role getRestaurantRole(int shift){
		Map<Role, Boolean> shiftRoles = sRolesFilled.get(shift);
			
		//RestaurantHostRole (1) - first priority
		for (Role iRole : shiftRoles.keySet()){
			if (iRole instanceof RestaurantHostRole){
				if (shiftRoles.get(iRole) == false){ //if role not filled
					shiftRoles.put(iRole, true);
					return (RestaurantHostRole) iRole;
				}
			}
		}
		
		//RestaurantCookRole (1) - first priority
		for (Role iRole : shiftRoles.keySet()){
			if (iRole instanceof RestaurantCookRole){
				if (shiftRoles.get(iRole) == false){ //if role not filled
					shiftRoles.put(iRole, true);
					return (RestaurantCookRole) iRole;
				}
			}
		}
		
		//RestaurantCashierRole (1) - first priority
		for (Role iRole : shiftRoles.keySet()){
			if (iRole instanceof RestaurantCashierRole){
				if (shiftRoles.get(iRole) == false){ //if role not filled
					shiftRoles.put(iRole, true);
					return (RestaurantCashierRole) iRole;
				}
			}
		}
		
		//RestaurantWaiterRole (limited)
		for (Role iRole : shiftRoles.keySet()){
			if (iRole instanceof RestaurantWaiterRole){
				if (shiftRoles.get(iRole) == false){ //if role not filled
					shiftRoles.put(iRole, true);
					return (RestaurantWaiterRole) iRole;
				}
			}
		}
		
		return null;
	}
	
	
	//HOUSING
	static int sRenterCount = 0;
	static int sHouseCount = 0;
//	static final int sHouseSize = 5;
	static final int sMaxRenters = 8;

	public static Role getHousingRole(Person person, int shift, boolean renter) {
		//landlord, renter, owner (in that order)	
		Map<Role, Boolean> shiftRoles = sRolesFilled.get(shift);
		
		for (Role iRole : shiftRoles.keySet()){
			if (iRole instanceof HousingLandlordRole){ //find role
				if (shiftRoles.get(iRole) == false){ //if role not filled
					shiftRoles.put(iRole, true); //fill it
					((HousingBaseRole) iRole).setHouse(getNextHouse());
					((HousingBaseRole) iRole).setPerson(person);
					return (HousingLandlordRole) iRole; //return role
				}
			}
		}
		
		if (renter) {
			if (sRenterCount < sMaxRenters) {
				sRenterCount++;
				HousingRenterRole newRenter = new HousingRenterRole(person);
				newRenter.setLandlord(ContactList.masterLandlord);
				return newRenter;
			}
		}
		
		HousingOwnerRole newOwnerRole = new HousingOwnerRole(person);
		newOwnerRole.setHouse(getNextHouse());
		return newOwnerRole;
	}
	
	public static House getNextHouse() {
		return ContactList.sHouseList.get(sHouseCount++ % ContactList.sHouseList.size());
	}
	
	public static List<Role> getRoleList(){
		return sRoles;
	}
	
}
