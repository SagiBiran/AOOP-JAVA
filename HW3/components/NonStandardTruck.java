package components;

import java.awt.Color;
import java.awt.Graphics;
import java.beans.PropertyChangeSupport;
import java.util.Random;

/**
 * * A vehicle for transporting packages of non-standard size (exceptional
 * cargo). All vehicles of this type are in the sorting center.
 *
 * @author Sagi Biran , ID: 205620859
 */
public class NonStandardTruck extends Truck {
    private int width, length, height;
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
        System.out.println("Creating " + this);
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
     * Performs a run (work unit) aka (in each beat) according to the following requirements:
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
    public void run() {
        while (true) {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (this) {
                while (threadSuspend)
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
            }
            if (!this.isAvailable()) {
                Package p = this.getPackages().get(0);
                this.setTimeLeft(this.getTimeLeft() - 1);
                if (this.getTimeLeft() == 0) {
                    if (p.getStatus() == Status.COLLECTION) {
                        System.out.println("NonStandartTruck " + this.getTruckID() + "has collected package " + p.getPackageID());
                        deliverPackage(p);
                    } else {
                        System.out.println("NonStandartTruck " + this.getTruckID() + "has delivered package " + p.getPackageID() + " to the destination");
                        this.getPackages().remove(p);
                        Status tempStatus = p.getStatus();
                        p.setStatus(Status.DELIVERED);
                        support = new PropertyChangeSupport(p);
                        support.addPropertyChangeListener(MainOffice.getInstance());
                        support.firePropertyChange("DELIVERED", tempStatus, p);
                        p.addTracking(new Tracking(MainOffice.getClock(), null, p.getStatus()));
                        setAvailable(true);
                    }
                }
            } else
                synchronized (this) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        }
    }

    @Override
    public void work() {
    }

    @Override
    public synchronized void deliverPackage(Package p) {
        int time = (Math.abs(p.getDestinationAddress().street - p.getDestinationAddress().street) % 10 + 1) * 10;
        this.setTimeLeft(time);
        this.initTime = time;
        Status tempStatus = p.getStatus();
        p.setStatus(Status.DISTRIBUTION);
        support = new PropertyChangeSupport(p);
        support.addPropertyChangeListener(MainOffice.getInstance());
        support.firePropertyChange("DISTRIBUTION", tempStatus, p);
        p.addTracking(new Tracking(MainOffice.getClock(), this, p.getStatus()));
        System.out.println("NonStandartTruck " + this.getTruckID() + " is delivering package " + p.getPackageID() + ", time left: " + this.getTimeLeft());
    }

    /**
     * helps print Non Standard truck in appropriate way.
     */
    @Override
    public String toString() {
        return "NonStandardTruck [" + super.toString() + ", length=" + length + ", width=" + width + ", height="
                + height + "]";
    }

    @Override
    public void paintComponent(Graphics g) {
        if (isAvailable()) return;
        Package p = this.getPackages().get(getPackages().size() - 1);
        Point start = null;
        Point end = null;
        Color col = null;
        if (p.getStatus() == Status.COLLECTION) {
            start = new Point(1140, 216);
            end = p.getSendPoint();
            col = new Color(255, 180, 180);
        } else if (p.getStatus() == Status.DISTRIBUTION) {
            start = p.getSendPoint();
            end = p.getDestPoint();
            col = Color.RED;
        }

        if (start != null) {
            int x2 = start.getX();
            int y2 = start.getY();
            int x1 = end.getX();
            int y1 = end.getY();
            double ratio = (double) this.getTimeLeft() / this.initTime;
            int dX = (int) (ratio * (x2 - x1));
            int dY = (int) (ratio * (y2 - y1));
            g.setColor(col);
            g.fillRect(dX + x1 - 8, dY + y1 - 8, 16, 16);
            g.setColor(Color.BLACK);
            g.fillOval(dX + x1 - 12, dY + y1 - 12, 10, 10);
            g.fillOval(dX + x1, dY + y1, 10, 10);
            g.fillOval(dX + x1, dY + y1 - 12, 10, 10);
            g.fillOval(dX + x1 - 12, dY + y1, 10, 10);
        }
    }
}

