package components;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

/**
 * Describes a local branch. Keeps a list of packages stored at the branch or
 * intended for collection from the sender's address To this branch, and a list
 * of vehicles that collect the packages from the sending customers and deliver
 * the packages to the receiving customers.
 *
 * @author Sagi Biran , ID: 205620859
 */

public class Branch implements Node, Runnable ,Cloneable{
    /**
     * Continuous number , starts from -1 to separate sorting center ("hub" , branch
     * number -1) from other local branches (0,1,2.....).
     */
    private int branchId;
    /**
     * static global counter variable that helps set proper id for each package that
     * created and make sure no id is repeated.
     */
    private static int counter = 0;
    /**
     * name of branch , mainly used for sorting center ("hub",once operate entire
     * system.)
     */
    private String branchName;
    /**
     * A collection of unsafe packages belonging to this branch.
     */
    protected ArrayList<Package> unsafeListPackages = new ArrayList<Package>();
    /**
     * A collection of vehicles belonging to this branch.
     */
    protected ArrayList<Truck> listTrucks = new ArrayList<Truck>();
    /**
     * A collection of packages belonging to this branch.
     */
    protected List<Package> listPackages = unsafeListPackages; //Collections.synchronizedList(unsafeListPackages);
    /**
     * point object to help paint hub properly on GUI.
     */
    private Point hubPoint;
    /**
     * point object to help paint branch properly on GUI.
     */
    private Point branchPoint;
    /**
     * boolean variable that helps stop Thread.
     */
    protected boolean threadPause = false;

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
    public Branch(String branchName, Package[] plist, Truck[] tlist) {
        this.branchId = counter++;
        this.branchName = branchName;
        addPackages(plist);
        addTrucks(tlist);
    }

    /**
     * @return list of packages located in specific branch
     */
    public synchronized List<Package> getPackages() {
        return this.listPackages;
    }

    /**
     * function will print branch is proper format
     */
    public void printBranch() {
        System.out.println("\nBranch name: " + branchName);
        System.out.println("Packages list:");
        for (Package pack : listPackages)
            System.out.println(pack);
        System.out.println("Trucks list:");
        for (Truck trk : listTrucks)
            System.out.println(trk);
    }

    /**
     * @param pack that will be added into packages list
     */
    public synchronized void addPackage(Package pack) {
        listPackages.add(pack);
    }

    /**
     * @return list of trucks located in specific branch
     */
    public ArrayList<Truck> getTrucks() {
        return this.listTrucks;
    }

    /**
     * @param trk that will be added into truck list
     */
    public void addTruck(Truck trk) {
        listTrucks.add(trk);
    }

    /**
     * @return hub x && y coordinates due painting on GUI
     */
    public Point getHubPoint() {
        return hubPoint;
    }

    /**
     * @return branch x && y coordinates due painting on GUI
     */
    public Point getBranchPoint() {
        return branchPoint;
    }

    /**
     * @param plist list of packages to merge with existence packages list
     */
    public synchronized void addPackages(Package[] plist) {
        for (Package pack : plist)
            listPackages.add(pack);
    }

    /**
     * @param trucksList list of trucks that wil be added into existence truck list
     */
    public void addTrucks(Truck[] trucksList) {
        for (Truck trk : trucksList)
            listTrucks.add(trk);
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

    /**
     * @return helps print branch details in appropriate way
     */
    @Override
    public String toString() {
        return "Branch " + branchId + ", branch name:" + branchName + ", packages: " + listPackages.size()
                + ", trucks: " + listTrucks.size();
    }

    /**
     * Collect package from Van and store it in branch packages collection.
     */
    @Override
    public synchronized void collectPackage(Package p) {
        for (Truck v : listTrucks) {
            if (v.isAvailable()) {
                synchronized (v) {
                    v.notify();
                }
                v.collectPackage(p);
                return;
            }
        }
    }

    /**
     * decided to no implement this method according to Michael Finkelstien
     * Implementation is OPTIONAL.
     */
    @Override
    public synchronized void deliverPackage(Package p) {
        for (Truck v : listTrucks) {
            if (v.isAvailable()) {
                synchronized (v) {
                    v.notify();
                }
                v.deliverPackage(p);
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

    }

    /**
     * Method purpose is to manage thread and the way he works due Start() lunch in MainOffice Class.
     * once this method is invoked then work method for this Class will invoke as well and
     * all components will start to work as programed in HW1 i.e: as Delivery System.
     */
    @Override
    public void run() {
        while (true) {
            synchronized (this) {
                while (threadPause)
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
            }
            synchronized (this) {
                for (Package p : listPackages) {
                    if (p.getStatus() == Status.CREATION) {
                        collectPackage(p);
                    }
                    if (p.getStatus() == Status.DELIVERY) {
                        deliverPackage(p);
                    }
                }
            }
        }
    }

    /**
     * Method Will suspend and pause thread.
     */
    public synchronized void setSuspend() {
        threadPause = true;
    }

    /**
     * Method will resume thread.
     */
    public synchronized void setResume() {
        threadPause = false;
        notify();
    }

    /*****************************************************************************************************************/
    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public void setUnsafeListPackages(ArrayList<Package> unsafeListPackages) {
        this.unsafeListPackages = unsafeListPackages;
    }

    public void setListPackages(List<Package> listPackages) {
        this.listPackages = listPackages;
    }

    public void setListTrucks(ArrayList<Truck> listTrucks) {
        this.listTrucks = listTrucks;
    }

    public static int getCounter() {
        return counter;
    }

    private boolean arePackagesInBranch() {
        for (Package p : listPackages) {
            if (p.getStatus() == Status.BRANCH_STORAGE)
                return true;
        }
        return false;
    }

    public static void setCounter(int counter) {
        Branch.counter = counter;
    }

    public void paintComponent(Graphics g, int y, int y2) {
        if (arePackagesInBranch())
            g.setColor(new Color(0, 0, 153));
        else
            g.setColor(new Color(51, 204, 255));
        g.fillRect(20, y, 40, 30);

        g.setColor(new Color(0, 102, 0));
        g.drawLine(60, y + 15, 1120, y2);
        branchPoint = new Point(60, y + 15);
        hubPoint = new Point(1120, y2);
    }

    //Prototype
    public Object clone()
    {
        Object clone = null;
        try
        {
            clone = super.clone();
        }
        catch (CloneNotSupportedException e)
        {
            e.printStackTrace();
        }
        return clone;
    }

}
