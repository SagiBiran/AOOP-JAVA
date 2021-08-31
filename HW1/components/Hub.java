
package components;

import java.util.ArrayList;

/**
 * Describing "main branch" in entire system.
 * 
 * @author Sagi Biran , ID: 205620859
 */

public class Hub extends Branch {
	/**
	 * Collection of objects of all local branches.
	 */
	ArrayList<Branch> branches = new ArrayList<Branch>();
	/**
	 * Addition by be, static variable that help to truck which branch should each
	 * standard truck goes. it helps sorting center ensure that the trips to the
	 * branches are in the order of the branch numbers. for example: if one last
	 * truck went out to the branch, 2 then The current truck will leave for branch
	 * 3 and so on.
	 */
	static private int branchCounter = 0;

	/**
	 * Constructor creates an object for this class named "HUB".
	 */
	Hub() {
		super("Hub");
	}

	/**
	 * decided to no implement this method according to Michael Finkelstien
	 * Implementation is OPTIONAL.
	 */
	@Override
	public void collectPackage(Package p) {

	}

	/**
	 * decided to no implement this method according to Michael Finkelstien
	 * Implementation is OPTIONAL.
	 */
	@Override
	public void deliverPackage(Package p) {

	}

	/**
	 * Describing a "work unit" that is performed in one clock. For each standard
	 * sorting center (Hub) truck, if the truck is available, it is shipped to some
	 * local branch in a circular fashion. For the purpose of the drive, the truck
	 * will load all the packages waiting to be transferred to the branch to which
	 * she travels, as long as the weight of the packages is less than the maximum
	 * weight she can carry. (The weight of a small package is one kilogram, the
	 * rest of the packages contain a variable that describes its weight). The
	 * status of the packages and their history are updated accordingly, a message
	 * is printed stating that the truck is leaving for this branch, travel time is
	 * been randomized to integer value between 1 and 10. If the non-standard truck
	 * is available, it will be checked whether there is a non-standard package in
	 * the sorting center waiting to be collected And its dimensions fit the truck.
	 * If so, the truck will be sent to pick up the package. Collect the non
	 * standard package from a customer is carried out according to exactly the same
	 * rules as the rest of the packages, only by a non-standard truck.
	 */
	@Override
	public void work() {
		// StandardTrucks Sections
		StandardTruck tempStanTruck;
		for (int standTruck = 0; standTruck < getListTrucks().size() - 1; standTruck++) {
			tempStanTruck = (StandardTruck) getListTrucks().get(standTruck);

			if (tempStanTruck.getDestination() != null && tempStanTruck.getDestination() == tempStanTruck.getOrigin()) {
				if (tempStanTruck.getTimeLeft() == 0)
					System.out.println("StandardTruck " + tempStanTruck.getTruckID() + " arrived to HUB");
				if (!tempStanTruck.getPackages().isEmpty()) {
					for (Package pack : tempStanTruck.getPackages()) {
						this.getListPackages().add(pack);
						pack.setStatus(Status.HUB_STORAGE);
						pack.getTracking().add(new Tracking(MainOffice.getClock() + tempStanTruck.getTimeLeft(), tempStanTruck.getDestination(), pack.getStatus()));
					}
				}
				tempStanTruck.getPackages().clear();
				tempStanTruck.setAvailable(true);
			}

			if (tempStanTruck.getAvailable()) {
				System.out.println("StandardTruck " + tempStanTruck.getTruckID() + " loaded packages at HUB");
				tempStanTruck.setAvailable(false);
				tempStanTruck.setOrigin(this);
				tempStanTruck.setDestination(getBranches().get(branchCounter % getBranches().size()));
				for (Package pack : getListPackages()) {
					if ((pack instanceof SmallPackage || pack instanceof StandardPackage) && (pack.getDestinationAddress().getZip() == branchCounter % getBranches().size())
							&& (pack.getStatus() == Status.HUB_STORAGE)) {
						tempStanTruck.getPackages().add(pack);
						pack.setStatus(Status.BRANCH_TRANSPORT);
						pack.addTracking(tempStanTruck, pack.getStatus());
					}
				}
				// we must check
				if (!getListPackages().isEmpty() && !tempStanTruck.getPackages().isEmpty())
					getListPackages().clear();
				tempStanTruck.setTimeLeft(MainOffice.getRand().nextInt(3) + 1);
				System.out.println("StandardTruck " + tempStanTruck.getTruckID() + " is on it's way to Branch " + tempStanTruck.getDestination().getBranchId() + ", time to arrive: "
						+ tempStanTruck.getTimeLeft());
				branchCounter++;
			}
			// picked truck is busy..
			else {
				// truck came back from branch and should unload all packges etc...
				tempStanTruck.work();
			}
		}

		// NonStandardTruck Sections
		NonStandardTruck tempNonTruck = (NonStandardTruck) getListTrucks().get(getListTrucks().size() - 1);
		if (tempNonTruck.getAvailable()) {
			for (Package pack : getListPackages()) {
				if (pack instanceof NonStandardPackage) {
					if (pack.getStatus() == Status.COLLECTION) {
						tempNonTruck.setOrigin(this);
						tempNonTruck.collectPackage(pack);
						tempNonTruck.getPackages().add(pack);
						this.getListPackages().remove(pack);
						break;
					}
				}
			}
		}
		// NonStandardTruck is busy..
		else
			tempNonTruck.work();
	}

	/**
	 * @return a collection of objects from all local branches.
	 */
	public ArrayList<Branch> getBranches() {
		return branches;
	}
	
	@Override
	public String toString() {
		return "Hub [branches=" + branches + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof Hub)) {
			return false;
		}
		Hub other = (Hub) obj;
		if (getBranches() == null) {
			if (other.getBranches() != null) {
				return false;
			}
		} else if (!getBranches().equals(other.getBranches())) {
			return false;
		}
		return true;
	}

	public void setBranches(ArrayList<Branch> branches) {
		this.branches = branches;
	}

}
