package sharedRegions;

import entities.HostessEnum;
import entities.PassengerEnum;
import entities.PilotEnum;
import genclass.*;
import main.Constants;

public class Repository {

	private PilotEnum plState;
	private HostessEnum htState;
	private PassengerEnum pgState[];
	private int nPassengersInQueue;
	private int nPassengersInFlight[];
	private int nPassengersPerFlight[];
	private int nPassengersAtDestination;
	private int nFlight;
	private String fileName;
	private TextFile log;
	
	public Repository(String fileName){
		plState = PilotEnum.FLBK;
		htState = HostessEnum.WTFL;
		pgState = new PassengerEnum[Constants.nPassengers];
		for(int i = 0; i<Constants.nPassengers; i++){
			pgState[i] = PassengerEnum.GTAP;
		}
		this.fileName=fileName;
		nFlight = 0;
		nPassengersInQueue = 0;
		nPassengersInFlight = new int[Constants.maxNumberFlights];
		nPassengersPerFlight = new int[Constants.maxNumberFlights];
		nPassengersAtDestination = 0;
		log = new TextFile();
		if (!log.openForWriting (".", fileName)){
			GenericIO.writelnString ("A opera��o de cria��o do ficheiro " + fileName + " falhou!");
            System.exit (1);
        }
	    log.writelnString ("                                 Airlift - Description of the internal state");
	    log.writelnString (" PT   HT   P00  P01  P02  P03  P04  P05  P06  P07  P08  P09  P10  P11  P12  P13  P14  P15  P16  P17  P18  P19  P20 InQ InF PTAL");
	    if (!log.close ()){ 
	    	GenericIO.writelnString ("A opera��o de fechar o ficheiro " + fileName + " falhou!");
	        System.exit (1);
	    }
	}
	       
    private synchronized void SaveState(){
		String string = String.format("%4s %4s %4s %4s %4s %4s %4s %4s %4s %4s %4s %4s %4s %4s %4s %4s %4s %4s %4s %4s %4s %4s %4s  %2d  %2d  %2d",plState ,htState,pgState[0],pgState[1],pgState[2],pgState[3],pgState[4],pgState[5],pgState[6],pgState[7],pgState[8],pgState[9],pgState[10],pgState[11],pgState[12],pgState[13],pgState[14],pgState[15],pgState[16],pgState[17],pgState[18],pgState[19],pgState[20],nPassengersInQueue,nPassengersInFlight[nFlight], nPassengersAtDestination);		
		if (!log.openForAppending (".", fileName)){
			GenericIO.writelnString ("A operação de criação do ficheiro " + fileName + " falhou!");
            System.exit (1);
        }
		log.writelnString(string);
	    if (!log.close ()){ 
	    	GenericIO.writelnString ("A opera��o de fechar o ficheiro " + fileName + " falhou!");
	        System.exit (1);
	    }
	}
    
    public synchronized void SaveCheck(int passID, int flight){
    	String string = "\nFlight "+flight+": passenger "+passID+" checked.";
		if (!log.openForAppending (".", fileName)){
			GenericIO.writelnString ("A opera��o de cria��o do ficheiro " + fileName + " falhou!");
            System.exit (1);
        }
		log.writelnString(string);
	    if (!log.close ()){ 
	    	GenericIO.writelnString ("A opera��o de fechar o ficheiro " + fileName + " falhou!");
	        System.exit (1);
	    }
    }
    
    public synchronized void SaveBoarding(int flight){
    	String string = "\nFlight "+flight+": boarding started.";
		if (!log.openForAppending (".", fileName)){
			GenericIO.writelnString ("A opera��o de cria��o do ficheiro " + fileName + " falhou!");
            System.exit (1);
        }
		log.writelnString(string);
	    if (!log.close ()){ 
	    	GenericIO.writelnString ("A opera��o de fechar o ficheiro " + fileName + " falhou!");
	        System.exit (1);
	    }
    }
    
    public synchronized void SaveArrival(int flight){
    	String string = "\nFlight "+flight+": arrived.";
		if (!log.openForAppending (".", fileName)){
			GenericIO.writelnString ("A opera��o de cria��o do ficheiro " + fileName + " falhou!");
            System.exit (1);
        }
		log.writelnString(string);
	    if (!log.close ()){ 
	    	GenericIO.writelnString ("A opera��o de fechar o ficheiro " + fileName + " falhou!");
	        System.exit (1);
	    }
    }
    
    public synchronized void SaveDeparture(int flight, int nPass ){
    	nPassengersPerFlight[flight]=nPass;
    	String string = "\nFlight "+flight+": departed with "+nPass+" passengers.";
		if (!log.openForAppending (".", fileName)){
			GenericIO.writelnString ("A opera��o de cria��o do ficheiro " + fileName + " falhou!");
            System.exit (1);
        }
		log.writelnString(string);
	    if (!log.close ()){ 
	    	GenericIO.writelnString ("A opera��o de fechar o ficheiro " + fileName + " falhou!");
	        System.exit (1);
	    }
    }
    
    public synchronized void SaveReturn(int flight){
    	String string = "\nFlight "+flight+": returning.";
		if (!log.openForAppending (".", fileName)){
			GenericIO.writelnString ("A opera��o de cria��o do ficheiro " + fileName + " falhou!");
            System.exit (1);
        }
		log.writelnString(string);
	    if (!log.close ()){ 
	    	GenericIO.writelnString ("A opera��o de fechar o ficheiro " + fileName + " falhou!");
	        System.exit (1);
	    }
    }
   
    public synchronized void sumUpResults(){
		if (!log.openForAppending (".", fileName)){
			GenericIO.writelnString ("A opera��o de cria��o do ficheiro " + fileName + " falhou!");
            System.exit (1);
        }
		log.writelnString("\nAirlift sum up:");
		for (int i=1; i <= nFlight ; i++){
			String string = "Flight "+(i)+" transported "+nPassengersPerFlight[i]+" passengers.";
			log.writelnString(string);
    	}
	    if (!log.close ()){ 
	    	GenericIO.writelnString ("A opera��o de fechar o ficheiro " + fileName + " falhou!");
	        System.exit (1);
	    }
    }
    
    public synchronized void setnPassengersInQueue(int nPassengersInQueue) {
		this.nPassengersInQueue = nPassengersInQueue;
	}
    
    public synchronized void setnPassengersInFlight(int[] nPassengersInFlight) {
		this.nPassengersInFlight = nPassengersInFlight;
	}


	public synchronized void setnFlight(int nFlight) {
		this.nFlight = nFlight;
	}


	public synchronized void setPilotState(PilotEnum plState){
		this.plState=plState;
		SaveState();
	}
    
    public synchronized void setHostessState(HostessEnum htState){
		this.htState=htState;
		SaveState();
	}
    
    public synchronized void setPassengerState(int passID, PassengerEnum pgState){
		this.pgState[passID]=pgState;
		SaveState();
	}

    public synchronized void setnPassengersAtDestination(int nPassengersAtDestination) {
		this.nPassengersAtDestination = nPassengersAtDestination;
	}
    
	public int getnFlight() {
		return nFlight;
	}

	public int[] getnPassengersInFlight() {
		return nPassengersInFlight;
	}
	
}
