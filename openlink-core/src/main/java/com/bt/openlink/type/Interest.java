package com.bt.openlink.type;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class Interest implements Serializable {
    private static final long serialVersionUID = 7189237932741479652L;
    @Nullable private final InterestId interestId;
    @Nullable private final InterestType interestType;
    @Nullable private final String label;
    @Nullable private final Boolean isDefault;
    @Nullable private final CallStatus callStatus;
    @Nullable private final Integer maxCalls;
    @Nullable private final PhoneNumber number;

    private Interest(@Nonnull final Builder builder) {
        this.interestId = builder.interestId;
        this.interestType = builder.interestType;
        this.label = builder.label;
        this.isDefault = builder.isDefault;
        this.callStatus = builder.callStatus;
        this.maxCalls = builder.maxCalls;
        this.number = builder.number;
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
    public Optional<Boolean> isDefaultInterest() {
        return Optional.ofNullable(isDefault);
    }

    @Nonnull
    public Optional<CallStatus> getCallStatus() {
        return Optional.ofNullable(callStatus);
    }

    @Nonnull
    public Optional<Integer> getMaxCalls() {
        return Optional.ofNullable(maxCalls);
    }

    @Nonnull
    public Optional<PhoneNumber> getNumber() {
        return Optional.ofNullable(number);
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
        @Nullable private CallStatus callStatus = null;
        @Nullable private Integer maxCalls = null;
        @Nullable private PhoneNumber number = null;

        private Builder() {
        }

        @Nonnull
        public static Builder start() {
            return new Builder();
        }

        @Nonnull
        public Interest build() {
            if (interestId == null) {
                throw new IllegalStateException("The interest id has not been set");
            }
            if (interestType == null) {
                throw new IllegalStateException("The interest type has not been set");
            }
            if (label == null) {
                throw new IllegalStateException("The interest label has not been set");
            }
            if (isDefault == null) {
                throw new IllegalStateException("The interest default indicator has not been set");
            }
            return new Interest(this);
        }

        @Nonnull
        public Interest build(final List<String> errors) {
            if (interestId == null) {
                errors.add("Invalid interest; missing interest id is mandatory");
            }
            if (interestType == null) {
                errors.add("Invalid interest; missing interest type is mandatory");
            }
            if (label == null) {
                errors.add("Invalid interest; missing interest label is mandatory");
            }
            if (isDefault == null) {
                errors.add("Invalid interest; missing interest default indicator is mandatory");
            }
            return new Interest(this);
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

        public Builder setCallStatus(@Nonnull final CallStatus callStatus) {
            this.callStatus = callStatus;
            return this;
        }

        public Builder setMaxCalls(final int maxCalls) {
            this.maxCalls = maxCalls;
            return this;
        }

        public Builder setNumber(@Nonnull final PhoneNumber number) {
            this.number = number;
            return this;
        }
    }
}
