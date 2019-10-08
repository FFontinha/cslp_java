package entities;
import main.Constants;
import sharedRegions.Airport;
import sharedRegions.Destination;
import sharedRegions.Plane;
import sharedRegions.Repository;


/**
 *	Este tipo de dados implementa o thread Passenger.<p>
 *  O ciclo de vida do passenger é  o seguinte:
 *     <br> Ele vai para o aeroporto numa altura aleatória;
 *     <br> Entra na fila para o aviao e quando for a sua vez mostra o passaporte;
 *     <br> Depois entra no aviao e espera que descole e a seguir aterre.;
 */
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

	/**
	 *  Ciclo de vida da thread Passenger.
	 */
	public void run(){
			travelToAirport();
			airport.waitInQueue();
			airport.showDocuments(ID);
			plane.boardThePlane();
			plane.waitForEndOfFlight();
			destin.leavePlane();
	}

	/**
	 * 	Operacao para mudar o ID da thread
	 * 		@param id ID da thread Passenger
	 */
	public void setPassengerID(int id){
		ID = id;
	}

	/**
	 * 	Operacao para receber o ID da thread
	 */
	public int getPassengerID(){
		return ID;
	}

	/**
	 * 	Operacao para mudar o estado da thread
	 * 		@param state Estado da thread Passenger
	 */
	public void setPassengerState(PassengerEnum state) {
		this.state = state;
	}

	/**
	 * 	Operacao para receber o estado da thread
	 */
	public PassengerEnum getPassengerState(){
		return state;
	}

	/**
	 * 	Operacao para ir para o aeroporto
	 */
	private void travelToAirport(){
		try{
		sleep ((long) (Constants.maxTravel*Math.random()+1));
		}catch(InterruptedException e) {}
	}
}
