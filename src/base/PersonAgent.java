package base;

import housing.interfaces.HousingBase;
import housing.roles.HousingBaseRole;
import housing.roles.HousingLandlordRole;
import housing.roles.HousingRenterRole;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import market.interfaces.MarketCustomer;
import market.roles.MarketCashierRole;
import market.roles.MarketCustomerRole;
import market.roles.MarketDeliveryTruckRole;
import market.roles.MarketWorkerRole;
import restaurant.intermediate.RestaurantCashierRole;
import restaurant.intermediate.RestaurantCookRole;
import restaurant.intermediate.RestaurantCustomerRole;
import restaurant.intermediate.RestaurantHostRole;
import restaurant.intermediate.RestaurantWaiterRole;
import restaurant.restaurant_cwagoner.roles.CwagonerCustomerRole;
import restaurant.restaurant_davidmca.roles.DavidCustomerRole;
import restaurant.restaurant_duvoisin.roles.AndreCustomerRole;
import restaurant.restaurant_jerryweb.JerrywebCustomerRole;
import restaurant.restaurant_maggiyan.roles.MaggiyanCustomerRole;
import restaurant.restaurant_smileham.roles.SmilehamCustomerRole;
import restaurant.restaurant_tranac.roles.TranacCustomerRole;
import restaurant.restaurant_xurex.RexCustomerRole;
import transportation.roles.CommuterRole;
import transportation.roles.CommuterRole.PersonState;
import bank.BankAction;
import bank.roles.BankCustomerRole;
import bank.roles.BankCustomerRole.EnumAction;
import bank.roles.BankGuardRole;
import bank.roles.BankMasterTellerRole;
import bank.roles.BankTellerRole;
import bank.test.mock.MockTellerRole;
import base.Event.EnumEventType;
import base.Item.EnumItemType;
import base.interfaces.Person;
import base.interfaces.Role;
import city.gui.CityPerson;
import city.gui.SimCityGui;
import city.gui.trace.AlertTag;


public class PersonAgent extends Agent implements Person {
	//----------------------------------------------------------DATA----------------------------------------------------------
	//Static data
	public static int sSSN = 0;
	public static int sRestaurantCounter = 0;
	public static int sHouseCounter = 0;
	public static int sBaseInventory = 0;
	public static int sBaseWanted = 1;
	//Roles and Job
	public static enum EnumJobType {	HOUSING,	//master landlord
										BANK,		//master teller, teller, guard... 
										MARKET, 	//cashier, worker...
										RESTAURANT, //...
										NONE};		//party person, non-norms (can add NN1, NN2, ...)
	public EnumJobType mJobType;
	public Map<Role, Boolean> mRoles; 	//roles, active -  i.e. WaiterRole, BankTellerRole, etc.
	
	//Lists
	private List<Person> mFriends; 						// best friends are those with same timeshift
	private List<Event> mEvents; 						// tree set ordered by time of event
	private Map<EnumItemType, Integer> mItemInventory; 	// personal inventory
	private Map<EnumItemType, Integer> mItemsDesired; 	// not ordered yet
	Set<Location> mHomeLocations; 						//multiple for landlord
	
	//Personal Variables
	private String mName; 
	private int mSSN;
	private int mTimeShift;
	private double mCash;
	private double mLoan;
	private boolean mHasCar;
	private boolean mAtJob;		//used in PAEA
	
	// GUI/Commuter Stuff
	public CityPerson mPersonGui;
	public CommuterRole mCommuterRole;
	public EnumCommuteTo mCommutingTo;
	public static enum EnumCommuteTo {	JOB,
										HOUSE,
										MARKET,
										RESTAURANT,
										BANK,
										PARTY,
										INSPECT};
	public EventParty mCurrentParty = null;
	public Boolean firstRun = true;
	
	// Inspector
//	public int inspectCounter;

	//PAEA Helpers
	public Semaphore semAnimationDone = new Semaphore(0);
	public boolean mRoleFinished = true;
	public MockTellerRole mJobRole;

	// ----------------------------------------------------------CONSTRUCTOR----------------------------------------------------------
	
	public PersonAgent() {
		initializePerson();
	}
	
	public PersonAgent(EnumJobType jobType, double cash, String name){
		mJobType = jobType;
		mCash = cash;
		mName = name;
		initializePerson();
		
		//Get job role and location; set active if necessary
		Role jobRole = null;
		switch (jobType){
			case BANK:
				jobRole = SortingHat.getBankRole(mTimeShift);
				if(mTimeShift==Time.GetShift() && jobRole != null){
					print("Bank role person auto set: "+jobRole.toString());
					if (jobRole instanceof BankTellerRole){
						ContactList.sBankList.get(((BankTellerRole)jobRole).mBankID).mGuard.msgReadyToWork((BankTellerRole)jobRole);
						ContactList.sBankList.get(((BankTellerRole)jobRole).mBankID).addPerson(jobRole);
					}
				}
				break;
			case MARKET:
				jobRole = SortingHat.getMarketRole(mTimeShift);
				break;
			case RESTAURANT:
				jobRole = SortingHat.getRestaurantRole(mTimeShift);
				if(jobRole != null)
					print(jobRole.toString());
				break;
			case HOUSING:
				jobRole = SortingHat.getHousingRole(this, mTimeShift, false);
				break;
			case NONE:
				break;
		}
		
		//Link person and role
		boolean active = (mTimeShift == Time.GetShift());	//set active if job shift is now
		if (jobRole != null){
			mRoles.put(jobRole, active);					//give person a reference to the role (and if currently filling role)
		}
		if (active){
			for (Role iRole : mRoles.keySet()){
				iRole.setPerson(this);						//give role a reference to the person (only if currently filling role)
			}
		}
		
		//Add customer/rider role possibilities
		mCommuterRole = new CommuterRole(this); 
		mCommuterRole.mActive = false;
		mCommutingTo = null;
		
		if (mName.contains("renter")) {
			print("made renter!");
			mRoles.put(SortingHat.getHousingRole(this, mTimeShift, true), true);
		}
		else {
			mRoles.put(SortingHat.getHousingRole(this, mTimeShift, false), true);
		}
		//mRoles.put(new CommuterRole(this), false); 
		mRoles.put(new BankCustomerRole(this, mSSN%2), false);
		mRoles.put(new MarketCustomerRole(this, mSSN%2), false);
		mRoles.put(new RestaurantCustomerRole(this), false);
	}
	
	private void initializePerson(){
		//Roles and Job
		mRoles = new HashMap<Role, Boolean>(); //role to active
		mAtJob = false;
		
		//Lists
		mFriends = new ArrayList<Person>();
		mEvents = Collections.synchronizedList(new ArrayList<Event>());
		mItemInventory = Collections.synchronizedMap(new HashMap<EnumItemType, Integer>());
			//populate inventory
			mItemInventory.put(EnumItemType.CAR,0);
			mItemInventory.put(EnumItemType.CHICKEN,sBaseInventory);
			mItemInventory.put(EnumItemType.PIZZA,sBaseInventory);
			mItemInventory.put(EnumItemType.STEAK,sBaseInventory);
			mItemInventory.put(EnumItemType.SALAD,sBaseInventory);
		mItemsDesired = Collections.synchronizedMap(new HashMap<EnumItemType, Integer>());
			mItemInventory.put(EnumItemType.CAR,0);
			mItemInventory.put(EnumItemType.CHICKEN,0);
			mItemInventory.put(EnumItemType.PIZZA,0);
			mItemInventory.put(EnumItemType.STEAK,0);
			mItemInventory.put(EnumItemType.SALAD,0);
		
		mHomeLocations = Collections.synchronizedSet(new HashSet<Location>());
		
		//Personal Variables
		mSSN = sSSN++; // assign SSN
		//mTimeShift = 0;
		mTimeShift = (mSSN % ContactList.cNumTimeShifts); // assign time schedule
		mLoan = 0;
		mHasCar = true; 
		
		//Role References
		//mPersonGui = new CityPerson(this, SimCityGui.getInstance(), sSSN * 5 % 600, sSSN % 10 + 250);
		//Role References
        Location startLocation = null;
        if (mSSN % 8 == 0) startLocation = new Location(60, 0);
        if (mSSN % 8 == 1) startLocation = new Location(0, 60);
        if (mSSN % 8 == 2) startLocation = new Location(540, 0);
        if (mSSN % 8 == 3) startLocation = new Location(0, 540);
        if (mSSN % 8 == 4) startLocation = new Location(60, 600);
        if (mSSN % 8 == 5) startLocation = new Location(600, 60);
        if (mSSN % 8 == 6) startLocation = new Location(540, 600);
        if (mSSN % 8 == 7) startLocation = new Location(600, 540);
        mPersonGui = new CityPerson(this, SimCityGui.getInstance(), startLocation);
        
		// Event Setup
		mEvents = Collections.synchronizedList(new ArrayList<Event>());
		
//		inspectCounter = -1;
	}
	
	// ----------------------------------------------------------MESSAGES----------------------------------------------------------
	public void msgTimeShift() {
		//if(mJobType == EnumJobType.BANK)
			//finished role if job
			mRoleFinished = true;
			if (Time.GetShift() == mTimeShift) {
				mRoles.put(getJobRole(), true);
			}
			//Leave job
			if ((mTimeShift + 1) % ContactList.cNumTimeShifts == Time.GetShift()){ //if job shift is over
				mAtJob = false;
				mRoles.put(getJobRole(), false); //set job role to false;
				mPersonGui.setPresent(true);
			}
			if(getJobRole()!=null)
				stateChanged();
	}
	
	public void msgStateChanged() {
		stateChanged();
	}

	public void msgAddEvent(Event event) {
		if(event.mEventType == EnumEventType.RSVP1){
			if(((EventParty)event).mHost.getName().equals("partyPersonFlake") && mSSN%ContactList.cNumTimeShifts==0){
				print("I am a deadbeat");
				return;
			}
		}
		mEvents.add(event);
	}
	
	public void msgAnimationDone(){
		if (semAnimationDone.availablePermits() == 0) semAnimationDone.release();
	}
	
	public void msgRoleFinished(){
		mRoleFinished = true;
		mPersonGui.setPresent(true);
		for (Role iRole : mRoles.keySet()){
			if(!(iRole instanceof HousingBase)){
				mRoles.put(iRole, false);
				iRole.setActive();
			}
		}
	}

	public void msgHereIsPayment(int senderSSN, double amount){
		mCash += amount;
	}
	
	public void msgOverdrawnAccount(double loan) {
		mLoan += loan;
	}

	// ----------------------------------------------------------SCHEDULER----------------------------------------------------------
	@Override
	public boolean pickAndExecuteAnAction() {
		if ((mRoleFinished) && (!mAtJob) ){
			// Process events (calendar)
			synchronized(mEvents){
				Collections.sort((mEvents));
			}
			if(mEvents.isEmpty()) {
//				//System.out.println("test");
				return false;
			}
			Event event = mEvents.get(0); //next event
			print("" + event.mEventType);
			if (event.mTime <= Time.GetTime()){ //only do events that have started
				if(!mName.contains("party") && !mName.contains("other") && !mName.contains("inter")) //required because party is not a role
					mRoleFinished = false; //doing a role
				processEvent(event);
				return true;
			}
		}
		
		// Do role actions
		for (Role iRole : mRoles.keySet()) {
			if (mRoles.get(iRole) && iRole!= null) {
				if (!iRole.hasPerson()) {
					print(iRole.toString());
					print("getPerson in iRole was null");
				}
				else if (mCommuterRole.mActive){
					if(mCommuterRole.pickAndExecuteAnAction()){
						return true;
					}
				}
				else if (iRole.pickAndExecuteAnAction()){
					return true;
				}
			}
		}
		
		//last choice - go home
		return false;
	}

	// ----------------------------------------------------------ACTIONS----------------------------------------------------------
	private synchronized void processEvent(Event event) {
		mAtJob = false;
		//One time events (Car)
		if (event.mEventType == EnumEventType.GET_CAR) {
			getCar();
		}
		else if (event.mEventType == EnumEventType.REQUEST_HOUSE) {
			requestHouse();
		}
		//Daily Recurring Events (Job, Eat)
		else if (event.mEventType == EnumEventType.JOB) {
			//bank is closed on weekends
			if (!(Time.IsWeekend()) || (mJobType != EnumJobType.BANK)){
				mAtJob = true;
				goToJob();
			}
		}
		else if (event.mEventType == EnumEventType.EAT) {
			eatFood();
		}

		//Intermittent Events (Deposit Check)
		else if (event.mEventType == EnumEventType.DEPOSIT_CHECK) {
			print("DepositCheck");
			depositCheck();
		}
		
		else if (event.mEventType == EnumEventType.ASK_FOR_RENT) {
			invokeRent();
		}
		
		else if (event.mEventType == EnumEventType.MAINTAIN_HOUSE) {
			invokeMaintenance();
		}
		
		//Party Events
		else if (event.mEventType == EnumEventType.INVITE1) {
			
			inviteToParty();
		}
		else if (event.mEventType == EnumEventType.INVITE2) {
			reinviteDeadbeats();
		}
		else if (event.mEventType == EnumEventType.RSVP1) {
			respondToRSVP();
		}
		else if (event.mEventType == EnumEventType.RSVP2) {
			respondToRSVP();
		}
		else if (event.mEventType == EnumEventType.PARTY) {
			if (event instanceof EventParty){
				if(((EventParty)event).mAttendees.isEmpty()){
					print("OMG THIS PARTY SUCKS and is cancelled");
					synchronized(mEvents){
						mEvents.remove(event);
					}
					return;
				}
				else{
					goParty((EventParty)event);
					mCurrentParty = (EventParty)event;
				}
			}
		}
		else if (event.mEventType == EnumEventType.PLANPARTY) {
			planParty(Time.GetTime());
		}
		
		//Inspection
		else if (event.mEventType == EnumEventType.INSPECTION) {
			inspect();
		}
		
		//Market
		else if (event.mEventType == EnumEventType.GO_TO_MARKET) {
			goToMarket();
		}
		
		mEvents.remove(event);
	}

/*************************************************************************/
	
	public void postCommute() {
		if(mCommutingTo != null) {
			switch(mCommutingTo) {
				case RESTAURANT:
					synchronized(ContactList.sOpenBuildings) {
						for (Role iRole : mRoles.keySet()){
							if (iRole instanceof RestaurantCustomerRole){
								((RestaurantCustomerRole) iRole).setPerson(this);
								
								if(((RestaurantCustomerRole)iRole).subRole instanceof AndreCustomerRole) {
									if(!ContactList.sOpenBuildings.get("R0")) {
										msgRoleFinished();
										assignNextEvent();
									}
								} else if(((RestaurantCustomerRole)iRole).subRole instanceof CwagonerCustomerRole) {
									if(!ContactList.sOpenBuildings.get("R1")) {
										msgRoleFinished();
										assignNextEvent();
									}
								} else if(((RestaurantCustomerRole)iRole).subRole instanceof JerrywebCustomerRole) {
									if(!ContactList.sOpenBuildings.get("R2")) {
										msgRoleFinished();
										assignNextEvent();
									}
								} else if(((RestaurantCustomerRole)iRole).subRole instanceof MaggiyanCustomerRole) {
									if(!ContactList.sOpenBuildings.get("R3")) {
										msgRoleFinished();
										assignNextEvent();
									}
								} else if(((RestaurantCustomerRole)iRole).subRole instanceof DavidCustomerRole) {
									if(!ContactList.sOpenBuildings.get("R4")) {
										msgRoleFinished();
										assignNextEvent();
									}
								} else if(((RestaurantCustomerRole)iRole).subRole instanceof SmilehamCustomerRole) {
									if(!ContactList.sOpenBuildings.get("R5")) {
										msgRoleFinished();
										assignNextEvent();
									}
								} else if(((RestaurantCustomerRole)iRole).subRole instanceof TranacCustomerRole) {
									if(!ContactList.sOpenBuildings.get("R6")) {
										msgRoleFinished();
										assignNextEvent();
									}
								} else if(((RestaurantCustomerRole)iRole).subRole instanceof RexCustomerRole) {
									if(!ContactList.sOpenBuildings.get("R7")) {
										msgRoleFinished();
										assignNextEvent();
									}
								}
							}
						}
					}
					break;
				case BANK:
					synchronized(ContactList.sOpenBuildings) {
						for (Role iRole : mRoles.keySet()){
							if (iRole instanceof BankCustomerRole){
								BankCustomerRole bankCustomerRole = (BankCustomerRole)iRole;
								ContactList.sBankList.get(bankCustomerRole.getBankID()).addPerson(bankCustomerRole);
								
								//plan robbery
								if(mName.contains("robber")){
									print("Robbery action added to bank options");
									bankCustomerRole.mActions.add(new BankAction(EnumAction.Robbery, 100));
								}
								
								//deposit check
								int deposit = 50;
								bankCustomerRole.mActions.add(new BankAction(EnumAction.Deposit, deposit));
								
								//pay back loan if needed
								if(mLoan > 0){
									double payment = Math.max(mCash, mLoan);
									mCash -= payment;
									bankCustomerRole.mActions.add(new BankAction(EnumAction.Payment, payment));
								}
								
								if(mSSN % 2 == 0) {
									if(!ContactList.sOpenBuildings.get("B1")) {
										msgRoleFinished();
										assignNextEvent();
									}
								} else {
									if(!ContactList.sOpenBuildings.get("B2")) {
										msgRoleFinished();
										assignNextEvent();
									}
								}
							}
						}
					}
					break;
				case HOUSE:
					HousingBase housingRole = getHousingRole();
					if (!((Role) housingRole).hasPerson()) {
						((Role) housingRole).setPerson(this);
					}
					mRoles.put((Role) housingRole, true);
					((Role) housingRole).setActive();
					
					// If inspector, disable.
					for (Role iRole : mRoles.keySet()){
						if (iRole instanceof InspectorRole){
							mRoles.put((InspectorRole)iRole, false);
						}
					}
					break;
				case JOB:
					mAtJob = true; //set to false in msgTimeShift
					Role jobRole = getJobRole();
					if(!jobRole.hasPerson()) {
						jobRole.setPerson(this);
					}
					mRoles.put(jobRole, true); //set role to active
					jobRole.setActive();
					break;
				case MARKET:
					if(mSSN % 2 == 0) {
						if(!ContactList.sOpenBuildings.get("M1")) {
							msgRoleFinished();
							assignNextEvent();
						}
					} else {
						if(!ContactList.sOpenBuildings.get("M2")) {
							msgRoleFinished();
							assignNextEvent();
						}
					}
					break;
				case PARTY:
					PartyRole partyPerson = null;
					for (Role iRole : mRoles.keySet()){
						if (iRole instanceof PartyRole){
							partyPerson = (PartyRole)iRole;
						}
					}
					mRoles.put(partyPerson, false);
					
					print("going to house #" + mCurrentParty.mHost
							.getHousingRole().getHouse().mHouseNum);
					
					((HousingBaseRole) getHousingRole()).gui.setPresent(true);
					SimCityGui.getInstance().cityview.mCityHousingList.get(mCurrentParty.mHost
							.getHousingRole().getHouse().mHouseNum).mPanel
							.addGui((Gui) ((HousingBaseRole) getHousingRole()).gui);
					((HousingBaseRole) getHousingRole()).gui.DoParty();
					mCurrentParty = null;
					break;
//				case INSPECT:
//					msgRoleFinished();
//					msgAddEvent(new Event(EnumEventType.INSPECTION, -1));
//					mCommuterRole.mState = PersonState.walking;
//					stateChanged();
//					break;
				default:
					break;
			}
			mCommutingTo = null;
			mCommuterRole.mActive = false;
			
			stateChanged();
		}
	}
	
/*************************************************************************/
	
	public void requestHouse() {
		print("Requesting House");
		if (getHousingRole().getHouse() == null) {
			((HousingRenterRole) getHousingRole()).msgRequestHousing();
		}
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				mPersonGui.setPresent(true);
				mPersonGui.DoGoToDestination(getHousingRole().getLocation());
				acquireSemaphore(semAnimationDone);
				mPersonGui.setPresent(false);
			}
		}, 1000);
	}
	
	public void getCar(){
		//add desired item
		mItemsDesired.put(EnumItemType.CAR, 1); //want 1 car
		//PAEA for role will message market cashier to start transaction
		mHasCar = false;
		
		Location location;
		if(mSSN%2 == 0) {
			location = ContactList.getDoorLocation(ContactList.cMARKET1_LOCATION);
		} else {
			location = ContactList.getDoorLocation(ContactList.cMARKET2_LOCATION);
		}
		
		//activate marketcustomer role
		for (Role iRole : mRoles.keySet()){
			if (iRole instanceof MarketCustomer){
				mRoles.put(iRole, true); //set active
				iRole.setPerson(this);
			}
		}
		
		mCommuterRole.mActive = true;
		mCommuterRole.setLocation(location);
		mCommutingTo = EnumCommuteTo.MARKET;
		mCommuterRole.mState = PersonState.walking;
	}
	
	public void inspect() {
		print("I AM INSPECTING");
		
//		inspectCounter++;
//		
//		if(inspectCounter < ContactList.sOpenPlaces.size()) {
//			InspectorRole inspector = null;
//			for (Role iRole : mRoles.keySet()){
//				if (iRole instanceof InspectorRole){
//					inspector = (InspectorRole)iRole;
//				}
//			}
//			if(inspector == null)
//				inspector = new InspectorRole(this);
//			
//			mRoles.put(inspector, true);
//			AlertLog.getInstance().logError(AlertTag.PERSON, getName(), "INSPECTCOUNTER: " + inspectCounter);
//			mCommuterRole.mActive = true;
//			synchronized(ContactList.sOpenPlaces) {
//				if(inspectCounter != 0) {
//					Location deleteMe = (Location)((ContactList.sOpenPlaces.keySet().toArray())[inspectCounter - 1]);
//					Inspection.sInspectionImages.get(deleteMe).disable();
//				}
//				
//				Location temp = (Location)((ContactList.sOpenPlaces.keySet().toArray())[inspectCounter]);
//				mCommuterRole.setLocation(temp);
//				Inspection.sInspectionImages.get(temp).enable();
//			}
//			mCommutingTo = EnumCommuteTo.INSPECT;
//			mCommuterRole.mState = PersonState.walking;
//		} else {
//			if(inspectCounter == ContactList.sOpenPlaces.size()) {
//				synchronized(ContactList.sOpenPlaces) {
//					Location deleteMe = (Location)((ContactList.sOpenPlaces.keySet().toArray())[inspectCounter - 1]);
//					Inspection.sInspectionImages.get(deleteMe).disable();
//				}
//			}
//			
//			getHousingRole().msgTimeToMaintain();
//			mCommuterRole.mActive = true;
//			mCommuterRole.setLocation(ContactList.cHOUSE_LOCATIONS.get(getHousingRole().getHouse().mHouseNum));
//			mCommutingTo = EnumCommuteTo.HOUSE;
//			mCommuterRole.mState = PersonState.walking;
//		}
		
		// OLD
		
		
		mPersonGui.setPresent(true);
		
		Location[] myDestinations = null;
		Boolean[] isOpen = null;
		synchronized(ContactList.sOpenPlaces) {
			if(mSSN % 3 == 0) {
				myDestinations = ContactList.sOpenPlaces.keySet().toArray(new Location[0]);
				isOpen = ContactList.sOpenPlaces.values().toArray(new Boolean[0]);
			} else if(mSSN % 3 == 1) {
				Location[] temp = ContactList.sOpenPlaces.keySet().toArray(new Location[0]);
				Boolean[] tempBool = ContactList.sOpenPlaces.values().toArray(new Boolean[0]);
				myDestinations = new Location[ContactList.sOpenPlaces.size()];
				isOpen = new Boolean[ContactList.sOpenPlaces.size()];
				for(int i = ContactList.sOpenPlaces.size() - 1; i >= 0; i--) {
					myDestinations[i] = temp[i];
					isOpen[i] = tempBool[i];
				}
			} else if(mSSN % 3 == 2) {
				Location[] temp = ContactList.sOpenPlaces.keySet().toArray(new Location[0]);
				Boolean[] tempBool = ContactList.sOpenPlaces.values().toArray(new Boolean[0]);
				myDestinations = new Location[ContactList.sOpenPlaces.size()];
				isOpen = new Boolean[ContactList.sOpenPlaces.size()];
				int j = ContactList.sOpenPlaces.size() - 1;
				for(int i = ContactList.sOpenPlaces.size() / 2; i < ContactList.sOpenPlaces.size(); i++) {
					myDestinations[j] = temp[i];
					isOpen[j] = tempBool[i];
					j--;
				}
				j = 0;
				for(int i = ContactList.sOpenPlaces.size() / 2; i >= 0; i--) {
					myDestinations[j] = temp[i];
					isOpen[j] = tempBool[i];
					j++;
				}
			}
		}
		for(int iLocation = 0; iLocation < myDestinations.length; iLocation++){
			if(myDestinations[iLocation] != null) {
				if(isOpen[iLocation]) {
					Inspection.sInspectionImages.get(myDestinations[iLocation]).enable();
					mPersonGui.DoGoToDestination(myDestinations[iLocation]);
					acquireSemaphore(semAnimationDone);
					Inspection.sInspectionImages.get(myDestinations[iLocation]).disable();
					print("Visited "+myDestinations[iLocation].toString());
					mPersonGui.setPresent(true);
				}
			}
		}
		
//		synchronized(ContactList.sOpenPlaces){
//			for(Location iLocation : ContactList.sOpenPlaces.keySet()){
//				if(ContactList.sOpenPlaces.get(iLocation)){
//					Inspection.sInspectionImages.get(iLocation).enable();
//					mPersonGui.DoGoToDestination(iLocation);
//					acquireSemaphore(semAnimationDone);
//					Inspection.sInspectionImages.get(iLocation).disable();
//					print("Visited "+iLocation.toString());
//					mPersonGui.setPresent(true);
//				}
//			}
//		}
		getHousingRole().msgTimeToMaintain();
		mPersonGui.DoGoToDestination(ContactList.cHOUSE_LOCATIONS.get(getHousingRole().getHouse().mHouseNum));
		acquireSemaphore(semAnimationDone);
		mPersonGui.setPresent(false);
	}
	
	public void goToJob() {
		//print("goToJob");
		Role jobRole = getJobRole();
		if(jobRole == null){
			return;
		}		
		mCommuterRole.mActive = true;
		mCommuterRole.setLocation(getJobLocation());
		mCommutingTo = EnumCommuteTo.JOB;
		mCommuterRole.mState = PersonState.walking;
	}

	public void eatFood() {
		if (isCheap() && getHousingRole().getHouse() != null){
			//DAVID I'm trying this out to see what happens...
			print("Going to market to buy food to eat at home");
			goToMarket();
			print("Going to eat at home");
			getHousingRole().msgEatAtHome();
			mCommuterRole.mActive = true;
			mCommuterRole.setLocation(getHousingRole().getLocation());
			mCommutingTo = EnumCommuteTo.HOUSE;
			mCommuterRole.mState = PersonState.walking;
		}else{
			print("Going to restaurant");
			
			//set random restaurant
			int restaurantChoice;
			Random rand = new Random();
			if (SimCityGui.TESTING) {
				restaurantChoice = SimCityGui.TESTNUM; //override if testing
			} else if(firstRun) {
				restaurantChoice = mSSN % 8;
				firstRun = false;
			} else {
				restaurantChoice = rand.nextInt(8);
			}
			
			RestaurantCustomerRole restCustRole = null;
			for (Role iRole : mRoles.keySet()){
				if (iRole instanceof RestaurantCustomerRole){
					restCustRole = (RestaurantCustomerRole)iRole;
				}
			}
			restCustRole.setRestaurant(restaurantChoice);
			
			mRoles.put(restCustRole, true);
			
			mCommuterRole.mActive = true;
			mCommuterRole.setLocation(ContactList.cRESTAURANT_LOCATIONS.get(restaurantChoice));
			mCommutingTo = EnumCommuteTo.RESTAURANT;
			mCommuterRole.mState = PersonState.walking;
			stateChanged();
		}		
	}
	

	private void goToMarket() {

		switch(mSSN % 4) {
			case 0:
				mItemsDesired.put(EnumItemType.PIZZA, sBaseWanted);
				break;
			case 1:
				mItemsDesired.put(EnumItemType.STEAK, sBaseWanted);
				break;
			case 2:
				mItemsDesired.put(EnumItemType.CHICKEN, sBaseWanted);
				break;
			case 3:
				mItemsDesired.put(EnumItemType.SALAD, sBaseWanted);
				break;
		}
		
		//activate marketcustomer role
		for (Role iRole : mRoles.keySet()){
			if (iRole instanceof MarketCustomer){
				mRoles.put(iRole, true); //set active
				iRole.setPerson(this);
			}
		}
		
		Location location;
		if(mSSN%2 == 0) {
			location = ContactList.getDoorLocation(ContactList.cMARKET1_LOCATION);
		} else {
			location = ContactList.getDoorLocation(ContactList.cMARKET2_LOCATION);
		}
		
		mCommuterRole.mActive = true;
		mCommuterRole.setLocation(location);
		mCommutingTo = EnumCommuteTo.MARKET;
		mCommuterRole.mState = PersonState.walking;
	}
	
	private void depositCheck() {		
		BankCustomerRole bankCustomerRole = null;
		for (Role iRole : mRoles.keySet()){
			if (iRole instanceof BankCustomerRole){
				bankCustomerRole = (BankCustomerRole)iRole;
			}
		}
		
		bankCustomerRole.setPerson(this);
		mRoles.put(bankCustomerRole, true);
		
		//GO TO BANK AND DO STUFF
		mCommuterRole.mActive = true;
		mCommuterRole.setLocation(bankCustomerRole.getBankID() == 0 ? ContactList.cBANK1_LOCATION:ContactList.cBANK2_LOCATION);
		mCommutingTo = EnumCommuteTo.BANK;
		mCommuterRole.mState = PersonState.walking;
	}
	
	private void planParty(int time){
		print("Planning a party");
		mEvents.add(new Event(EnumEventType.INVITE1, time));
		if(!mName.equals("partyPerson"))
			mEvents.add(new Event(EnumEventType.INVITE2, time+2));
		Location partyLocation = getHousingRole().getLocation();
		mEvents.add(new EventParty(EnumEventType.PARTY, time+4, partyLocation, this, mFriends));
		//mEvents.add(new EventParty(EnumEventType.PARTY, time+4, ((HousingBaseRole)getHousingRole()).getLocation(), this, mFriends));
	}

	private void goParty(EventParty event) {
		print("Going to party");

//		mPersonGui.DoGoToDestination(event.mLocation);
//		acquireSemaphore(semAnimationDone);
//		mPersonGui.setPresent(false);
//		((HousingBaseRole) getHousingRole()).gui.setPresent(true);
//		SimCityGui.getInstance().cityview.mCityHousingList.get(event.mHost
//				.getHousingRole().getHouse().mHouseNum).mPanel
//				.addGui((Gui) ((HousingBaseRole) getHousingRole()).gui);
//		((HousingBaseRole) getHousingRole()).gui.DoParty();
		
		PartyRole partyPerson = null;
		for (Role iRole : mRoles.keySet()){
			if (iRole instanceof PartyRole){
				partyPerson = (PartyRole)iRole;
			}
		}
		if(partyPerson == null)
			partyPerson = new PartyRole(this);
		
		mRoles.put(partyPerson, true);
		
		mCommuterRole.mActive = true;
		mCommuterRole.setLocation(event.mLocation);
		mCommutingTo = EnumCommuteTo.PARTY;
		mCommuterRole.mState = PersonState.walking;
		
		mRoleFinished = false;
	}

	private void inviteToParty() {
		if(mFriends.isEmpty()){
			for (Person iPerson : ContactList.sPersonList){
				mFriends.add(iPerson);
			}
			print("There are "+ContactList.sPersonList.size()+" people in the city");
			print("Created "+mFriends.size()+" friends for party host");
		}
		print("First RSVP is sent out");
		//party is in 3 days
		//send RSVP1 and event invite
		Location partyLocation = getHousingRole().getLocation();//new Location(100, 0);
		Event party = new EventParty(EnumEventType.PARTY, Time.GetTime()+4, partyLocation, this, mFriends);
		
		//Event party = new EventParty(EnumEventType.PARTY, Time.GetTime()+4, ((HousingBaseRole)getHousingRole()).getLocation(), this, mFriends);
		Event rsvp  = new EventParty(EnumEventType.RSVP1, -1, this); //respond immediately
		if(mName.equals("partyPersonFlake")){
			for (Person iFriend : mFriends){
				iFriend.msgAddEvent(rsvp);
				iFriend.msgAddEvent(party);
			}
		}
		else{ //partyPerson or partyPersonNO
			for (Person iFriend : mFriends){
				if(iFriend.getTimeShift()==mTimeShift){
					iFriend.msgAddEvent(rsvp);
					iFriend.msgAddEvent(party);
				}
			}
		}
	}

	private void reinviteDeadbeats() {
		print("Second RSVP is sent out");
		EventParty party = null;
		for (Event iEvent : mEvents){
			if (iEvent instanceof EventParty){
				if (((EventParty) iEvent).mHost == this){
					party = (EventParty) iEvent;
				}
			}
		}
		synchronized(party.mAttendees){
			for (Person iPerson : party.mAttendees.keySet()){
				if (party.mAttendees.get(iPerson) == false){ //haven't responded yet
					Event rsvp = new EventParty(EnumEventType.RSVP2, -1, this);
					iPerson.msgAddEvent(rsvp);
				}
			}
		}
	}
	
	private void respondToRSVP(){
		synchronized(mEvents){
		for (Event iEvent : mEvents){
			if (iEvent instanceof EventParty){
				if(((EventParty) iEvent).mHost.getName().equals("partyPersonNO")){
					synchronized(((EventParty)iEvent).mAttendees){
						((EventParty) iEvent).mAttendees.remove(this);
					}
					print("Responding to RSVP: NO"); mPersonGui.disable();
				}
				else if (((EventParty) iEvent).mHost.getTimeShift() == mTimeShift){
					synchronized(((EventParty)iEvent).mAttendees){
						((EventParty) iEvent).mAttendees.put(this, true);
					}
					print("Responding to RSVP: YES");
				}else{
					synchronized(((EventParty)iEvent).mAttendees){
						((EventParty) iEvent).mAttendees.remove(this);
					}
					print("Responding to RSVP: NO"); mPersonGui.disable();
				}
			}
		}
		}
	}
	
	public void invokeRent() {
		print("invokeRent");
		((HousingLandlordRole) getHousingRole()).mActive = true;
		((HousingLandlordRole) getHousingRole()).msgTimeToCheckRent(); //this role is always active
	}
	
	public void invokeMaintenance() {
		if (getHousingRole().getHouse() != null) {
			mCommuterRole.mActive = true;
			mCommuterRole.setLocation(getHousingRole().getLocation());
			mCommutingTo = EnumCommuteTo.HOUSE;
			mCommuterRole.mState = PersonState.walking;
			((HousingBaseRole) getHousingRole()).msgTimeToMaintain();
		}
	}
	
	/*private List<Person> getBestFriends(){
		List<Person> bestFriends = new ArrayList<Person>();
		for (Person iPerson : mFriends){
			if (iPerson.getTimeShift() == mTimeShift) bestFriends.add(iPerson);
		}
		return bestFriends;
	}*/
	
	private boolean isCheap(){
		return (mLoan == 0 && mCash < 30);
	}

	public void acquireSemaphore(Semaphore semaphore){
		try {
			semaphore.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private Role getJobRole(){
		for (Role iRole : mRoles.keySet()){
			//Bank roles
			if (	//Bank jobs
					iRole instanceof BankGuardRole ||
					iRole instanceof BankMasterTellerRole ||
					iRole instanceof BankTellerRole ||
					//Market jobs
					iRole instanceof MarketCashierRole ||
					iRole instanceof MarketDeliveryTruckRole ||
					iRole instanceof MarketWorkerRole ||
					//Restaurant job
					iRole instanceof RestaurantCashierRole ||
					iRole instanceof RestaurantCookRole ||
					iRole instanceof RestaurantHostRole ||
					iRole instanceof RestaurantWaiterRole){
				return iRole;
			}
		}
		//print("job role null!");
		return null;
	}
	
	private Location getJobLocation(){
		if (getJobRole() == null)
			return null;
		return getJobRole().getLocation();
	}
	
	// ----------------------------------------------------------ACCESSORS----------------------------------------------------------
	
	public void addRole(Role role, boolean active) {
		mRoles.put(role, active);
		print(this.getName());
		if (role.getPerson() == null) {
			print("person is null in addrole");
		}
	}

	public void removeRole(Role r) {
		mRoles.put(r, false);
//		mRoles.remove(r);
	}

	public double getCash() {
		return mCash;
	}

	public void setCash(double cash) {
		mCash = cash;
	}

	public void addCash(double amount) {
		mCash += amount;

	}
	
	public void setLoan(double loan) {
		mLoan = loan;
	}
	
	public void subLoan(double loan) {
		mLoan -= loan;
	}
	
	public double getLoan() {
		return mLoan;
	}

	public Map<EnumItemType, Integer> getItemsDesired() {
		return mItemsDesired;
	}

	public int getSSN() {
		return mSSN;
	}

	public Map<EnumItemType, Integer> getItemInventory() {
		return mItemInventory;
	}
	
	public String getName(){
		return mName;
	}
	
	public int getTimeShift(){
		return mTimeShift;
	}
	
	public void setName(String name) {
		mName = name;
	}
	public void setSSN(int SSN) {
		mSSN = SSN;
	}

	@Override
	public void setItemsDesired(Map<EnumItemType, Integer> map) {
		mItemsDesired = map;
	}

	@Override
	public Map<Role, Boolean> getRoles() {
		return mRoles;
	}

	@Override
	public HousingBase getHousingRole() {
		for (Role iRole : mRoles.keySet()){
			if(iRole instanceof HousingBase){
				return (HousingBase) iRole;
			}
		}
		return null;
	}

	@Override
	public CityPerson getPersonGui() {
		return (CityPerson)mPersonGui;
	}

	@Override
	public void setGuiPresent() {
		mPersonGui.setPresent(true);
//		mPersonGui.setX(250);
//		mPersonGui.setY(300);
	}
	
	public CityPerson getGui(){
		return mPersonGui;
	}

	@Override
	public void setJobFalse() {
		mAtJob = false;
	}
	
	public boolean hasCar() {
		return mHasCar;
	}
	
	public void setHasCar(boolean c) {
		mHasCar = c;
	}
	
	public void Do(String msg) {
		super.Do(msg, AlertTag.PERSON);
	}
	
	public void print(String msg) {
		super.print(msg, AlertTag.PERSON);
	}
	
	public void print(String msg, Throwable e) {
		super.print(msg, AlertTag.PERSON, e);
	}

	public EnumJobType getJobType() {
		return mJobType;
	}

	
	public List<Event> getEvents() {
		return mEvents;
	}
	
	public boolean getAtJob(){
		return mAtJob;
	}
	
	public List<Person> getFriendList(){
		return mFriends;
	}
	
	public void assignNextEvent(){
		Random rand = new Random();
		mEvents.add(ContactList.sEventList.get(rand.nextInt(ContactList.sEventList.size())));
		mCommuterRole.mState = PersonState.walking;
		stateChanged();
	}

	@Override
	public void setJobType(EnumJobType type) {
		mJobType = type;
	}
}
