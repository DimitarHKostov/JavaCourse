package bg.sofia.uni.fmi.mjt.shopping.item;

import java.util.Objects;

public class Chocolate implements Item {
    private final String id;

    public Chocolate(String id) {
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

        Chocolate chocolate = (Chocolate) o;

        return this.id.equals(chocolate.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}