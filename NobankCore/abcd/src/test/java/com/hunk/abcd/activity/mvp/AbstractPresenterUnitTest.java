package com.hunk.abcd.activity.mvp;

import android.support.annotation.NonNull;

import com.hunk.abcd.Testable;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

/**
 * @author HunkDeng
 * @since 2016/7/3
 */
public class AbstractPresenterUnitTest extends Testable implements BasePresenter<IFakeView> {

    /**
     * @see #attach()
     * @param view
     */
    @Deprecated
    @Override
    public void attach(IFakeView view) {}

    @Test
    public void attach() {
        IFakePresenter<IFakeView> fakePresenter = getTestObj();
        IFakeView fakeView = getView();
        fakePresenter.attach(fakeView);

        assertNotNull(fakePresenter.getView());
        assertEquals(fakeView, fakePresenter.getView());
    }

    @Test
    @Override
    public void detach() {
        FakePresenter fakePresenter = getTestObj();
        fakePresenter.attach(getView());
        fakePresenter.detach();

        assertNull(fakePresenter.getView());
    }

    @Override
    public FakeView getView() {
        return mock(FakeView.class);
    }

    @NonNull
    private FakePresenter getTestObj() {
        return new FakePresenter();
    }
}
