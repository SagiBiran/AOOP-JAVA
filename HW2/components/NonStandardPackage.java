package components;

import java.awt.*;

/**
 * Represents the non-standard size packages.
 * @author Sagi Biran , ID: 205620859
 */
public class NonStandardPackage extends Package {
    private int width, length, height;
    /**
     * Constructor that accepts as arguments priority, sender and recipient
     * addresses, and package dimensions.
     * @param priority priority of package when created.
     * @param senderAddress sender address of package.
     * @param destinationAdress destination for this package (zip branch + streent number).
     * @param width width of non standard package.
     * @param length length of non standard package.
     * @param height height of non standard package.
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof NonStandardPackage)) {
            return false;
        }
        NonStandardPackage other = (NonStandardPackage) obj;
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

    /**
     * Method will draw lines between sender(Address) to hub.
     *
     * @param g graphic object
     * @param hub    branch object (branch).
     * @param spacious will help to make position correction when needed.
     */
    public void senderLineDrawing(Graphics g, Branch hub, int spacious) {

        g.setColor(new Color(245, 20, 20));
        g.drawLine((int) this.getSenderAddressX() + spacious, (int) this.getSenderAddressY() + spacious, (int) hub.getX(), (int) hub.getY());

    }

    /**
     * Method will draw lines from hub to destination(Address).
     * @param g graphic object
     * @param spacious ill help to make position correction when needed.
     */
    public void drawDestinationLines(Graphics g, int spacious) {

        g.setColor(new Color(245, 20, 20));
        g.drawLine((int) this.getDestinationAddressX() + spacious, (int) this.getDestinationAddressY() + spacious, (int) this.getSenderAddressX() + spacious, (int) this.getSenderAddressY() + spacious);

    }

}
