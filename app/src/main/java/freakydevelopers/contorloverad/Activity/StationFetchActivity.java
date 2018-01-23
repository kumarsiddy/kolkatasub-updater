package freakydevelopers.contorloverad.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import freakydevelopers.contorloverad.Database.MySQLiteOpenHelper;
import freakydevelopers.contorloverad.Pojo.Station;
import freakydevelopers.contorloverad.Pojo.Train;
import freakydevelopers.contorloverad.R;
import freakydevelopers.contorloverad.Utils.Logger;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class StationFetchActivity extends AppCompatActivity implements View.OnClickListener {

    private List<Train> trains;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Observable<Train> stationObservable;
    private MySQLiteOpenHelper mySQLiteOpenHelper;
    private Button startMagic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_fetch);
        trains = (List<Train>) getIntent().getSerializableExtra("list");
        mySQLiteOpenHelper = MySQLiteOpenHelper.getInstance(getApplicationContext());
        startMagic = findViewById(R.id.start_magic);
        startMagic.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        stationObservable = Observable.create(new ObservableOnSubscribe<Train>() {
            @Override
            public void subscribe(ObservableEmitter<Train> e) throws Exception {
                for (Train train : trains)
                    e.onNext(train);
                e.onComplete();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (compositeDisposable != null && !compositeDisposable.isDisposed())
            compositeDisposable.dispose();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_magic:
                startMagic();
                break;
        }
    }

    private void startMagic() {
        stationObservable
                .observeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Observer<Train>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Train train) {
                        Document doc = null;
                        try {
                            doc = Jsoup.connect(train.getLinkToSchedule()).get(); // URL shortened!
                            Element element = doc.select("table[class=RouteList table table-bordered table-condensed]").first();
                            if (element != null) {
                                final Element tagtbody = element.getElementsByTag("tbody").first();
                                final Elements rows = tagtbody.children();
                                List<Station> stations = new ArrayList<>();

                                for (int i = 1; i < rows.size(); ++i) {
                                    String stationName, stationCode, arrival;
                                    int stationId, distance, datePlus;
                                    Element row = rows.get(i);
                                    stationName = row.child(3).text();
                                    stationCode = row.child(2).text();
                                    if (i == 1)
                                        arrival = row.child(6).text().trim().replace(".", ":");
                                    else
                                        arrival = row.child(5).text().trim().replace(".", ":");
                                    stationId = mySQLiteOpenHelper.getStationId(stationCode.trim());
                                    distance = Integer.parseInt(row.child(8).text().trim());
                                    datePlus = Integer.parseInt(row.child(9).text().trim());
                                    stations.add(
                                            new Station(

                                            ));
//                                    Logger.d(train.getTrainNo() + "  " + stationName + "  " + stationCode + "  " + arrival + "  " + stationId + "  " + distance + "  " + datePlus);
                                }
                            } else {
                                Logger.d("Manual" + train.getTrainNo());
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


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
}
