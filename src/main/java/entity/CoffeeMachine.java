package entity;

import controller.BeverageController;
import exceptions.BeverageNotFoundException;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * This is the root level class, which provides the interface to interact with the coffee machine.
 * It is created by the CoffeeMachineFactory class using the input JSON file to fill up
 * the Inventory and Beverage Recipes.
 */
public class CoffeeMachine {
    // Number of outlets supported by the coffee machine.
    private Integer numberOfOutlets;
    // Thread Pool instance having number of threads equal to number of outlets.
    private ThreadPoolExecutor outletExecutor;
    // Inventory maintains what all ingredients are present and in what quantity.
    private Inventory inventory;
    // Beverage Repository is a fixed list of beverages and their respective recipes.
    private BeverageRepository beverageRepository;

    public CoffeeMachine(Integer numberOfOutlets, ThreadPoolExecutor outletExecutor, Inventory inventory,
                         BeverageRepository beverageRepository) {
        this.numberOfOutlets = numberOfOutlets;
        this.outletExecutor = outletExecutor;
        this.inventory = inventory;
        this.beverageRepository = beverageRepository;
    }

    /**
     *  This method will get the list of beverages that can be requested on this machine.
     */
    public Set<String> getSupportedBeverageList() {
        return beverageRepository.getListOfBeverages();
    }

    /**
     * This method will take one beverage name as input and queue it up for processing.
     * It will throw an exception if an unsupported beverage name is requested.
     *
     * Otherwise, it will queue an asynchronous task and return the Future value for the same.
     * The actual process is run by the class -> BeverageController
     */
    public CompletableFuture<Void> requestForBeverage(String beverageName) throws BeverageNotFoundException {
        if(!beverageRepository.isValidBeverageName(beverageName))
            throw new BeverageNotFoundException("Fatal Error: Invalid Beverage name requested!");

        return CompletableFuture.runAsync(
                new BeverageController(beverageName, inventory, beverageRepository),
                outletExecutor);
    }

    /**
     * This method refills a given ingredient name with the given Quantity.
     * It will ignore the case if we try to:
     * - refill with a negative value
     * - refill a non-existent ingredient in our inventory.
     */
    public void refillIngredientByGivenQuantity(String ingredientName, Integer quantityToRefill) {
        inventory.refillInventory(ingredientName, quantityToRefill);
        System.out.println(String.format("%s refilled by %d ml now", ingredientName, quantityToRefill));
    }

    /**
     * Clean up method to shutdown the Thread Execution Service, effectively shutting down the coffee machine.
     */
    public void shutDownCoffeeMachine() {
        outletExecutor.shutdown();
    }
}
