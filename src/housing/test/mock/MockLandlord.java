package housing.test.mock;

import base.PersonAgent;

import base.interfaces.Person;
import base.interfaces.Role;
import test.mock.MockPerson;
import housing.interfaces.HousingLandlord;
import housing.interfaces.HousingRenter;

/**
 * A sample MockRenter built to unit test a LandlordRole.
 *
 * @author Maggi Yang 
 *
 */
public class MockLandlord extends Mock implements Role, HousingLandlord {

	public MockLandlord(String name) {
		super();

	}

	@Override
	public void msgIWouldLikeToLiveHere(HousingRenter r, double cash, int SSN) {
		log.add(new LoggedEvent("Received msgIWouldLikeToLiveHere"));
		
	}

	@Override
	public void msgHereIsPayment(int SSN, double paymentAmt) {
		log.add(new LoggedEvent("Received msgHereIsPayment: " + paymentAmt));
		
	}

	@Override
	public int getRenterListSize() {
		
		return 0;
	}

	@Override
	public boolean pickAndExecuteAnAction() {
		
		return false;
	}


	@Override
	public void setPerson(Person person) {
		
		
	}

	@Override
	public int getSSN() {
		
		return 0;
	}



}
