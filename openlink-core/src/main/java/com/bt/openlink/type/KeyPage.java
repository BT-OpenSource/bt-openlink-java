package com.bt.openlink.type;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class KeyPage implements Serializable {

    private static final long serialVersionUID = -2982906178792658434L;
    @Nullable private KeyPageId keyPageId;
    @Nullable private KeyPageLabel keyPageLabel;
    @Nullable private KeyPageModule keyPageModule;
    @Nullable private KeyPageLocalKeyPage localKeyPage;
    @Nonnull private List<Key> keys;

    private KeyPage(Builder builder) {
        this.keyPageId = builder.keypageId;
        this.keyPageLabel = builder.keypageLabel;
        this.keyPageModule = builder.keypageModule;
        this.localKeyPage = builder.localKeypage;
        this.keys = builder.keys;
    }

    @Nonnull
    public Optional<KeyPageId> getKeyPageId() {
        return Optional.ofNullable(keyPageId);
    }

    @Nonnull
    public Optional<KeyPageLabel> getKeyPageLabel() {
        return Optional.ofNullable(keyPageLabel);
    }

    @Nonnull
    public Optional<KeyPageModule> getKeyPageModule() {
        return Optional.ofNullable(keyPageModule);
    }

    @Nonnull
    public Optional<KeyPageLocalKeyPage> getLocalKeyPage() {
        return Optional.ofNullable(localKeyPage);
    }

    @Nonnull
    public List<Key> getKeys() {
        return keys;
    }

    @Override
    public String toString() {
        return "KeyPage[" +
                "keyPageId=" + keyPageId +
                "keyPageLabel=" + keyPageLabel +
                "keyPageModule=" + keyPageModule +
                "localKeyPage=" + localKeyPage +
                "]";
    }

    public static final class Builder {

        @Nullable private KeyPageId keypageId;
        @Nullable private KeyPageLabel keypageLabel;
        @Nullable private KeyPageModule keypageModule;
        @Nullable private KeyPageLocalKeyPage localKeypage;
        @Nonnull private List<Key> keys = new ArrayList<>();

        private Builder() {
        }

        @Nonnull
        public static Builder start() {
            return new Builder();
        }

        public KeyPage build() {
            if (keypageId == null) {
                throw new IllegalStateException("The keypage id has not been set");
            }
            if (keypageLabel == null) {
                throw new IllegalStateException("The keypage label has not been set");
            }
            if (keypageModule == null) {
                throw new IllegalStateException("The keypage module has not been set");
            }
            if (localKeypage == null) {
                throw new IllegalStateException("The keypage localKeyPage has not been set");
            }
            return new KeyPage(this);
        }

        public KeyPage build(final List<String> errors) {
            if (keypageId == null) {
                errors.add("Invalid keypage: missing keypage id is mandatory");
            }
            if (keypageLabel == null) {
                errors.add("Invalid keypage: missing keypage label is mandatory");
            }
            if (keypageModule == null) {
                errors.add("Invalid keypage: missing keypage module is mandatory");
            }
            if (localKeypage == null) {
                errors.add("Invalid keypage: missing keypage localKeyPage is mandatory");
            }
            return new KeyPage(this);
        }

        public Builder setkeypageId(@Nullable KeyPageId keypageId) {
            this.keypageId = keypageId;
            return this;
        }

        public Builder setKeypageLabel(@Nullable KeyPageLabel keypageLabel) {
            this.keypageLabel = keypageLabel;
            return this;
        }

        public Builder setKeypageModule(@Nullable KeyPageModule keypageModule) {
            this.keypageModule = keypageModule;
            return this;
        }

        public Builder setLocalKeypage(@Nullable KeyPageLocalKeyPage localKeypage) {
            this.localKeypage = localKeypage;
            return this;
        }

        public Builder addKey(@Nonnull Key key) {
            this.keys.add(key);
            return this;
        }

        public Builder addKeys(@Nonnull List<Key> keys) {
            this.keys = keys;
            return this;
        }

    }
}
