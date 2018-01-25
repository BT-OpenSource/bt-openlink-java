package com.bt.openlink.type;

import java.util.List;

import javax.annotation.Nonnull;

public abstract class CallFeature extends Feature {

    private static final long serialVersionUID = 8051006650246959083L;

    protected CallFeature(@Nonnull final AbstractFeatureBuilder builder) {
        super(builder);
    }

    public abstract static class AbstractCallFeatureBuilder<B extends AbstractCallFeatureBuilder> extends AbstractFeatureBuilder<B> {
        public abstract CallFeature build(final List<String> parseErrors);
    }

}
