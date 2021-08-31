package components;

import java.util.ArrayList;
import java.util.Random;

/**
 * Describing "main branch" in entire system.
 *
 * @author Sagi Biran , ID: 205620859
 */
public class Hub extends Branch {
    /**
     * Collection of objects of all local branches.
     */
    private ArrayList<Branch> branches = new ArrayList<>();
    /**
     * Addition by be, static variable that help to truck which branch should each
     * standard truck goes. it helps sorting center ensure that the trips to the
     * branches are in the order of the branch numbers. for example: if one last
     * truck went out to the branch, 2 then The current truck will leave for branch
     * 3 and so on.
     */
    private int currentIndex = 0;

    /**
     * Constructor creates an object for this class named "HUB".
     */
    public Hub() {
        super("HUB");
    }

    /**
     * @return a collection of objects from all local branches.
     */
    public ArrayList<Branch> getBranches() {
        return branches;
    }

    /**
     * Method will add branch to branch array that belongs to hub.
     */
    public void add_branch(Branch branch) {
        branches.add(branch);
    }

    /**
     * Method will send StandardTruck to branch in order to do his Work.
     */
    public synchronized void sendTruck(StandardTruck t) {
        synchronized (t) {
            t.notify();
        }
        t.setAvailable(false);
        t.setDestination(branches.get(currentIndex));
        t.load(this, t.getDestination(), Status.BRANCH_TRANSPORT);
        t.setTimeLeft(((new Random()).nextInt(10) + 1) * 10);
        t.initTime = t.getTimeLeft();
        System.out.println(t.getName() + " is on it's way to " + t.getDestination().getName() + ", time to arrive: " + t.getTimeLeft());
        currentIndex = (currentIndex + 1) % branches.size();
    }

    /**
     * method will send NonStandardTruck to branch distension in order to collect package.
     */
    public synchronized void shipNonStandard(NonStandardTruck t) {
        for (Package p : listPackages) {
            if (p instanceof NonStandardPackage) {
				/*if (((NonStandardPackage) p).getHeight() <= t.getHeight()
					&& ((NonStandardPackage) p).getLength()<=t.getLength()
					&& ((NonStandardPackage) p).getWidth()<=t.getWidth()){*/
                synchronized (t) {
                    t.notify();
                }
                t.collectPackage(p);
                listPackages.remove(p);
                return;
            }
        }
    }

    @Override
    public void work() {

    }

    /**
     * Method will mange thread work and invoke Start() function for hub operation.
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
            for (Truck t : listTrucks) {
                if (t.isAvailable()) {
                    if (t instanceof NonStandardTruck) {
                        shipNonStandard((NonStandardTruck) t);
                    } else {
                        sendTruck((StandardTruck) t);
                    }
                }
            }
        }
    }
}
