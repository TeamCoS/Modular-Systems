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
     * Provides what type of fuel is used for sorting purposes.
     * @return Type of provider
     */
    FuelProviderType type();

    public static enum FuelProviderType {
        LIQUID(5),
        POWER(10),
        ITEM(1),
        OTHER(0);

        public final int sortValue;

        private FuelProviderType(int sortValue) {
            this.sortValue = sortValue;
        }
    }

    final class FuelSorter implements Comparator<FuelProvider> {
        @Override
        public int compare(FuelProvider o1, FuelProvider o2) {
            if(o1.type() != null && o2.type() != null) {
                if (o1.canProvide() && !o2.canProvide()) {
                    return 1;
                } else if (o2.canProvide() && !o1.canProvide()) {
                    return -1;
                } else if (o1.type() == o2.type()) {
                    return Double.compare(o1.fuelProvided(), o2.fuelProvided());
                } else {
                    return Integer.compare(o1.type().sortValue, o2.type().sortValue);
                }
            }
            return 0;
        }

        @Override
        public boolean equals(Object obj) {
            return this == obj;
        }
    }
}
