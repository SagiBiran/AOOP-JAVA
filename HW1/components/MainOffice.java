package components;

import java.util.ArrayList;
import java.util.Random;

/**
 * An object of this class manages the entire system, operates a clock, the
 * branches and vehicles, creates the packages "Simulates customers" and
 * transfers them to the appropriate branches.
 * 
 * @author Sagi Biran , ID: 205620859
 */
public class MainOffice {
	/**
	 * Initialized to zero, each time preceded by one. Represents the amount of
	 * beats that have passed since the system was started.
	 */
	 static private int clock = 0;
	/**
	 * my addition - Static variable that will get "clock time" value when inserted
	 * by user. used in order to keep original time as it should be and manipulated
	 * time with new variable "playTime".
	 */
	static private int playTime;
	/**
	 * my addition - variable that will get "branches" integer value when first
	 * construct object of this class in main. will help to create exactly number of
	 * branches,trucks etc ..
	 */
	private int numberOFbranches;
	/**
	 * An object of a "Sorting Center", containing all the active branches in the
	 * game/
	 */
	private Hub hub;
	/**
	 * A collection of all the packages that exist in the system (including those
	 * that have already been provided to the customer).
	 */
	ArrayList<Package> packages = new ArrayList<Package>();
	/**
	 * my addition , random object will created for that to manipulate random tasks
	 * over the system.
	 */
	static private Random rand = new Random();

	/**
	 * A constructor who receives the number of branches that will be in the game
	 * and the number of vehicles per branch. The constructor creates a "Hub object"
	 * (Sorting Center).
	 * 
	 * @param trucksForBranch will add to "Hub" Standard trucks in the quantity in
	 *                        this parameter + 1 Non Standard Truck.
	 * @param branches        will add to "Hub" number of branches due this
	 *                        parameter and each branch will add trucks of type
	 *                        "VAN" due "trucksforBranch" parameter.
	 */
	public MainOffice(int branches, int trucksForBranch) {
		this.numberOFbranches = branches;
		hub = new Hub();
		for (int i = 0; i < trucksForBranch; i++)
			hub.listTrucks.add(new StandardTruck());
		hub.listTrucks.add(new NonStandardTruck());
		System.out.println();
		for (int i = 0; i < branches; i++) {
			getHub().branches.add(new Branch());
			for (int j = 0; j < trucksForBranch; j++) {
				getHub().branches.get(i).listTrucks.add(new Van());
			}
			System.out.println();
		}
	}

	/**
	 * A function takes as an argument the number of beats that the system will
	 * execute and activates the beats (tick) this number of times.
	 * 
	 * @param playTime number of beats that the system will execute.
	 */
	public void play(int playTime) {
		setPlayTime(playTime);
		System.out.println("========================== START ==========================");
		for (int i = 0; i < playTime; i++)
			tick();
		System.out.println("\n========================== STOP ==========================\n\n\n");
		printReport();
	}

	/**
	 * Prints a tracking report for all packages in the system. For each package
	 * prints the entire contents of the tracking collection of the package
	 */
	public void printReport() {
		for (int i = 0; i < packages.size(); i++) {
			packages.get(i).printTracking();
			System.out.println();
		}
	}

	/**
	 * Prints the value of the clock in "MM: SS" format.
	 * 
	 * @return value of the watch in "MM: SS" format.
	 */
	public String clockString() {
		return String.format("%02d:%02d", getClock() / 60, getClock() % 60);
	}

	/**
	 * describe "beat" in entire system. Each time this function is activated ("in
	 * each beat") the following actions are performed: The clock is printed and
	 * promoted at one. All branches, Sorting Center and vehicles perform one work
	 * unit. Every 5 beats a random new package is created. After the last beat, the
	 * job termination. message ("STOP") is printed and then a history report
	 * transfers is printed for all packages created during the run of system.
	 */
	public void tick() {
		System.out.println(clockString());
		if (getClock() % 5 == 0)
			addPackage();
		setClock(getClock() + 1);
		getHub().work();
		for (Branch branch : getHub().getBranches())
			branch.work();
	}

	/**
	 * Operated every 5 beats. Creates packages as follows: Lottery the type of
	 * Package (small / standard / non-standard), the priority, the addresses of the
	 * sender and recipient, as well as depending on the type of package that was
	 * being randomized, the additional data: For a small package the value of
	 * acknowledge is being randomized. For a standard package the weight is
	 * randomized -number between 1 and 10. For a non-standard package the values
	 * of: height (integer up to 400), width (integer up to 500) and length (integer
	 * up to 1000) are randomized, After the data is randomized, a suitable package
	 * is created and associated with the appropriate branch: small or standard
	 * packages are transferred to a local branch (whose number is the "zip" value
	 * at the sender's address), and non-standard packages are transferred to the
	 * Sorting Center.
	 */
	public void addPackage() {
		Package tempPackage = randomPackage();
		System.out.println("Creating " + tempPackage);
		getPackages().add(tempPackage);
		if (tempPackage instanceof NonStandardPackage)
			getHub().listPackages.add(tempPackage);
		else
			getHub().branches.get(tempPackage.getSenderAddress().getZip()).listPackages.add(tempPackage);
	}

	/*
	 * helper function will operated every 5 beat due "tick" method call. will
	 * generate a random type of package from existed and return this package.
	 */
	public Package randomPackage() {
		Random random = new Random();
		int randomChoice = random.nextInt(3);
		int randomPerBranches = random.nextInt(getNumberOFbranches());
		//
		int randomPerBranches2 = random.nextInt(getNumberOFbranches());
		int randomSenderStreet = random.nextInt(999999);
		if (randomChoice == 0) {
			boolean randomboolean = random.nextBoolean();
			return new SmallPackage(Priority.values()[randomChoice], new Address(randomPerBranches, randomSenderStreet), new Address(randomPerBranches2, randomSenderStreet), randomboolean);
		} else if (randomChoice == 1) {
			int randomWeight = random.nextInt(10) + 1;
			return new StandardPackage(Priority.values()[randomChoice], new Address(randomPerBranches, randomSenderStreet), new Address(randomPerBranches2, randomSenderStreet), randomWeight);
		} else {
			int randomWidth = random.nextInt(500) + 1;
			int randomLength = random.nextInt(1000) + 1;
			int randomHeight = random.nextInt(400) + 1;
			return new NonStandardPackage(Priority.values()[randomChoice], new Address(randomPerBranches, randomSenderStreet), new Address(randomPerBranches2, randomSenderStreet), randomWidth,
					randomLength, randomHeight);
		}

	}

	/**
	 * getters&setters for MainOffice class and object fields.
	 ********************************************************/

	/**
	 * 
	 * @return clock time for entire system.
	 */
	public static int getClock() {
		return clock;
	}

	public static void setClock(int newClock) {
		clock = newClock;
	}

	/**
	 * @return hub object that manages entire system.
	 */
	public Hub getHub() {
		return hub;
	}

	public void setHub(Hub hub) {
		this.hub = hub;
	}

	/**
	 * @return packages in entire system included packages that have been delivered
	 *         to costumers.
	 */
	public ArrayList<Package> getPackages() {
		return packages;
	}

	public void setPackages(ArrayList<Package> packages) {

		this.packages = packages;
	}

	/**
	 * @return number of branches that created.
	 */
	public int getNumberOFbranches() {
		return numberOFbranches;
	}

	public void setNumberOFbranches(int numBranches) {
		this.numberOFbranches = numBranches;
	}

	/**
	 * @see getClock()
	 */

	public static int getPlayTime() {
		return playTime;
	}

	public void setPlayTime(int playTime) {
		MainOffice.playTime = playTime;
	}

	/**
	 * @return random object (object of "Random" class) to manipulate random stuffs.
	 */
	public static Random getRand() {
		return rand;
	}

	public void setRand(Random rand) {
		MainOffice.rand = rand;
	}
	/*********************************************************************************************************************************************************************/

	@Override
	public String toString() {
		return "MainOffice [numberOFbranches=" + getNumberOFbranches() + ", hub=" + getHub() + ", packages=" + getPackages() + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof MainOffice)) {
			return false;
		}
		MainOffice other = (MainOffice) obj;
		if (getHub() == null) {
			if (other.getHub() != null) {
				return false;
			}
		} else if (!getHub().equals(other.getHub())) {
			return false;
		}
		if (getNumberOFbranches() != other.getNumberOFbranches()) {
			return false;
		}
		if (getPackages() == null) {
			if (other.getPackages() != null) {
				return false;
			}
		} else if (!getPackages().equals(other.getPackages())) {
			return false;
		}
		return true;
	}
}
