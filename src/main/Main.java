package main;
import entities.Hostess;
import entities.Passenger;
import entities.Pilot;
import sharedRegions.Airport;
import sharedRegions.Destination;
import sharedRegions.Plane;
import sharedRegions.Repository;

public class Main {
	public static void main(String[] args) {
		Repository repo = new Repository("log.txt");
		Airport airport = new Airport(repo);
		Plane plane = new Plane(repo);
		Destination destination = new Destination(plane, repo);
		Passenger[] pg = new Passenger[Constants.nPassengers];
		Hostess ht;
		Pilot pl;
		
		//Criaçao
		for (int passengers = 0; passengers < Constants.nPassengers; passengers++){
			pg[passengers] = new Passenger(passengers, airport, plane, destination, repo);
		}
		ht = new Hostess("Hospedeira", airport, plane, repo);
		pl = new Pilot("Piloto",destination, plane, airport, repo);
		
		//Inicializaao
		for (int passengers = 0; passengers < Constants.nPassengers; passengers++)
			pg[passengers].start();
		ht.start();
		pl.start();
		
		//Execuçao
		for (int passengers = 0; passengers < Constants.nPassengers; passengers++) {
			try {
				pg[passengers].join();
			} catch (InterruptedException e) {}
		}
		try {
			ht.join();
		} catch (InterruptedException e) {}
		try {
			pl.join();
		} catch (InterruptedException e) {}
	}
}
