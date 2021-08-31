package components;

import java.util.ArrayList;
import java.util.Random;

/**
 * Class that representing the vehicles for transporting packages.
 * 
 * @author Sagi Biran , ID: 205620859
 */
public abstract class Truck implements Node {
	/**
	 * static variable to make sure different id is set for each object that created
	 * and inherit from this class.
	 */
	private static int globalTrackID = 2000;
	/**
	 * Serial number of vehicle, vehicle numbering starts from 2000.
	 */
	private final int truckID;
	/**
	 * Vehicle ID number.
	 */
	private final String licensePlate;
	/**
	 * Model of vehicle.
	 */
	private final String truckModel;
	/**
	 * my addition, origin variable(branch type) inorder to truck where which car is
	 * located and used to manipulate transfers between vehicles and branches
	 * (included hub).
	 */
	private Branch origin;
	/**
	 * Vehicle availability (available for transportation, the field gets a false
	 * value when the vehicle goes to collect / deliver packages).
	 */
	private boolean available;
	/**
	 * Time left until the end of the transport.
	 */
	private int timeLeft;
	/**
	 * List of packages for transportation that are in the vehicle.
	 */
	ArrayList<Package> packages = new ArrayList<Package>();

	/**
	 * A random default constructor that create an object with a license plate and
	 * model of a vehicle at random. A car's model consists of letter "M" and a
	 * number between 0 and 4. A license plate consists of three numbers separated
	 * by a line, according to the pattern - xxx-xx-xxx.
	 */

	public Truck() {
		Random rand = new Random();
		this.truckModel = "M" + Integer.toString((rand.nextInt(5)));
		this.licensePlate = Integer.toString((rand.nextInt(900) + 100)) + "-" + Integer.toString((rand.nextInt(90) + 10)) + "-" + Integer.toString((rand.nextInt(900) + 100));
		truckID = globalTrackID;
		++globalTrackID;
		//
		available = true;
	}

	/**
	 * A builder who receives as arguments a number plate and model of the vehicle
	 * and create an object.
	 * 
	 * @param licensePlate
	 * @param truckModel
	 */
	public Truck(String licensePlate, String truckModel) {
		this.licensePlate = licensePlate;
		this.truckModel = truckModel;
		truckID = globalTrackID;
		++globalTrackID;
		available = true;
	}

	/********************************************************
	 * getters&setters
	 ********************************************************/

	public int getTruckID() {
		return truckID;
	}

	public String getLicensePlate() {
		return licensePlate;
	}

	public String getTruckModel() {
		return truckModel;
	}

	public boolean getAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public int getTimeLeft() {
		return timeLeft;
	}

	public void setTimeLeft(int timeLeft) {
		this.timeLeft = timeLeft;
	}

	public static int getGlobalTrackID() {
		return globalTrackID;
	}

	public static void setGlobalTrackID(int globalTrackID) {
		Truck.globalTrackID = globalTrackID;
	}

	public ArrayList<Package> getPackages() {
		return packages;
	}

	public void setPackages(ArrayList<Package> packages) {
		this.packages = packages;
	}

	public Branch getOrigin() {
		return origin;
	}

	public void setOrigin(Branch origin) {
		this.origin = origin;
	}

	/********************************************************************************************************************************/
	/**
	 * helps print any truck type information in appropriate way.
	 */
	@Override
	public String toString() {
		return " [truckID=" + getTruckID() + ", licensePlate=" + getLicensePlate() + ", truckModel=" + getTruckModel() + ", available=" + getAvailable();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Truck)) {
			return false;
		}
		Truck other = (Truck) obj;
		if (getAvailable() != other.getAvailable()) {
			return false;
		}
		if (getLicensePlate() == null) {
			if (other.getLicensePlate() != null) {
				return false;
			}
		} else if (!getLicensePlate().equals(other.getLicensePlate())) {
			return false;
		}
		if (getOrigin() == null) {
			if (other.getOrigin() != null) {
				return false;
			}
		} else if (!getOrigin().equals(other.getOrigin())) {
			return false;
		}
		if (getPackages() == null) {
			if (other.getPackages() != null) {
				return false;
			}
		} else if (!getPackages().equals(other.getPackages())) {
			return false;
		}
		if (getTimeLeft() != other.getTimeLeft()) {
			return false;
		}
		if (getTruckID() != other.getTruckID()) {
			return false;
		}
		if (getTruckModel() == null) {
			if (other.getTruckModel() != null) {
				return false;
			}
		} else if (!getTruckModel().equals(other.getTruckModel())) {
			return false;
		}
		return true;
	}

}
