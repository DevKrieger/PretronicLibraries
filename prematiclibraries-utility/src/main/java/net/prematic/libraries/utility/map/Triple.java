package net.prematic.libraries.utility.map;

import java.util.Objects;

public class Triple<F, S, T> {

    private F first;
    private S second;
    private T third;

    public Triple(F first, S second, T third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public F getFirst() {
        return first;
    }

    public S getSecond() {
        return second;
    }

    public T getThird() {
        return third;
    }

    public void setFirst(F first) {
        this.first = first;
    }

    public void setSecond(S second) {
        this.second = second;
    }

    public void setThird(T third) {
        this.third = third;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object instanceof Triple) {
            Triple triple = (Triple) object;
            if (!Objects.equals(first, triple.first)) return false;
            if (!Objects.equals(second, triple.second)) return false;
            return Objects.equals(third, triple.third);
        }
        return false;
    }
}