package freakydevelopers.contorloverad.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import freakydevelopers.contorloverad.Pojo.Train;
import freakydevelopers.contorloverad.Utils.Logger;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static freakydevelopers.contorloverad.Utils.Constants.SUN;
import static freakydevelopers.contorloverad.Utils.Constants.TRAINNAME;
import static freakydevelopers.contorloverad.Utils.Constants.TRAINNO;
import static freakydevelopers.contorloverad.Utils.Constants.TRAINTABLE;

/**
 * Created by PURUSHOTAM on 1/19/2018.
 */

public class DatabaseUpdater {

    private final Context context;
    private List<Train> trains;
    private Observable<Train> trainObservable;
    private CompositeDisposable compositeDisposable;
    private MySQLiteOpenHelper mySQLiteOpenHelper;
    private SQLiteDatabase database;

    public DatabaseUpdater(Context context, final List<Train> trains) {
        this.context = context;
        this.trains = trains;
        mySQLiteOpenHelper = new MySQLiteOpenHelper(context);
        trainObservable = Observable.
                create(new ObservableOnSubscribe<Train>() {
                    @Override
                    public void subscribe(ObservableEmitter<Train> e) throws Exception {
                        for (Train train : trains)
                            e.onNext(train);
                        e.onComplete();
                    }
                });
    }

    public void updateDatabasewithNewData() {

        trainObservable
                .observeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Observer<Train>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                        database = mySQLiteOpenHelper.getWritableDatabase();
                    }

                    @Override
                    public void onNext(Train train) {

                        String sql_1 = "select * from " + TRAINTABLE + " where " + TRAINNO + "=" + train.getTrainNo();
                        Cursor cursor = database.rawQuery(sql_1, null);
                        if (cursor != null && cursor.moveToFirst()) {
                            do {
                                String trainNo = cursor.getString(cursor.getColumnIndexOrThrow(TRAINNO));
                                String trainName = cursor.getString(cursor.getColumnIndexOrThrow(TRAINNAME));
                                boolean sun = cursor.getInt(cursor.getColumnIndexOrThrow(SUN)) == 1;
                                boolean mon = cursor.getInt(cursor.getColumnIndexOrThrow(SUN)) == 1;
                                boolean tue = cursor.getInt(cursor.getColumnIndexOrThrow(SUN)) == 1;
                                boolean wed = cursor.getInt(cursor.getColumnIndexOrThrow(SUN)) == 1;
                                boolean thu = cursor.getInt(cursor.getColumnIndexOrThrow(SUN)) == 1;
                                boolean fri = cursor.getInt(cursor.getColumnIndexOrThrow(SUN)) == 1;
                                boolean sat = cursor.getInt(cursor.getColumnIndexOrThrow(SUN)) == 1;

                                Logger.d(trainNo);

                            } while (cursor.moveToNext());
                        } else if (cursor == null) {
                            Logger.d(train.getTrainName() + train.getTrainNo());
                        }
                        cursor.close();

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (database != null) {
                            database.close();
                        }
                    }

                    @Override
                    public void onComplete() {
                        compositeDisposable.dispose();
                        if (database != null) {
                            database.close();
                        }
                    }
                });

    }


}
