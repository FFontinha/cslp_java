package entities;
import main.Constants;
import sharedRegions.Airport;
import sharedRegions.Plane;
import sharedRegions.Repository;



/**
 *	Este tipo de dados implementa o thread Hostess.<p>
 *  O ciclo de vida da hostess � o seguinte:
 *     <li> �o principio ela espera pelo proximo voo;
 *     <li> Depois espera que um passageiro entre na fila para o aviao e faz check ao passaporte;
 *     <li> Apos certas condicoes, ela informa que ao aviao vai descolar;
 *     <li> Isto tudo enquanto o numero de passageiros no destino � menor ao numero total de passageiros.
 */
public class Hostess extends Thread {

	private HostessEnum state;
	private Repository repo;
	private String name;
	private Airport airport;
	private Plane plane;
	private int passID;
	private int nPass;


	public Hostess(String name, Airport airport, Plane plane, Repository repo){
		this.name = name;
		this.airport = airport;
		this.plane = plane;
		this.repo = repo;
		this.state = HostessEnum.WTFL;
	}
	
	/**
	   *  Ciclo de vida da thread Hostess.
	   */
	public void run(){
		do{
			airport.waitForNextFlight();
			nPass=0;
			while((passID=airport.waitForPassenger())!=-1){
				airport.checkPassport(passID);
				nPass+=1;
			}
			plane.informPlaneReadyToTakeOff(nPass);
		}while(airport.getNumbOfCheckedPass()<Constants.nPassengers);
	}	
	
	/**
	 * 	Operacao para mudar o estado da thread
	 * 		@param state Estado da thread Hostess
	 */
	public void setHostessState(HostessEnum state) {
		this.state = state;
	}
	
	/**
	 * 	Operacao para retornar o numero de passageiros no voo
	 * 		@return nPass Numero de passageiros no voo
	 */
	public int getPassengersInFlight(){
		return nPass;
	}
	
	/**
	 * 	Operacao para retornar o estado da thread
	 * 		@return state Estado da thread Hostess
	 */
	public HostessEnum getHostessState() {
		return state;
	}
	
}