package restaurant.restaurant_smileham.test.mock;


/**
 * This is the base class for all mocks.
 *
 * @author Sean Turner
 *
 */
public class SmilehamMock {
	protected String mName;
	
	public SmilehamMock(String name) {
		this.mName = name;
	}

	public String getName() {
		return mName;
	}

	public String toString() {
		return this.getClass().getName() + ": " + mName;
	}

}
