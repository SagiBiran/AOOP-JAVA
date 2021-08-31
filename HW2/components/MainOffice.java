package components;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * An object of this class manages the entire system, operates a clock, the
 * branches and vehicles, creates the packages "Simulates customers" and
 * transfers them to the appropriate branches.
 *
 * @author Sagi Biran , ID: 205620859
 */

public class MainOffice implements Runnable, Utilities {
    /**
     * Initialized to zero, each time preceded by one. Represents the amount of
     * beats that have passed since the system was started.
     */
    private static int clock = 0;
    private static Hub hub;
    private ArrayList<Package> packages = new ArrayList<Package>();
    /**
     * object will handle panel for entire system
     */
    private JPanel panel;
    private int PackagesAmount;
    /**
     * 3 Arraylist objects inorder to collect all component involved in the entire system
     * inorder to work with threads
     */
    private ArrayList<Truck> trucks = new ArrayList<Truck>();
    private ArrayList<Branch> branches = new ArrayList<Branch>();
    private ArrayList<Utilities> utilitiesElements = new ArrayList<Utilities>();
    /***/

    /**
     * boolean variable that helps suspend Thread.
     */
    private boolean threadPause = false;
    /**
     * boolean variable that helps stop Thread.
     */
    private boolean quit_stop = false;

    /**
     * A constructor who receives the number of branches that will be in the game
     * and the number of vehicles per branch. The constructor creates a "Hub object"
     * (Sorting Center).
     *
     * @param trucksForBranch will add to "Hub" Standard trucks in the quantity in
     *                        this parameter + 1 Non Standard Truck.
     * @param branches        will add to "Hub" number of branches due this
     *                        parameter and each branch will add trucks of type
     *                        "VAN" due "trucksforBranch" parameter.
     */
    public MainOffice(int branches, int trucksForBranch, int numberOfPackages) {
        System.out.println("\n\n========================== START ==========================");
        addHub(trucksForBranch);
        this.PackagesAmount = numberOfPackages;
        addBranches(branches, trucksForBranch);
        utilitiesElements.addAll(this.branches);
        utilitiesElements.addAll(trucks);
    }


    public static Hub getHub() {
        return hub;
    }


    public static int getClock() {
        return clock;
    }

    public void setPanel(JPanel panel) {
        this.panel = panel;
    }

    /**
     * @return branches Branches that located  in main office obj
     */
    public ArrayList<Branch> getBranches() {
        return branches;
    }

    /**
     * get array of Package.
     *
     * @return array of Package on main office.
     */
    public ArrayList<Package> getPackages() {
        return packages;
    }

    /**
     * @return number of packages that existed in main office.
     */
    public int getNumberOfPackages() {
        return PackagesAmount;
    }

    /**
     * @return array of trucks located in main office.
     */
    public ArrayList<Truck> getTrucks() {
        return trucks;
    }

    /**
     * A function takes as an argument the number of beats that the system will
     * execute and activates the beats (tick) this number of times.
     *
     * @param playTime number of beats that the system will execute.
     */
    public void play(int playTime) {
        for (int i = 0; i < playTime; i++) {
            tick();
        }
        System.out.println("\n========================== STOP ==========================\n\n");
        printReport();
    }

    /**
     * Prints a tracking report for all packages in the system. For each package
     * prints the entire contents of the tracking collection of the package
     */
    public void printReport() {
        for (Package pack : packages) {
            System.out.println("\nTRACKING " + pack);
            for (Tracking track : pack.getTracking())
                System.out.println(track);
        }
    }

    /**
     * Prints the value of the clock in "MM: SS" format.
     *
     * @return value of the watch in "MM: SS" format.
     */
    public String clockString() {
        String s = "";
        int minutes = clock / 60;
        int seconds = clock % 60;
        s += (minutes < 10) ? "0" + minutes : minutes;
        s += ":";
        s += (seconds < 10) ? "0" + seconds : seconds;
        return s;
    }

    /**
     * describe "beat" in entire system. Each time this function is activated ("in
     * each beat") the following actions are performed: The clock is printed and
     * promoted at one. All branches, Sorting Center and vehicles perform one work
     * unit. Every 5 beats a random new package is created. After the last beat, the
     * job termination. message ("STOP") is printed and then a history report
     * transfers is printed for all packages created during the run of system.
     */
    public void tick() {
        System.out.println(clockString());
        if (clock % 5 == 0 && PackagesAmount > this.packages.size()) {
            addPackage();
        }
        clock++;
    }

    /**
     * Method will gets truck for branch and add them one by one
     *
     * @param trucksForBranch amount of truck to be added into branch
     */
    public void addHub(int trucksForBranch) {
        hub = new Hub();
        hub.setX(1145);
        hub.setY(350);
        Truck tempTruck;
        for (int i = 0; i < trucksForBranch; i++) {
            tempTruck = new StandardTruck();
            hub.addTruck(tempTruck);
            trucks.add(tempTruck);
        }
        tempTruck = new NonStandardTruck();
        hub.addTruck(tempTruck);
        trucks.add(tempTruck);
        this.branches.add(hub);
    }

    /**
     * Method does same job as upper method but allow this function to have branch parameter to ease
     * addition
     *
     * @param branches that will gets trucks
     * @param trucks   that will be added into branch
     */
    public void addBranches(int branches, int trucks) {
        Truck tempTruck;
        for (int i = 0; i < branches; i++) {
            Branch branch = new Branch();
            branch.setX(70);
            int space = (700 / (branches + 2));
            branch.setY(space + (i * space));
            if (i != 0) {
                branch.setHubX(1145);
                branch.setHubY(290 + (i * (800 / this.PackagesAmount - 1) * (200 / (branch.getBranchId() + 1))));
            }

            for (int j = 0; j < trucks; j++) {
                tempTruck = new Van();
                branch.addTruck(tempTruck);
                this.trucks.add(tempTruck);
            }
            hub.add_branch(branch);
            this.branches.add(branch);
        }
    }

    /**
     * method will create package that will added to main office packages list
     */
    public void addPackage() {
        Random r = new Random();
        Package pack;
        Priority priority = Priority.values()[r.nextInt(3)];
        Address sender = new Address(r.nextInt(hub.getBranches().size()), r.nextInt(999999) + 100000);
        Address dest = new Address(r.nextInt(hub.getBranches().size()), r.nextInt(999999) + 100000);

        switch (r.nextInt(3)) {
            case 0:
                pack = new SmallPackage(priority, sender, dest, r.nextBoolean());
                pack.getSenderBranch().addPackage(pack);
                break;
            case 1:
                pack = new StandardPackage(priority, sender, dest, r.nextFloat() + (r.nextInt(9) + 1));
                pack.getSenderBranch().addPackage(pack);
                break;
            case 2:
                pack = new NonStandardPackage(priority, sender, dest, r.nextInt(1000), r.nextInt(500), r.nextInt(400));
                hub.addPackage(pack);
                break;
            default:
                pack = null;
                return;
        }

        this.packages.add(pack);

    }

    /**
     * Method purpose is to manage thread and the way he works due Start() lunch in MainOffice Class.
     * once this method is invoked then work method for this Class will invoke as well and
     * all components will start to work as programed in HW1 i.e: as Delivery System.
     */
    @Override
    public void run() {
        for (Utilities utilities : utilitiesElements) {
            Thread t = new Thread((Runnable) utilities);
            t.start();
        }

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
            panel.repaint();
            tick();
        }
    }

    /**
     * Method Will suspend and pause thread.
     */
    @Override
    public synchronized void setSuspend() {
        for (Utilities utilities : utilitiesElements)
            utilities.setSuspend();
        threadPause = true;
    }

    /**
     * Method will resume thread.
     */
    @Override
    public synchronized void setResume() {
        for (Utilities utilities : utilitiesElements)
            utilities.setResume();
        threadPause = false;
        notify();
    }

    /**
     * Method will stop thread permanently .
     */
    public void setStop() {
        setResume();
        for (Utilities utilities : utilitiesElements)
            utilities.setStop();
        quit_stop = true;
    }

}
