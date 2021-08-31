package components;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

import java.beans.PropertyChangeSupport;

/**
 * Class that representing the vehicles for transporting packages.
 *
 * @author Sagi Biran , ID: 205620859
 */
public abstract class Truck implements Node, Runnable {
    private static int countID = 2000;
    final private int truckID;
    private String licensePlate;
    private String truckModel;
    private boolean available = true;
    private int timeLeft = 0;
    private ArrayList<Package> packages = new ArrayList<>();
    protected int initTime;
    /**
     * boolean variable that helps suspend Thread.
     */
    protected boolean threadSuspend = false;
    /**
     * PropertyChangeSupport object to update listens(mainOffice)
     * and fire messages due situations
     */
    PropertyChangeSupport support;

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
    }

    public ArrayList<Package> getPackages() {
        return packages;
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    /**
     * Method will set time for truck task .
     *
     * @param timeLeft time task to set into truck.
     */
    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
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
     * @param p package that should collect.
     */
    @Override
    public synchronized void collectPackage(Package p) {
        setAvailable(false);
        int time = (p.getSenderAddress().street % 10 + 1) * 10;
        this.setTimeLeft(time);
        this.initTime = time;
        this.packages.add(p);
        Status tempStatus = p.getStatus();
        p.setStatus(Status.COLLECTION);
        support = new PropertyChangeSupport(p);
        support.addPropertyChangeListener(MainOffice.getInstance());
        support.firePropertyChange("COLLECTION", tempStatus, p);
        p.addTracking(new Tracking(MainOffice.getClock(), this, p.getStatus()));
        System.out.println(getName() + " is collecting package " + p.getPackageID() + ", time to arrive: " + getTimeLeft());
    }

    @Override
    public synchronized void deliverPackage(Package p) {
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
     * @return name type of truck .
     */
    public String getName() {
        return this.getClass().getSimpleName() + " " + truckID;
    }

    /**
     * Method Will suspend and pause thread.
     */
    public synchronized void setSuspend() {
        threadSuspend = true;
    }

    /**
     * Method will resume thread.
     */
    public synchronized void setResume() {
        threadSuspend = false;
        notify();
    }

    public abstract void paintComponent(Graphics g);
}
