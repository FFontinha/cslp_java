package entities;

/**
 * Estados da thread Hostess
 * 
 *		<li> Waiting for flight;
 *		<li> Waiting for passenger;
 *		<li> Check passport;
 *		<li> Ready to flight.
 */
public enum HostessEnum {
	WTFL,
	WFPS,
	CKPS,
	RDTF;
}
