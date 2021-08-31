package components;

/**
 * The class represents a record in the package transfer history. Each package
 * contains a collection of type records, with each change of status (and
 * location) of a package a new record is added to the collection. Each record
 * includes the time of creation of the record (by The clock of the program),
 * the point where the package is located and the status of the package.
 * 
 * @author Sagi Biran , ID: 205620859
 */

public class Tracking {
	/**
	 * Integer, value of the system clock as soon as the record is created.
	 */
	private int time;
	/**
	 * Package location: (customer / branch / sorting center / transport vehicle).
	 * When the package is with the customer (sender or recipient), this field value
	 * is equal to null.
	 */
	private Node node;
	/**
	 * Package status as soon as the record is created.
	 */
	private Status status;

	/**
	 * Default constructor who takes as arguments time for creation the package,
	 * node-location(Costumer) and status for Creation ("Creation").
	 * 
	 * @param time
	 * @param node
	 * @param status
	 */
	public Tracking(int time, Node node, Status status) {
		this.time = time;
		this.status = status;
		this.node = node;
	}

	/********************************************************
	 * getters&setters
	 ********************************************************/

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	/********************************************************************************************************************************/
	/**
	 * method helps to print each track in appropriate way while compare node
	 * argument(location) value.
	 */
	@Override
	public String toString() {

		// means this is not Customer // TO Understand how to fix casting properly
		if (getNode() instanceof Branch) {
			setNode((Branch) getNode());
			int temp = ((Branch) getNode()).getBranchId();
			if (temp == -1)
				return getTime() + ":" + " Hub" + ", status=" + getStatus();
			return getTime() + ":" + " Branch " + temp + ", status=" + getStatus();
		}

		if (getNode() instanceof NonStandardTruck) {
			setNode((NonStandardTruck) getNode());
			int temp = ((NonStandardTruck) getNode()).getTruckID();
			return getTime() + ":" + " NonStandardTruck " + temp + ", status=" + getStatus();
		}
		if (getNode() instanceof StandardTruck) {
			setNode((StandardTruck) getNode());
			int temp = ((StandardTruck) getNode()).getTruckID();
			return getTime() + ":" + " StandardTruck " + temp + ", status=" + getStatus();
		}
		if (getNode() instanceof Van) {
			setNode((Van) getNode());
			int temp = ((Van) getNode()).getTruckID();
			return getTime() + ":" + " Van " + temp + ", status=" + getStatus();
		}
		if (getNode() != null)
			return getNode().getClass().getName() + ", status=" + getStatus();
		return getTime() + ":" + " Customer" + ", status=" + getStatus();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Tracking)) {
			return false;
		}
		Tracking other = (Tracking) obj;
		if (getNode() == null) {
			if (other.getNode() != null) {
				return false;
			}
		} else if (!getNode().equals(other.getNode())) {
			return false;
		}
		if (getStatus() != other.getStatus()) {
			return false;
		}
		if (getTime() != other.getTime()) {
			return false;
		}
		return true;
	}

}
