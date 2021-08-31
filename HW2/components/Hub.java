package components;

import java.util.ArrayList;
import java.util.Random;

/**
 * Describing "main branch" in entire system.
 *
 * @author Sagi Biran , ID: 205620859
 */

public class Hub extends Branch implements Runnable, Utilities {
    /**
     * Collection of objects of all local branches.
     */
    private ArrayList<Branch> branches = new ArrayList<Branch>();

    /**
     * Addition by be, static variable that help to truck which branch should each
     * standard truck goes. it helps sorting center ensure that the trips to the
     * branches are in the order of the branch numbers. for example: if one last
     * truck went out to the branch, 2 then The current truck will leave for branch
     * 3 and so on.
     */
    private int currentIndex = 0;


    /**
     * boolean variable that helps suspend Thread.
     */
    private boolean threadPause = false;

    /**
     * boolean variable that helps stop Thread.
     */
    private boolean quit_stop = false;

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
     * method will send NonStandardTruck to branch distension in order to collect package.
     */
    public void shipNonStandard(NonStandardTruck nonStandardTruck) {
        for (Package pack : packages) {
            if (pack instanceof NonStandardPackage) {
                nonStandardTruck.collectPackage(pack);
                nonStandardTruck.setSenderspot(pack);
                nonStandardTruck.setSource(this);
                packages.remove(pack);
                return;
            }
        }
    }

    /**
     * Describing "work unit" that is performed in one clock. For each standard
     * sorting center (Hub) truck, if the truck is available, it is shipped to some
     * local branch in a circular fashion. For the purpose of the drive, the truck
     * will load all the packages waiting to be transferred to the branch to which
     * she travels, as long as the weight of the packages is less than the maximum
     * weight she can carry. (The weight of a small package is one kilogram, the
     * rest of the packages contain a variable that describes its weight). The
     * status of the packages and their history are updated accordingly, a message
     * is printed stating that the truck is leaving for this branch, travel time is
     * been randomized to integer value between 1 and 10. If the non-standard truck
     * is available, it will be checked whether there is a non-standard package in
     * the sorting center waiting to be collected And its dimensions fit the truck.
     * If so, the truck will be sent to pick up the package. Collect the non
     * standard package from a customer is carried out according to exactly the same
     * rules as the rest of the packages, only by a non-standard truck.
     */
    @Override
    public void work() {
        for (Truck truck : listTrucks) {
            if (truck.isAvailable()) {
                if (truck instanceof NonStandardTruck)
                    shipNonStandard((NonStandardTruck) truck);
                else {
                    sendTruck((StandardTruck) truck);
                }
            }
            truck.work();
        }
    }

    /**
     * Method will send StandardTruck to branch in order to do his Work.
     */
    public void sendTruck(StandardTruck truck) {
        truck.setAvailable(false);
        truck.setDestination(branches.get(currentIndex));
        truck.load(this, truck.getDestination(), Status.BRANCH_TRANSPORT);
        int time = ((new Random()).nextInt(10) + 1) * 10;
        truck.setTimeLeft(time);
        truck.setTime(time);
        truck.setSource(this);
        System.out.println(truck.getName() + " is on it's way to " + truck.getDestination().getName() + ", time to arrive: " + truck.getTimeLeft());
        currentIndex = (currentIndex + 1) % branches.size();
    }


    /**
     * Suspend thread.
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
     * Method will mange thread work and invoke Start() function for hub operation.
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

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

}
