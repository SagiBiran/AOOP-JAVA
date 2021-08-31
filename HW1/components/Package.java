package components;

import java.util.ArrayList;

/**
 * A general type represents packages.
 * 
 * @author Sagi Biran , ID: 205620859
 */
public abstract class Package {
	/**
	 * static variable to make sure different id is set for each object that created
	 * and inherit from this class.
	 */
	private static int globalID = 1000;
	/**
	 * Package ID number. Package numbering starts from.
	 */
	private final int packageID;
	/**
	 * describes package's priority (enum type).
	 */
	private Priority priority;
	/**
	 * current status
	 */
	private Status status;
	/**
	 * sender's address
	 */
	private Address senderAddress;
	/**
	 * Recipient's address
	 */
	private Address destinationAddress;
	/**
	 * A collection of records with a transfer history (objects of the Tracking
	 * Class).
	 */
	ArrayList<Tracking> tracking = new ArrayList<Tracking>();

	/**
	 * A constructor who accepts as arguments priority, addresses of sender and
	 * receives for this package.
	 * 
	 * @param priority
	 * @param senderAddress
	 * @param destinationAdress
	 */

	public Package(Priority priority, Address senderAddress, Address destinationAdress) {
		packageID = globalID;
		++globalID;
		this.priority = priority;
		this.senderAddress = senderAddress;
		this.destinationAddress = destinationAdress;
		status = Status.CREATION;
		this.getTracking().add(new Tracking(MainOffice.getClock() + 1, null, status));
		status = Status.COLLECTION;
	}

	/**
	 * Receives an object of type (Node/Vehicle/branch) and an object-Status creates
	 * and adds an object of the Tracking class to the tracking collection in the
	 * class.
	 * 
	 * @param node   interface type that define behavior for collect/deliver package
	 *               for classes that implement this.
	 * @param status status of package at this moment.
	 */
	void addTracking(Node node, Status status) {
		getTracking().add(new Tracking(MainOffice.getClock(), node, status));
	}

	/**
	 * abstract method that will override in sub classes.
	 */
	abstract void printTracking();

	/********************************************************
	 * getters&setters
	 ********************************************************/
	public int getPackageID() {
		return packageID;
	}

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Address getSenderAddress() {
		return senderAddress;
	}

	public void setSenderAddress(Address senderAddress) {
		this.senderAddress = senderAddress;
	}

	public Address getDestinationAddress() {
		return destinationAddress;
	}

	public void setDestinationAddress(Address destinationAddress) {
		this.destinationAddress = destinationAddress;
	}

	public static int getGlobalID() {
		return globalID;
	}

	public static void setGlobalID(int globalID) {
		Package.globalID = globalID;
	}

	public ArrayList<Tracking> getTracking() {
		return tracking;
	}

	public void setTracking(ArrayList<Tracking> tracking) {
		this.tracking = tracking;
	}

	/******************************************************************************************************************************/
	/**
	 * method helps to print package in appropriate way
	 */
	@Override
	public String toString() {
		return " [packageID=" + getPackageID() + ", priority=" + getPriority() + ", status=" + getStatus() + ", startTime=" + ", senderAddress=" + getSenderAddress() + ", destinationAddress="
				+ getDestinationAddress();
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Package)) {
			return false;
		}
		Package other = (Package) obj;
		if (getDestinationAddress() == null) {
			if (other.getDestinationAddress() != null) {
				return false;
			}
		} else if (!getDestinationAddress().equals(other.getDestinationAddress())) {
			return false;
		}
		if (getPackageID() != other.getPackageID()) {
			return false;
		}
		if (getPriority() != other.getPriority()) {
			return false;
		}
		if (getSenderAddress() == null) {
			if (other.getSenderAddress() != null) {
				return false;
			}
		} else if (!getSenderAddress().equals(other.getSenderAddress())) {
			return false;
		}
		if (getStatus() != other.getStatus()) {
			return false;
		}
		if (getTracking() == null) {
			if (other.getTracking() != null) {
				return false;
			}
		} else if (!getTracking().equals(other.getTracking())) {
			return false;
		}
		return true;
	}

}
