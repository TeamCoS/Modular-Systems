package com.teamcos.modularsystems.fuelprovider;

public interface FuelProvider {
    /**
     * Whether the provider can provide any fuel.
     * @return
     */
    boolean canProvide();

    /**
     * How much fuel will be provided when consume is called. Should be mainly used for display purposes.
     * @return
     */
    double fuelProvided();

    /**
     * Consumes fuel and returns how much fuel is provided.
     * @return
     */
    double consume();

    /**
     * Provides what type of fuel is used for sorting purposes.
     * @return
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
}
