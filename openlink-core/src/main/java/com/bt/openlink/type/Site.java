package com.bt.openlink.type;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Site {

    public enum Type {
        BTSM, CISCO, IPT, ITS;

        public static Optional<Type> from(@Nullable final String value) {
            for (final Type type : Type.values()) {
                if(type.name().equals(value)) {
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
    @Nonnull private final List<String> parseErrors;

    private Site(@Nonnull final Builder builder, @Nullable final List<String> parseErrors) {
        this.id = builder.id;
        this.isDefault = builder.isDefault;
        this.type = builder.type;
        this.name = builder.name;
        if (parseErrors == null) {
            this.parseErrors = Collections.emptyList();
        } else {
            this.parseErrors = parseErrors;
        }
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

    @Nonnull
    public List<String> getParseErrors() {
        return parseErrors;
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
            return new Site(this, null);
        }

        @Nonnull
        public Site build(final List<String> parseErrors) {
            return new Site(this, parseErrors);
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
