package program;

import javax.swing.*;

/**
 * This Class Main will simulates the work of a delivery company. The company
 * has several branches, a sorting center and a head office. Distribution
 * vehicles are associated with each branch - The Branches have vehicles of the
 * Van type and the Sorting Center has vehicles of the "standard truck" type and
 * one other vehicle of the "non-standard truck" type. In favor of carrying out
 * exceptional cargo transports. When there is a delivery to be made, a system
 * creates a "package" and associates it with the appropriate branch.
 *
 * @author Sagi Biran , ID: 205620859
 */
public class Main extends JFrame {
    private static final long serialVersionUID = 1L;
    private PostSystemPanel panel;

    /**
     * main method of entire system that should create panels,main office object
     * and invoke entire system as expected.
     */
    public static void main(String[] args) {
        Main fr = new Main();
        fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fr.setSize(1200, 700);
        fr.setVisible(true);
    }

    /**
     * default construct to invoke entire system
     */
    public Main() {
        super("Post tracking system");
        panel = new PostSystemPanel(this);
        add(panel);
        panel.setVisible(true);
    }
}
