package config;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * Class to represent the coffee machine part in JSON.
 */
public class MachineConfig {
    private OutletConfig outlets;

    @JsonProperty("total_items_quantity")
    private Map<String,Integer> totalItemsQuantity;
    private Map<String, Map<String, Integer>> beverages;

    public OutletConfig getOutlets() {
        return outlets;
    }

    public void setOutlets(OutletConfig outlets) {
        this.outlets = outlets;
    }

    public Map<String, Integer> getTotalItemsQuantity() {
        return totalItemsQuantity;
    }

    public void setTotalItemsQuantity(Map<String, Integer> totalItemsQuantity) {
        this.totalItemsQuantity = totalItemsQuantity;
    }

    public Map<String, Map<String, Integer>> getBeverages() {
        return beverages;
    }

    public void setBeverages(Map<String, Map<String, Integer>> beverages) {
        this.beverages = beverages;
    }
}
