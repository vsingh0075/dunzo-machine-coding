package controller;

import entity.Beverage;
import entity.BeverageRepository;
import entity.Inventory;
import entity.vo.BulkConsumptionRequest;
import exceptions.IngredientConsumptionException;

/**
 * This is the main runner class for the threads.
 * It picks up the Inventory and Beverage Recipe data from the CoffeeMachine class.
 *
 * It will try to prepare one beverage whose name has been passed.
 */
public class BeverageController implements Runnable {
    // Beverage name requested
    private String beverageName;
    // Machine's inventory
    private Inventory ingredientInventory;
    // Repository of Beverage recipes.
    private BeverageRepository beverageRepository;

    public BeverageController(String beverageName, Inventory ingredientInventory, BeverageRepository beverageRepository) {
        this.beverageName = beverageName;
        this.ingredientInventory = ingredientInventory;
        this.beverageRepository = beverageRepository;
    }

    @Override
    public void run() {
        // Get the beverage recipe from the beverage repository using the name.
        Beverage requestedBeverage = beverageRepository.getBeverage(beverageName);
        // Preparation time. We use the constant PREP_TIME_IN_SECONDS to make the Thread sleep.
        // Currently its ONE. It can be set to any value, including zero. Results are consistent.
        Integer prepTime = requestedBeverage.PREP_TIME_IN_SECONDS;

        // Create a temporary request object, containing the ingredients and respective amounts needed.
        BulkConsumptionRequest consumptionRequest = new BulkConsumptionRequest(requestedBeverage.getBeverageIngredients());
        try {
            // Check if its feasible to prepare the beverage.
            // If it is, then prepare it, otherwise show the insufficient ingredients message.
            ingredientInventory.bulkConsume(consumptionRequest);
        } catch (IngredientConsumptionException e) {
            System.out.println(String.format("%s cannot be prepared because %s", beverageName, e.getMessage()));
            return;
        }
        System.out.println(String.format("Waiting %d seconds for %s to prepare!", prepTime, beverageName));
        try {
            Thread.sleep(prepTime * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Indicate that the preparation was successful.
        System.out.println(String.format("%s is prepared", beverageName));
    }
}
