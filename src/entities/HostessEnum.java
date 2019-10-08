package entities;

/**
 * Estados da thread Hostess
 * 
 *		<br> Waiting for flight;
 *		<br> Waiting for passenger;
 *		<br> Check passport;
 *		<br> Ready to flight.
 */
public enum HostessEnum {
	WTFL,
	WFPS,
	CKPS,
	RDTF;
}
