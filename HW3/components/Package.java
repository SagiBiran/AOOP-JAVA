package components;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

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
    private ArrayList<Tracking> tracking = new ArrayList<>();
    private Branch branch = null;
    /**
     * will represent X && Y coordinate due sender address
     */
    private Point sendPoint;
    /**
     * will represent  X && Y  coordinate due destination address
     */
    private Point destPoint;
    /**
     * will represent  X && Y  coordinate due branch sender address
     */
    private Point bInPoint;
    /**
     * will represent  X && Y  coordinate due branch destination address
     */
    private Point bOutPoint;
    /**
     * PropertyChangeSupport object to update listens(mainOffice)
     * and fire messages due situations
     */
    private PropertyChangeSupport support;

    /**
     * A constructor who accepts as arguments priority, addresses of sender and
     * receives for this package.
     *
     * @param priority           represent the priority of the package
     * @param senderAddress      represent sender address (due packages information)
     * @param destinationAddress represent destination address (due packages information)
     */
    public Package(Priority priority, Address senderAddress, Address destinationAddress) {
        packageID = countID++;
        this.priority = priority;
        this.status = Status.CREATION;
        this.senderAddress = senderAddress;
        this.destinationAddress = destinationAddress;
        support = new PropertyChangeSupport(this);
        this.addPropertyChangeListener(MainOffice.getInstance());
        tracking.add(new Tracking(MainOffice.getClock(), null, status));
        support.firePropertyChange("CREATION", null, this);
    }

    /**
     * @param pcl inform listener (MainOffice) due changes with status of each package due entire system.
     */
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    /**
     * @return a collection of objects from all local branches.
     */
    public Branch getBranch() {
        return this.branch;
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

    /**
     * Get address for sender
     *
     * @return address for sender package.
     */
    public Address getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(Address senderAddress) {
        this.senderAddress = senderAddress;
    }

    /**
     * Get address for destination
     *
     * @return address for destination package.
     */
    public Address getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(Address destinationAddress) {
        this.destinationAddress = destinationAddress;
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
     *
     * @param t tracking object
     */
    public void addTracking(Tracking t) {
        tracking.add(t);
    }

    /**
     * method will return  package tracking Array.
     *
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
     * method helps to print package in appropriate way
     */
    @Override
    public String toString() {
        return "packageID=" + packageID + ", priority=" + priority + ", status=" + status + ", startTime="
                + ", senderAddress=" + senderAddress + ", destinationAddress=" + destinationAddress;
    }

    public Point getSendPoint() {
        return sendPoint;
    }

    public Point getDestPoint() {
        return destPoint;
    }

    public Point getBInPoint() {
        return bInPoint;
    }

    public Point getBOutPoint() {
        return bOutPoint;
    }

    public void paintComponent(Graphics g, int x, int offset) {
        if (status == Status.CREATION || (branch == null && status == Status.COLLECTION))
            g.setColor(new Color(204, 0, 0));
        else
            g.setColor(new Color(255, 180, 180));
        g.fillOval(x, 20, 30, 30);

        if (status == Status.DELIVERED)
            g.setColor(new Color(204, 0, 0));
        else
            g.setColor(new Color(255, 180, 180));
        g.fillOval(x, 583, 30, 30);


        if (branch != null) {
            g.setColor(Color.BLUE);
            g.drawLine(x + 15, 50, 40, 100 + offset * this.senderAddress.getZip());
            sendPoint = new Point(x + 15, 50);
            bInPoint = new Point(40, 100 + offset * this.senderAddress.getZip());
            g.drawLine(x + 15, 583, 40, 130 + offset * this.destinationAddress.getZip());
            destPoint = new Point(x + 15, 583);
            bOutPoint = new Point(40, 130 + offset * this.destinationAddress.getZip());

        } else {
            g.setColor(Color.RED);
            g.drawLine(x + 15, 50, x + 15, 583);
            g.drawLine(x + 15, 50, 1140, 216);
            sendPoint = new Point(x + 15, 50);
            destPoint = new Point(x + 15, 583);

        }
    }
}
