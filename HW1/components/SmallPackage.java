package components;

/**
 * Represents small packages.
 * @author Sagi Biran , ID: 205620859
 */
public class SmallPackage extends Package {
	private boolean acknowledge;

	/**
	 * A Constructor who receives as arguments priority and addresses sends and
	 * receives of the package, and if delivery confirmation is required.
	 * @param priority
	 * @param senderAddress
	 * @param destinationAdress
	 * @param acknowledge
	 */
	public SmallPackage(Priority priority, Address senderAddress, Address destinationAdress, boolean acknowledge) {
		super(priority, senderAddress, destinationAdress);
		/**
		 * /** This field receives a true value if the package requires delivery
		 * confirmation after delivery to the recipient.
		 */
		this.acknowledge = acknowledge;

	}
	/**
	 * @return whether a small package included delivery confirmation or not.
	 */
	public boolean isAcknowledge() {
		return acknowledge;
	}

	public void setAcknowledge(boolean acknowledge) {
		this.acknowledge = acknowledge;
	}

	/**
	 * prints all tracks that have been made during package's trip to the costumer.
	 */
	@Override
	void printTracking() {
		System.out.println("TRACKING " + toString());
		for (int i = 0; i < tracking.size(); i++)
			System.out.println(tracking.get(i));
	}
	/**
	 * helps prints small package in appropriate way.
	 */
	@Override
	public String toString() {
		return "SmallPackage" + super.toString() + " ,acknowledge= " + isAcknowledge() + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof SmallPackage)) {
			return false;
		}
		SmallPackage other = (SmallPackage) obj;
		if (isAcknowledge() != other.isAcknowledge()) {
			return false;
		}
		return true;
	}

}
