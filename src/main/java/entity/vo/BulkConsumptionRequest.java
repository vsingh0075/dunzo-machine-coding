package entity.vo;

import java.util.Map;

/**
 * Used to create consumption requests for one beverage.
 */
public class BulkConsumptionRequest {
    private Map<String, Integer> ingredientsToConsume;

    public BulkConsumptionRequest(Map<String, Integer> ingredientsToConsume) {
        this.ingredientsToConsume = ingredientsToConsume;
    }

    public Map<String, Integer> getIngredientsToConsume() {
        return ingredientsToConsume;
    }
}
