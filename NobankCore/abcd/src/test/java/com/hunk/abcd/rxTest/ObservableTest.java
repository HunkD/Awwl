package com.hunk.abcd.rxTest;

import com.hunk.abcd.Testable;

import static java.lang.System.out;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import org.junit.Test;

/**
 * @author HunkDeng
 * @since 2016/7/8
 */
public class ObservableTest extends Testable {

    @Test
    public void callSequence() {
        Observable<String> objectObservable = Observable.just("ssss");
        objectObservable.observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                out.println("first subscriber: " + s);
            }
        });
        objectObservable.observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                out.println("second subscriber: " + s);
            }
        });
    }
}
