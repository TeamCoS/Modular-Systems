package com.teamcos.modularsystems.calculations;

public interface Calculation {

    double calculate(int blockCount);

    public static class StandardValues {
        final double xOffset;
        final double yOffset;
        final double xCoefficient;
        final double yCoefficient;
        final double perBlockCap;

        public StandardValues(double xOffset, double yOffset, double xCoefficient, double yCoefficient, double perBlockCap) {
            this.xOffset = xOffset;
            this.yOffset = yOffset;
            this.xCoefficient = xCoefficient;
            this.yCoefficient = yCoefficient;
            this.perBlockCap = perBlockCap;
        }
    }
}
