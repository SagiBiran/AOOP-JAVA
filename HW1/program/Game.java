package program;

import components.MainOffice;

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

public class Game {
	/**
	 * inside main class, we will create an object of "MainOffice" this object class
	 * manages the entire system ,means: operates a clock, the branches and
	 * vehicles, creates the packages "Simulates customers" and transfers them to
	 * the appropriate branches. for this object I've used "MainOffice's parameter
	 * constructor" that will take 5 as branches amount , and 4 as trucks number for
	 * each branch created. immediately after that we will lunch "play" method that
	 * takes as an argument the number of beats that the system will execute and
	 * activates the beat (tick) this number of times.
	 * 
	 * @param the command line arguments
	 * 
	 */
	public static void main(String[] args) {
		MainOffice game = new MainOffice(5, 4);
		game.play(60);
	}

}
