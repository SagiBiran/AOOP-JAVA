package components;

/**
 * The class represents a record in the package transfer history. Each package
 * contains a collection of type records, with each change of status (and
 * location) of a package a new record is added to the collection. Each record
 * includes the time of creation of the record (by The clock of the program),
 * the point where the package is located and the status of the package.
 *
 * @author Sagi Biran , ID: 205620859
 */
public class Tracking {
    /**
     * Integer, value of the system clock as soon as the record is created.
     */
    public final int time;
    /**
     * Package location: (customer / branch / sorting center / transport vehicle).
     * When the package is with the customer (sender or recipient), this field value
     * is equal to null.
     */
    public final Node node;
    /**
     * Package status as soon as the record is created.
     */
    public final Status status;

    /**
     * Default constructor who takes as arguments time for creation the package,
     * node-location(Costumer) and status for Creation ("Creation").
     *
     * @param time   see time variable doc
     * @param node   see node variable doc
     * @param status see status variable doc
     */
    public Tracking(int time, Node node, Status status) {
        super();
        this.time = time;
        this.node = node;
        this.status = status;
    }

    /**
     * method helps to print each track in appropriate way while compare node
     * argument(location) value.
     */
    @Override
    public String toString() {
        String name = (node == null) ? "Customer" : node.getName();
        return time + ": " + name + ", status=" + status;
    }
}
