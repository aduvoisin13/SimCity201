package transportation.interfaces;

public interface TransportationRider {
	public void msgBoardBus();
	public void msgAtStop(int stopBusIsAt);
	public String getName();
	
	//public void NotifyBus();
	public void msgAtBusStop(int currentStop, int destinationStop);
}
