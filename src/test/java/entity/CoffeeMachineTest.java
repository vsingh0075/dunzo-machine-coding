package entity;

import exceptions.BeverageNotFoundException;
import factory.CoffeeMachineFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class CoffeeMachineTest {

    private CoffeeMachine machine;

    @Before
    public void setup() throws Exception {
        // Create the coffee machine for running the tests.
        this.machine = CoffeeMachineFactory.createCoffeeMachine("src/main/resources/input.json");
    }

    @After
    public void tearDown() {
        // Shut down coffee machine after all tests.
        this.machine.shutDownCoffeeMachine();
    }

    @Test
    public void testPrepareMultipleBeverages() throws Exception {
        // The standard test case -> prepare multiple beverages.
        List<String> requests = Arrays.asList("hot_tea", "hot_coffee", "black_tea", "green_tea");
        List<CompletableFuture> futures = requests.stream().map(request -> {
            try {
                return machine.requestForBeverage(request);
            } catch (BeverageNotFoundException e) {
                Assert.fail();
                return CompletableFuture.completedFuture(null);
            }
        }).collect(Collectors.toList());
        futures.forEach(CompletableFuture::join);
    }

    @Test
    public void testPrepareSingleBeverage4TimesWithoutRefill() {
        // Prepare a single beverage multiple times. Please note we are not refilling our inventory.
        List<String> requests = Arrays.asList("hot_tea", "hot_tea", "hot_tea", "hot_tea");
        List<CompletableFuture> futures = requests.stream().map(request -> {
            try {
                return machine.requestForBeverage(request);
            } catch (BeverageNotFoundException e) {
                Assert.fail();
                return CompletableFuture.completedFuture(null);
            }
        }).collect(Collectors.toList());
        futures.forEach(CompletableFuture::join);
    }

    @Test
    public void testPrepareSingleBeverage4TimesWithRefill() throws Exception {
        // Prepare a single beverage multiple times.
        // We are Refilling hot water and tea leaves syrup to make sure the machine can prepare all four beverages.
        List<CompletableFuture> futures = new ArrayList<>();
        futures.add(machine.requestForBeverage("hot_tea"));
        futures.add(machine.requestForBeverage("hot_tea"));
        machine.refillIngredientByGivenQuantity("hot_water", 500);
        futures.add(machine.requestForBeverage("hot_tea"));
        machine.refillIngredientByGivenQuantity("tea_leaves_syrup", 100);
        futures.add(machine.requestForBeverage("hot_tea"));
        futures.forEach(CompletableFuture::join);
    }

    @Test(expected = BeverageNotFoundException.class)
    public void testPrepareUnsupportedBeverage() throws Exception {
        // Try to prepare a unsupported beverage.
        // Machine should immediately deny the request by throwing BeverageNotFoundException.
        machine.requestForBeverage("A Very Random Not Existing Drink");
    }

    @Test
    public void testGetAllBeverageNames() {
        // Get all the supported beverage names.
        Set<String> allBeverages = machine.getSupportedBeverageList();
        Assert.assertEquals(4, allBeverages.size());
        Assert.assertTrue(allBeverages.contains("hot_tea"));
        Assert.assertTrue(allBeverages.contains("hot_coffee"));
        Assert.assertTrue(allBeverages.contains("green_tea"));
        Assert.assertTrue(allBeverages.contains("black_tea"));

    }
}
