package bg.sofia.uni.fmi.mjt.shopping.item;

import java.util.Objects;

public class Apple implements Item {
    private final String id;

    public Apple(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Apple apple = (Apple) o;

        return this.id.equals(apple.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}