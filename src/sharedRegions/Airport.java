package sharedRegions;
import Structures.FIFOGen;
import entities.Hostess;
import entities.HostessEnum;
import entities.Passenger;
import entities.PassengerEnum;
import entities.Pilot;
import entities.PilotEnum;
import main.Constants;

public class Airport {
	
	private boolean readyforboarding;
	private boolean end;
	private int nPassengersInQueue;
	public int NumbOfChecked;
	private int nFlight;
	private int id;
	private FIFOGen fifo;
	private Repository repo;
	
	public Airport(Repository repo) {
		readyforboarding = false;
		nPassengersInQueue = 0;
		NumbOfChecked = 0;
		nFlight = 0;
		id = -1;
		this.fifo = new FIFOGen(Constants.nPassengers);
		this.repo = repo;
	}
	
	public synchronized void waitInQueue(){				//Passenger
		Passenger pg = (Passenger) Thread.currentThread();
		pg.setPassengerState(PassengerEnum.INQE);
		nPassengersInQueue++;
		int passID = pg.getPassengerID();
		fifo.in(passID);
		repo.setnPassengersInQueue(nPassengersInQueue);
		repo.setPassengerState(pg.getPassengerID(), pg.getPassengerState());
		notifyAll();
	}
	
	public synchronized void waitForNextFlight(){		//Hostess
		Hostess ht = (Hostess) Thread.currentThread();
		ht.setHostessState(HostessEnum.WTFL);
		repo.setHostessState(ht.getHostessState());
		while(!readyforboarding){
			try {
				wait();
			} catch (InterruptedException e) {}
		}
		readyforboarding = false;
	}
	
	public synchronized void informPlaneReadyForBoarding(){	//Pilot
		nFlight++;
		repo.setnFlight(nFlight);
		repo.SaveBoarding(nFlight);
		Pilot pl = (Pilot) Thread.currentThread();
		pl.setPilotState(PilotEnum.RDFB);
		repo.setPilotState(pl.getPilotState());
		readyforboarding = true;
		notifyAll();
	}
	
	public synchronized int waitForPassenger(){			//Hostess
		Hostess ht = (Hostess) Thread.currentThread();
		ht.setHostessState(HostessEnum.WFPS);
		repo.setHostessState(ht.getHostessState());
		if(NumbOfChecked == Constants.nPassengers || ht.getPassengersInFlight() == Constants.flyingCapacity_max || ht.getPassengersInFlight() >= Constants.flyingCapacity_min && fifo.isEmpty()){
			return -1;
		}
		while(fifo.isEmpty() && end==false ){
			try {
				wait();
			} catch (InterruptedException e) {}
		}
		id = (int) fifo.out();
		return id;
	}
	
	public synchronized void checkPassport(int passID){			//Hostess
		Hostess ht = (Hostess) Thread.currentThread();
		ht.setHostessState(HostessEnum.CKPS);
		NumbOfChecked++;
		nPassengersInQueue--;
		repo.setnPassengersInQueue(nPassengersInQueue);
		repo.SaveCheck(passID, nFlight);
		repo.setHostessState(ht.getHostessState());
		notifyAll();
		while(id!=-1){
			try {
				wait();
			} catch (InterruptedException e) {}
		}
		if(NumbOfChecked == Constants.nPassengers){
			end = true;
		}
		id=passID;
		notifyAll();
	}
	
	public synchronized void showDocuments(int nPass){				//Passenger
		while(nPass!=id){
			try {
				wait();
			} catch (InterruptedException e) {}
		}
		id = -1;
		notifyAll();
	}

	public int getNumbOfCheckedPass() {
		return NumbOfChecked;
	}
}
