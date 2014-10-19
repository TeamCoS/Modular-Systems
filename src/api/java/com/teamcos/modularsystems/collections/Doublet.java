package com.teamcos.modularsystems.collections;

public class Doublet<A, B> {

    private final A first;
    private final B second;

    public Doublet(A first, B second) {
        this.first = first;
        this.second = second;
    }

    public A getFirst() {
        return first;
    }

    public B getSecond() {
        return second;
    }
}
