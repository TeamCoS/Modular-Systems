package com.pauljoda.modularsystems.core.providers;

import java.util.Comparator;

/**
 * Modular-Systems
 * Created by Dyonovan on 24/07/15
 */
public interface IPowerProvider {

    /**
     * Whether the provider can provide any power.
     * @return Can this provide
     */
    boolean canProvide();

    /**
     * How much power will be provided when consume is called. Should be mainly used for display purposes.
     * @return How much would be consumed
     */
    double fuelProvided();

    /**
     * Consumes power and returns how much power is provided.
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
    PowerProviderType type();

    enum PowerProviderType {
        RF,
        EU,
        MANA
    }

    final class PowerSorter implements Comparator<IPowerProvider> {
        @Override
        public int compare(IPowerProvider o1, IPowerProvider o2) {
            return -Integer.compare(o1.getPriority(), o2.getPriority());
        }

        @Override
        public boolean equals(Object obj) {
            return this == obj;
        }
    }
}
