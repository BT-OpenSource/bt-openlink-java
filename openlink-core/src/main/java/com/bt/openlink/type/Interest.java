package com.bt.openlink.type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class Interest {
    @Nonnull private final List<String> parseErrors;
    @Nullable private final InterestId interestId;
    @Nullable private final InterestType interestType;
    @Nullable private final String label;
    @Nullable private final Boolean isDefault;

    private Interest(@Nonnull final Builder builder, @Nullable final List<String> parseErrors) {
        this.interestId = builder.interestId;
        this.interestType = builder.interestType;
        this.label = builder.label;
        this.isDefault = builder.isDefault;
        if (parseErrors == null) {
            this.parseErrors = Collections.emptyList();
        } else {
            this.parseErrors = parseErrors;
        }
    }

    @Nonnull
    public List<String> getParseErrors() {
        return new ArrayList<>(parseErrors);
    }

    @Nonnull
    public Optional<InterestId> getId() {
        return Optional.ofNullable(interestId);
    }

    @Nonnull
    public Optional<InterestType> getType() {
        return Optional.ofNullable(interestType);
    }

    @Nonnull
    public Optional<String> getLabel() {
        return Optional.ofNullable(label);
    }

    @Nonnull
    public Optional<Boolean> isDefault() {
        return Optional.ofNullable(isDefault);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Interest)) {
            return false;
        }
        final Interest that = (Interest) o;
        return Objects.equals(this.interestId, that.interestId) &&
                Objects.equals(this.interestType, that.interestType) &&
                Objects.equals(this.label, that.label) &&
                Objects.equals(this.isDefault, that.isDefault);
    }

    @Override
    public int hashCode() {
        return Objects.hash(interestId, interestType, label, isDefault);
    }

    @Override
    public String toString() {
        return "Interest[" +
                "interestId=" + interestId +
                ", interestType=" + interestType +
                ", label='" + label + '\'' +
                ", isDefault=" + isDefault +
                ']';
    }

    public static final class Builder {

        @Nullable private InterestId interestId = null;
        @Nullable private InterestType interestType = null;
        @Nullable private String label = null;
        @Nullable private Boolean isDefault = null;

        private Builder() {
        }

        @Nonnull
        public static Builder start() {
            return new Builder();
        }

        @Nonnull
        public Interest build() {
            return new Interest(this, null);
        }

        @Nonnull
        private Interest build(final List<String> parseErrors) {
            return new Interest(this, parseErrors);
        }

        public Builder setId(@Nonnull final InterestId interestId) {
            this.interestId = interestId;
            return this;
        }

        public Builder setType(@Nonnull final InterestType interestType) {
            this.interestType = interestType;
            return this;
        }

        public Builder setLabel(@Nonnull final String label) {
            this.label = label;
            return this;
        }

        public Builder setDefault(final boolean isDefault) {
            this.isDefault = isDefault;
            return this;
        }
    }
}
