package program;

import components.PostTrackingSysPanel;

import javax.swing.*;

/**
 * This Class game will simulates the work of a delivery company. The company
 * has several branches, a sorting center and a head office. Distribution
 * vehicles are associated with each branch - The Branches have vehicles of the
 * Van type and the Sorting Center has vehicles of the "standard truck" type and
 * one other vehicle of the "non-standard truck" type. In favor of carrying out
 * exceptional cargo transports. When there is a delivery to be made, a system
 * creates a "package" and associates it with the appropriate branch.
 *
 * @author Sagi Biran , ID: 205620859
 */
public class Game extends JFrame{

    /**
     * main method of entire system that should create panels,main office object
     * and invoke entire system as expected.
     */
    private PostTrackingSysPanel panel;

    public static void main(String[] args) {
        Game mainFrame = new Game();
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1200, 700);
        mainFrame.setVisible(true);
    }

    /**
     * default construct to invoke entire system
     */
    public Game() {
        super("Port tracking system");
        panel = new PostTrackingSysPanel(this);
        add(panel);
        panel.setVisible(true);
    }

}
