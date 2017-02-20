package com.mmazzarolo.dev.topgithub.utils;


import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by Arison on 2017/2/15.
 * 
 */
public class RxBus {

    private static volatile RxBus mDefaultInstance;
    
    private final Subject<Object, Object> _bus = new SerializedSubject<>(PublishSubject.create());
    
    public static RxBus getInstance() {
        if (mDefaultInstance == null) {
            synchronized (RxBus.class) {
                if (mDefaultInstance == null) {
                    mDefaultInstance = new RxBus();
                }
            }
        }
        return mDefaultInstance;
    }

    public void send(Object o) {
        _bus.onNext(o);
    }

    public Observable<Object> toObservable() {
        return _bus;
    }
}
