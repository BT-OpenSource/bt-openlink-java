package com.bt.openlink.type;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * List of recommended subscription status.<br>
 * Reference: XEP-0060 Publish-Subscribe Subscription States https://xmpp.org/extensions/xep-0060.html#substates
 */
public enum SubscriptionState {

    NONE("None"),
    PENDING("Pending"),
    UNCONFIGURED("Unconfigured"),
    SUBSCRIBED("Subscribed");

    @Nonnull private final String id;

    SubscriptionState(@Nonnull final String id) {
        this.id = id;
    }

    @Nonnull
    public String getId() {
        return id;
    }

    @Nonnull
    public static Optional<SubscriptionState> from(@Nullable final String value) {
        for (final SubscriptionState featureType : SubscriptionState.values()) {
            if (featureType.id.equalsIgnoreCase(value)) {
                return Optional.of(featureType);
            }
        }
        return Optional.empty();
    }

}
