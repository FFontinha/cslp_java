package entities;
import main.Constants;
import sharedRegions.Airport;
import sharedRegions.Destination;
import sharedRegions.Plane;
import sharedRegions.Repository;

public class Pilot extends Thread {

	private PilotEnum state;
	private Destination destination;
	private Plane plane;
	private Airport airport;
	private Repository repo;
	private String name;
	
	//Construtor
	public Pilot(String name, Destination destination, Plane plane, Airport airport, Repository repo) {
		this.name = name;
		this.destination = destination;
		this.plane = plane;
		this.airport = airport;
		this.repo = repo;
		this.state = PilotEnum.FLBK;
	}

	public void run(){
		do{
			airport.informPlaneReadyForBoarding();
			plane.waitForAllInBoard();
			plane.flyToDestinationPoint();
			plane.dropPassengers();
			plane.flyDeparturePoint();
		}while(destination.getNumbOfTransportedPass()<Constants.nPassengers);
		repo.sumUpResults();
			
	}
	
	public void setPilotState(PilotEnum state) {
		this.state = state;
	}


	public PilotEnum getPilotState() {
		return state;
	}
}
