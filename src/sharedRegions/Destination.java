package sharedRegions;
import entities.Passenger;
import entities.PassengerEnum;

public class Destination {
	
	private Repository repo;
	private Plane plane;
	private int nTransportedPass;
	private int nPassengersInFlight[];

	public Destination(Plane plane, Repository repo) {
		this.repo = repo;
		this.plane = plane;
		nTransportedPass = 0;
	}

	/**
	 *  Passageiro: Sair do aviao
	 */
	public synchronized void leavePlane(){			//Passenger
		Passenger pg = (Passenger) Thread.currentThread();
		pg.setPassengerState(PassengerEnum.ATDS);
		nTransportedPass++;
		int nFlight=repo.getnFlight();
		nPassengersInFlight=repo.getnPassengersInFlight();
		nPassengersInFlight[nFlight]--;
		repo.setnPassengersInFlight(nPassengersInFlight);
		repo.setnPassengersAtDestination(nTransportedPass);
		repo.setPassengerState(pg.getPassengerID(),pg.getPassengerState());
		if(nPassengersInFlight[nFlight]==0){
			plane.alertPilotplaneIsEmpty();
		}
		notifyAll();	
	}

	public int getNumbOfTransportedPass(){			//Pilot
		return nTransportedPass;
	}
}
