package baway.com.rxjavademo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


public class MainActivity extends AppCompatActivity {
    public static final String TAG = null;

    /**
     * subscribe()
     * 通常我们使用的是带(Observer)参数的subscribe(Observer)方法
     * 不带任何参数的subscribe()方法代表上游随便发,下游收到算我输
     * 带有一个Consumer的subscribe(Consumer)方法代表只接收onNext方法
     * 剩下类推
     * ObservableEmitter
     * Disposable
     * 调用dispose()方法上游还会接着走
     * 上游不会因为发送了onComplete而停止
     * 下游中onSubscribe()方法是最先调用的
     * 发射规则:
     * 上游可以发送无限个onNext, 下游也可以接收无限个 onNext.
     * 当上游发送了一个 onComplete 后, 上游 onComplete 之后的事件将会继续发送, 而下游收到onComplete事件之后将不再继续接收事件.
     * 当上游发送了一个 onError 后, 上游 onError 之后的事件将继续发送, 而下游收到 onError 事件之后将不再继续接收事件.
     * 上游可以不发送 onComplete 或 onError .
     * 最为关键的是 onComplete 和 onError 必须唯一并且互斥, 即不能发多个 onComplete , 也不能发多个 onError,
     * 也不能先发一个 onComplete , 然后再发一个 onError , 反之亦然
     * 注: 关于onComplete和onError唯一并且互斥这一点, 是需要自行在代码中进行控制, 如果你的代码逻辑中违背了这个规则,
     * 并不一定会导致程序崩溃*. 比如发送多个onComplete是可以正常运行的, 依然是收到第一个onComplete就不再接收了,
     * 但若是发送多个onError, 则收到第二个onError事件会导致程序会崩溃.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        //创建一个上游 observable
//        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
//            @Override
//            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
//                e.onNext(1);
//                e.onNext(2);
//                e.onNext(3);
//                e.onComplete();
//            }
//        });
//        //创建一个下游 observer
//        Observer<Integer> observer = new Observer<Integer>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//
//                Log.e(TAG, "subscribe");
//            }
//
//            @Override
//            public void onNext(Integer value) {
//                Log.e(TAG, "" + value);
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Log.e(TAG, "error");
//            }
//
//            @Override
//            public void onComplete() {
//                Log.e(TAG, "complete");
//            }
//        };
//
//        //建立连接
//        observable.subscribe(observer);



        //下面打印的 顺序为：
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                Log.d(TAG, "emit 1");
                 e.onNext(1);
                Log.d(TAG, "emit 2");
                 e.onNext(2);
                Log.d(TAG, "emit 3");
                e.onNext(3);
                Log.d(TAG, "emit complete");
                e.onComplete();
                Log.d(TAG, "emit 4");
                e.onNext(4);

            }
        }).subscribe(new Observer<Integer>() {
            private Disposable mDisposable;
            private int i;

            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "subscribe");
                mDisposable = d;
            }

            @Override
            public void onNext(Integer value) {
                Log.d(TAG, "onNext:" + value);
                i++;
                if (i == 2) {
                    Log.d(TAG, "dispose");
                    mDisposable.dispose();
                    Log.d(TAG, "isDisposed:" + mDisposable.isDisposed());
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "error");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "complete");
            }
        });

    }

}
