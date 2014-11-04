package com.teamcos.modularsystems.calculations;

public class StandardValues {
    public static final StandardValues LinearIdentity = new StandardValues(1);
    public static final StandardValues Constant0 = new StandardValues("constant", 0, 0, 0, 0, 0, 0);
    private final double xOffset;
    private final double yOffset;
    private final double xCoefficient;
    private final double yCoefficient;
    private final double perBlockCap;
    private final double perBlockFloor;
    private final String equation;

    public StandardValues(String equation1, double xOffset, double yOffset, double xCoefficient, double yCoefficient, double perBlockCap, double perBlockFloor) {
        this.equation = equation1;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.xCoefficient = xCoefficient;
        this.yCoefficient = yCoefficient;
        this.perBlockCap = perBlockCap;
        this.perBlockFloor = perBlockFloor;
    }

    public StandardValues(double yCoefficient) {
        this.equation = "linear";
        this.xOffset = 0;
        this.yOffset = 0;
        this.xCoefficient = 1;
        this.yCoefficient = yCoefficient;
        this.perBlockCap = Double.MAX_VALUE;
        this.perBlockFloor = - Double.MAX_VALUE;
    }

    public double getxOffset() {
        return xOffset;
    }

    public double getyOffset() {
        return yOffset;
    }

    public double getxCoefficient() {
        return xCoefficient;
    }

    public double getyCoefficient() {
        return yCoefficient;
    }

    public double getPerBlockCap() {
        return perBlockCap;
    }

    public double getPerBlockFloor() {
        return perBlockFloor;
    }

    public String getEquation() {
        return equation;
    }
}
