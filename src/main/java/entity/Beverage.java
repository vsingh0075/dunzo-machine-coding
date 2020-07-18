package entity;

import java.util.Map;

/**
 * Class to represent a supported beverage.
 */
public class Beverage {
    // Name of beverage
    private String name;
    // List of all ingredient names required along with amount.
    private Map<String, Integer> beverageIngredients;
    // Prep time, just for simulation purpose. Safe to change it to ZERO.
    public final Integer PREP_TIME_IN_SECONDS = 1;

    public Beverage(String name, Map<String, Integer> beverageIngredients) {
        this.name = name;
        this.beverageIngredients = beverageIngredients;
    }
    public Map<String, Integer> getBeverageIngredients() {
        return beverageIngredients;
    }
}
