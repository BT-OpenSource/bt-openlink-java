package com.bt.openlink.type;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Key implements Serializable {

    private static final long serialVersionUID = -2669473458366898279L;
    @Nullable private KeyId id;
    @Nullable private KeyLabel label;
    @Nullable private KeyFunction function;
    @Nullable private KeyModifier modifier;
    @Nullable private KeyQualifier qualifier;
    @Nullable private KeyColor color;
    @Nullable private KeyInterest interest;

    private Key(@Nonnull Builder builder) {
        this.id = builder.id;
        this.label = builder.label;
        this.function = builder.function;
        this.modifier = builder.modifier;
        this.qualifier = builder.qualifier;
        this.color = builder.color;
        this.interest = builder.interest;
    }

    @Nonnull
    public Optional<KeyId> getId() {
        return Optional.ofNullable(id);
    }

    @Nonnull
    public Optional<KeyLabel> getLabel() {
        return Optional.ofNullable(label);
    }

    @Nonnull
    public Optional<KeyFunction> getFunction() {
        return Optional.ofNullable(function);
    }

    @Nonnull
    public Optional<KeyModifier> getModifier() {
        return Optional.ofNullable(modifier);
    }

    @Nonnull
    public Optional<KeyQualifier> getQualifier() {
        return Optional.ofNullable(qualifier);
    }

    @Nonnull
    public Optional<KeyColor> getColor() {
        return Optional.ofNullable(color);
    }

    @Nonnull
    public Optional<KeyInterest> getInterest() {
        return Optional.ofNullable(interest);
    }

    public static final class Builder {

        @Nullable private KeyId id;
        @Nullable private KeyLabel label;
        @Nullable private KeyFunction function;
        @Nullable private KeyModifier modifier;
        @Nullable private KeyQualifier qualifier;
        @Nullable private KeyColor color;
        @Nullable private KeyInterest interest;

        private Builder() {
        }

        @Nonnull
        public static Builder start() {
            return new Builder();
        }

        @Nonnull
        public Key build() {
            if (id == null) {
                throw new IllegalStateException("The key id has not been set.");
            }
            if (label == null) {
                throw new IllegalStateException("The key label has not been set.");
            }
            if (function == null) {
                throw new IllegalStateException("The key function has not been set.");
            }
            if (qualifier == null) {
                throw new IllegalStateException("The key qualifier has not been set.");
            }
            if (modifier == null) {
                throw new IllegalStateException("The key modifier has not been set.");
            }
            if (color == null) {
                throw new IllegalStateException("The key color has not been set.");
            }
            if (interest == null) {
                throw new IllegalStateException("The key interest has not been set.");
            }
            return new Key(this);
        }

        @Nonnull
        public Key build(final List<String> errors) {
            if (id == null) {
                errors.add("Invalid key : missing key id is mandatory.");
            }
            if (label == null) {
                errors.add("Invalid key : missing key label is mandatory.");
            }
            if (function == null) {
                errors.add("Invalid key : missing key function is mandatory.");
            }
            if (qualifier == null) {
                errors.add("Invalid key : missing key qualifier is mandatory.");
            }
            if (modifier == null) {
                errors.add("Invalid key : missing key modifier is mandatory.");
            }
            if (color == null) {
                errors.add("Invalid key : missing key color is mandatory.");
            }
            if (interest == null) {
                errors.add("Invalid key : missing key interest is mandatory.");
            }
            return new Key(this);
        }

        public Builder setId(@Nullable KeyId id) {
            this.id = id;
            return this;
        }

        public Builder setLabel(@Nullable KeyLabel label) {
            this.label = label;
            return this;
        }

        public Builder setFunction(@Nullable KeyFunction function) {
            this.function = function;
            return this;
        }

        public Builder setModifier(@Nullable KeyModifier modifier) {
            this.modifier = modifier;
            return this;
        }

        public Builder setQualifier(@Nullable KeyQualifier qualifier) {
            this.qualifier = qualifier;
            return this;
        }

        public Builder setColor(@Nullable KeyColor color) {
            this.color = color;
            return this;
        }

        public Builder setInterest(@Nullable KeyInterest interest) {
            this.interest = interest;
            return this;
        }
    }
}
