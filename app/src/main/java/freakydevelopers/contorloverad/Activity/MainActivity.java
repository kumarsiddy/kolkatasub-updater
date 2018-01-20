package freakydevelopers.contorloverad.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

import freakydevelopers.contorloverad.Database.MySQLiteOpenHelper;
import freakydevelopers.contorloverad.Pojo.Train;
import freakydevelopers.contorloverad.R;
import freakydevelopers.contorloverad.Utils.Logger;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Observable<Document> myObservable;
    private Observable<Document> myObservable2;
    private EditText enterURL;
    private Button parseBtn;
    private TextView textView;
    private List<Train> trains = new ArrayList<>();
    private AutoCompleteTextView fromStation, toStation;
    private Map<String, String> stationMap = new HashMap<>();
    private List<String> strings = new ArrayList<>();
    private Context context;
    private MySQLiteOpenHelper mySQLiteOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        initView();
        parseStations();
        isStoragePermissionGranted();
        mySQLiteOpenHelper = new MySQLiteOpenHelper(context.getApplicationContext());
    }

    private void initView() {
        fromStation = findViewById(R.id.from_station);
        toStation = findViewById(R.id.to_station);
        parseBtn = findViewById(R.id.parse);
        textView = findViewById(R.id.text);
        parseBtn.setOnClickListener(this);
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
    protected void onResume() {
        super.onResume();
        String query = "select * from db_information";

        try (SQLiteDatabase database = mySQLiteOpenHelper.getReadableDatabase()) {
            Cursor cursor = database.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    String s = cursor.getString(cursor.getColumnIndexOrThrow("dbdate"));
                    Logger.d(s);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                parseURL(getCompleteURL(fromStation.getText().toString(), toStation.getText().toString()));
                break;

        }
    }

    private void parseStations() {
        initObserver2();

        myObservable2
                .observeOn(AndroidSchedulers.mainThread())
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
                        ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, strings.toArray(new String[strings.size()]));
                        fromStation.setAdapter(adapter);
                        toStation.setAdapter(adapter);
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


                            Element element = document.select("table[class=TrainsList table table-bordered table-condensed ]").first();
                            final Element tagtbody = element.getElementsByTag("tbody").first();

                            final Elements rows = tagtbody.children();


                            for (int i = 1; i < rows.size(); ++i) {
                                String trainNo, trainName, arrivalTime, departureTime, runningDay;
                                final Element row = rows.get(i);

                                if (row.childNodeSize() == 8) {

                                    runningDay = row.child(0).child(0).child(0).child(1).child(1).text();
                                    trainNo = row.child(3).getElementsByTag("a").first().text();
                                    trainName = row.child(4).text();
                                    arrivalTime = row.child(5).text().replace(".", ":");
                                    departureTime = row.child(6).text().replace(".", ":");

                                    trains.add(new Train(
                                            trainName,
                                            trainNo,
                                            runningDay
                                    ));
                                } else {
                                    Logger.d("FUCK YOU!!!");
                                }

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
                        Intent intent = new Intent(getApplicationContext(), TrainListActivity.class);
                        intent.putExtra("list", (Serializable) trains);
                        startActivity(intent);
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
        } else { //permission is automatically granted on sdk<23 upon installation
            Logger.d("Permission is granted");
            return true;
        }
    }

}
