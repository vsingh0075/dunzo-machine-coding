package entity;

import entity.vo.BulkConsumptionRequest;
import exceptions.IngredientConsumptionException;

import java.util.Map;

/**
 * This class maintains the current state of Coffee Machine's inventory.
 */
public class Inventory {
    // Name of Ingredient -> list of Ingredient objects containing the quantity.
    private Map<String, Ingredient> listOfIngredients;

    public Inventory(Map<String, Ingredient> listOfIngredients) {
        this.listOfIngredients = listOfIngredients;
    }

    public Ingredient getIngredient(String ingredientName) {
        return listOfIngredients.getOrDefault(ingredientName, null);
    }

    /**
     *  This is the function where the consumption of resources is done from the inventory, to prepare a beverage.
     *  1) First check the availability of all the required ingredients.
     *  2) If everything is available, Consume and update the quantity of ingredients in inventory(synchronized).
     *  3) Otherwise the preparation will fail and won't consume any resource.
     */
    public synchronized void bulkConsume(BulkConsumptionRequest consumptionRequest) 
            throws IngredientConsumptionException {
        for (Map.Entry<String, Integer> ingredientEntry : consumptionRequest.getIngredientsToConsume().entrySet()) {
            Ingredient ingredientToConsume = getIngredient(ingredientEntry.getKey());
            ingredientToConsume.checkAvailability(ingredientEntry.getValue());
        }

        for (Map.Entry<String, Integer> ingredientEntry : consumptionRequest.getIngredientsToConsume().entrySet()) {
            Ingredient ingredientToConsume = getIngredient(ingredientEntry.getKey());
            ingredientToConsume.consume(ingredientEntry.getValue());
        }
    }

    /**
     *  Refills the given ingredients quantity in the inventory (synchronized).
     */
    public synchronized void refillInventory(String ingredientName, Integer quantityToRefill) {
        if(quantityToRefill <= 0)
            return;
        Ingredient ingredientToRefill = getIngredient(ingredientName);
        if(ingredientToRefill == null) {
            listOfIngredients.put(ingredientName, new Ingredient(ingredientName, quantityToRefill));
        }
        ingredientToRefill.refill(quantityToRefill);
    }
}
