package components;

import java.awt.*;
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
    private static int countID = 1000;
    /**
     * Package ID number. Package numbering starts from.
     */
    final private int packageID;
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
    private ArrayList<Tracking> tracking = new ArrayList<Tracking>();

    /**
     * will represent X coordinate due sender address
     */
    private double senderXcoordinate;
    /**
     * will represent Y coordinate due sender address
     */
    private double senderYcoordinate;
    /**
     * will represent Y coordinate due destination address
     */
    private double destinationXcoordinate;
    /**
     * will represent Y coordinate due destination address
     */
    private double destinationYcoordinate;


    /**
     * A constructor who accepts as arguments priority, addresses of sender and
     * receives for this package.
     *
     * @param priority          represent the priority of the package
     * @param senderAddress
     * @param destinationAdress
     */

    public Package(Priority priority, Address senderAddress, Address destinationAdress) {
        packageID = countID++;
        this.senderXcoordinate = 150 + ((this.packageID - 1000) * 70);
        this.senderYcoordinate = 30;
        this.destinationXcoordinate = 150 + ((this.packageID - 1000) * 70);
        this.destinationYcoordinate = 560;
        this.priority = priority;
        this.status = Status.CREATION;
        this.senderAddress = senderAddress;
        this.destinationAddress = destinationAdress;
        tracking.add(new Tracking(MainOffice.getClock(), null, status));
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


    public int getPackageID() {
        return packageID;
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

    /**
     *
     * @return name of package type
     */
    public String getName() {
        return "package " + getPackageID();
    }


    public void setDestinationAddress(Address destinationAdress) {
        this.destinationAddress = destinationAdress;
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
    public void addTracking(Node node, Status status) {
        tracking.add(new Tracking(MainOffice.getClock(), node, status));
    }

    /**
     * same purpose as above method but get tracking object instead of Node & Status
     * @param t
     */
    public void addTracking(Tracking t) {
        tracking.add(t);
    }

    /**
     * method will return  package tracking Array.
     * @return package tracking array.
     */
    public ArrayList<Tracking> getTracking() {
        return tracking;
    }

    /**
     * Method will print tracking as expected
     */
    public void printTracking() {
        for (Tracking t : tracking)
            System.out.println(t);
    }

    /**
     *
     * @return sender Branch
     */
    public Branch getSenderBranch() {
        return MainOffice.getHub().getBranches().get(getSenderAddress().getZip());
    }

    /**
     *
     * @return destination Branch
     */
    public Branch getDestBranch() {
        return MainOffice.getHub().getBranches().get(getDestinationAddress().getZip());
    }


    /**
     * Get coordinate x of sender location.
     *
     * @return coordinate x.
     */
    public double getSenderAddressX() {
        return senderXcoordinate;
    }

    /**
     * Get coordinate y of sender location.
     *
     * @return coordinate y.
     */
    public double getSenderAddressY() {
        return senderYcoordinate;
    }

    /**
     * Get coordinate x of Destination location.
     *
     * @return coordinate x.
     */
    public double getDestinationAddressX() {
        return destinationXcoordinate;
    }

    /**
     * Get coordinate y of Destination location.
     *
     * @return coordinate y.
     */
    public double getDestinationAddressY() {
        return destinationYcoordinate;
    }

    /**
     * method helps to print package in appropriate way
     */
    @Override
    public String toString() {
        return "packageID=" + packageID + ", priority=" + priority + ", status=" + status + ", senderAddress=" + senderAddress + ", destinationAddress=" + destinationAddress;
    }

    /**
     * Method will help to  add package with tracking function.
     * @param node   package address.
     * @param status package status.
     */
    public void addRecordForTrack(Status status, Node node) {
        setStatus(status);
        addTracking(node, status);
    }

    /**
     * method will help draw Packages from sender
     * @param g graphic object
     */
    public void drawPackageSender(Graphics g) {
        if (this.status == Status.CREATION) {
            g.setColor(new Color(205, 0, 0));
        } else {
            if (this instanceof NonStandardPackage) {
                if (this.status == Status.COLLECTION) {
                    g.setColor(new Color(205, 0, 0));
                } else {
                    g.setColor(new Color(255, 175, 175));
                }
            } else {
                g.setColor(new Color(255, 175, 175));
            }
        }
        g.fillOval((int) this.senderXcoordinate, (int) this.senderYcoordinate, 30, 30);
    }

    /**
     * funcitgon will help to draw Packages from Destination
     * @param g graphic object
     */
    public void drawPackageDestination(Graphics g) {
        if (this.status == Status.DELIVERED) {
            g.setColor(new Color(205, 0, 0));
        } else {
            g.setColor(new Color(255, 175, 175));
        }
        g.fillOval((int) this.destinationXcoordinate, (int) this.destinationYcoordinate, 30, 30);
    }

    /**
     * function will help to draw lines between packages that located in Sender
     * @param g graphic object
     * @param spacious to make sure we use appropriate space between every draw
     */
    public void drawSenderLines(Graphics g, int spacious) {
        g.setColor(new Color(0, 0, 155));
        g.drawLine((int) this.getSenderAddressX() + spacious, (int) this.getSenderAddressY() + spacious, (int) this.getSenderBranch().getX(), (int) this.getSenderBranch().getY());
    }

    /**
     * function will help to draw lines between packages that located in destination Address
     * @param g graphic object
     * @param spacious to make sure we use appropriate space between every draw
     */
    public void drawDestinationLines(Graphics g, int spacious) {
        g.setColor(new Color(0, 0, 155));
        g.drawLine((int) this.getDestinationAddressX() + spacious, (int) this.getDestinationAddressY() + spacious, (int) this.getDestBranch().getX(), (int) this.getDestBranch().getY());
    }

}
