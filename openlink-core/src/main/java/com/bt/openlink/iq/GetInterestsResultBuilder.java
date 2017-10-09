package com.bt.openlink.iq;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import javax.annotation.Nonnull;

import com.bt.openlink.type.Interest;
import com.bt.openlink.type.InterestId;

public abstract class GetInterestsResultBuilder<B extends GetInterestsResultBuilder, J, T extends Enum<T>> extends IQBuilder<B, J, T> {

    @Nonnull private final List<Interest> interests = new ArrayList<>();

    protected GetInterestsResultBuilder(final Class<T> typeClass) {
        super(typeClass);
    }

    @Nonnull
    @Override
    public String getExpectedIQType() {
        return "result";
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    public B addInterest(@Nonnull final Interest interest) {
        this.interests.add(interest);
        return (B) this;
    }

    @Nonnull
    public List<Interest> getInterests() {
        return interests;
    }

    @Override
    protected void validate() {
        super.validate();
        validateUniqueness(interestId -> {
            throw new IllegalStateException("Each interest id must be unique - " + interestId + " appears more than once");
        });
    }

    private void validateUniqueness(final Consumer<InterestId> errorConsumer) {
        for (int i = 0; i < interests.size(); i++) {
            final Optional<InterestId> interestIdOptional = interests.get(i).getId();
            for (int j = i + 1; j < interests.size(); j++) {
                if (interestIdOptional.isPresent() && interestIdOptional.equals(interests.get(j).getId())) {
                    errorConsumer.accept(interestIdOptional.get());
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
        validateUniqueness(interestId -> errors.add("Invalid get-interests result stanza; each interest id must be unique - " + interestId + " appears more than once"));
    }
}
