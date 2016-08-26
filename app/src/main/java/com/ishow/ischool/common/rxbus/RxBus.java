package com.ishow.ischool.common.rxbus;

import android.support.annotation.NonNull;

import java.util.HashMap;

import rx.Observable;
import rx.functions.Action1;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;
import rx.subscriptions.CompositeSubscription;

/**
 * courtesy: https://gist.github.com/benjchristensen/04eef9ca0851f3a5d7bf
 */
public class RxBus {


    private static volatile RxBus instance;
    private CompositeSubscription subscriptions;
    private HashMap<String, CompositeSubscription> map;

    // 单例RxBus
    public static RxBus getDefault() {
        if (instance == null) {
            synchronized (RxBus.class) {
                if (instance == null) {
                    instance = new RxBus();
                }
            }
        }
        return instance;
    }
    //private final PublishSubject<Object> _bus = PublishSubject.create();

    // If multiple threads are going to emit events to this
    // then it must be made thread-safe like this instead
    private final Subject<Object, Object> _bus = new SerializedSubject<>(PublishSubject.create());

    public void post(Object o) {
        _bus.onNext(o);
    }

    public Observable<Object> toObserverable() {
        return _bus;
    }

    public boolean hasObservers() {
        return _bus.hasObservers();
    }


    public void addSubscribe(@NonNull String key,Action1 action1) {
        subscriptions = new CompositeSubscription();
        subscriptions.add(toObserverable().subscribe(action1));

        if (map == null) map = new HashMap<>();
        map.put(key, subscriptions);

    }

    public void removeSubscribe(String key) {
       if (map!=null){
           map.get(key).unsubscribe();
           map.remove(key);
       }
    }
}