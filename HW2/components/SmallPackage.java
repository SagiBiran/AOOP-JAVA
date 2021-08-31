package components;

import java.awt.*;

/**
 * Represents small packages.
 *
 * @author Sagi Biran , ID: 205620859
 */

public class SmallPackage extends Package {
    /**
     * This field receives a true value if the package requires delivery
     * confirmation after delivery to the recipient.
     */
    private boolean acknowledge;


    /**
     * A Constructor who receives as arguments priority and addresses sends and
     * receives of the package, and if delivery confirmation is required.
     *
     * @param priority          for current package
     * @param senderAddress     for current package
     * @param destinationAdress for current package
     * @param acknowledge       whethera`zz this package included verification or not
     */
    public SmallPackage(Priority priority, Address senderAddress, Address destinationAdress, boolean acknowledge) {
        super(priority, senderAddress, destinationAdress);
        this.acknowledge = acknowledge;
        System.out.println("Creating " + this);

    }

    /**
     * @return whether a small package included delivery confirmation or not.
     */
    public boolean isAcknowledge() {
        return acknowledge;
    }


    public void setAcknowledge(boolean acknowledge) {
        this.acknowledge = acknowledge;
    }

    /**
     * helps print small package in appropriate way.
     */
    @Override
    public String toString() {
        return "SmallPackage [" + super.toString() + ", acknowledge=" + acknowledge + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof SmallPackage)) {
            return false;
        }
        SmallPackage other = (SmallPackage) obj;
        if (isAcknowledge() != other.isAcknowledge()) {
            return false;
        }
        return true;
    }

    /**
     * Method will draw lines between package sender(Address) and branch.
     * @param g graphic object
     */
    public void drawSenderLines(Graphics g) {
        g.setColor(new Color(0, 0, 155));
        g.drawLine((int) this.getSenderAddressX() + 15, (int) this.getSenderAddressY() + 15, (int) this.getSenderBranch().getX(), (int) this.getSenderBranch().getY());
    }

    /**
     * Method will draw lines from destination(Address) to exact branch.
     * @param g graphic object
     */
    public void drawDestinationLines(Graphics g) {
        g.setColor(new Color(0, 0, 155));
        g.drawLine((int) this.getDestinationAddressX() + 15, (int) this.getDestinationAddressY() + 15, (int) this.getDestBranch().getX(), (int) this.getDestBranch().getY());
    }

}
