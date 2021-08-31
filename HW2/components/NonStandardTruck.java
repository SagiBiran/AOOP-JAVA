package components;

import java.awt.*;
import java.util.Random;

/**
 * * A vehicle for transporting packages of non-standard size (exceptional
 * cargo). All vehicles of this type are in the sorting center.
 *
 * @author Sagi Biran , ID: 205620859
 */
public class NonStandardTruck extends Truck {
    private int width, length, height;
    private Package senderspot;

    /**
     * A default constructor that produces an object with a vehicle ID number and
     * model at random.
     */
    public NonStandardTruck() {
        super();
        Random r = new Random();
        width = (r.nextInt(3) + 2) * 100;
        length = (r.nextInt(6) + 10) * 100;
        height = (r.nextInt(2) + 3) * 100;
        System.out.println(this);
    }

    /**
     * Constructor that accepts arguments: license plate number, model of the
     * vehicle and maximum length / width / height of cargo that the vehicle can
     * carry.
     *
     * @param licensePlate indicate license plate number
     * @param truckModel   indicate model of vehicle
     * @param length       maximum length cargo that the vehicle can carry
     * @param width        width length cargo that the vehicle can carry
     * @param height       height length cargo that the vehicle can carry
     */
    public NonStandardTruck(String licensePlate, String truckModel, int length, int width, int height) {
        super(licensePlate, truckModel);
        this.width = width;
        this.length = length;
        this.height = height;
        System.out.println(this);
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
     * method will return package Sender address .
     *
     * @return senderAddress - Sender Location.
     */
    public Package getSenderspot() {
        return senderspot;
    }

    /**
     * Method will set package Sender address .
     *
     * @param senderspot Sender Location.
     */
    public void setSenderspot(Package senderspot) {
        this.senderspot = senderspot;
    }

    /**
     * Performs a work unit (in each beat) according to the following requirements:
     * An available vehicle does nothing. A vehicle that is in drive already, will
     * reduce is time for arriving by one then if after reduction the value in time
     * is equal to zero, so the trip ended and a vehicle reached its destination. If
     * the purpose of the trip was to collect a package from a customer Sender
     * (COLLECTION) The vehicle will be transferred for delivery, the status of the
     * package will be updated, a suitable registration will be added to the
     * tracking list of the package, and a message will be printed that the vehicle
     * has collected the package. If the purpose of the trip was to deliver the
     * package to the customer (DISTRIBUTION) the package will be removed from the
     * list The packages in the vehicle, the status of the package and the transfer
     * history will be updated accordingly and a message will be printed stating
     * that the package Delivered to the customer, after that the vehicle switches
     * to "free" mode.
     */
    @Override
    public void work() {
        if (!this.isAvailable()) {
            Package pack = this.getPackages().get(0);
            this.setTimeLeft(this.getTimeLeft() - 1);
            if (this.getTimeLeft() == 0) {
                if (pack.getStatus() == Status.COLLECTION) {
                    System.out.println(getName() + " has collected " + pack.getName());
                    deliverPackage(pack);
                } else {
                    System.out.println(getName() + " has delivered " + pack.getName() + " to the destination");
                    this.getPackages().remove(pack);
                    pack.addRecordForTrack(Status.DELIVERED, null);
                    setAvailable(true);
                }
            }
        }
    }

    /**
     * decided to no implement this method according to Michael Finkelstien
     * Implementation is OPTIONAL.
     */
    @Override
    public void deliverPackage(Package pack) {
        int time = Math.abs(pack.getDestinationAddress().getStreet() - pack.getSenderAddress().getStreet()) % 10 + 1;
        this.setTimeLeft(time * 10);
        this.setTime(time * 10);
        pack.addRecordForTrack(Status.DISTRIBUTION, this);
        System.out.println(getName() + " is delivering " + pack.getName() + ", time left: " + this.getTimeLeft());
    }

    /**
     * helps print Non Standard truck in appropriate way.
     */
    @Override
    public String toString() {
        return "NonStandardTruck [" + super.toString() + ", length=" + length + ", width=" + width + ", height="
                + height + "]";
    }

    /**
     * Method will invoke Start() function for hub operation i.e run non standard trucks thread
     */
    @Override
    public void run() {
        work();
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof NonStandardTruck)) {
            return false;
        }
        NonStandardTruck other = (NonStandardTruck) obj;
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
     * Method will draw non standard trucks as requested.
     *
     * @param g       graphic object
     * @param balance will help to relocate components correction.
     */
    public void drawNonStandardTruck(Graphics g, int balance) {
        double x1Coordinate = 0, x2Coordinate = 0, y1Coordinate = 0, y2Coordinate = 0;
        if (this.getPackages().get(0).getStatus() == Status.COLLECTION) {
            x1Coordinate = 1145;
            y1Coordinate = 350;
            x2Coordinate = this.getSenderspot().getSenderAddressX() + balance;
            y2Coordinate = this.getSenderspot().getSenderAddressY() + balance;
            g.setColor(new Color(245, 175, 175));
        }
        if (this.getPackages().get(0).getStatus() == Status.DISTRIBUTION) {
            x1Coordinate = this.getSenderspot().getSenderAddressX() + balance;
            y1Coordinate = this.getSenderspot().getSenderAddressY() + balance;
            x2Coordinate = this.getPackages().get(0).getDestinationAddressX() + balance;
            y2Coordinate = this.getPackages().get(0).getDestinationAddressY() + balance;
            g.setColor(new Color(245, 5, 5));
        }
        float nTemp = ((float) getTime() - (float) getTimeLeft()) / (float) getTime();
        float vTemp = (float) getTimeLeft() / (float) getTime();
        int x3Coordinate = (int) (((nTemp * x2Coordinate) + (vTemp * x1Coordinate)) / (vTemp + nTemp));
        int y3Coordinate = (int) (((nTemp * y2Coordinate) + (vTemp * y1Coordinate)) / (vTemp + nTemp));
        g.fillRect(x3Coordinate - (balance / 2), y3Coordinate - (balance / 2), 16, 16);
        g.setColor(Color.BLACK);
        g.fillOval(x3Coordinate + 2, y3Coordinate + 2, 10, 10);
        g.fillOval(x3Coordinate - 11, y3Coordinate - 11, 10, 10);
        g.fillOval(x3Coordinate + 2, y3Coordinate - 11, 10, 10);
        g.fillOval(x3Coordinate - 11, y3Coordinate + 2, 10, 10);
    }

}

