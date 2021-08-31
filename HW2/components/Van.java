package components;

import java.awt.*;

/**
 * A vehicle that collects a package from the sender's address to the local
 * branch and delivers the package from the destination branch to the
 * recipient's address (one package Only per trip). The class has no fields.
 *
 * @author Sagi Biran , ID: 205620859
 */

public class Van extends Truck {
    /**
     * A default constructor that produces a random object according to the same
     * rules as in the parent class(TRUCK).
     */
    public Van() {
        super();
        System.out.println(this);
    }

    /**
     * Produces an object with given parameters.
     *
     * @param licensePlate indicate license plate number
     * @param truckModel   indicate model of vehicle
     */
    public Van(String licensePlate, String truckModel) {
        super(licensePlate, truckModel);
        System.out.println(this);
    }

    /**
     * helps print Van truck in appropriate way.
     */
    @Override
    public String toString() {
        return "Van [" + super.toString() + "]";
    }

    /**
     * method for deliver package from van to branch and update package status + add
     * tracking.
     */
    @Override
    public void deliverPackage(Package pack) {
        addPackage(pack);
        setAvailable(false);
        int time = pack.getDestinationAddress().getStreet() % 10 + 1;
        this.setTimeLeft(time * 10);
        this.setTime(time * 10);
        pack.addRecordForTrack(Status.DISTRIBUTION, this);
        System.out.println(getName() + " is delivering " + pack.getName() + ", time left: " + getTimeLeft());
    }

    /**
     * Performs a work unit (in each beat) according to the following requirements:
     * An available vehicle does nothing. A vehicle that is in the process of being
     * reduced reduces the time left to the end of its journey if after the
     * reduction in value The time is equal to zero, so the trip ended and a vehicle
     * reached its destination. A vehicle found during a trip reduces the time left
     * to end the trip by 1. If after the reduction the time value is equal To zero,
     * then the trip ended and a vehicle performed the mission for which it was
     * sent. If the purpose of the trip was to collect a package from a sending
     * customer (COLLECTION), the package will at this point move from the vehicle
     * to the branch, the status of the package will be updated, a suitable
     * registration will be added to the package tracking list, and a message will
     * be printed. In addition, the vehicle will change its condition to free. If
     * the purpose of the trip was to deliver the package to the customer
     * (DISTRIBUTION) the package will be removed from the list of packages In the
     * vehicle, the status of the package and the transfer history will be updated
     * accordingly and a notice will be printed that the package has been delivered
     * to the customer. In the case of a small package with the option to send a
     * delivery confirmation, a notification of sending a delivery confirmation will
     * be printed.
     */
    @Override
    public void work() {
        if (!isAvailable()) {
            setTimeLeft(getTimeLeft() - 1);
            if (this.getTimeLeft() == 0) {
                for (Package pack : this.getPackages()) {
                    if (pack.getStatus() == Status.COLLECTION) {
                        pack.addRecordForTrack(Status.BRANCH_STORAGE, pack.getSenderBranch());
                        System.out.println(getName() + " has collected " + pack.getName() + " and arrived back to " + pack.getSenderBranch().getName());
                    } else {
                        pack.addRecordForTrack(Status.DELIVERED, null);
                        pack.getDestBranch().removePackage(pack);
                        System.out.println(getName() + " has delivered " + pack.getName() + " to the destination");
                        if (pack instanceof SmallPackage && ((SmallPackage) pack).isAcknowledge()) {
                            System.out.println("Acknowledge sent for " + pack.getName());
                        }
                    }
                }
                this.getPackages().removeAll(getPackages());
                this.setAvailable(true);
            }
        }
    }

    /**
     * Method will draw line from branch to distention that located in van.
     */
    public void drawVan(Graphics g, int spacious) {
        double x1Coordinate = 0, x2Coordinate = 0, y1Coordinate = 0, y2Coordinate = 0;
        if (this.getPackages().get(0).getStatus() == Status.COLLECTION) {
            x1Coordinate = this.getPackages().get(0).getSenderAddressX() + spacious;
            y1Coordinate = this.getPackages().get(0).getSenderAddressY() + spacious;
            x2Coordinate = this.getPackages().get(0).getSenderBranch().getX();
            y2Coordinate = this.getPackages().get(0).getSenderBranch().getY();
        }
        if (this.getPackages().get(0).getStatus() == Status.DISTRIBUTION) {
            x1Coordinate = this.getPackages().get(0).getSenderBranch().getX();
            y1Coordinate = this.getPackages().get(0).getSenderBranch().getY();
            x2Coordinate = this.getPackages().get(0).getDestinationAddressX() + spacious;
            y2Coordinate = this.getPackages().get(0).getDestinationAddressY() + spacious;
        }
        float nTemp = ((float) getTime() - (float) getTimeLeft()) / (float) getTime();
        float vTemp = (float) getTimeLeft() / (float) getTime();
        int x3Coordinate = (int) (((nTemp * x2Coordinate) + (vTemp * x1Coordinate)) / (vTemp + nTemp));
        int y3Coordinate = (int) (((nTemp * y2Coordinate) + (vTemp * y1Coordinate)) / (vTemp + nTemp));
        g.setColor(new Color(3, 3, 149));
        g.fillRect(x3Coordinate - (spacious / 2), y3Coordinate - (spacious / 2), 16, 16);
        g.setColor(Color.BLACK);
        g.fillOval(x3Coordinate + 2, y3Coordinate + 2, 10, 10);
        g.fillOval(x3Coordinate - 11, y3Coordinate - 11, 10, 10);
        g.fillOval(x3Coordinate + 2, y3Coordinate - 11, 10, 10);
        g.fillOval(x3Coordinate - 11, y3Coordinate + 2, 10, 10);
    }

}
