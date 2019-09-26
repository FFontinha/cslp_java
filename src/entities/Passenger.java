package entities;
import main.Constants;
import sharedRegions.Airport;
import sharedRegions.Destination;
import sharedRegions.Plane;
import sharedRegions.Repository;

public class Passenger extends Thread {

	private PassengerEnum state;
	private String name;
	private int ID;
	private Airport airport;
	private Plane plane;
	private Destination destin;
	
	public Passenger(String name, int ID, Airport airport, Plane plane, Destination destin, Repository repo){
		this.name =name;
		this.ID = ID;
		this.airport = airport;
		this.plane = plane;
		this.destin = destin;
		this.state = PassengerEnum.GTAP;
	}
	
	public Passenger(int ID, Airport airport, Plane plane, Destination destin, Repository repo){
		this.ID = ID;
		this.airport = airport;
		this.plane = plane;
		this.destin = destin;
		this.state = PassengerEnum.GTAP;
	}
	
	public void run(){
			travelToAirport();
			airport.waitInQueue();
			airport.showDocuments(ID);
			plane.boardThePlane();
			plane.waitForEndOfFlight();
			destin.leavePlane();
	}
	
	public void setPassengerID(int id){
		ID = id;
	}
	
	public int getPassengerID(){
		return ID;
	}
	
	public void setPassengerState(PassengerEnum state) {
		this.state = state;
	}
	
	public PassengerEnum getPassengerState(){
		return state;
	}
	
	private void travelToAirport(){
		try{
		sleep ((long) (Constants.maxTravel*Math.random()+1));
		}catch(InterruptedException e) {}
	}
}
