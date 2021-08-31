package components;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Class will helps to create a memento of MainOffice object and
 * get back into last state that captured.
 * will use a hard copy from MainObject to implement restoration.
 *
 * @author Sagi Biran , ID: 205620859
 */
public class MainOfficeMemento {
    private static int clock = 0;
    private static Hub hub;
    private static ArrayList<Package> packages = new ArrayList<>();
    private ArrayList<Customer> customers;
    private JPanel panel;
    private static Map<String, Branch> BranchMap = new HashMap<>();


    public MainOfficeMemento(int clock, Hub hub, ArrayList<Package> packages, ArrayList<Customer> customers, JPanel panel, Map<String, Branch> BranchMap) {
        MainOfficeMemento.clock = clock;
        MainOfficeMemento.hub = hub;
        MainOfficeMemento.packages = packages;
        this.customers = customers;
        this.panel = panel;
        MainOfficeMemento.BranchMap = BranchMap;
    }

    public static int getClock() {
        return clock;
    }

    public static Hub getHub() {
        return hub;
    }

    public static ArrayList<Package> getPackages() {
        return packages;
    }

    public ArrayList<Customer> getCustomers() {
        return customers;
    }

    public JPanel getPanel() {
        return panel;
    }

    public static Map<String, Branch> getBranchMap() {
        return BranchMap;
    }
}
