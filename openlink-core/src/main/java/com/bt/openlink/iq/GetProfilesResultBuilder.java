package com.bt.openlink.iq;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import javax.annotation.Nonnull;

import com.bt.openlink.type.Profile;
import com.bt.openlink.type.ProfileId;

public abstract class GetProfilesResultBuilder<B extends GetProfilesResultBuilder, J, T extends Enum<T>> extends IQBuilder<B, J, T> {

    @Nonnull private List<Profile> profiles = new ArrayList<>();

    protected GetProfilesResultBuilder(final Class<T> typeClass) {
        super(typeClass);
    }

    @Nonnull
    @Override
    public String getExpectedIQType() {
        return "result";
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    public B addProfile(@Nonnull final Profile profile) {
        this.profiles.add(profile);
        return (B) this;
    }

    @Nonnull
    public List<Profile> getProfiles() {
        return profiles;
    }

    @Override
    protected void validate() {
        super.validate();
        validateUniqueness(profileId -> {
            throw new IllegalStateException("Each profile id must be unique - " + profileId + " appears more than once");
        });
    }

    private void validateUniqueness(final Consumer<ProfileId> errorConsumer) {
        for (int i = 0; i < profiles.size(); i++) {
            final Optional<ProfileId> profileIdOptional = profiles.get(i).getId();
            for (int j = i + 1; j < profiles.size(); j++) {
                if (profileIdOptional.isPresent() && profileIdOptional.equals(profiles.get(j).getId())) {
                    errorConsumer.accept(profileIdOptional.get());
                }
            }
        }
    }

    @Override
    public void validate(final List<String> errors) {
        validate(errors, true);
    }

    protected void validate(List<String> errors, boolean checkIQFields) {
        if (checkIQFields) {
            super.validate(errors);
        }
        validateUniqueness(profileId -> errors.add("Invalid get-profiles request stanza; each profile id must be unique - " + profileId + " appears more than once"));
    }
}