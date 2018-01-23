package freakydevelopers.contorloverad.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import freakydevelopers.contorloverad.Adapter.TrainListAdapter;
import freakydevelopers.contorloverad.Database.MySQLiteOpenHelper;
import freakydevelopers.contorloverad.Pojo.Train;
import freakydevelopers.contorloverad.R;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TrainListActivity extends AppCompatActivity {

    private List<Train> trains;
    private Context context;
    private RecyclerView recyclerView;
    private TrainListAdapter trainListAdapter;
    LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Observable<Train> trainObservable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_list);
        initView();
        trains = (List<Train>) getIntent().getSerializableExtra("list");
        mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        trainListAdapter = new TrainListAdapter(context, trains);
        recyclerView.setAdapter(trainListAdapter);
        trainListAdapter.notifyDataSetChanged();
        initObserver();
    }

    private void initObserver() {
        trainObservable = Observable
                .create(new ObservableOnSubscribe<Train>() {
                    @Override
                    public void subscribe(ObservableEmitter<Train> e) throws Exception {
                        for (Train train : trains)
                            e.onNext(train);
                        e.onComplete();
                    }
                });
    }

    private void initView() {
        recyclerView = findViewById(R.id.recycler_view);
    }

    @Override
    protected void onStart() {
        super.onStart();
        trainObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Train>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Train train) {
                        MySQLiteOpenHelper mySQLiteOpenHelper = MySQLiteOpenHelper.getInstance(getApplicationContext());
                        mySQLiteOpenHelper.checkAndUpdate(train);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (compositeDisposable != null && !compositeDisposable.isDisposed())
            compositeDisposable.dispose();
    }
}
