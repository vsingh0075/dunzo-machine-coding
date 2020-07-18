package exceptions;

/**
 * Thrown when Ingredient Quantity is Insufficient or Ingredient is not available while consuming.
 */
public class IngredientConsumptionException extends Exception {
    public IngredientConsumptionException(String message) {
        super(message);
    }
}
