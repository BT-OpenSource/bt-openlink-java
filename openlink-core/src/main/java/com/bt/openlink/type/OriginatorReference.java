package com.bt.openlink.type;

import java.io.Serializable;
import java.util.Objects;

import javax.annotation.Nonnull;

public final class OriginatorReference implements Serializable {

    private static final long serialVersionUID = -7760726364861459213L;
    @Nonnull private final String key;
    @Nonnull private final String value;

    public OriginatorReference(@Nonnull final String key, @Nonnull final String value) {
        this.key = key;
        this.value = value;
    }

    @Nonnull
    public String getKey() {
        return key;
    }

    @Nonnull
    public String getValue() {
        return value;
    }

    @Nonnull
    @Override
    public String toString() {
        return String.format("%s=%s", key, value);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }

        final OriginatorReference that = (OriginatorReference) o;
        return Objects.equals(this.key, that.key)
                && Objects.equals(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }

}
