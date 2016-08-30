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


    public <T> void register(Class<T> type, Action1 action1) {
        subscriptions = new CompositeSubscription();
        subscriptions.add(toObserverable(type).subscribe(action1));

        if (map == null) map = new HashMap<>();
        map.put(type.getSimpleName(), subscriptions);

    }

    public <T> void unregister(Class<T> type) {
        if (map != null) {
            if (!map.containsKey(type.getSimpleName())) {
                return;
            }
            map.get(type.getSimpleName()).unsubscribe();
            map.remove(type.getSimpleName());
        }
    }
}