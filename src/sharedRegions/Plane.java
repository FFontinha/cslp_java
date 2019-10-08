package sharedRegions;
import entities.Hostess;
import entities.HostessEnum;
import entities.Passenger;
import entities.PassengerEnum;
import entities.Pilot;
import entities.PilotEnum;
import main.Constants;

public class Plane {
	
	private Repository repo;
	private boolean atDestination;
	private boolean readyToTakeOff;
	private boolean isEmpty;
	private int nPassengersInFlight[];
	private int nPassengersBoarded;
	
	public Plane (Repository repo){
		this.repo=repo;
		atDestination = false;
		readyToTakeOff = false;
		nPassengersInFlight = new int[Constants.maxNumberFlights];
		nPassengersBoarded = 0;
	}

	/**
	 *  Hospedeira: Informa que o aviao pode descolar
	 */
	public synchronized void informPlaneReadyToTakeOff(int nPassChecked){		//Hostess
		Hostess ht = (Hostess) Thread.currentThread();
		while(nPassengersBoarded!=ht.getPassengersInFlight()){
			try {
				wait();
			} catch (InterruptedException e) {}
		}
		nPassengersBoarded = 0;
		ht.setHostessState(HostessEnum.RDTF);
		int nFlight=repo.getnFlight();
		repo.SaveDeparture(nFlight, nPassChecked);
		repo.setHostessState(ht.getHostessState());
		readyToTakeOff = true;
		notifyAll();
	}

	/**
	 *  Piloto: Deixar os passageiros no destino
	 */
	public synchronized void dropPassengers(){		//Pilot
		Pilot pl = (Pilot) Thread.currentThread();
		pl.setPilotState(PilotEnum.DRPP);
		repo.SaveArrival(repo.getnFlight());
		int nFlight=repo.getnFlight();
		repo.setPilotState(pl.getPilotState());
		repo.setnFlight(nFlight);
		atDestination = true;
		notifyAll();
		while(!isEmpty){
			try {
				wait();
			} catch (InterruptedException e) {}
		}
	}

	/**
	 *  Pilot: Voar para o destino
	 */
	public synchronized void flyToDestinationPoint(){		//Pilot
		Pilot pl = (Pilot) Thread.currentThread();
		pl.setPilotState(PilotEnum.FLFW);
		repo.setPilotState(pl.getPilotState());
		try{ 
			Thread.sleep((long) (1000.0 + Math.random()*Constants.MAXFLIGHT));
	       }
	       catch (InterruptedException e) {}
	}

	/**
	 *  Piloto: Esperar até o aviao estar pronto
	 */
	public synchronized void waitForAllInBoard() {			//Pilot
		Pilot pl = (Pilot) Thread.currentThread();
		pl.setPilotState(PilotEnum.WTFB);
		repo.setPilotState(pl.getPilotState());
		while(!readyToTakeOff){
			try {
				wait();
			} catch (InterruptedException e) {}
		}
		readyToTakeOff=false;
		notifyAll();
	}

	/**
	 *  Piloto: Voar de volta
	 */
	public synchronized void flyDeparturePoint() {			//Pilot
		Pilot pl = (Pilot) Thread.currentThread();
		pl.setPilotState(PilotEnum.FLBK);
		int nFlight=repo.getnFlight();
		repo.SaveReturn(nFlight);
		repo.setPilotState(pl.getPilotState());
		try{ 
			Thread.sleep((long) (1000.0 + Math.random()*Constants.MAXFLIGHT));
	       }
	       catch (InterruptedException e) {}
	}

	/**
	 *  Passageiro: Entrar no aviao
	 */
	public synchronized void boardThePlane() {				//Passenger
		Passenger pg = (Passenger) Thread.currentThread();
		pg.setPassengerState(PassengerEnum.INFL);
		isEmpty=false;
		int nFlight=repo.getnFlight();
		nPassengersInFlight[nFlight]++;
		nPassengersBoarded++;
		repo.setnPassengersInFlight(nPassengersInFlight);
		repo.setPassengerState(pg.getPassengerID(), pg.getPassengerState());
		notifyAll();
	}

	/**
	 *  Passageiro: Esperar que o aviao chegue ao destino
	 */
	public synchronized void waitForEndOfFlight() {			//Passenger
		while(!atDestination){
			try {
				wait();
			} catch (InterruptedException e) {}
		}
	}

	/**
	 *  Passageiro: Avisar que o aviao está vazio
	 */
	public synchronized void alertPilotplaneIsEmpty(){		//Passenger
		isEmpty=true;
		atDestination=false;
		notifyAll();
	}	
}

