package components;

import java.awt.Color;
import java.awt.Graphics;
import java.beans.PropertyChangeSupport;

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
        System.out.println("Creating " + this);
    }

    /**
     * Produces an object with given parameters.
     *
     * @param licensePlate indicate license plate number
     * @param truckModel   indicate model of vehicle
     */
    public Van(String licensePlate, String truckModel) {
        super(licensePlate, truckModel);
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
    public synchronized void deliverPackage(Package p) {
        this.getPackages().add(p);
        setAvailable(false);
        int time = (p.getDestinationAddress().street % 10 + 1) * 10;
        this.setTimeLeft(time);
        this.initTime = time;
        Status tempStatus = p.getStatus();
        p.setStatus(Status.DISTRIBUTION);
        support = new PropertyChangeSupport(p);
        support.addPropertyChangeListener(MainOffice.getInstance());
        support.firePropertyChange("DISTRIBUTION", tempStatus, p);
        p.addTracking(new Tracking(MainOffice.getClock(), this, p.getStatus()));
        System.out.println("Van " + this.getTruckID() + " is delivering package " + p.getPackageID() + ", time left: " + this.getTimeLeft());
    }

    /**
     * Performs a run unit (in each beat) according to the following requirements:
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
            Branch branch = null;
            if (!this.isAvailable()) {
                this.setTimeLeft(this.getTimeLeft() - 1);
                if (this.getTimeLeft() == 0) {
                    for (Package p : this.getPackages()) {
                        if (p.getStatus() == Status.COLLECTION) {
                            branch = MainOffice.getHub().getBranches().get(p.getSenderAddress().zip);
                            synchronized (branch) {
                                Status tempStatus = p.getStatus();
                                p.setStatus(Status.BRANCH_STORAGE);
                                support = new PropertyChangeSupport(p);
                                support.addPropertyChangeListener(MainOffice.getInstance());
                                support.firePropertyChange("BRANCH_STORAGE", tempStatus, p);

                                System.out.println("Van " + this.getTruckID() + " has collected package " + p.getPackageID() + " and arrived back to branch " + branch.getBranchId());
                                branch.addPackage(p);
                            }
                        } else {
                            Status tempStatus = p.getStatus();
                            p.setStatus(Status.DELIVERED);
                            support = new PropertyChangeSupport(p);
                            support.addPropertyChangeListener(MainOffice.getInstance());
                            support.firePropertyChange("COLLECTION", tempStatus, p);
                            branch = MainOffice.getHub().getBranches().get(p.getDestinationAddress().zip);
                            synchronized (branch) {
                                branch.listPackages.remove(p);
                                branch = null;
                                System.out.println("Van " + this.getTruckID() + " has delivered package " + p.getPackageID() + " to the destination");
                                if (p instanceof SmallPackage && ((SmallPackage) p).isAcknowledge()) {
                                    System.out.println("Acknowledge sent for package " + p.getPackageID());
                                }
                            }
                        }
                        p.addTracking(new Tracking(MainOffice.getClock(), branch, p.getStatus()));
                    }
                    this.getPackages().removeAll(getPackages());
                    this.setAvailable(true);
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
    public void paintComponent(Graphics g) {
        if (isAvailable()) return;
        Package p = this.getPackages().get(getPackages().size() - 1);
        Point start = null;
        Point end = null;
        if (p.getStatus() == Status.COLLECTION) {
            start = p.getSendPoint();
            end = p.getBInPoint();
        } else if (p.getStatus() == Status.DISTRIBUTION) {
            start = p.getBOutPoint();
            end = p.getDestPoint();
        }
        if (start != null) {
            int x2 = start.getX();
            int y2 = start.getY();
            int x1 = end.getX();
            int y1 = end.getY();
            double ratio = (double) this.getTimeLeft() / this.initTime;
            int dX = (int) (ratio * (x2 - x1));
            int dY = (int) (ratio * (y2 - y1));
            g.setColor(Color.BLUE);
            g.fillRect(dX + x1 - 8, dY + y1 - 8, 16, 16);
            g.setColor(Color.BLACK);
            g.fillOval(dX + x1 - 12, dY + y1 - 12, 10, 10);
            g.fillOval(dX + x1, dY + y1, 10, 10);
            g.fillOval(dX + x1, dY + y1 - 12, 10, 10);
            g.fillOval(dX + x1 - 12, dY + y1, 10, 10);
        }
    }
}
