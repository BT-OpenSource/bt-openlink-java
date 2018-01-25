package com.bt.openlink.type;

import java.io.Serializable;
import java.util.Objects;

import javax.annotation.Nonnull;

public class AbstractType<T extends Serializable> implements Serializable {
    private static final long serialVersionUID = 1777241541299084888L;
    @Nonnull private final T value;

    protected AbstractType(@Nonnull final T value) {
        this.value = value;
    }

    @Nonnull
    public T value() {
        return value;
    }

    @Nonnull
    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }

        final AbstractType that = (AbstractType) o;
        return Objects.equals(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

}
