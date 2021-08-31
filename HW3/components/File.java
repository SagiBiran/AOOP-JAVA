package components;

import java.io.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * File Class will describe a file object that will create a txt file,
 * and method to read and write from this file.
 *@author Sagi Biran , ID: 205620859
 */
public class File {

    private final String fileName = "tracking.txt";
    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
    private static int reportNumber = 1;
    private static volatile File file = null;

    private File() {
        try {
            FileWriter fw = new FileWriter(fileName, false);
            fw.write("");
            fw.close();
        } catch (IOException e) {
            e.getStackTrace();
        }
    }

    /**
     * singleton implementation to file object, "LAZY INITIATION"
     * @return FILE Object (new or already exited)
     */
    public static File getInstance() {
        if (file == null) {
            file = new File();
        }
        return file;
    }
    /**
     * method will help to write data into txt file
     * @param packages packages that will be written into this file.
     */
    public void writeReport(Package packages) {
        rwLock.writeLock().lock();
        try {
            FileWriter fw;
            fw = new FileWriter(fileName, true);
            String sentence = reportNumber + ". " + packages.toString() + "\n";
            fw.write(sentence);
            reportNumber++;
            fw.close();
        } catch (IOException e) {
            e.getStackTrace();
        } finally {
            rwLock.writeLock().unlock();
        }
    }
    /**
     * method will help to read data from txt file that have been created.
     */
    public BufferedReader readReport() {
        BufferedReader bufferedReader = null;
        rwLock.readLock().lock();
        try {
            FileReader file = new FileReader(fileName);
            bufferedReader = new BufferedReader(file);
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        } finally {
            rwLock.readLock().unlock();
        }
        return bufferedReader;
    }
}
