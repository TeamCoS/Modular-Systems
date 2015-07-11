package com.pauljoda.modularsystems.core.providers;

import java.util.Comparator;

public interface FuelProvider {
    /**
     * Whether the provider can provide any fuel.
     * @return Can this provide
     */
    boolean canProvide();

    /**
     * How much fuel will be provided when consume is called. Should be mainly used for display purposes.
     * @return How much would be consumed
     */
    double fuelProvided();

    /**
     * Consumes fuel and returns how much fuel is provided.
     * @return How much was consumed
     */
    double consume();

    /**
     * Used to set the priority for this provider
     * @return 0 First, higher last
     */
    int getPriority();

    /**
     * Provides what type of fuel is used for sorting purposes.
     * @return Type of provider
     */
    FuelProviderType type();


    enum FuelProviderType {
        LIQUID,
        POWER,
        ITEM,
        OTHER
    }

    final class FuelSorter implements Comparator<FuelProvider> {
        @Override
        public int compare(FuelProvider o1, FuelProvider o2) {
            return Integer.compare(o1.getPriority(), o2.getPriority());
        }

        @Override
        public boolean equals(Object obj) {
            return this == obj;
        }
    }
}
