package components;

/**
 * * A vehicle for transporting packages of non-standard size (exceptional
 * cargo). All vehicles of this type are in the sorting center.
 * 
 * @author Sagi Biran , ID: 205620859
 */
public class NonStandardTruck extends Truck {
	private int width;
	private int length;
	private int height;

	/**
	 * A default constructor that produces an object with a vehicle ID number and
	 * model at random.
	 */
	public NonStandardTruck() {
		super();
		// according to output given by lecture, no values as been asked in the homework
		// pdf.
		this.length = 1400;
		this.width = 400;
		this.height = 300;
		System.out.println(toString());

	}

	/**
	 * Constructor that accepts arguments: license plate number, model of the
	 * vehicle and maximum length / width / height of cargo that the vehicle can
	 * carry.
	 * 
	 * @param licensePlate indicate license plate number
	 * @param truckModel   indicate model of vehicle
	 * @param length       maximum length cargo that the vehicle can carry
	 * @param width        width length cargo that the vehicle can carry
	 * @param height       height length cargo that the vehicle can carry
	 */

	public NonStandardTruck(String licensePlate, String truckModel, int length, int width, int height) {
		super(licensePlate, truckModel);
		this.length = length;
		this.width = width;
		this.height = height;

	}

	/**
	 * method to collect package from "CUSTOMER"
	 */
	@Override
	public void collectPackage(Package p) {
		p.addTracking(this, p.getStatus());
		this.setTimeLeft(2);
		System.out.println("NonStandardTruck " + this.getTruckID() + " is collecting package  " + p.getPackageID() + " ,time to arrive: " + this.getTimeLeft());
		this.setAvailable(false);
	}

	/**
	 * decided to no implement this method according to Michael Finkelstien
	 * Implementation is OPTIONAL.
	 */
	@Override
	public void deliverPackage(Package p) {
	}

	/**
	 * Performs a work unit (in each beat) according to the following requirements:
	 * An available vehicle does nothing. A vehicle that is in drive already, will
	 * reduce is time for arriving by one then if after reduction the value in time
	 * is equal to zero, so the trip ended and a vehicle reached its destination. If
	 * the purpose of the trip was to collect a package from a customer Sender
	 * (COLLECTION) The vehicle will be transferred for delivery, the status of the
	 * package will be updated, a suitable registration will be added to the
	 * tracking list of the package, and a message will be printed that the vehicle
	 * has collected the package. If the purpose of the trip was to deliver the
	 * package to the customer (DISTRIBUTION) the package will be removed from the
	 * list The packages in the vehicle, the status of the package and the transfer
	 * history will be updated accordingly and a message will be printed stating
	 * that the package Delivered to the customer, after that the vehicle switches
	 * to "free" mode.
	 */
	@Override
	public void work() {
		if (this.getAvailable())
			return;
		this.setTimeLeft(getTimeLeft() - 1);
		if (getTimeLeft() == 0) {
			if (this.getPackages().get(0).getStatus() == Status.COLLECTION) {
				this.getPackages().get(0).setStatus(Status.DISTRIBUTION);
				this.getPackages().get(0).addTracking(this, Status.DISTRIBUTION);
				System.out.println("NonStandardTruck " + this.getTruckID() + " has collected package  " + this.getPackages().get(0).getPackageID());
				this.setTimeLeft(2);
				System.out.println("NonStandardTruck " + getTruckID() + " is delivering package " + this.getPackages().get(0).getPackageID() + " ,time left: " + getTimeLeft());

			}
		} else if (this.getPackages().get(0).getStatus() == Status.DISTRIBUTION) {

			System.out.println("NonStandardTruck " + this.getTruckID() + " has delivered package " + this.getPackages().get(0).getPackageID() + " to the destination");
			this.getPackages().get(0).addTracking(null, Status.DELIVERED);
			this.getPackages().remove(0);
			this.setAvailable(true);
		}

	}

	/********************************************************
	 * getters&setters
	 ********************************************************/

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	/********************************************************************************************************************************/
	/**
	 * helps print Non Standard truck in appropriate way.
	 */
	public String toString() {
		return "Creating NonStandardTruck" + super.toString() + " ,length=" + getLength() + " ,width=" + getWidth() + " ,height=" + getHeight() + "]";
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof NonStandardTruck)) {
			return false;
		}
		NonStandardTruck other = (NonStandardTruck) obj;
		if (getHeight() != other.getHeight()) {
			return false;
		}
		if (getLength() != other.getLength()) {
			return false;
		}
		if (getWidth() != other.getWidth()) {
			return false;
		}
		return true;
	}

}
