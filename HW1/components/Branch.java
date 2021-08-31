package components;

import java.util.ArrayList;

/**
 * Describes a local branch. Keeps a list of packages stored at the branch or
 * intended for collection from the sender's address To this branch, and a list
 * of vehicles that collect the packages from the sending customers and deliver
 * the packages to the receiving customers.
 * 
 * @author Sagi Biran , ID: 205620859
 */

public class Branch implements Node {
	/**
	 * Continuous number , starts from -1 to separate sorting center ("hub" , branch
	 * number -1) from other local branches (0,1,2.....).
	 */

	private static int globalID = -1;
	/*
	 * static global id variable that helps set proper id for each package that
	 * created and make sure no id is repeated.
	 */
	private final int branchId;
	/*
	 * name of branch , mainly used for sorting center ("hub",once operate entire
	 * system.)
	 */
	private String branchName;
	/**
	 * A collection of vehicles belonging to this branch.
	 */
	ArrayList<Truck> listTrucks = new ArrayList<Truck>();
	/**
	 * A collection of packages that are in the branch and packages that must be
	 * collected that are shipped by senders to this branch.
	 */
	ArrayList<Package> listPackages = new ArrayList<Package>();

	/*
	 * Default constructor, calculates the id number of the branch and creates the
	 * name of the branch.
	 */
	public Branch() {
		branchId = globalID++;
		if (getBranchId() == -1)
			branchName = "HUB";
		else
			branchName = "Branch " + Integer.toString(branchId);
		System.out.println("Creating Branch " + getBranchId() + ", branch name:" + getBranchName() + ", packages:" + listPackages.size() + ", trucks:" + listPackages.size());
	};

	/**
	 * A constructor who gets a branch name, calculates the id number of the branch.
	 * 
	 * @param branchName
	 */
	public Branch(String branchName) {
		branchId = globalID++;
		this.branchName = branchName;
		System.out.println("Creating Branch " + getBranchId() + ", branch name:" + getBranchName() + ", packages:" + listPackages.size() + ", trucks:" + listPackages.size());

	}

	/**
	 * Collect package from Van and store it in branch packages collection.
	 */
	@Override
	public void collectPackage(Package p) {
		this.getListPackages().add(p);
		p.setStatus(Status.BRANCH_STORAGE);
		p.addTracking(this, Status.BRANCH_STORAGE);
	}

	/**
	 * decided to no implement this method according to Michael Finkelstien
	 * Implementation is OPTIONAL.
	 */
	public void deliverPackage(Package p) {

	}

	/**
	 * • A work unit performed by a branch every time the system clock beats. • For
	 * each package that is in the branch, if it is in the waiting status for
	 * collection from a customer, an attempt is made to collect - if There is a
	 * vehicle available, he goes out to pick up the package, a random calculation
	 * is made that describes the time of arrival at the customer and the status of
	 * the vehicle goes to status - not available. • Same as for any package waiting
	 * to be distributed, if there is a vehicle available, it is sent to deliver the
	 * package. In both cases, the status and history of the package are updated and
	 * appropriate prints are created.
	 */
	@Override
	public void work() {
		Van tempVan;
		for (Truck truck : getListTrucks()) {
			tempVan = (Van) truck;
			if (tempVan.getAvailable()) {
				if (!getListPackages().isEmpty()) {
					for (Package pack : getListPackages()) {
						if (pack.getStatus() == Status.COLLECTION) {
							tempVan.setOrigin(this);
							tempVan.collectPackage(pack);
							tempVan.getPackages().add(pack);
							this.getListPackages().remove(pack);
							break;
						} else if (pack.getStatus() == Status.DELIVERY) {
							tempVan.setOrigin(this);
							tempVan.deliverPackage(pack);
							tempVan.getPackages().add(pack);
							this.getListPackages().remove(pack);
							break;
						}
						// means no job for van at this moment
						else
							return;
					}
				}
			}
			// van is not available
			else
				tempVan.work();
		}
	}

	/********************************************************
	 * getters&setters
	 ********************************************************/

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public static int getGlobalID() {
		return globalID;
	}

	public static void setGlobalID(int globalID) {
		Branch.globalID = globalID;
	}

	public ArrayList<Truck> getListTrucks() {
		return listTrucks;
	}

	public void setListTrucks(ArrayList<Truck> listTrucks) {
		this.listTrucks = listTrucks;
	}

	public ArrayList<Package> getListPackages() {
		return listPackages;
	}

	public void setListPackages(ArrayList<Package> listPackages) {
		this.listPackages = listPackages;
	}

	public int getBranchId() {
		return branchId;
	}

	/********************************************************************************************************************************/
	/**
	 * helps print branch name in appropriate way.
	 */
	@Override
	public String toString() {
		return "Branch" + getBranchId();
	}

	@Override

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Branch)) {
			return false;
		}
		Branch other = (Branch) obj;
		if (getBranchId() != other.getBranchId()) {
			return false;
		}
		if (getBranchName() == null) {
			if (other.getBranchName() != null) {
				return false;
			}
		} else if (!getBranchName().equals(other.getBranchName())) {
			return false;
		}
		if (getListPackages() == null) {
			if (other.getListPackages() != null) {
				return false;
			}
		} else if (!getListPackages().equals(other.getListPackages())) {
			return false;
		}
		if (getListTrucks() == null) {
			if (other.getListTrucks() != null) {
				return false;
			}
		} else if (!getListTrucks().equals(other.getListTrucks())) {
			return false;
		}
		return true;
	}

}
