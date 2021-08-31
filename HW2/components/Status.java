package components;

/**
 * ENUM which includes a list of statuses corresponding to the shipping stages.
 * Enum values explanation:
 * "COLLECTION" - This status a package receives when a transport vehicle is sent to pick it up from the sender's address.
 * "CREATION" Initial status of each created package.
 * "BRANCH_STORAGE" - The package collected from a customer has arrived at the sender's local branch.
 * "HUB_TRANSPORT" - The package is on the way from the local branch to the sorting center.
 * "HUB_STORAGE" - The package has arrived at the HUB and is awaiting transfer to the destination branch.
 * "BRANCH_TRANSPORT" - The package is on its way from the sorting center to the destination branch..
 * "DELIVERY" - The package has arrived at the destination branch and is ready for delivery to the end customer.
 * "DISTRIBUTION" - The package on the way from the destination branch to the end customer.
 * "DELIVERED" - The package was delivered to the final customer.
 *
 * @author Sagi Biran , ID: 205620859
 */

public enum Status {
    CREATION, COLLECTION, BRANCH_STORAGE, HUB_TRANSPORT, HUB_STORAGE, BRANCH_TRANSPORT, DELIVERY, DISTRIBUTION, DELIVERED

}
