package freakydevelopers.contorloverad.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Observable<Document> myObservable;
    private Observable<Document> myObservable2;
    private Button parseBtn, fetchStationBtn, saveToStorage;
    private List<Train> trains = new ArrayList<>();
    private AutoCompleteTextView fromStation, toStation;
    private Map<String, String> stationMap = new HashMap<>();
    private List<String> strings = new ArrayList<>();
    private Context context;
    private boolean whichBtn;
    //    private static final String URL = "http://kolkatalocaltrain.info/howrah-jn-railway-station"; // Howrah Junction
//    private static final String URL = "http://kolkatalocaltrain.info/sealdah-railway-station";   // Sealdah Junction
//    private static final String URL = "http://kolkatalocaltrain.info/bandel-jn-railway-station";  // Bandel Junction
//    private static final String URL = "http://kolkatalocaltrain.info/barddhaman-railway-station";   // Burdwan Junction
//    private static final String URL = "http://kolkatalocaltrain.info/dum-dum-railway-station";   // Dum Dum Junction
//    private static final String URL = "http://kolkatalocaltrain.info/lalgola-railway-station";   // Lalgola Junction
//    private static final String URL = "http://kolkatalocaltrain.info/shantipur-railway-station";   // Shantipur Junction
//    private static final String URL = "http://kolkatalocaltrain.info/gede-railway-station";   // Gede Junction
//    private static final String URL = "http://kolkatalocaltrain.info/ranaghat-jn-railway-station";   // Ranaghat Junction
//    private static final String URL = "http://kolkatalocaltrain.info/naihati-jn-railway-station";   // Naihati Junction
//    private static final String URL = "http://kolkatalocaltrain.info/barasat-jn-railway-station";   // Barasat  Junction
//    private static final String URL = "http://kolkatalocaltrain.info/hasanabad-jn-railway-station";   // Barasat  Junction
//    private static final String URL = "http://kolkatalocaltrain.info/ballygunge-jn-railway-station";   // Ballygunge  Junction
//    private static final String URL = "http://kolkatalocaltrain.info/sonarpur-jn-railway-station";   // Sonarpur  Junction
//    private static final String URL = "http://kolkatalocaltrain.info/canning-railway-station";   // Canning  Junction
//    private static final String URL = "http://kolkatalocaltrain.info/baruipur-jn-railway-station";   // Baruipur  Junction
//    private static final String URL = "http://kolkatalocaltrain.info/diamond-harbour-railway-station";   // Diamond Harbour Junction
//    private static final String URL = "http://kolkatalocaltrain.info/budge-budge-railway-station";   // Budge Budge Railway Station
//    private static final String URL = "http://kolkatalocaltrain.info/lakshmikantapur-railway-station";   // LakshmiKantpur Station
//    private static final String URL = "http://kolkatalocaltrain.info/namkhana-railway-station";   // Namkhana Station
//    private static final String URL = "http://kolkatalocaltrain.info/kamarkundu-railway-station";   // Kamarkundu Station
//    private static final String URL = "http://kolkatalocaltrain.info/seoraphuli-jn-railway-station";   // Seoraphulli Station
//    private static final String URL = "http://kolkatalocaltrain.info/santragachi-jn-railway-station";   // Santragachi Station
//    private static final String URL = "http://kolkatalocaltrain.info/kharagpur-jn-railway-station";   // Kharagpur Station
    private static final String URL = "http://kolkatalocaltrain.info/bangaon-jn-railway-station";   // Kharagpur Station


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        initView();
        parseStations();
        isStoragePermissionGranted();
    }

    private void initView() {
        fromStation = findViewById(R.id.from_station);
        toStation = findViewById(R.id.to_station);
        fromStation.setText(URL);
        toStation.setText(URL);
        parseBtn = findViewById(R.id.parse);
        fetchStationBtn = findViewById(R.id.fetch_station);
        saveToStorage = findViewById(R.id.save_database);
        parseBtn.setOnClickListener(this);
        fetchStationBtn.setOnClickListener(this);
        saveToStorage.setOnClickListener(this);
    }

    private void initObserver(final String url) {
        myObservable = Observable
                .create(new ObservableOnSubscribe<Document>() {
                    @Override
                    public void subscribe(ObservableEmitter<Document> e) throws Exception {
                        Document doc = null;
                        doc = Jsoup.connect(url).get(); // URL shortened!
                        e.onNext(doc);
                        e.onComplete();
                    }
                });

    }


    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
            Logger.d("Observer Destroyed!");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.parse:
                whichBtn = true;
                parseURL(fromStation.getText().toString());
                break;
            case R.id.fetch_station:
                whichBtn = false;
                parseURL(toStation.getText().toString());
                break;

        }
    }

    private void parseStations() {
        initObserver2();

        myObservable2
                .observeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Observer<Document>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Document document) {
                        Element element = document.select("[id=selectFrom]").first();
                        for (int i = 0; i < element.childNodeSize(); ++i) {
                            Element row = element.child(i);
                            strings.add(row.text());
                            stationMap.put(row.text(),
                                    row.attr("value"));
                        }

                    }


                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
//                        ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, strings.toArray(new String[strings.size()]));
//                        fromStation.setAdapter(adapter);
//                        toStation.setAdapter(adapter);
                    }
                });
    }

    private void initObserver2() {
        myObservable2 = Observable
                .create(new ObservableOnSubscribe<Document>() {
                    @Override
                    public void subscribe(ObservableEmitter<Document> e) throws Exception {
                        Document doc = null;
                        doc = Jsoup.parse(readFromAsset());
                        e.onNext(doc);
                        e.onComplete();
                    }
                });
    }

    private void parseURL(String url) {
        Logger.d("URL" + url);
        initObserver(url);
        myObservable
                .observeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Observer<Document>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Document document) {

                        try {
                            Element element = document.select("table[class=TrainsListPassing table table-bordered table-condensed]").first();
                            final Element tagtbody = element.getElementsByTag("tbody").first();

                            final Elements rows = tagtbody.children();


                            for (int i = 1; i < rows.size(); ++i) {
                                String trainNo, trainName, arrivalTime, departureTime, runningDay, linkToSchedule;
                                final Element row = rows.get(i); //It is denotion for tr

                               /* if (row.childNodeSize() == 12) {*/

                                runningDay = row.child(11).text();
                                String[] strings = row.child(1).text().split(",");
                                trainNo = strings[0].trim();
                                trainName = strings[1].trim();
                                Element link = row.child(1).getElementsByTag("a").first();
                                linkToSchedule = "http://kolkatalocaltrain.info" + link.attr("href");

                                trains.add(new Train(
                                        trainName,
                                        trainNo,
                                        runningDay,
                                        linkToSchedule
                                ));

//                                Logger.d(runningDay + " " + trainNo + " " + trainName + " " + linkToSchedule);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        if (whichBtn) {
                            Intent intent = new Intent(getApplicationContext(), TrainListActivity.class);
                            intent.putExtra("list", (Serializable) trains);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(getApplicationContext(), StationFetchActivity.class);
                            intent.putExtra("list", (Serializable) trains);
                            startActivity(intent);
                        }

                    }
                });
    }

    private String getCompleteURL(String from, String to) {
        Logger.d(from);
        Logger.d(to);
        String fromName, fromCode, toName, toCode;
        fromName = from.replace(" ", "-").toLowerCase();
        toName = to.replace(" ", "-").toLowerCase();
        fromCode = stationMap.get(from);
        toCode = stationMap.get(to);
        return "http://kolkatalocaltrain.info/" + fromName + "-to-" + toName + "?from=" + fromCode + "&to=" + toCode;
    }

    private String readFromAsset() {
        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    getAssets().open("station_list.txt")));

            // do reading, usually loop until end of file reading
            StringBuilder sb = new StringBuilder();
            String mLine = reader.readLine();
            while (mLine != null) {
                sb.append(mLine); // process line
                mLine = reader.readLine();
            }
            reader.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Logger.d("Permission is granted");
                return true;
            } else {
                Logger.d("Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else {
            //permission is automatically granted on sdk<23 upon installation
            Logger.d("Permission is granted");
            return true;
        }
    }

}
