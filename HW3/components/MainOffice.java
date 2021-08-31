package components;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JPanel;

/**
 * An object of this class manages the entire system, operates a clock, the
 * branches and vehicles, creates the packages "Simulates customers" and
 * transfers them to the appropriate branches.
 *
 * @author Sagi Biran , ID: 205620859
 */
public class MainOffice implements Runnable, PropertyChangeListener {

    private static MainOffice mainOffice = null;
    /**
     * Initialized to zero, each time preceded by one. Represents the amount of
     * beats that have passed since the system was started.
     */
    private static int clock = 0;
    private static Hub hub;
    /**
     * 2 Arraylist objects inorder to collect all component involved in the entire system
     * inorder to work with threads
     */
    private static ArrayList<Package> packages = new ArrayList<>();
    private ArrayList<Customer> customers = new ArrayList<>();
    /**
     * object will handle panel for entire system
     */
    private JPanel panel;
    private int maxPackages;
    /**
     * boolean variable that helps suspend Thread.
     */
    private boolean threadSuspend = false;
    /**
     * file Object to open txt file,write and write (reports due status changes belongs to packages).
     */
    File file = File.getInstance();

    //Prototype
    private static Map<String, Branch> BranchMap = new HashMap<String, Branch>();

    /**
     * A constructor who receives the number of branches that will be in the game
     * and the number of vehicles per branch. The constructor creates a "Hub object"
     * (Sorting Center).
     *
     * @param trucksForBranch will add to "Hub" Standard trucks in the quantity in
     *                        this parameter + 1 Non Standard Truck.
     * @param branches        will add to "Hub" number of branches due this
     *                        parameter and each branch will add trucks of type
     *                        "VAN" due "trucksBranch" parameter.
     * @param maxPack         max packages for entire system.
     */
    private MainOffice(int branches, int trucksForBranch, JPanel panel, int maxPack) {
        this.panel = panel;
        this.maxPackages = 50;
        addHub(trucksForBranch);
        addBranches(branches, trucksForBranch);
        for (int i = 0; i < 10; i++) {
            customers.add(new Customer());
        }
        System.out.println("\n\n========================== START ==========================");
    }

    public static MainOffice getInstance(int branches, int trucksForBranch, JPanel panel, int maxPack) {
        if (mainOffice == null) {
            mainOffice = new MainOffice(branches, trucksForBranch, panel, maxPack);
        }
        return mainOffice;
    }

    public static MainOffice getInstance() {
        return mainOffice;
    }

    public static Hub getHub() {
        return hub;
    }

    public static int getClock() {
        return clock;
    }

    /**
     * method will create package that will added to main office packages list
     */
    public static void addPackage(Package p) {
        packages.add(p);

    }

    /**
     * Method purpose is to manage thread and the way he works due Start() lunch in MainOffice Class.
     * once this method is invoked then work method for this Class will invoke as well and
     * all components will start to work as programed in HW1 i.e: as Delivery System.
     */
    @Override
    public void run() {
        /** ----BONUS BONUS BONUS BONUS BONUS BONUS----
         * Thread Pool for customer processes - customers will work At the same time without unnecessary waiting for each other and 2
         * processes (ThreadPool size 2) will take care of them all
         */
        ExecutorService executor = Executors.newFixedThreadPool(2);
        for (int i = 0; i < 10; i++) {
            executor.execute(customers.get(i));
        }
        executor.shutdown();
        Thread hubThread = new Thread(hub);
        hubThread.start();
        for (Truck t : hub.listTrucks) {
            Thread trackThread = new Thread(t);
            trackThread.start();
        }
        for (Branch b : hub.getBranches()) {
            Thread branch = new Thread(b);
            for (Truck t : b.listTrucks) {
                Thread trackThread = new Thread(t);
                trackThread.start();
            }
            branch.start();
        }
        while (true) {
            synchronized (this) {
                while (threadSuspend)
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
            }
            tick();
        }
    }

    /**
     * Prints a tracking report for all packages in the system. For each package
     * prints the entire contents of the tracking collection of the package
     */
    public void printReport() {
        for (Package p : packages) {
            System.out.println("\nTRACKING " + p);
            for (Tracking t : p.getTracking())
                System.out.println(t);
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

    public void tick() {
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(clockString());
        clock++;
        /*if (clock++ % 5 == 0 && maxPackages > 0) {
            addPackage();
            maxPackages--;
        }*/
		/*branchWork(hub);
		for (Branch b:hub.getBranches()) {
			branchWork(b);
		}*/
        panel.repaint();
    }

    public void branchWork(Branch b) {
        for (Truck t : b.listTrucks) {
            t.work();
        }
        b.work();
    }

    /**
     * Method will gets truck for branch and add them one by one
     *
     * @param trucksForBranch amount of truck to be added into branch
     */
    public void addHub(int trucksForBranch) {
        hub = new Hub();
        for (int i = 0; i < trucksForBranch; i++) {
            Truck t = new StandardTruck();
            hub.addTruck(t);
        }
        Truck t = new NonStandardTruck();
        hub.addTruck(t);
    }

    /**
     * Method does same job as upper method but allow this function to have branch parameter to ease
     * addition
     *
     * @param branches that will gets trucks
     * @param trucks   that will be added into branch
     */
    public void addBranches(int branches, int trucks) {
        for (int i = 0; i < branches; i++) {
            Branch branch = new Branch();
            for (int j = 0; j < trucks; j++) {
                branch.addTruck(new Van());
            }
            hub.add_branch(branch);
            BranchMap.put(branch.getName(), branch);
        }
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
     * Method Will suspend and pause thread.
     */
    public synchronized void setSuspend() {
        threadSuspend = true;
        for (Truck t : hub.listTrucks) {
            t.setSuspend();
        }
        for (Branch b : hub.getBranches()) {
            for (Truck t : b.listTrucks) {
                t.setSuspend();
            }
            b.setSuspend();
        }
        hub.setSuspend();
    }

    /**
     * Method will resume thread.
     */
    public synchronized void setResume() {
        threadSuspend = false;
        notify();
        hub.setResume();
        for (Truck t : hub.listTrucks) {
            t.setResume();
        }
        for (Branch b : hub.getBranches()) {
            b.setResume();
            for (Truck t : b.listTrucks) {
                t.setResume();
            }
        }
    }

    @Override
    public synchronized void propertyChange(PropertyChangeEvent evt) {
        file.writeReport((Package) evt.getNewValue());
    }

    //Prototype
    public static Branch getBranch(String BranchName) {
        return (Branch) BranchMap.get(BranchName).clone();
    }

    public static void setBranchMap(String name, Branch br) {
        BranchMap.put(name, br);
    }


    //Memento
    public ArrayList<Customer> getCustomers() {
        return customers;
    }

    public JPanel getPanel() {
        return panel;
    }

    public static Map<String, Branch> getBranchMap() {
        return BranchMap;
    }

    public static void setClock(int clock) {
        MainOffice.clock = clock;
    }

    public static void setHub(Hub hub) {
        MainOffice.hub = hub;
    }

    public static void setPackages(ArrayList<Package> packages) {
        MainOffice.packages = packages;
    }

    public void setCustomers(ArrayList<Customer> customers) {
        this.customers = customers;
    }

    public void setPanel(JPanel panel) {
        this.panel = panel;
    }

    public static void setBranchMap(Map<String, Branch> branchMap) {
        BranchMap = branchMap;
    }
}
