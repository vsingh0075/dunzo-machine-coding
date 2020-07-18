package starter;

import entity.CoffeeMachine;
import exceptions.BeverageNotFoundException;
import factory.CoffeeMachineFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * The Runner class for one test run.
 * 1) Create the CoffeeMachine using the Factory class.
 * 2) Create the list of 4 sample beverages to be prepared.
 * 3) Prepare the beverages and wait for the Async process to be completed.
 */
public class CoffeeMachineRunner {
    public static void main(String[] args) throws Exception {
        try {
            CoffeeMachine machine = CoffeeMachineFactory.createCoffeeMachine("src/main/resources/input.json");
            List<String> requests = Arrays.asList("hot_tea", "hot_coffee", "black_tea", "green_tea");
            List<CompletableFuture> futures = requests.stream().map(request -> {
                try {
                    return machine.requestForBeverage(request);
                } catch (BeverageNotFoundException e) {
                    e.printStackTrace();
                    return CompletableFuture.completedFuture(null);
                }
            }).collect(Collectors.toList());
            futures.forEach(CompletableFuture::join);
            machine.shutDownCoffeeMachine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
