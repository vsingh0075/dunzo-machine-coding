package entity;

import exceptions.IngredientConsumptionException;

/**
 *  This class represents a single ingredient, having a name and quantity.
 *  Used by -> Beverage class to store the recipe instances.
 *  Used by -> Inventory class to store the current quantity for ingredients in the Coffee Machine.
 */
public class Ingredient {
    private String name;
    private Integer quantity;

    public Ingredient(String name, Integer quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    /**
     *  Update the quantity of this ingredient as part of consumption by Beverage Preparation process.
     */
    public synchronized void consume(Integer quantityToConsume) throws IngredientConsumptionException {
        checkAvailability(quantityToConsume);
        quantity -= quantityToConsume;
    }

    /**
     *  Check if sufficient quantity is present before consuming the ingredient.
     *  Throws an error, if quantity is insufficient/unavailable.
     */
    public synchronized void checkAvailability(Integer quantityNeeded) throws IngredientConsumptionException {
        if(quantity == 0) {
            throw new IngredientConsumptionException(String.format("%s is not available", name));
        }
        else if(quantity < quantityNeeded) {
            throw new IngredientConsumptionException(String.format("%s is not sufficient", name));
        }
    }

    /**
     *  Update the quantity of this ingredient.
     */
    public synchronized void refill(Integer quantityToRefill) {
        quantity += quantityToRefill;
    }
}
