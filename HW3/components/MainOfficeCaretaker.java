package components;

import java.util.ArrayList;

/**
 * Class will help to implement memento design pattern and will offer an option to
 * restore into last "state" made by the system. (could be due "clone" option that
 * helps clone a branch from the system by the user request)/
 *
 * @author Sagi Biran , ID: 205620859
 */
public class MainOfficeCaretaker {

    private ArrayList<MainOfficeMemento> mementoArrayList;

    /**
     * method will help add "states" to memento "queue" and use it by request
     * (ex: back to last state made by user.)
     * @param m object of last machine Object.
     */
    public void addMemento(MainOfficeMemento m) {
        if (mementoArrayList == null) {
            mementoArrayList = new ArrayList<>();
            mementoArrayList.add(m);
        }
        mementoArrayList.add(m);
    }

    /**
     * @return system into last sate with Memento object.
     */
    public MainOfficeMemento getMemento() {
        MainOfficeMemento m = mementoArrayList.get(mementoArrayList.size() - 1);
        mementoArrayList.remove(mementoArrayList.size() - 1);
        return m;
    }

    public boolean CaretakerIsEmpty() {
        return mementoArrayList.isEmpty();
    }
}
