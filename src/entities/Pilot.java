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


	/**
	 *	Este tipo de dados implementa o thread Pilot.<p>
	 *  O ciclo de vida do passenger é  o seguinte:
	 *     <br> Ele avisa que oaviao esta pronto para receber passageiros;
	 *     <br> Espera que tenha passageiros suficientes e descola;
	 *     <br> Aterra e deixa os passageiros e voa de volta;
	 */
	public Pilot(String name, Destination destination, Plane plane, Airport airport, Repository repo) {
		this.name = name;
		this.destination = destination;
		this.plane = plane;
		this.airport = airport;
		this.repo = repo;
		this.state = PilotEnum.FLBK;
	}

	/**
	 *  Ciclo de vida da thread Pilot.
	 */
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

	/**
	 * 	Operacao para mudar o estado da thread
	 * 		@param state Estado da thread Passenger
	 */
	public void setPilotState(PilotEnum state) {
		this.state = state;
	}


	/**
	 * 	Operacao para receber o estado da thread
	 */
	public PilotEnum getPilotState() {
		return state;
	}
}
