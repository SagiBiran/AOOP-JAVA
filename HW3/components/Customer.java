package components;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Describes a Customer that sends packages and waitting from them to get
 * into destinations! ONCE ALL PACKAGES HAVE BEEN DELIVERED BY ALL CUSTOMERS = System is done its work.
 *
 * @author Sagi Biran , ID: 205620859
 */

public class Customer implements Runnable {
    List<Package> packages = Collections.synchronizedList(new ArrayList<>());
    private int costumerID;
    private final Address senderAddress;
    Random r = new Random();
    File file = File.getInstance();
    private int counter = 0;

    /**
     * default constructor for Customer object.
     */
    public Customer() {
        costumerID = r.nextInt(999999) + 100000;
        senderAddress = new Address(r.nextInt(MainOffice.getHub().getBranches().size()), r.nextInt(999999) + 100000);
    }

    /**
     * method will create package that will added to main office packages list
     */
    public void addPackage() {
        Package p;
        Branch br;
        Priority priority = Priority.values()[r.nextInt(3)];
        Address dest = new Address(r.nextInt(MainOffice.getHub().getBranches().size()), r.nextInt(999999) + 100000);

        switch (r.nextInt(3)) {
            case 0 -> {
                p = new SmallPackage(priority, senderAddress, dest, r.nextBoolean());
                br = MainOffice.getHub().getBranches().get(senderAddress.zip);
                br.addPackage(p);
                p.setBranch(br);
            }
            case 1 -> {
                p = new StandardPackage(priority, senderAddress, dest, r.nextFloat() + (r.nextInt(9) + 1));
                br = MainOffice.getHub().getBranches().get(senderAddress.zip);
                br.addPackage(p);
                p.setBranch(br);
            }
            case 2 -> {
                p = new NonStandardPackage(priority, senderAddress, dest, r.nextInt(1000), r.nextInt(500), r.nextInt(400));
                MainOffice.getHub().addPackage(p);
            }
            default -> p = null;
        }
        MainOffice.addPackage(p);
        this.packages.add(p);
    }

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            try {
                addPackage();
                Thread.sleep(r.nextInt((4) + 2) * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        boolean finish = false;
        while (!finish) {
            try {
                if (checkIfFinished()) {
                    finish = true;
                }
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * method will check for each customers wheters all his packages have delivered or not.
     * @return - return is described a line upon.
     */
    boolean checkIfFinished() {
        BufferedReader br = file.readReport();
        String strLine = "";
        while (true) {
            try {
                if ((strLine = br.readLine()) == null) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (Package p : packages) {
                if (strLine.contains(String.valueOf(p.getPackageID()))) {
                    if (strLine.contains("DELIVERED")) {
                        counter++;
                    }
                }
            }
            if (counter == 5) {
                return true;
            }
            counter = 0;
            return false;
        }
        return false;
    }
}

