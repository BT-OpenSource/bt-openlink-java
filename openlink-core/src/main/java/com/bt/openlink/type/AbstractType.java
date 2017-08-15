package com.bt.openlink.type;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AbstractType<T> {
    private final T value;

    protected AbstractType(final T value) {
        this.value = value;
    }

    @Nullable
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
