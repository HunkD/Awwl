package com.hunk.abcd.rxTest;

import com.hunk.abcd.Testable;

import static java.lang.System.out;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import org.junit.Test;

/**
 * @author HunkDeng
 * @since 2016/7/8
 */
public class ObservableTest extends Testable {

    /**
     * Proof that each subscriber will run as list sync.
     */
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

    /**
     * Try to test and proof the difference between {@link Observable#subscribeOn(Scheduler)} and
     * {@link Observable#observeOn(Scheduler)}. <br>
     *
     * Follow link have a great explanation on this
     * http://tomstechnicalblog.blogspot.com/2016/02/rxjava-understanding-observeon-and.html
     */
    @Test
    public void callThread() {
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                out.println("emit runOn: " + Thread.currentThread().getName());
                for (int i = 0; i < 10; i++) {
                    out.println("emit str: hello" + i + ", runOn: " + Thread.currentThread().getName());
                    subscriber.onNext("hello" + i);
                }
            }
        });

        observable
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.newThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        out.println(s + ", runOn: " + Thread.currentThread().getName());
                    }
                });
    }
}
