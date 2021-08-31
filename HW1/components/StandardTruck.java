package components;

/**
 * A vehicle for transferring packages from the sorting center to the branches
 * and back. All vehicles of this type are in the sorting center.
 * 
 * @author Sagi Biran , ID: 205620859
 */
public class StandardTruck extends Truck {
	/**
	 * Maximum weight that a vehicle can carry.
	 */
	private int maxWeight;
	/**
	 * Target branch / sorting center.
	 */
	private Branch destination;

	/**
	 * A default constructor that produces an object with a license plate number and
	 * a vehicle model at random.
	 */
	public StandardTruck() {
		super();
		this.maxWeight = (MainOffice.getRand().nextInt(3) + 1) * 100;
		System.out.println(toString());
	}

	/**
	 * Builder that accepts as arguments: license plate number, vehicle model and
	 * maximum weight.
	 * 
	 * @param licensePlate
	 * @param truckModel
	 * @param maxWeight
	 */
	public StandardTruck(String licensePlate, String truckModel, int maxWeight) {
		super(licensePlate, truckModel);
		this.maxWeight = maxWeight;
	}

	/**
	 * method will help standard truck to collect packs from branch into HUB
	 */
	@Override
	public void collectPackage(Package p) {
		p.setStatus(Status.HUB_TRANSPORT);
		p.addTracking(this, p.getStatus());
		this.getPackages().add(p);
	}

	/**
	 * method will help to deliver packs from trucks into branch.
	 */
	@Override
	public void deliverPackage(Package p) {
		p.setStatus(Status.DELIVERY);
		p.addTracking(this.getDestination(), p.getStatus());
	}

	/**
	 * according to the following requirements: An available vehicle does nothing. A
	 * vehicle that is in drive already, will reduce his time for arriving by one
	 * then if after reduction the value in time is equal to zero, so the trip ended
	 * and a vehicle reached its destination and the vehicle must transfer all the
	 * packages inside it to the point it has reached (local branch or sorting
	 * center), while updating the status and history of packages. If the trip ended
	 * at the sorting center, then the vehicle must switch to "free" mode.
	 * Otherwise, the trip ended at the local branch, the vehicle has to load the
	 * packages from this branch and take them to the sorting center. Also in this
	 * case the status and history of packages is updated, new time is set at random
	 * for the vehicle. A message will be printed that the vehicle has left back
	 * from a local branch to a sorting center.
	 */
	@Override
	public void work() {
		if (this.getAvailable())
			return;
		this.setTimeLeft(getTimeLeft() - 1);
		if (getTimeLeft() == 0) {
			System.out.println("StandardTruck " + this.getTruckID() + " arrived to Branch " + this.getDestination().getBranchId());
			System.out.println("StandardTruck " + this.getTruckID() + " unloaded packages at Branch " + this.getDestination().getBranchId());
			if (!this.getPackages().isEmpty()) {
				for (Package pack : this.getPackages()) {
					if (pack.getStatus() == Status.BRANCH_TRANSPORT) {
						this.deliverPackage(pack);
						this.getDestination().getListPackages().add(pack);
					}
				}
				this.getPackages().clear();
			}
			System.out.println("StandardTruck " + this.getTruckID() + " loaded packages at Branch " + this.getDestination().getBranchId());
			// standard truck should deliver packages from branch to HUB
			boolean flag = false;
			if (!this.getDestination().getListPackages().isEmpty()) {
				for (Package pack : this.getDestination().getListPackages()) {
					if (pack.getStatus() == Status.BRANCH_STORAGE) {
						this.collectPackage(pack);
						flag = true;
					}
				}
				if (flag)
					this.getDestination().getListPackages().clear();
			}
			// standard truck will delivers all packages to branch. //packages should be
			// with BRANCH_TRANSPORT status
			this.setTimeLeft(MainOffice.getRand().nextInt(3) + 1);
			// to indicate that truck finished his work.
			this.setDestination(this.getOrigin());
			System.out.println("StandardTruck " + this.getTruckID() + " is on it's way to the HUB, " + "time to arrive: " + this.getTimeLeft());
		}
	}

	/********************************************************
	 * getters&setters
	 ********************************************************/

	public int getMaxWeight() {
		return maxWeight;
	}

	public void setMaxWeight(int maxWeight) {
		this.maxWeight = maxWeight;
	}

	public Branch getDestination() {
		return destination;
	}

	public void setDestination(Branch destination) {
		this.destination = destination;
	}

	/********************************************************************************************************************************/
	/**
	 * helps print standard truck in aprropriate way.
	 */
	public String toString() {
		return "Creating StandartTruck" + super.toString() + " ,maxWeight=" + getMaxWeight() + "]";
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof StandardTruck)) {
			return false;
		}
		StandardTruck other = (StandardTruck) obj;
		if (getDestination() == null) {
			if (other.getDestination() != null) {
				return false;
			}
		} else if (!getDestination().equals(other.getDestination())) {
			return false;
		}
		if (getMaxWeight() != other.getMaxWeight()) {
			return false;
		}
		return true;
	}

}
