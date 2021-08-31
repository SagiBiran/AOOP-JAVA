package components;

/**
 * A vehicle that collects a package from the sender's address to the local
 * branch and delivers the package from the destination branch to the
 * recipient's address (one package Only per trip). The class has no fields.
 * 
 * @author Sagi Biran , ID: 205620859
 */
public class Van extends Truck {
	/**
	 * A default constructor that produces a random object according to the same
	 * rules as in the parent class(TRUCK).
	 */
	public Van() {
		super();
		System.out.println("Creating " + toString());
	}

	/**
	 * Produces an object with given parameters.
	 * 
	 * @param licensePlate indicate license plate number
	 * @param truckModel   indicate model of vehicle
	 */
	public Van(String licensePlate, String truckModel) {
		super(licensePlate, truckModel);
	}

	/**
	 * method to collect package from "CUSTOMER"
	 */
	@Override

	public void collectPackage(Package p) {
		p.addTracking(this, p.getStatus());
		this.setTimeLeft(p.getSenderAddress().getStreet() % 10 + 1);
		System.out.println("Van " + this.getTruckID() + " is collecting package  " + p.getPackageID() + " ,time to arrive: " + this.getTimeLeft());
		this.setAvailable(false);
	}

	/**
	 * method for deliver package from van to branch and update package status + add
	 * tracking.
	 */
	@Override
	public void deliverPackage(Package p) {
		p.setStatus(Status.DISTRIBUTION);
		p.addTracking(this, p.getStatus());
		this.setTimeLeft(p.getDestinationAddress().getStreet() % 10 + 1);
		System.out.println("Van " + this.getTruckID() + " is delivering package " + p.getPackageID() + " time left: " + this.getTimeLeft());
		this.setAvailable(false);
	}

	/**
	 * Performs a work unit (in each beat) according to the following requirements:
	 * An available vehicle does nothing. A vehicle that is in the process of being
	 * reduced reduces the time left to the end of its journey if after the
	 * reduction in value The time is equal to zero, so the trip ended and a vehicle
	 * reached its destination. A vehicle found during a trip reduces the time left
	 * to end the trip by 1. If after the reduction the time value is equal To zero,
	 * then the trip ended and a vehicle performed the mission for which it was
	 * sent. If the purpose of the trip was to collect a package from a sending
	 * customer (COLLECTION), the package will at this point move from the vehicle
	 * to the branch, the status of the package will be updated, a suitable
	 * registration will be added to the package tracking list, and a message will
	 * be printed. In addition, the vehicle will change its condition to free. If
	 * the purpose of the trip was to deliver the package to the customer
	 * (DISTRIBUTION) the package will be removed from the list of packages In the
	 * vehicle, the status of the package and the transfer history will be updated
	 * accordingly and a notice will be printed that the package has been delivered
	 * to the customer. In the case of a small package with the option to send a
	 * delivery confirmation, a notification of sending a delivery confirmation will
	 * be printed.
	 */
	@Override
	public void work() {
		if (this.getAvailable())
			return;
		this.setTimeLeft(getTimeLeft() - 1);
		if (getTimeLeft() == 0) {
			if (this.getPackages().get(0).getStatus() == Status.COLLECTION) {
				this.getOrigin().collectPackage(this.getPackages().get(0));
				System.out.println("Van " + this.getTruckID() + " has collected package  " + this.getPackages().get(0).getPackageID() + " and arrived back to branch "
						+ this.getPackages().get(0).getSenderAddress().getZip());
				this.getPackages().remove(0);
				this.setAvailable(true);
			} else if (this.getPackages().get(0).getStatus() == Status.DISTRIBUTION) {

				System.out.println("Van " + this.getTruckID() + " has delivered package " + this.getPackages().get(0).getPackageID() + " to the destination");
				this.getPackages().get(0).addTracking(null, Status.DELIVERED);
				this.getPackages().remove(0);
				this.setAvailable(true);
			}
		}
	}

	@Override
	/**
	 * helps print Van truck in appropriate way.
	 */
	public String toString() {
		return "Van" + super.toString() + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof Van)) {
			return false;
		}
		return true;
	}
}
