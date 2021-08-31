package components;

/**
 * Interface that represents the location of a package, can refer to branches
 * and trucks (all points where the package can be located In the various stages
 * of its transfer).
 * 
 * @author Sagi Biran , ID: 205620859
 */
public interface Node {
	/**
	 * A method that handles the collection / receipt of a package by the
	 * implementing class.
	 * 
	 * @param p pack to collect.
	 */
	void collectPackage(Package p);

	/**
	 * A method that handles the delivery of the package to the next person in the
	 * transfer chain.
	 * @param p package to deliver.
	 */
	void deliverPackage(Package p);

	/**
	 * A method that performs a ONE work unit.
	 */
	void work();
}
