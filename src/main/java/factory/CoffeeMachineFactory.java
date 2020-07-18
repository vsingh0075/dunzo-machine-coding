package factory;

import com.fasterxml.jackson.databind.ObjectMapper;
import config.CoffeeMachineConfig;
import config.MachineConfig;
import entity.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * This is the factory class for our Coffee Machine.
 * It will initialize and return a new coffee machine, given the input JSON as per the requirements.
 * It will parse the following data from the Json:
 *   1) Number of Outlets -> Size of Thread Pool.
 *   2) Ingredients present in the machine and their respective amount.
 *   3) Beverages supported and their recipes (what and how much ingredients needed)
 *
 * It initializes three classes:
 *   1) ThreadPoolExecutor -> size being number of outlets. To dispatch async processes on one of these threads.
 *   2) BeverageRepository -> Manages the supported beverage recipes with ingredient requirements.
 *   3) Inventory -> Manages the available ingredients in the coffee machine.
 *
 * Input JSON file -> src/main/resources/input.json
 */
public class CoffeeMachineFactory {

    public static CoffeeMachine createCoffeeMachine(String inputJsonFilePath) throws IOException {

        /*
         CoffeeMachineConfig is the Root Level class to Parse the input JSON file received.
         Parse the input json.
        */
        ObjectMapper mapper = new ObjectMapper();
        CoffeeMachineConfig coffeeMachineConfig = mapper.readValue(new File(inputJsonFilePath), CoffeeMachineConfig.class);

        // Create the ThreadPoolExecutor using the number of outlets in the coffee machine.
        MachineConfig machineConfig = coffeeMachineConfig.getMachine();
        Integer numberOfOutlets = machineConfig.getOutlets().getCountN();
        ThreadPoolExecutor outlets = (ThreadPoolExecutor)Executors.newFixedThreadPool(numberOfOutlets);

        // Create the Map of "ingredient name" -> "ingredient quantity available in coffee machine"
        Map<String, Integer> totalItemsQuantity = machineConfig.getTotalItemsQuantity();
        Map<String, Ingredient> allIngredients = new HashMap<>();
        totalItemsQuantity.forEach((itemName, itemQuantity) -> {
            Ingredient ingredient = new Ingredient(itemName, itemQuantity);
            allIngredients.put(itemName, ingredient);
        });

        // Create the Map of "beverage name" -> "beverage receipe".
        Map<String, Map<String, Integer>> beverageList = machineConfig.getBeverages();
        Map<String, Beverage> allBeverages = new HashMap<>();
        beverageList.forEach((beverageName, ingredientsMap) -> {
            // Every beverage recipe consists of beverage name and corresponding map of (ingredient name-quantity needed)
            Beverage beverage = new Beverage(beverageName, ingredientsMap);
            allBeverages.put(beverageName, beverage);

            // If some beverage needs an ingredient and if its not present in the coffee machine initially,
            // initialize the quantity of that ingredient as ZERO in our inventory.
            ingredientsMap.keySet().forEach((ingredientName) ->
                allIngredients.putIfAbsent(ingredientName, new Ingredient(ingredientName, 0))
            );
        });

        // Create the classes to represent Beverage Repository and Coffee Machine's Inventory.
        BeverageRepository beverageRepository = new BeverageRepository(allBeverages);
        Inventory inventory = new Inventory(allIngredients);

        // Create and return the instance of coffee machine.
        CoffeeMachine coffeeMachine = new CoffeeMachine(numberOfOutlets, outlets, inventory, beverageRepository);

        return coffeeMachine;
    }
}
