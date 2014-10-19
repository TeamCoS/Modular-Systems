package com.teamcos.modularsystems.furnace.config;

public interface Calculation {

    double calculate(int blockCount);

    public static class LinearCalculation implements Calculation {

        private double perBlock;

        public LinearCalculation(double perBlock) {
            this.perBlock = perBlock;
        }

        public double calculate(int blockCount) {
            return Math.max(0, blockCount * perBlock);
        }
    }

    public static class ConstantCalculation implements Calculation {

        private final double efficiency;

        public ConstantCalculation(double efficiency) {
            this.efficiency = efficiency;
        }

        @Override
        public double calculate(int blockCount) {
            return blockCount > 0 ? efficiency : 0;
        }
    }

    public static class LogCalculation implements Calculation {

        private final double scalar;

        public LogCalculation(double scalar) {
            this.scalar = scalar;
        }

        @Override
        public double calculate(int blockCount) {
            return scalar * Math.log(blockCount);
        }
    }

    public static class ParabolicCalculation implements Calculation {

        private final double coefficient;

        public ParabolicCalculation(double coefficient) {
            this.coefficient = coefficient;
        }

        @Override
        public double calculate(int blockCount) {
            return coefficient * blockCount * blockCount;
        }
    }
}
