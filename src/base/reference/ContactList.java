package base.reference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import bank.roles.BankMasterTellerRole;
import base.Location;
import base.interfaces.Person;

public class ContactList {
	
	//------------------------------------------------------PROJECT SETTINGS----------------------------------------------------------
	
	
	
	//----------------------------------------------------------LISTS----------------------------------------------------------
	public static List<Person> sPersonList = Collections.synchronizedList(new ArrayList<Person>());
	
	public static List<Market> sMarketList = Collections.synchronizedList(new ArrayList<Market>());
	public static List<House> sHouses = Collections.synchronizedList(new ArrayList<House>());
	public static List<Bank> sBanks = Collections.synchronizedList(new ArrayList<Bank>());
	
	//DAVID JERRY SHANE: 0 TOP PRIORITY - add house/bank/market and separate data from gui
	
	//----------------------------------------------------------LOCATIONS----------------------------------------------------------
	
	//GRID LOCATIONS AND INTERFACE (Used here and in A**)
	public static int cGRID_POINT1 = 100;
	public static int cGRID_POINT2 = 180;
	public static int cGRID_POINT3 = 205;
	public static int cGRID_POINT4 = 285;
	public static int cGRID_POINT5 = 315;
	public static int cGRID_POINT6 = 395;
	public static int cGRID_POINT7 = 420;
	public static int cGRID_POINT8 = 500;
	
	public static Location gridLocation(int x, int y){
		assert ((x < 4) && (y < 4));
		int xdim = 0;
		switch (x){
			case 0: xdim = cGRID_POINT1;
				break;
			case 1: xdim = cGRID_POINT3;
				break;
			case 2: xdim = cGRID_POINT5;
				break;
			case 3: xdim = cGRID_POINT7;
				break;
		}
		
		int ydim = 0;
		switch (y){
			case 0: ydim = cGRID_POINT1;
				break;
			case 1: ydim = cGRID_POINT3;
				break;
			case 2: ydim = cGRID_POINT5;
				break;
			case 3: ydim = cGRID_POINT7;
				break;
		}
		
		return new Location(xdim, ydim);
	}
	
	public static Location getDoorLocation(Location location){
		return new Location(location.mX - 5, location.mY - 5);
	}
	
	public static final Location cBANK1_LOCATION = gridLocation(0, 0);
	public static final Location cBANK2_LOCATION = gridLocation(3, 0);
	public static final Location cMARKET1_LOCATION = gridLocation(0, 3);
	public static final Location cMARKET2_LOCATION = gridLocation(3, 3);
	public static List<Location> cHOUSE_LOCATIONS;
	public static List<Location> cRESTAURANT_LOCATIONS;
	public static List<Location> cBUS_STOPS;
	public static BankMasterTellerRole masterTeller;
	
	
	//setup job locations
	static final Location cRESTAURANT_LOCATION1 = gridLocation(1, 0); //aduvoisin	0
	static final Location cRESTAURANT_LOCATION2 = gridLocation(2, 0); //cwagoner	1
	static final Location cRESTAURANT_LOCATION3 = gridLocation(1, 3); //jerrywebb	2
	static final Location cRESTAURANT_LOCATION4 = gridLocation(2, 3); //maggiyang	3
	static final Location cRESTAURANT_LOCATION5 = gridLocation(0, 1); //davidmca	4
	static final Location cRESTAURANT_LOCATION6 = gridLocation(0, 2); //smileham	5
	static final Location cRESTAURANT_LOCATION7 = gridLocation(3, 1); //tranac		6
	static final Location cRESTAURANT_LOCATION8 = gridLocation(3, 2); //xurex		7
	static {
		List<Location> list = new ArrayList<Location>();
		list.add(cRESTAURANT_LOCATION1);
		list.add(cRESTAURANT_LOCATION2);
		list.add(cRESTAURANT_LOCATION3);
		list.add(cRESTAURANT_LOCATION4);
		list.add(cRESTAURANT_LOCATION5);
		list.add(cRESTAURANT_LOCATION6);
		list.add(cRESTAURANT_LOCATION7);
		list.add(cRESTAURANT_LOCATION8);
		cRESTAURANT_LOCATIONS = Collections.unmodifiableList(list);
	}
	
	// Bus Stop locations
	static final Location cBusStop0 = new Location(60, 60);
	static final Location cBusStop1 = new Location(60, 515);
	static final Location cBusStop2 = new Location(515, 515);
	static final Location cBusStop3 = new Location(515, 60);
	static {
		List<Location> bslist = new ArrayList<Location>();
		bslist.add(cBusStop0);
		bslist.add(cBusStop1);
		bslist.add(cBusStop2);
		bslist.add(cBusStop3);
		cBUS_STOPS = Collections.unmodifiableList(bslist);
	}
	
	//setup housing locations
		static {
			List<Location> list = new ArrayList<Location>();
			for (int iHouse = 0 ; iHouse < 80; iHouse++){ //80 Houses
				int xCord, yCord = 0;
				if (iHouse / 20 == 0) {					//North
					xCord = 100 + 20 * (iHouse % 20);
					yCord = 0;
				} else if (iHouse / 20 == 2) {			//South
					xCord = 100 + 20 * (iHouse % 20);
					yCord = 580;
				} else if (iHouse / 20 == 3) {			//West
					xCord = 0;
					yCord = 100 + 20 * (iHouse % 20);
				} else {								//East
					xCord = 580;
					yCord = 100 + 20 * (iHouse % 20);
				}
				Location houseLocation = new Location(xCord, yCord);
				list.add(houseLocation);
			}
			cHOUSE_LOCATIONS = Collections.unmodifiableList(list);
		}
		
	
	//----------------------------------------------------------OTHER----------------------------------------------------------
		
	public static void SendPayment(int senderSSN, int receiverSSN, double amount){
		masterTeller.msgSendPayment(senderSSN, receiverSSN, amount);
	}
	//REX: 5 change list iteration and put bank master teller outside
}