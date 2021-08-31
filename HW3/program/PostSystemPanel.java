package program;

import java.awt.*;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import components.*;
import components.Package;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * This class will implement main panel for entire System
 *
 * @author Sagi Biran , ID: 205620859
 */
public class PostSystemPanel extends JPanel implements ActionListener {
    private static final long serialVersionUID = 1L;
    private Main frame;
    private JPanel p1;
    private JButton[] b_num;
    private String[] names = {"Create system", "Start", "Stop", "Resume", "All packages info", "Branch info", "CloneBranch", "Restore", "Report"};
    private JScrollPane scrollPane;
    private boolean isTableVisible = false;
    private boolean isTable2Visible = false;
    private int colorInd = 0;
    private boolean started = false;
    private MainOffice game = null;
    private int packagesNumber;
    private int branchesNumber;
    private int trucksNumber;
    //Memento
    MainOfficeCaretaker caretaker = new MainOfficeCaretaker();

    /**
     * PostTrackingSysPanel default constructor that construct entire system depend on GUI
     *
     * @param f game frame object
     */
    public PostSystemPanel(Main f) {
        frame = f;
        isTableVisible = false;
        setBackground(new Color(255, 255, 255));
        p1 = new JPanel();
        p1.setLayout(new GridLayout(1, 7, 0, 0));
        p1.setBackground(new Color(0, 150, 255));
        b_num = new JButton[names.length];
        for (int i = 0; i < names.length; i++) {
            b_num[i] = new JButton(names[i]);
            b_num[i].addActionListener(this);
            b_num[i].setBackground(Color.lightGray);
            p1.add(b_num[i]);
        }
        setLayout(new BorderLayout());
        add("South", p1);
    }

    /**
     * Method will create entire system as requested.
     */
    public void createNewPostSystem(int branches, int trucks, int packages) {
        if (started) return;
        game = MainOffice.getInstance(branches, trucks, this, packages);
        packagesNumber = packages;
        trucksNumber = trucks;
        branchesNumber = branches;
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
        if (game == null) return;
        Hub hub = MainOffice.getHub();
        ArrayList<Branch> branches = hub.getBranches();
        int offset = 403 / (branchesNumber - 1);
        int offset2 = 140 / (branchesNumber - 1);
        int y = 100;
        int y2 = 246;
        g.setColor(new Color(0, 102, 0));
        g.fillRect(1120, 216, 40, 200);
        for (Branch br : branches) {
            br.paintComponent(g, y, y2);
            y += offset;
            y2 += offset2;
        }
        int x = 150;
        int offset3 = (1154 - 300) / (packagesNumber - 1);
        for (Package p : game.getPackages()) {
            p.paintComponent(g, x, offset);
            x += offset3;
        }
        for (Branch br : branches) {
            for (Truck tr : br.getTrucks()) {
                tr.paintComponent(g);
            }
        }
        for (Truck tr : hub.getTrucks()) {
            tr.paintComponent(g);
        }
    }

    /**
     * Method will display system dialog panel.
     */
    public void add() {
        CreatePostSystemlDialog dial = new CreatePostSystemlDialog(frame, this, "Create post system");
        dial.setVisible(true);
    }

    /**
     * Method will invoke start button operation.
     */
    public void start() {
        if (game == null || started) return;
        Thread t = new Thread(game);
        started = true;
        t.start();
    }

    /**
     * Method will invoke resume button operation.
     */
    public void resume() {
        if (game == null) return;
        game.setResume();
    }

    /**
     * Method will invoke stop button operation.
     */
    public void stop() {
        if (game == null) return;
        game.setSuspend();
    }

    /**
     * Method will show all branch information.
     */
    public void branchInfo() {
        if (game == null || !started) return;

        if (scrollPane != null) scrollPane.setVisible(false);
        isTableVisible = false;
        isTable2Visible = false;
        String[] branchesStrs = new String[MainOffice.getHub().getBranches().size() + 1];
        branchesStrs[0] = "Sorting center";
        for (int i = 1; i < branchesStrs.length; i++)
            branchesStrs[i] = "Branch " + i;
        JComboBox cb = new JComboBox(branchesStrs);
        String[] options = {"OK", "Cancel"};
        String title = "Choose branch";
        int selection = JOptionPane.showOptionDialog(null, cb, title,
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options,
                options[0]);

        if (selection == 1) return;
        //System.out.println(cb.getSelectedIndex());
        if (!isTable2Visible) {
            int i = 0;
            String[] columnNames = {"Package ID", "Sender", "Destination", "Priority", "Staus"};
            List<Package> packages = null;
            int size = 0;
            if (cb.getSelectedIndex() == 0) {
                packages = MainOffice.getHub().getPackages();
                size = packages.size();
            } else {
                packages = MainOffice.getHub().getBranches().get(cb.getSelectedIndex() - 1).getPackages();
                size = packages.size();
                int diff = 0;
                for (Package p : packages) {
                    if (p.getStatus() == Status.BRANCH_STORAGE) {
                        diff++;
                    }
                }
                size = size - diff / 2;
            }
            String[][] data = new String[size][columnNames.length];
            for (Package p : packages) {
                boolean flag = false;
                for (int j = 0; j < i; j++)
                    if (data[j][0].equals("" + p.getPackageID())) {
                        flag = true;
                        break;
                    }
                if (flag) continue;
                data[i][0] = "" + p.getPackageID();
                data[i][1] = "" + p.getSenderAddress();
                data[i][2] = "" + p.getDestinationAddress();
                data[i][3] = "" + p.getPriority();
                data[i][4] = "" + p.getStatus();
                i++;
            }
            JTable table = new JTable(data, columnNames);
            scrollPane = new JScrollPane(table);
            scrollPane.setSize(450, table.getRowHeight() * (size) + 24);
            add(scrollPane, BorderLayout.CENTER);
            isTable2Visible = true;
        } else
            isTable2Visible = false;

        scrollPane.setVisible(isTable2Visible);
        repaint();
    }

    /**
     * method will invoke button operation due user choose.
     *
     * @param e event that should be active.
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == b_num[0])
            add();
        else if (e.getSource() == b_num[1])
            start();
        else if (e.getSource() == b_num[2])
            stop();
        else if (e.getSource() == b_num[3])
            resume();
        else if (e.getSource() == b_num[4])
            info();
        else if (e.getSource() == b_num[5])
            branchInfo();
        else if (e.getSource() == b_num[6]) {
            try {
                cloneBranch();
            } catch (CloneNotSupportedException cloneNotSupportedException) {
                cloneNotSupportedException.printStackTrace();
            }
        } else if (e.getSource() == b_num[7])
            restore();
        else if (e.getSource() == b_num[8])
            report();
    }

    public void info() {
        if (game == null || !started) return;
        if (isTable2Visible) {
            scrollPane.setVisible(false);
            isTable2Visible = false;
        }
        if (!isTableVisible) {
            int i = 0;
            String[] columnNames = {"Package ID", "Sender", "Destination", "Priority", "Staus"};
            ArrayList<Package> packages = game.getPackages();
            String[][] data = new String[packages.size()][columnNames.length];
            for (Package p : packages) {
                data[i][0] = "" + p.getPackageID();
                data[i][1] = "" + p.getSenderAddress();
                data[i][2] = "" + p.getDestinationAddress();
                data[i][3] = "" + p.getPriority();
                data[i][4] = "" + p.getStatus();
                i++;
            }
            JTable table = new JTable(data, columnNames);
            scrollPane = new JScrollPane(table);
            scrollPane.setSize(450, table.getRowHeight() * (packages.size()) + 24);
            add(scrollPane, BorderLayout.CENTER);
            isTableVisible = true;
        } else
            isTableVisible = false;

        scrollPane.setVisible(isTableVisible);
        repaint();
    }

    public void setColorIndex(int ind) {
        this.colorInd = ind;
        repaint();
    }

    public void setBackgr(int num) {
        switch (num) {
            case 0 -> setBackground(new Color(255, 255, 255));
            case 1 -> setBackground(new Color(0, 150, 255));
        }
        repaint();
    }

    private void restore() {
        if (game == null || !started) return;

        if (scrollPane != null) scrollPane.setVisible(false);
        String[] options = {"Yes", "Cancel"};

        if (caretaker.CaretakerIsEmpty()) {
            JOptionPane.showMessageDialog(new JFrame(), "No branch added.");
            return;
        }

        String title = "Restore system";
        int selection = JOptionPane.showOptionDialog(null, "Do you Want to restore the system?", "Restore system", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options,
                options[0]);
        if (selection == 0) {
            this.branchesNumber--;
            this.restore(caretaker.getMemento());
            System.out.println("####################################-Restoration has been made! system returned into internal state!(before cloning branch)-####################################");
        }
    }

    private void cloneBranch() throws CloneNotSupportedException {
        if (game == null || !started) return;

        if (scrollPane != null) scrollPane.setVisible(false);
        String[] branchesStrs = new String[MainOffice.getHub().getBranches().size()];
        for (int i = 0; i < branchesStrs.length; i++)
            branchesStrs[i] = "Branch " + (i + 1);
        JComboBox cb = new JComboBox(branchesStrs);
        String[] options = {"OK", "Cancel"};
        String title = "Choose branch";
        int selection = JOptionPane.showOptionDialog(null, cb, title,
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options,
                options[0]);

        if (selection == 1) return;

        this.caretaker.addMemento(createMemento());

        Branch clone_branch = MainOffice.getBranch("Branch " + (selection + 1));
        Branch.setCounter(Branch.getCounter() + 1);
        clone_branch.setBranchName("Branch " + clone_branch.getBranchId());
        clone_branch.setListPackages(new ArrayList<>());

        this.branchesNumber++;
        int truck_num = clone_branch.getTrucks().size();
        clone_branch.getTrucks().clear();
        clone_branch.getPackages().clear();
        for (int j = 0; j < truck_num; j++) {
            clone_branch.addTruck(new Van());
        }

        MainOffice.setBranchMap(clone_branch.getName(), clone_branch);
        MainOffice.getHub().add_branch(clone_branch);

        Thread branch = new Thread(MainOffice.getHub().getBranches().get(MainOffice.getHub().getBranches().size() - 1));
        for (Truck t : MainOffice.getHub().getBranches().get(MainOffice.getHub().getBranches().size() - 1).getTrucks()) {
            Thread trackThread = new Thread(t);
            trackThread.start();
        }
        branch.start();
    }

    private void report() {
        try {
            // file path to  open tracking.txt file (Once we click on "REPORT" button).
            File u = new File(System.getProperty("user.dir") + "/tracking.txt");
            Desktop d = Desktop.getDesktop();
            d.open(u);
        } catch (IOException event) {
            JOptionPane.showMessageDialog(this, event.getMessage());
        }
    }

    public MainOfficeMemento createMemento() {
        return new MainOfficeMemento(MainOffice.getClock(), MainOffice.getHub(), MainOffice.getInstance().getPackages(), MainOffice.getInstance().getCustomers(), MainOffice.getInstance().getPanel(), MainOffice.getBranchMap());
    }

    public void restore(MainOfficeMemento m) {
        MainOffice.setClock(MainOfficeMemento.getClock());
        MainOffice.setHub(MainOfficeMemento.getHub());
        MainOffice.setPackages(MainOfficeMemento.getPackages());
        game.setCustomers(m.getCustomers());
        game.setPanel(m.getPanel());
        MainOffice.setBranchMap(MainOfficeMemento.getBranchMap());
        MainOffice.getHub().getBranches().remove(MainOffice.getHub().getBranches().size()-1);
    }
}