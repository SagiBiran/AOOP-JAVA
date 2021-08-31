package components;

import java.util.ArrayList;
import java.util.Random;

/**
 * Class that representing the vehicles for transporting packages.
 *
 * @author Sagi Biran , ID: 205620859
 */
public abstract class Truck implements Node, Runnable, Utilities {
    private static int countID = 2000;
    final private int truckID;
    private boolean available = true;
    private String truckModel;
    private int timeLeft = 0;
    private String licensePlate;
    private ArrayList<Package> packages = new ArrayList<Package>();
    /**
     * boolean variable that helps suspend Thread.
     */
    private boolean threadPause = false;
    /**
     * boolean variable that helps stop Thread.
     */
    private boolean quit_stop = false;

    /**
     * will represent time for task of the truck.
     */
    private int taskTime = 1;

    /**
     * will represent what's truck branch source
     */
    private Branch source;
    /**
     * will represent what's truck branch destination
     */
    private Branch destination;

    /**
     * A random default constructor that create an object with a license plate and
     * model of a vehicle at random. A car's model consists of letter "M" and a
     * number between 0 and 4. A license plate consists of three numbers separated
     * by a line, according to the pattern - xxx-xx-xxx.
     */
    public Truck() {
        truckID = countID++;
        Random r = new Random();
        licensePlate = (r.nextInt(900) + 100) + "-" + (r.nextInt(90) + 10) + "-" + (r.nextInt(900) + 100);
        truckModel = "M" + r.nextInt(5);
        System.out.print("Creating ");
    }

    /**
     * A builder who receives as arguments a number plate and model of the vehicle
     * and create an object.
     *
     * @param licensePlate of current truck
     * @param truckModel   of current truck
     */
    public Truck(String licensePlate, String truckModel) {
        truckID = countID++;
        this.licensePlate = licensePlate;
        this.truckModel = truckModel;
        System.out.print("Creating ");
    }


    public ArrayList<Package> getPackages() {
        return packages;
    }


    public int getTimeLeft() {
        return timeLeft;
    }


    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
    }


    public Branch getSource() {
        return source;
    }


    public void setSource(Branch source) {
        this.source = source;
    }

    public Branch getDestination() {
        return destination;
    }

    public void setDestination(Branch destination) {
        this.destination = destination;
    }

    /**
     * helps print any truck type information in appropriate way.
     */
    @Override
    public String toString() {
        return "truckID=" + truckID + ", licensePlate=" + licensePlate + ", truckModel=" + truckModel + ", available= " + available;
    }

    /**
     * method will help to collect and transport package.
     *
     * @param pack package that should collect.
     */
    @Override
    public void collectPackage(Package pack) {
        setAvailable(false);
        int time = pack.getSenderAddress().getStreet() % 10 + 1;
        this.setTimeLeft(time);
        this.packages.add(pack);
        pack.setStatus(Status.COLLECTION);
        pack.addTracking(new Tracking(MainOffice.getClock(), this, pack.getStatus()));
        System.out.println(getName() + " is collecting package " + pack.getPackageID() + ", time to arrive: " + getTimeLeft());
    }


    public boolean isAvailable() {
        return available;
    }


    public int getTruckID() {
        return truckID;
    }


    public void setAvailable(boolean available) {
        this.available = available;
    }

    /**
     * method will help to add package into truck
     *
     * @param pack package to add
     */
    public void addPackage(Package pack) {
        this.packages.add(pack);
    }

    /**
     * @return name type of truck .
     */
    public String getName() {
        return this.getClass().getSimpleName() + " " + getTruckID();
    }

    public int getTime() {
        return taskTime;
    }

    /**
     * Method will set time for truck task .
     *
     * @param time time task to set into truck.
     */
    public void setTime(int time) {
        this.taskTime = time;
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
            if (threadPause) return;
            work();
        }
    }

}
