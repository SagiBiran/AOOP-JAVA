package components;

import program.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * This class will implement main panel for entire System
 *
 * @author Sagi Biran , ID: 205620859
 */

public class PostTrackingSysPanel extends JPanel implements ActionListener {

    private Game mainFrame;
    private JPanel panel;
    private JButton[] buttons;
    private String[] names = {"Create system", "Start", "Stop", "Resume", "All packages info", "Branch info"};
    private boolean isStarted = false;
    private MainOffice gameObj = null;
    private JScrollPane scrollPane;
    private boolean isTableVisible;

    /**
     * PostTrackingSysPanel default constructor that construct entire system depend on GUI
     *
     * @param frame game frame object
     */
    public PostTrackingSysPanel(Game frame) {
        this.mainFrame = frame;
        isTableVisible = false;
        setBackground(new Color(255, 255, 255));
        panel = new JPanel();
        panel.setLayout(new GridLayout(1, 10, 0, 0));
        panel.setBackground(new Color(192, 192, 192));
        buttons = new JButton[names.length];

        for (int i = 0; i < names.length; i++) {
            buttons[i] = new JButton(names[i]);
            buttons[i].addActionListener(this);
            buttons[i].setBackground(Color.lightGray);
            panel.add(buttons[i]);
        }

        setLayout(new BorderLayout());
        add(panel, BorderLayout.SOUTH);
    }

    /**
     * method will invoke button operation due user choose.
     *
     * @param e event that should be active.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buttons[0])
            add();
        else if (e.getSource() == buttons[1])
            start();
        else if (e.getSource() == buttons[2])
            stop();
        else if (e.getSource() == buttons[3])
            resume();
        else if (e.getSource() == buttons[4])
            packsInformation(-99);
        else if (e.getSource() == buttons[5])
            if (gameObj.getBranches().size() != 0) {
                branchesInformation();
            }
    }

    /**
     * Method will display system dialog panel.
     */
    public void add() {
        PostSystDialog dialog = new PostSystDialog(mainFrame, this, "Create post system");
        dialog.setVisible(true);
    }

    /**
     * Method will invoke start button operation.
     */
    public void start() {
        if (gameObj == null || isStarted) return;
        isStarted = true;
        Thread t = new Thread(gameObj);
        t.start();
    }

    /**
     * Method will invoke stop button operation.
     */
    public void stop() {
        if (gameObj == null) return;
        gameObj.setSuspend();
    }

    /**
     * Method will invoke resume button operation.
     */
    public void resume() {
        if (gameObj == null) return;
        gameObj.setResume();
    }

    /**
     * Method will display all package info.
     * @param branch branch that belongs to package.
     */
    public void packsInformation(int branch) {
        if (gameObj == null) return;
        if (!isTableVisible) {
            int i = 0;
            String[] columnNames = {"Package ID", "Sender", "Destination", "Priority", "Status"};

            ArrayList<Package> packeges;
            if (branch == -99) {
                packeges = gameObj.getPackages();
            } else {
                packeges = gameObj.getBranches().get(branch).getPackages();
            }
            String[][] data = new String[packeges.size()][columnNames.length];

            for (Package p : packeges) {
                data[i][0] = "" + p.getPackageID();
                data[i][1] = "" + p.getSenderAddress().getZip();
                data[i][2] = "" + p.getDestinationAddress().getZip();
                data[i][3] = "" + p.getPriority();
                data[i][4] = "" + p.getStatus();
                i++;
            }

            JTable tempTable = new JTable(data, columnNames);
            scrollPane = new JScrollPane(tempTable);
            scrollPane.setSize(450, tempTable.getRowHeight() * (packeges.size()) + 24);
            add(scrollPane, BorderLayout.CENTER);
            isTableVisible = true;
        } else
            isTableVisible = false;

        scrollPane.setVisible(isTableVisible);
        repaint();
    }

    /**
     * Method will show all branch information.
     */
    public void branchesInformation() {
        JPanel tempPanel = new JPanel(new GridBagLayout());
        JComboBox tempCombo = new JComboBox();
        String[] options = {"Cancel", "OK"};
        tempCombo.setEditable(true);
        for (int i = 0; i < gameObj.getBranches().size(); i++) {
            tempCombo.addItem(gameObj.getBranches().get(i).getBranchName());
        }
        JOptionPane.showOptionDialog(null, tempCombo,
                "Choose Branch", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options,
                null);
        tempPanel.add(tempCombo);
        packsInformation(tempCombo.getSelectedIndex());
    }

    /**
     * Method will create entire system as requested.
     */
    public void CreateSystem(int branches, int trucks, int packages) {
        if (gameObj != null)
            gameObj.setStop();
        gameObj = new MainOffice(branches, trucks, packages);
        gameObj.setPanel(this);
        isStarted = false;
        repaint();
    }

    /**
     * Important method that will paint every components in the system,
     * this method will used many times during running of the system.
     *
     * @param g graphic object
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int spacious = 15;
        if (gameObj == null) return;
        for (int i = 1; i < gameObj.getBranches().size(); i++) {
            gameObj.getBranches().get(i).drawLines(g, i);
        }
        for (int i = 0; i < gameObj.getPackages().size(); i++) {
            if (gameObj.getPackages().get(i) instanceof NonStandardPackage) {
                ((NonStandardPackage) gameObj.getPackages().get(i)).senderLineDrawing(g, gameObj.getBranches().get(0), spacious);
            } else {
                gameObj.getPackages().get(i).drawSenderLines(g, spacious);
            }
            gameObj.getPackages().get(i).drawDestinationLines(g, spacious);
            gameObj.getPackages().get(i).drawPackageSender(g);
            gameObj.getPackages().get(i).drawPackageDestination(g);
        }
        for (int j = 0; j < gameObj.getBranches().size(); j++) {
            gameObj.getBranches().get(j).drawBranch(g);
        }
        for (int f = 0; f < gameObj.getTrucks().size(); f++) {
            Truck t = gameObj.getTrucks().get(f);
            if (t instanceof Van && !t.isAvailable()) {
                ((Van) t).drawVan(g, spacious);
            }
            if (t instanceof NonStandardTruck && !t.isAvailable()) {
                ((NonStandardTruck) t).drawNonStandardTruck(g, spacious);
            }
            if (t instanceof StandardTruck && !t.isAvailable()) {
                ((StandardTruck) t).drawStandardTruck(g, spacious);
            }
        }
    }
}

