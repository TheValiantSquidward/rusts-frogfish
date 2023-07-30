package net.rustandsquid.rustsfrogfish.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum NeilVariant {
    DEFAULT(0),
    DULL(1);

    private static final NeilVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(NeilVariant::getId)).toArray(NeilVariant[]::new);
    private final int id;

    NeilVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static NeilVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
