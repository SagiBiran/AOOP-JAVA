package components;

/**
 * Address (of the sender or of the recipient). The address consists of two
 * integers, separated by a line. The first number (zip) determines the branch
 * to which the address belongs, the second number (street) determines the
 * street number.
 * 
 * @author Sagi Biran , ID: 205620859
 */
public class Address {
	private int zip;
	private int street;

	public Address(int zip, int street) {
		this.zip = zip;
		this.street = street;
	}

	/********************************************************
	 * getters&setters
	 ********************************************************/

	public int getZip() {
		return zip;
	}

	public void setZip(int zip) {
		this.zip = zip;
	}

	public int getStreet() {
		return street;
	}

	public void setStreet(int street) {
		this.street = street;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Address)) {
			return false;
		}
		Address other = (Address) obj;
		if (getStreet() != other.getStreet()) {
			return false;
		}
		if (getZip() != other.getZip()) {
			return false;
		}
		return true;
	}

	/**
	 * helps prints address in appropriate way.
	 */
	@Override
	public String toString() {
		return getZip() + "-" + getStreet();
	}

	/**
	 * helper function to check whether a integer contain 6 digits
	 * @param stNumber street number of package.
	 * @return whether street's packages is valid or not.
	 */

	public boolean hasSixDigits(int stNumber) { // 111111
		int counter = 0;
		while (stNumber > 0) {
			stNumber = stNumber / 10;
			counter++;
		}
		if (counter != 6)
			return false;
		return true;
	}
	/********************************************************************************************************************************/

}
