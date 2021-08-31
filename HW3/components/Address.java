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
    public final int zip;
    public final int street;

    public Address(int zip, int street) {
        this.zip = zip;
        this.street = street;
    }

    /*********************getters&setters***********************************/
    public int getZip() {
        return zip;
    }

    /**
     * @return address object printed in appropriate way.
     */
    @Override
    public String toString() {
        return zip + "-" + street;
    }
}
