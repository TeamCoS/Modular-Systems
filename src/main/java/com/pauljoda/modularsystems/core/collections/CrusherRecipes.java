package com.pauljoda.modularsystems.core.collections;

/**
 * Modular-Systems
 * Created by Dyonovan on 22/07/15
 */
public class CrusherRecipes {
    protected String input;
    protected String output;
    protected int qty;

    public CrusherRecipes(String input, String output, int qty) {
        this.input = input;
        this.output = output;
        this.qty = qty;
    }

    public String getInput() {
        return input;
    }

    public String getOutput() {
        return output;
    }

    public int getQty() {
        return qty;
    }
}
