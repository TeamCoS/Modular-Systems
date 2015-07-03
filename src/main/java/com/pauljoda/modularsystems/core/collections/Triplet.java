package com.pauljoda.modularsystems.core.collections;

/**
 * Used to hold three types of data
 * @param <A> The first type
 * @param <B> The second type
 * @param <C> The third type
 */
public class Triplet<A, B, C> {
    protected A a;
    protected B b;
    protected C c;

    public Triplet(A first, B second, C third) {
        a = first;
        b = second;
        c = third;
    }

    public A getFirst() {
        return a;
    }

    public void setFirst(A toSet) {
        a = toSet;
    }

    public B getSecond() {
        return b;
    }

    public void setSecond(B toSet) {
        b = toSet;
    }

    public C getThird() {
        return c;
    }

    public void setThird(C toSet) {
        c = toSet;
    }

    @Override
    public boolean equals(Object obj) {
        //Self Check
        if(this == obj) return true;

        Triplet other = (Triplet)obj;

        return  this.a.equals(other.getFirst()) &&
                this.b.equals(other.getSecond()) &&
                this.c.equals(other.getThird());
    }
}
