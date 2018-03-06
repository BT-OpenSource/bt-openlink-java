package com.bt.openlink.type;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Site implements Serializable {

    private static final long serialVersionUID = -5504996561253140171L;

    public enum Type {
        AVAYA, BTSM, CISCO, IPT, ITS;

        public static Optional<Type> from(@Nullable final String value) {
            for (final Type type : Type.values()) {
                if (type.name().equals(value)) {
                    return Optional.of(type);
                }
            }
            return Optional.empty();
        }
    }

    @Nullable private final Long id;
    @Nullable private final Boolean isDefault;
    @Nullable private final Type type;
    @Nullable private final String name;

    private Site(@Nonnull final Builder builder) {
        this.id = builder.id;
        this.isDefault = builder.isDefault;
        this.type = builder.type;
        this.name = builder.name;
    }

    @Nonnull
    public Optional<Long> getId() {
        return Optional.ofNullable(id);
    }

    @Nonnull
    public Optional<Boolean> isDefault() {
        return Optional.ofNullable(isDefault);
    }

    @Nonnull
    public Optional<Type> getType() {
        return Optional.ofNullable(type);
    }

    @Nonnull
    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    public static final class Builder {

        @Nullable private Long id = null;
        @Nullable private Boolean isDefault = null;
        @Nullable private Type type = null;
        @Nullable private String name = null;

        private Builder() {
        }

        public static Builder start() {
            return new Builder();
        }

        public Site build() {
            if (id == null) {
                throw new IllegalStateException("The site id has not been set");
            }
            if (type == null) {
                throw new IllegalStateException("The site type has not been set");
            }
            if (name == null) {
                throw new IllegalStateException("The site name has not been set");
            }
            return new Site(this);
        }

        @Nonnull
        public Site build(@Nonnull final List<String> errors) {
            if (id == null) {
                errors.add("Invalid site; missing site id is mandatory");
            }
            if (type == null) {
                errors.add("Invalid site; missing site type is mandatory");
            }
            if (name == null) {
                errors.add("Invalid site; missing site name is mandatory");
            }
            return new Site(this);
        }

        public Builder setId(final long id) {
            this.id = id;
            return this;
        }

        public Builder setDefault(final boolean isDefault) {
            this.isDefault = isDefault;
            return this;
        }

        public Builder setType(@Nonnull final Type type) {
            this.type = type;
            return this;
        }

        public Builder setName(@Nonnull final String name) {
            this.name = name;
            return this;
        }
    }
}
