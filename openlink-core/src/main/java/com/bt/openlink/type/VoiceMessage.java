package com.bt.openlink.type;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public class VoiceMessage implements Serializable {

    private static final long serialVersionUID = 5120882907473105773L;

    @Nullable private final String label;
    @Nullable private final VoiceMessageStatus status;
    @Nullable private final ManageVoiceMessageAction action;
    @Nullable private final Duration msgLength;
    @Nullable private final Instant creationDate;

    VoiceMessage(@Nonnull final Builder builder) {
        this.label = builder.label;
        this.status = builder.status;
        this.action = builder.action;
        this.msgLength = builder.msgLength;
        this.creationDate = builder.creationDate;
    }

    @Nonnull
    public Optional<Instant> getCreationDate() {
        return Optional.ofNullable(creationDate);
    }

    @Nonnull
    public Optional<Duration> getMsgLength() {
        return Optional.ofNullable(msgLength);
    }

    @Nonnull
    public Optional<ManageVoiceMessageAction> getAction() {
        return Optional.ofNullable(action);
    }

    @Nonnull
    public Optional<String> getLabel() {
        return Optional.ofNullable(label);
    }

    @Nonnull
    public Optional<VoiceMessageStatus> getStatus() {
        return Optional.ofNullable(status);
    }

    public static final class Builder<B extends VoiceMessage.Builder> {
        private Builder() {
        }

        @Nonnull
        public static VoiceMessage.Builder start() {
            return new VoiceMessage.Builder();
        }

        @Nullable private String label = null;
        @Nullable private VoiceMessageStatus status = null;
        @Nullable private ManageVoiceMessageAction action = null;
        @Nullable private Duration msgLength = null;
        @Nullable private Instant creationDate = null;

        @SuppressWarnings("unchecked")
        public B setCreationDate(@Nonnull final Instant creationDate) {
            this.creationDate = creationDate;
            return (B) this;
        }

        @SuppressWarnings("unchecked")
        public B setMsgLength(@Nonnull final Duration msgLength) {
            this.msgLength = msgLength;
            return (B) this;
        }

        @SuppressWarnings("unchecked")
        public B setAction(@Nonnull final ManageVoiceMessageAction action) {
            this.action = action;
            return (B) this;
        }

        @SuppressWarnings("unchecked")
        public B setLabel(@Nonnull final String label) {
            this.label = label;
            return (B) this;
        }

        @SuppressWarnings("unchecked")
        public B setStatus(@Nonnull final VoiceMessageStatus status) {
            this.status = status;
            return (B) this;
        }

        protected void validate() {
            if (label == null) {
                throw new IllegalStateException("The VoiceMessage label has not been set");
            }
            if (status == null) {
                throw new IllegalStateException("The VoiceMessage status has not been set");
            }
            if (action == null) {
                throw new IllegalStateException("The VoiceMessage action has not been set");
            }
            if (msgLength == null) {
                throw new IllegalStateException("The VoiceMessage msgLength has not been set");
            }
            if (creationDate == null) {
                throw new IllegalStateException("The VoiceMessage creationDate has not been set");
            }
        }

        public void validate(final List<String> errors) {
            if (label == null) {
                errors.add("Invalid VoiceMessage; missing label is mandatory");
            }
            if (status == null) {
                errors.add("Invalid VoiceMessage; missing status is mandatory");
            }
            if (action == null) {
                errors.add("Invalid VoiceMessage; missing action is mandatory");
            }
            if (msgLength == null) {
                errors.add("Invalid VoiceMessage; missing msgLength is mandatory");
            }
            if (creationDate == null) {
                errors.add("Invalid VoiceMessage; missing creationDate is mandatory");
            }
        }

        @Nonnull
        public VoiceMessage build() {
            validate();
            return new VoiceMessage(this);
        }

        @Nonnull
        public VoiceMessage build(final List<String> errors) {
            validate(errors);
            return new VoiceMessage(this);
        }

    }
}
