package com.bt.openlink.type;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class KeyPageTest {

    private static final KeyPageId KEY_PAGE_ID = KeyPageId.from("KeyPageId1").get();
    private static final KeyPageLabel KEY_PAGE_LABEL = KeyPageLabel.from("KeyPage Label 1").get();
    private static final KeyPageLocalKeyPage KEY_PAGE_LOCAL_KEY_PAGE = KeyPageLocalKeyPage.from("1").get();
    private static final KeyPageModule KEY_PAGE_MODULE = KeyPageModule.from("2").get();
    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldBuildKeyPageInstance() {
        final KeyPage keyPage = KeyPage.Builder.start()
                .setkeypageId(KEY_PAGE_ID)
                .setKeypageLabel(KEY_PAGE_LABEL)
                .setLocalKeypage(KEY_PAGE_LOCAL_KEY_PAGE)
                .setKeypageModule(KEY_PAGE_MODULE)
                .build();
        assertThat(keyPage.getKeyPageId().get(), is(KEY_PAGE_ID));
        assertThat(keyPage.getKeyPageLabel().get(), is(KEY_PAGE_LABEL));
        assertThat(keyPage.getLocalKeyPage().get(), is(KEY_PAGE_LOCAL_KEY_PAGE));
        assertThat(keyPage.getKeyPageModule().get(), is(KEY_PAGE_MODULE));
    }

    @Test
    public void shouldFailWithoutMandatoryValues() {
        final List<String> errors = new ArrayList<>();
        KeyPage keyPage = KeyPage.Builder.start().build(errors);

        assertThat(errors, contains(
                "Invalid keypage: missing keypage id is mandatory",
                "Invalid keypage: missing keypage label is mandatory",
                "Invalid keypage: missing keypage module is mandatory",
                "Invalid keypage: missing keypage localKeyPage is mandatory"));
        assertThat(keyPage.getKeyPageId(), is(Optional.empty()));
        assertThat(keyPage.getKeyPageLabel(), is(Optional.empty()));
        assertThat(keyPage.getLocalKeyPage(), is(Optional.empty()));
        assertThat(keyPage.getKeyPageModule(), is(Optional.empty()));
    }

    @Test
    public void shouldFailWithoutKeyPageId() {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The keypage id has not been set");
        KeyPage.Builder.start().build();
    }

    @Test
    public void shouldFailWithoutKeyPageLabel() {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The keypage label has not been set");
        KeyPage.Builder.start()
                .setkeypageId(KEY_PAGE_ID)
                .build();
    }

    @Test
    public void shouldFailWithoutKeyPageModule() {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The keypage module has not been set");
        KeyPage.Builder.start()
                .setkeypageId(KEY_PAGE_ID)
                .setKeypageLabel(KEY_PAGE_LABEL)
                .build();
    }

    @Test
    public void shouldFailWithoutLocalKeyPage() {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The keypage localKeyPage has not been set");
        KeyPage.Builder.start()
                .setkeypageId(KEY_PAGE_ID)
                .setKeypageLabel(KEY_PAGE_LABEL)
                .setKeypageModule(KEY_PAGE_MODULE)
                .build();
    }
}