package components;

import java.awt.*;
import java.util.Random;

/**
 * A vehicle for transferring packages from the sorting center to the branches
 * and back. All vehicles of this type are in the sorting center.
 *
 * @author Sagi Biran , ID: 205620859
 */


public class StandardTruck extends Truck {
    /**
     * Maximum weight that a vehicle can carry.
     */
    private int maxWeight;
    /**
     * Target branch / sorting center.
     */
    private Branch destination;
    /**
     * will indicate which branch is now operate by current standard truck.
     */
    private Branch currentBranch;

    /**
     * A default constructor that produces an object with a license plate number and
     * a vehicle model at random.
     */

    public StandardTruck() {
        super();
        maxWeight = ((new Random()).nextInt(2) + 2) * 100;
        System.out.println(this);

    }

    /**
     * Builder that accepts as arguments: license plate number, vehicle model and
     * maximum weight.
     *
     * @param licensePlate of the standard truck
     * @param truckModel   of the standard truck
     * @param maxWeight    that the standard truck can handle
     */
    public StandardTruck(String licensePlate, String truckModel, int maxWeight) {
        super(licensePlate, truckModel);
        this.maxWeight = maxWeight;
        System.out.println(this);

    }


    public Branch getDestination() {
        return destination;
    }


    public void setDestination(Branch destination) {
        this.destination = destination;
    }


    public int getMaxWeight() {
        return maxWeight;
    }


    public void setMaxWeight(int maxWeight) {
        this.maxWeight = maxWeight;
    }

    /**
     * set StandardTrucks current Branch.
     *
     * @param currentBranch StandardTrucks current branch.
     */
    public void setCurrent(Branch currentBranch) {
        this.currentBranch = currentBranch;
    }

    /**
     * helps print standard truck in appropriate way.
     */
    @Override
    public String toString() {
        return "StandartTruck [" + super.toString() + ",maxWeight=" + maxWeight + "]";
    }

    /**
     * Method purpose is to unload cargo in Hub/Branch Sender
     */
    public void unload() {
        for (Package pack : getPackages()) {
            deliverPackage(pack);
        }
        getPackages().removeAll(getPackages());
        System.out.println(getName() + " unloaded packages at " + destination.getName());
    }

    /**
     * method will help to deliver packs from trucks into branch.
     */
    @Override
    public void deliverPackage(Package pack) {
        if (destination == MainOffice.getHub())
            pack.addRecordForTrack(Status.HUB_STORAGE, destination);
        else
            pack.addRecordForTrack(Status.DELIVERY, destination);
        destination.addPackage(pack);
    }

    /**
     * Method will collect and transport package from branch to hub.
     *
     * @param sender sender branch.
     * @param dest   destination branch.
     * @param status status value.
     */
    public void load(Branch sender, Branch dest, Status status) {
        double totalWeight = 0;
        for (int i = 0; i < sender.getPackages().size(); i++) {
            Package p = sender.getPackages().get(i);
            if (p.getStatus() == Status.BRANCH_STORAGE || (p.getStatus() == Status.HUB_STORAGE && p.getDestBranch() == dest)) {
                if (p instanceof SmallPackage && totalWeight + 1 <= maxWeight || totalWeight + ((StandardPackage) p).getWeight() <= maxWeight) {
                    getPackages().add(p);
                    sender.removePackage(p);
                    i--;
                    p.addRecordForTrack(status, this);
                }
            }
        }
        System.out.println(this.getName() + " loaded packages at " + sender.getName());
    }

    /**
     * according to the following requirements: An available vehicle does nothing. A
     * vehicle that is in drive already, will reduce his time for arriving by one
     * then if after reduction the value in time is equal to zero, so the trip ended
     * and a vehicle reached its destination and the vehicle must transfer all the
     * packages inside it to the point it has reached (local branch or sorting
     * center), while updating the status and history of packages. If the trip ended
     * at the sorting center, then the vehicle must switch to "free" mode.
     * Otherwise, the trip ended at the local branch, the vehicle has to load the
     * packages from this branch and take them to the sorting center. Also in this
     * case the status and history of packages is updated, new time is set at random
     * for the vehicle. A message will be printed that the vehicle has left back
     * from a local branch to a sorting center.
     */
    public void work() {
        if (!isAvailable()) {
            setTimeLeft(getTimeLeft() - 1);
            if (getTimeLeft() == 0) {
                currentBranch = destination;
                System.out.println("StandardTruck " + getTruckID() + " arrived to " + destination.getName());
                unload();
                if (destination == MainOffice.getHub()) {
                    setAvailable(true);
                    destination = null;
                } else {
                    load(destination, MainOffice.getHub(), Status.HUB_TRANSPORT);
                    setTimeLeft((new Random()).nextInt(6) + 1);
                    destination = MainOffice.getHub();
                    System.out.println(this.getName() + " is on it's way to the HUB, time to arrive: " + getTimeLeft());
                }
            }
        }
    }

    /**
     * method will draw standard trucks
     *
     * @param g
     * @param spacious will help to make location fix location
     */
    public void drawStandardTruck(Graphics g, int spacious) {
        double x1Coordinate = 0, x2Coordinate = 0, y1Coordinate = 0, y2Coordinate = 0;
        if (this.getDestination().getBranchId() != -1) {
            x1Coordinate = destination.getHubX();
            y1Coordinate = destination.getHubY();
            x2Coordinate = destination.getX();
            y2Coordinate = destination.getY();
        } else {
            x1Coordinate = this.currentBranch.getX();
            y1Coordinate = this.currentBranch.getY();
            x2Coordinate = this.currentBranch.getHubX();
            y2Coordinate = this.currentBranch.getHubY();

        }
        float nTemp = ((float) getTime() - (float) getTimeLeft()) / (float) getTime();
        float vTemp = (float) getTimeLeft() / (float) getTime();
        int x3Coordinate = (int) (((nTemp * x2Coordinate) + (vTemp * x1Coordinate)) / (vTemp + nTemp));
        int y3Coordinate = (int) (((nTemp * y2Coordinate) + (vTemp * y1Coordinate)) / (vTemp + nTemp));
        if (this.getPackages().size() == 0) {
            g.setColor(new Color(5, 100, 5));

        } else {
            g.setColor(new Color(100, 245, 100));
        }
        g.fillRect(x3Coordinate - (spacious / 2), y3Coordinate - (spacious / 2), 16, 16);
        g.setColor(Color.BLACK);
        g.fillOval(x3Coordinate + 2, y3Coordinate + 2, 10, 10);
        g.fillOval(x3Coordinate - 11, y3Coordinate - 11, 10, 10);
        g.fillOval(x3Coordinate + 2, y3Coordinate - 11, 10, 10);
        g.fillOval(x3Coordinate - 11, y3Coordinate + 2, 10, 10);
        if (this.getPackages().size() > 0) {
            g.setFont(new Font("TimesRoman", Font.BOLD, 12));
            g.setColor(Color.BLACK);
            g.drawString(String.valueOf(this.getPackages().size()), x3Coordinate, y3Coordinate - 20);
        }
    }


}
