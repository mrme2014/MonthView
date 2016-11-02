package com.ishow.ischool.common.rxbus;

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
    private HashMap<String, CompositeSubscription> mSubscriptionMap;

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

    /**
     * 返回指定类型的Observable实例
     *
     * @param type
     * @param <T>
     * @return
     */
    public <T> Observable<T> toObserverable(final Class<T> type) {
        return _bus.ofType(type);
    }

    public boolean hasObservers() {
        return _bus.hasObservers();
    }

    /**
     * 取消订阅
     *
     * @param o
     */
    public void unregister(Object o) {
        if (mSubscriptionMap == null) {
            return;
        }

        String key = o.getClass().getName();
        if (!mSubscriptionMap.containsKey(key)) {
            return;
        }
        if (mSubscriptionMap.get(key) != null) {
            mSubscriptionMap.get(key).unsubscribe();
        }

        mSubscriptionMap.remove(key);
    }

    /**
     * 注册
     * @param object
     * @param type
     * @param action1
     * @param <T>
     */
    public <T> void register(Object object, Class<T> type, Action1 action1) {

        if (mSubscriptionMap == null) {
            mSubscriptionMap = new HashMap<>();
        }
        String key = object.getClass().getName();
        if (mSubscriptionMap.get(key) != null) {
            mSubscriptionMap.get(key).add(toObserverable(type).subscribe(action1));
        } else {
            CompositeSubscription compositeSubscription = new CompositeSubscription();
            compositeSubscription.add(toObserverable(type).subscribe(action1));
            mSubscriptionMap.put(key, compositeSubscription);
        }

    }

}