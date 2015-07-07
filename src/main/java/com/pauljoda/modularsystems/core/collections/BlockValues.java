package com.pauljoda.modularsystems.core.collections;

import com.pauljoda.modularsystems.core.calculations.Calculation;

public class BlockValues {
    protected Calculation speedFunction;
    protected Calculation efficiencyFunction;
    protected Calculation multiplicityFunction;

    public BlockValues(Calculation c1, Calculation c2, Calculation c3) {
        speedFunction = new Calculation(c1);
        efficiencyFunction = new Calculation(c2);
        multiplicityFunction = new Calculation(c3);
    }

    public Calculation getSpeedFunction() {
        return speedFunction;
    }

    public void setSpeedFunction(Calculation speedFunction) {
        this.speedFunction = new Calculation(speedFunction);
    }

    public Calculation getEfficiencyFunction() {
        return efficiencyFunction;
    }

    public void setEfficiencyFunction(Calculation efficiencyFunction) {
        this.efficiencyFunction = new Calculation(efficiencyFunction);
    }

    public Calculation getMultiplicityFunction() {
        return multiplicityFunction;
    }

    public void setMultiplicityFunction(Calculation multiplicityFunction) {
        this.multiplicityFunction = new Calculation(multiplicityFunction);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BlockValues that = (BlockValues) o;

        if (getSpeedFunction() != null ? !getSpeedFunction().equals(that.getSpeedFunction()) : that.getSpeedFunction() != null)
            return false;
        if (getEfficiencyFunction() != null ? !getEfficiencyFunction().equals(that.getEfficiencyFunction()) : that.getEfficiencyFunction() != null)
            return false;
        return !(getMultiplicityFunction() != null ? !getMultiplicityFunction().equals(that.getMultiplicityFunction()) : that.getMultiplicityFunction() != null);
    }

    @Override
    public int hashCode() {
        int result = getSpeedFunction() != null ? getSpeedFunction().hashCode() : 0;
        result = 31 * result + (getEfficiencyFunction() != null ? getEfficiencyFunction().hashCode() : 0);
        result = 31 * result + (getMultiplicityFunction() != null ? getMultiplicityFunction().hashCode() : 0);
        return result;
    }
}
