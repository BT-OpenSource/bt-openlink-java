package com.bt.openlink.type;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class VoiceMessage implements Serializable {

    private static final long serialVersionUID = 5120882907473105773L;

    @Nullable private final String label;
    @Nullable private final VoiceMessageStatus status;
    @Nullable private final ManageVoiceMessageAction action;
    @Nullable private final Duration msgLength;
    @Nullable private final Instant creationDate;
    @Nullable private final PhoneNumber extension;

    private VoiceMessage(@Nonnull final Builder builder) {
        this.label = builder.label;
        this.status = builder.status;
        this.action = builder.action;
        this.msgLength = builder.msgLength;
        this.creationDate = builder.creationDate;
        this.extension = builder.extension;
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
    public Optional<PhoneNumber> getExtension() {
        return Optional.ofNullable(extension);
    }

    @Nonnull
    public Optional<String> getLabel() {
        return Optional.ofNullable(label);
    }

    @Nonnull
    public Optional<VoiceMessageStatus> getStatus() {
        return Optional.ofNullable(status);
    }

    public static final class Builder {
        private Builder() {
        }

        @Nonnull
        public static Builder start() {
            return new Builder();
        }

        @Nullable private String label = null;
        @Nullable private VoiceMessageStatus status = null;
        @Nullable private ManageVoiceMessageAction action = null;
        @Nullable private Duration msgLength = null;
        @Nullable private Instant creationDate = null;
        @Nullable private PhoneNumber extension = null;

        public Builder setExtension(@Nullable final PhoneNumber extension) {
            this.extension = extension;
            return this;
        }

        public Builder setCreationDate(@Nonnull final Instant creationDate) {
            this.creationDate = creationDate;
            return this;
        }

        public Builder setMsgLength(@Nonnull final Duration msgLength) {
            this.msgLength = msgLength;
            return this;
        }

        public Builder setAction(@Nonnull final ManageVoiceMessageAction action) {
            this.action = action;
            return this;
        }

        public Builder setLabel(@Nonnull final String label) {
            this.label = label;
            return this;
        }

        public Builder setStatus(@Nonnull final VoiceMessageStatus status) {
            this.status = status;
            return this;
        }

        protected void validate() {
            if (status == null) {
                throw new IllegalStateException("The VoiceMessage status has not been set");
            }
            if (action == null) {
                throw new IllegalStateException("The VoiceMessage action has not been set");
            } else {
                if (action == ManageVoiceMessageAction.QUERY) {
                    if (label == null) {
                        throw new IllegalStateException("The VoiceMessage label has not been set");
                    }
                    if (msgLength == null) {
                        throw new IllegalStateException("The VoiceMessage msgLength has not been set");
                    }
                    if (creationDate == null) {
                        throw new IllegalStateException("The VoiceMessage creationDate has not been set");
                    }
                } else if (action == ManageVoiceMessageAction.PLAYBACK || action == ManageVoiceMessageAction.RECORD) {
                    if (extension == null) {
                        throw new IllegalStateException("The VoiceMessage extension has not been set");
                    }
                }
            }
        }

        public void validate(final List<String> errors) {
            if (status == null) {
                errors.add("Invalid VoiceMessage; missing status is mandatory");
            }
            if (action == null) {
                errors.add("Invalid VoiceMessage; missing action is mandatory");
            } else {
                if (label == null) {
                    errors.add("Invalid VoiceMessage; missing label is mandatory");
                }
                if (action == ManageVoiceMessageAction.QUERY) {
                    if (msgLength == null) {
                        errors.add("Invalid VoiceMessage; missing msgLength is mandatory");
                    }
                    if (creationDate == null) {
                        errors.add("Invalid VoiceMessage; missing creationDate is mandatory");
                    }
                } else if (action == ManageVoiceMessageAction.PLAYBACK || action == ManageVoiceMessageAction.RECORD) {
                    if (extension == null) {
                        errors.add("Invalid VoiceMessage; missing creationDate is mandatory");
                    }
                }
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
