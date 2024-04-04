package checkInSimulation;

// Class to represent a flight, including its details and a list of passengers.
public class Flight implements Comparable<Flight> {
	
	private String flight_code;// Unique identifier for the flight.
	private String date;// Date of the flight.
	private String destination;// Destination of the flight.
	private String carrier;// Airline carrier of the flight.
	private int capacity;// Maximum number of passengers for the flight.
	private double maxWeight; // Maximum weight capacity for baggage.
	private double maxVolume;// Maximum volume capacity for baggage.
	private PassengerList passengerList;// List of passengers booked on this flight.
	// List of passengers booked on this flight.
	public Flight() {}
	
	/**
	 * Constructor to initialize a flight with its details.
	 */
	public Flight(String flight_code, String date, String destination, String carrier, int capacity, double maxWeight,
			double maxVolume) {
		this.flight_code = flight_code;
		this.date = date;
		this.destination = destination;
		this.carrier = carrier;
		this.capacity = capacity;
		this.maxWeight = maxWeight;
		this.maxVolume = maxVolume;
		 // Initialize the passenger list for the flight
		this.passengerList = new PassengerList();
	}

	/**
	 * @return The flight code.
	 */
	public String getFlight() {
		return flight_code;
	}

	/**
	 * @return The date.
	 */
	public String getDate() {
		return date;
	}
	
	/**
	 * @return The maxWeight.
	 */
	public double getMaxWeight() {
		return maxWeight;
	}
	
	

	/**
	 * @return The destination.
	 */
	public String getDestination() {
		return destination;
	}

	/**
	 * @return The carrier.
	 */
	public String getCarrier() {
		return carrier;
	}

	/**
	 * @return The capacity.
	 */
	public int getCapacity() {
		return capacity;
	}

	/**
	 * @return The maximum weight.
	 */
	public double getWeight() {
		return maxWeight;
	}

	/**
	 * @return The maximum volume.
	 */
	public double getVolume() {
		return maxVolume;
	}

	/**
	 * @return The passenger list.
	 */
	public PassengerList getList() {
		return passengerList;
	}
	// Methods to calculate various metrics related to the flight's passengers and baggage.
	/**
	 * @return The number of passengers checked-in.
	 */
	public int numberOfCheckIn() {
		int cnt = 0;
		for (int i = 0; i < passengerList.getNumberOfEntries(); i++) {
			Passenger p = passengerList.getByIdx(i);
			if (p.getCheckin().equals("Yes"))
				cnt++;
		}
		return cnt;
	}

	/**
	 * @return The total weight of the baggage.
	 */
	public double totalWeight() {
		double sum = 0;
		for (int i = 0; i < passengerList.getNumberOfEntries(); i++) {
			Passenger p = passengerList.getByIdx(i);
			if (p.getCheckin().equals("Yes"))
				sum += p.getWeight();
		}
		return sum;
	}

	/**
	 * @return The total volume of the baggage.
	 */
	public double totalVolume() {
		double sum = 0;
		for (int i = 0; i < passengerList.getNumberOfEntries(); i++) {
			Passenger p = passengerList.getByIdx(i);
			if ((p.getCheckin().equals("Yes")))
				sum += p.getVolume();
		}
		return sum;
	}

	/**
	 * @return The total excess baggage fees.
	 */
	public double totalFees() {
		double sum = 0;
		for (int i = 0; i < passengerList.getNumberOfEntries(); i++) {
			Passenger p = passengerList.getByIdx(i);
			if ((p.getCheckin().equals("Yes")))
				sum += p.excess_fee();
		}
		return sum;
	}

	/**
	 * Determine if the flight is overweight.
	 */
	public boolean check_weight() {
		if (totalWeight() > maxWeight)
			return false;
		return true;
	}

	/**
	 * Determine if the flight exceeds its maximum volume.
	 */
	public boolean check_volume() {
		if (totalVolume() > maxVolume)
			return false;
		return true;
	}

	/**
	 * Test for content equality between two flights.
	 * 
	 * @param other The object to compare to this one.
	 * @return true if the argument object has the same flight code.
	 */
	public boolean equals(Object other) {
		if (other instanceof Flight) {
			Flight otherFlight = (Flight) other;
			return flight_code.equals(otherFlight.getFlight());
		} else {
			return false;
		}
	}

	/**
	 * Compare this Flight object against another, for the purpose of sorting. The
	 * fields are compared by flight code.
	 * 
	 * @param otherFlight The flight to be compared against.
	 * @return a negative integer if this flight_code comes before the parameter's
	 *         flight_code, zero if they are equal and a positive integer if this
	 *         comes after the other.
	 */

	public int compareTo(Flight otherFlight) {
		return flight_code.compareTo(otherFlight.getFlight());
	}

	/**
	 * @return A string containing all details.
	 */
	public String toString() {
		return String.format("%-8s", flight_code) + String.format("%-8s", date) + String.format("%-5s", destination)
				+ String.format("%-5s", carrier) + String.format("%d", capacity) + String.format("%f", maxWeight)
				+ String.format("%f", maxVolume);
	}

}
