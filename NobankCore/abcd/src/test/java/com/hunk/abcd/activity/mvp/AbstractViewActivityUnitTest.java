package com.hunk.abcd.activity.mvp;

import com.hunk.abcd.Testable;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.robolectric.Robolectric.setupActivity;

/**
 * @author HunkDeng
 * @since 2016/7/3
 */
public class AbstractViewActivityUnitTest extends Testable implements IFakeView<IFakePresenter> {
    /**
     * @see #setPresenter()
     */
    @Deprecated
    @Override
    public void setPresenter(IFakePresenter presenter) {
    }

    @Test
    public void setPresenter() {
        IFakeView<IFakePresenter> fakeView = getTestObj();
        IFakePresenter fakePresenter = getPresenter();

        assertNotNull(fakeView.getPresenter());

        fakeView.setPresenter(fakePresenter);
        assertEquals(fakePresenter, fakeView.getPresenter());
    }

    @Override
    public FakePresenter getPresenter() {
        return mock(FakePresenter.class);
    }

    @Test
    @Override
    public void showError(Throwable e) {

    }


    private FakeView getTestObj() {
        return setupActivity(FakeView.class);
    }
}
