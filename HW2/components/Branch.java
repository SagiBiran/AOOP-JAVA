package components;

import java.awt.*;
import java.util.ArrayList;

/**
 * Describes a local branch. Keeps a list of packages stored at the branch or
 * intended for collection from the sender's address To this branch, and a list
 * of vehicles that collect the packages from the sending customers and deliver
 * the packages to the receiving customers.
 *
 * @author Sagi Biran , ID: 205620859
 */

public class Branch implements Node, Utilities, Runnable {

    /**
     * Continuous number , starts from -1 to separate sorting center ("hub" , branch
     * number -1) from other local branches (0,1,2.....).
     */

    private static int counter = -1;
    /**
     * static global counter variable that helps set proper id for each package that
     * created and make sure no id is repeated.
     */
    private int branchId;
    /**
     * name of branch , mainly used for sorting center ("hub",once operate entire
     * system.)
     */
    private String branchName;
    /**
     * A collection of vehicles belonging to this branch.
     */
    protected ArrayList<Package> packages = new ArrayList<Package>();
    /**
     * Default constructor, calculates the id number of the branch and creates the
     * name of the branch.
     */
    protected ArrayList<Truck> listTrucks = new ArrayList<Truck>();

    /**
     * A collection of packages that are in the branch and packages that must be
     * collected that are shipped by senders to this branch.
     */

    /**
     * will hold x coordinate due branch paint on panel.
     */
    private double x;
    /**
     * will hold y coordinate due branch paint on panel.
     */
    private double y;
    /**
     * variable hold
     */
    private int hubX;
    /**
     *
     */
    private int hubY;

    /**
     * boolean variable that helps suspend Thread.
     */
    private boolean threadPause = false;
    /**
     * boolean variable that helps stop Thread.
     */
    private boolean quit_stop = false;

    /**
     * default Constructor
     */
    public Branch() {
        this("Branch " + counter);
    }


    /**
     * A constructor who gets a branch name, calculates the id number of the branch.
     */
    public Branch(String branchName) {
        this.branchId = counter++;
        this.branchName = branchName;
        System.out.println("\nCreating " + this);
    }

    /**
     * branch constructor that gets 3 parameters
     */
    public Branch(String branchName, Package[] packeslist, Truck[] truckslist) {
        this.branchId = counter++;
        this.branchName = branchName;
        addPackages(packeslist);
        addTrucks(truckslist);
    }

    /**
     * function will print branch is proper format
     */
    public void printBranch() {
        System.out.println("\nBranch name: " + branchName);
        System.out.println("Packages list:");
        for (Package pack : packages)
            System.out.println(pack);
        System.out.println("Trucks list:");
        for (Truck truck : listTrucks)
            System.out.println(truck);
    }

    /**
     * @param pack that will be added into packages list
     */
    public void addPackage(Package pack) {
        packages.add(pack);
    }

    /**
     * @param truck that will be added into truck list
     */
    public void addTruck(Truck truck) {
        listTrucks.add(truck);
    }

    /**
     * @param packagesList list of packages to merge with existence packages list
     */
    public void addPackages(Package[] packagesList) {
        for (Package pack : packagesList)
            packages.add(pack);
    }

    /**
     * @param trucksList list of trucks that wil be added into existence truck list
     */
    public void addTrucks(Truck[] trucksList) {
        for (Truck truck : trucksList)
            listTrucks.add(truck);
    }

    /**
     * @return unique branch id
     */
    public int getBranchId() {
        return branchId;
    }

    /**
     * @return unique branch name
     */
    public String getName() {
        return branchName;
    }

    public String getBranchName() {
        return branchName;
    }

    /**
     * @return helps print branch details in appropriate way
     */
    @Override
    public String toString() {
        return "Branch " + branchId + ", branch name:" + branchName + ", packages: " + packages.size()
                + ", trucks: " + listTrucks.size();
    }


    /**
     * Collect package from Van and store it in branch packages collection.
     */
    @Override
    public void collectPackage(Package p) {
        for (Truck truck : listTrucks) {
            if (truck.isAvailable()) {
                truck.collectPackage(p);
                return;
            }
        }
    }


    /**
     * decided to no implement this method according to Michael Finkelstien
     * Implementation is OPTIONAL.
     */
    @Override
    public void deliverPackage(Package p) {
        for (Truck truck : listTrucks) {
            if (truck.isAvailable()) {
                truck.deliverPackage(p);
                return;
            }
        }
    }


    /**
     *  A work unit performed by a branch every time the system clock beats.  For
     * each package that is in the branch, if it is in the waiting status for
     * collection from a customer, an attempt is made to collect - if There is a
     * vehicle available, he goes out to pick up the package, a random calculation
     * is made that describes the time of arrival at the customer and the status of
     * the vehicle goes to status - not available.  Same as for any package waiting
     * to be distributed, if there is a vehicle available, it is sent to deliver the
     * package. In both cases, the status and history of the package are updated and
     * appropriate prints are created.
     */
    @Override
    public void work() {
        for (Truck truck : listTrucks) {
            truck.work();
        }
        localWork();
    }

    /**
     * method that implement work done by branches and vans
     */
    public void localWork() {
        for (Package p : packages) {
            if (p.getStatus() == Status.CREATION) {
                collectPackage(p);
            }
            if (p.getStatus() == Status.DELIVERY) {
                deliverPackage(p);
            }
        }
    }

    /**
     * @return list of packages located in specific branch
     */
    public ArrayList<Package> getPackages() {
        return packages;
    }

    /**
     * method will get a package (1 only) and remove it from packages list
     *
     * @param p package that will be removed from list
     */
    public void removePackage(Package p) {
        packages.remove(p);
    }

    /**
     * Method Will suspend and pause thread.
     */
    @Override
    public void setSuspend() {
        threadPause = true;
    }

    /**
     * Method will resume thread.
     */
    @Override
    public synchronized void setResume() {
        threadPause = false;
        notify();
    }

    /**
     * Method will stop thread permanently .
     */
    @Override
    public void setStop() {
        quit_stop = true;
    }

    /**
     * Method purpose is to manage thread and the way he works due Start() lunch in MainOffice Class.
     * once this method is invoked then work method for this Class will invoke as well and
     * all components will start to work as programed in HW1 i.e: as Delivery System.
     */
    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(500);
                synchronized (this) {
                    while (threadPause)
                        wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (quit_stop) return;
            work();
        }
    }

    /**
     * Method will draw lines on panel between hub and branches.
     *
     * @param g        graphic object
     * @param position will hold position correction to draw components in best accuracy.
     */
    public void drawLines(Graphics g, int position) {
        g.setColor(new Color(0, 105, 0));
        setHubX(1145);
        setHubY(290 + (position - 1) * (200 / (counter + 1)));
        g.drawLine((int) this.getX(), (int) this.getY(), this.getHubX(), this.getHubY());
    }

    /**
     * draw Branch components on panel as circles .
     * This method will print Hub and branches to instance of checking! (Hub is supertype of Branch)
     * @param g graphic object
     */

    public void drawBranch(Graphics g) {
        int xAxis = 0, yAxis = 0;
        Polygon temp_Polygon = new Polygon();
        if (this instanceof Hub) {
            g.setColor(new Color(0, 105, 0));
            temp_Polygon.addPoint((int) getX() - 20, (int) getY() - 100);
            temp_Polygon.addPoint((int) getX() + 20, (int) getY() - 100);
            temp_Polygon.addPoint((int) getX() + 20, (int) getY() + 100);
            temp_Polygon.addPoint((int) getX() - 20, (int) getY() + 100);
            temp_Polygon.translate(xAxis, yAxis);
            g.drawPolygon(temp_Polygon);
            g.fillPolygon(temp_Polygon);
            //means this is Branch obj and NOT hub
        } else {
            if (this.packages.size() != 0) {
                g.setColor(new Color(0, 0, 155));

            } else {
                g.setColor(new Color(50, 200, 255));
            }
            temp_Polygon.addPoint((int) getX() - 20, (int) getY() - 15);
            temp_Polygon.addPoint((int) getX() + 20, (int) getY() - 15);
            temp_Polygon.addPoint((int) getX() + 20, (int) getY() + 15);
            temp_Polygon.addPoint((int) getX() - 20, (int) getY() + 15);
            temp_Polygon.translate(xAxis, yAxis);
            g.drawPolygon(temp_Polygon);
            g.fillPolygon(temp_Polygon);
        }
    }

    /**
     * Get branch x coordinate.
     * @return branch x coordinate
     */
    public double getX() {
        return x;
    }

    /**
     * Set branch x coordinate.
     * @param x branch x coordinate
     */

    public void setX(int x) {
        this.x = x;
    }

    /**
     * Get branch y coordinate.
     * @return branch y coordinate
     */
    public double getY() {
        return y;
    }

    /**
     * Set branch y coordinate.
     * @param y branch y coordinate
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * @return Hub x coordinate
     */
    public int getHubX() {
        return hubX;
    }

    /**
     * @param hubX set Hub x coordinate
     */
    public void setHubX(int hubX) {
        this.hubX = hubX;
    }

    /**
     * @return get hub y coordinate
     */
    public int getHubY() {
        return hubY;
    }

    /**
     * @param hubY set hub y coordinate
     */
    public void setHubY(int hubY) {
        this.hubY = hubY;
    }

}
