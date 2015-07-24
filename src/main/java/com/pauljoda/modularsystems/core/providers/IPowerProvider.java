package com.pauljoda.modularsystems.core.providers;

/**
 * Modular-Systems
 * Created by Dyonovan on 24/07/15
 */
public interface IPowerProvider {

    /**
     * Whether the provider can provide any power.
     * @return Can this provide
     */
    boolean canSupply();

    /**
     * How much power will be provided when consume is called. Should be mainly used for display purposes.
     * @return How much would be consumed
     */
    double fuelSupplied(int maxAmount);

    /**
     * Consumes power and returns how much power is provided.
     * @return How much was consumed
     */
    double supply(int maxAmount);
}
