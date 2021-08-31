package components;

/**
 * Class represents packages with varying weights over one kilogram.
 * 
 * @author Sagi Biran , ID: 205620859
 */
public class StandardPackage extends Package {
	/**
	 * weight of standard package when created in the system.
	 */
	private double weight;

	/**
	 * Constructor that receives as arguments: priority, sender and recipient
	 * addresses of package and package weight.
	 * @param priority          priority of this package when created.
	 * @param senderAddress     where should this package be collected from
	 *                          constumer.
	 * @param destinationAdress where should this package go for distribution.
	 * @param weight            indicate weight of this package.
	 */
	public StandardPackage(Priority priority, Address senderAddress, Address destinationAdress, double weight) {
		super(priority, senderAddress, destinationAdress);
		this.weight = weight;

	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	/**
	 * prints all tracks that have been made during package's trip to the costumer.
	 */
	@Override
	void printTracking() {
		System.out.println("TRACKING " + toString());
		for (int i = 0; i < tracking.size(); i++) {
			System.out.println(tracking.get(i));
		}
	}

	@Override
	/**
	 * helps print standard package in appropriate way.
	 */
	public String toString() {
		return "StandardPackage" + super.toString() + " ,weight=" + weight + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof StandardPackage)) {
			return false;
		}
		return true;
	}

}
