package com.bt.openlink.type;

import javax.annotation.Nonnull;
import java.util.Optional;

public enum ManageVoiceMessageAction {

    CREATE("Create", "Creates a voice message playlist", 1, true, true),
    RECORD("Record", "Prepares a new message for Recording to replace any existing message, and gives a channel number to dial to record. The old message is kept for audit purposes", 0, false, true),
    EDIT("Edit", "Replaces only the label for voice message or voice message playlist", 1, false, true),
    PLAYBACK("Playback", "Prepares a Playback of recorded messages or voice message playlists, and gives number(s) to dial to playback", 1, true, false),
    SAVE("Save", "Saves data for a newly recorded message", 1, true, false),
    ARCHIVE("Archive", "Moves voice messages from the online store to the offline store", 1, true, false),
    DELETE("Delete", "Deletes voice messages or a voice message playlists", 1, true, false),
    QUERY("Query", "Returns the details of voice messages or playlists", 1, true, false),
    SEARCH("Search", "Returns the results of search operation on the voice messages datastore. A list words (separated by commas) to be used for searching the message labels", 1, true, true);


    @Nonnull private final String id;
    @Nonnull private final String label;
    private final int minFeaturesRequired;
    private final boolean allowMultipleFeatures;
    private final boolean requiresLabel;

    ManageVoiceMessageAction(@Nonnull final String id,
                             @Nonnull final String label,
                             final int minFeaturesRequired,
                             final boolean allowMultipleFeatures,
                             final boolean requiresLabel) {
        this.id = id;
        this.label = label;
        this.minFeaturesRequired = minFeaturesRequired;
        this.allowMultipleFeatures = allowMultipleFeatures;
        this.requiresLabel = requiresLabel;
    }

    @Nonnull
    public String getId() {
        return id;
    }

    @Nonnull
    public String getLabel() {
        return label;
    }

    public boolean requiresLabel() {
        return requiresLabel;
    }

    public int getMinFeaturesRequired() {
        return minFeaturesRequired;
    }

    public boolean allowsMultipleFeatures() {
        return allowMultipleFeatures;
    }

    public static Optional<ManageVoiceMessageAction> from(final String value) {
        for (final ManageVoiceMessageAction requestAction : ManageVoiceMessageAction.values()) {
            if (requestAction.id.equalsIgnoreCase(value)) {
                return Optional.of(requestAction);
            }
        }
        return Optional.empty();
    }
}
