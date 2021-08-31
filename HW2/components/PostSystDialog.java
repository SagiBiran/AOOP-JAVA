package components;

import program.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 * This class will implement main system dialog for entire System
 *
 * @author Sagi Biran , ID: 205620859
 */


public class PostSystDialog extends JDialog implements ActionListener {

    private static final long serialVersionUID = 1L;
    private JPanel panel_1, panel_2;
    private JButton ok_button, cancel_button;
    private JLabel branchesAmount_lbl, TracksPerBranch_lbl, packagesAmount_lbl;
    private JSlider branchesAmount_sl, TracksPerBranch_sl, packagesAmount_sl;
    private PostTrackingSysPanel ptSystemPanel;

    /**
     * CreatePostSystDialog default constructor.
     * @param parent main panel.
     * @param panel  system Panel.
     * @param title  title for panel.
     */
    public PostSystDialog(Game parent, PostTrackingSysPanel panel, String title) {
        super((Game) parent, title, true);
        ptSystemPanel = panel;

        setSize(600, 400);
        setBackground(new Color(238, 238, 238));
        panel_1 = new JPanel();
        panel_2 = new JPanel();

        panel_1.setLayout(new GridLayout(6, 6));

        branchesAmount_lbl = new JLabel("Number of branches", JLabel.CENTER);
        panel_1.add(branchesAmount_lbl);
        branchesAmount_sl = new JSlider(1, 10);
        branchesAmount_sl.setMajorTickSpacing(1);
        branchesAmount_sl.setMinorTickSpacing(1);
        branchesAmount_sl.setPaintTicks(true);
        branchesAmount_sl.setPaintLabels(true);
        panel_1.add(branchesAmount_sl);

        TracksPerBranch_lbl = new JLabel("Number of trucks per branch", JLabel.CENTER);
        panel_1.add(TracksPerBranch_lbl);
        TracksPerBranch_sl = new JSlider(1, 10);
        TracksPerBranch_sl.setMajorTickSpacing(1);
        TracksPerBranch_sl.setMinorTickSpacing(1);
        TracksPerBranch_sl.setPaintTicks(true);
        TracksPerBranch_sl.setPaintLabels(true);
        panel_1.add(TracksPerBranch_sl);

        packagesAmount_lbl = new JLabel("Number of packages", JLabel.CENTER);
        panel_1.add(packagesAmount_lbl);
        packagesAmount_sl = new JSlider(2, 20);
        packagesAmount_sl.setMajorTickSpacing(2);
        packagesAmount_sl.setMinorTickSpacing(2);
        packagesAmount_sl.setPaintTicks(true);
        packagesAmount_sl.setPaintLabels(true);
        panel_1.add(packagesAmount_sl);

        panel_2.setLayout(new GridLayout(1, 2, 5, 5));
        ok_button = new JButton("OK");
        ok_button.addActionListener(this);
        ok_button.setBackground(Color.lightGray);
        panel_2.add(ok_button);
        cancel_button = new JButton("Cancel");
        cancel_button.addActionListener(this);
        cancel_button.setBackground(Color.lightGray);
        panel_2.add(cancel_button);

        setLayout(new BorderLayout());
        add("North", panel_1);
        add("South", panel_2);
    }

    /**
     *method will invoke buttons(ok or cancel) operation due user choose.
     * @param e event that should be active.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == ok_button) {
            ptSystemPanel.CreateSystem(branchesAmount_sl.getValue(), TracksPerBranch_sl.getValue(), packagesAmount_sl.getValue());
            setVisible(false);
        } else
            setVisible(false);
    }
}