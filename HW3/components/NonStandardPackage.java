package components;

/**
 * Represents the non-standard size packages.
 *
 * @author Sagi Biran , ID: 205620859
 */
public class NonStandardPackage extends Package {
    private int width, length, height;

    /**
     * Constructor that accepts as arguments priority, sender and recipient
     * addresses, and package dimensions.
     *
     * @param priority          priority of package when created.
     * @param senderAddress     sender address of package.
     * @param destinationAdress destination for this package (zip branch + streent number).
     * @param width             width of non standard package.
     * @param length            length of non standard package.
     * @param height            height of non standard package.
     */
    public NonStandardPackage(Priority priority, Address senderAddress, Address destinationAdress, int width, int length, int height) {
        super(priority, senderAddress, destinationAdress);
        this.width = width;
        this.length = length;
        this.height = height;
        System.out.println("Creating " + this);
    }

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

    /**
     * Method helps print non standard package in aprropriate way.
     */
    @Override
    public String toString() {
        return "NonStandardPackage [" + super.toString() + ", width=" + width + ", length=" + length + ", height=" + height + "]";
    }
}
