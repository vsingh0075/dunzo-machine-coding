package entity;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 *  Class to represent the all the beverages supported.
 */
public class BeverageRepository {
    // Beverage Name -> List of Beverage Recipe instances.
    private Map<String, Beverage> listOfBeverages;

    public BeverageRepository(Map<String, Beverage> listOfBeverages) {
        this.listOfBeverages = listOfBeverages;
    }

    // Get beverage recipe from its name
    public Beverage getBeverage(String beverageName) {
        if(listOfBeverages == null)
            return null;
        return listOfBeverages.getOrDefault(beverageName, null);
    }

    // Check if beverage name is valid or not.
    public boolean isValidBeverageName(String beverageName) {
        return listOfBeverages != null && listOfBeverages.containsKey(beverageName);
    }

    // Get the set of all supported beverages.
    public Set<String> getListOfBeverages() {
        if(listOfBeverages == null)
            return Collections.emptySet();
        return listOfBeverages.keySet();
    }
}
