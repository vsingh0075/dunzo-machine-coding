package config;

/**
 * Root level class to parse the input JSON.
 */
public class CoffeeMachineConfig {
    private MachineConfig machine;

    public MachineConfig getMachine() {
        return machine;
    }

    public void setMachine(MachineConfig machine) {
        this.machine = machine;
    }
}
