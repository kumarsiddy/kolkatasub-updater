package freakydevelopers.contorloverad.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import freakydevelopers.contorloverad.Pojo.Train;
import freakydevelopers.contorloverad.Pojo.TrainDay;
import freakydevelopers.contorloverad.Utils.Logger;

/**
 * Created by PURUSHOTAM on 1/18/2018.
 **/

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    private static MySQLiteOpenHelper mySQLiteOpenHelper;
    private static final String DB_NAME = "database";
    private static final int VERSION = 100;
    private final String DB_PATH;
    private final Context context;
    private SQLiteDatabase mDataBase;

    //Constants for the Table
    private static final String TRAINID = "_id";
    private static final String TRAINTABLE = "train_table";
    private static final String TRAINNO = "trainNO";
    private static final String TRAINNAME = "trainName";


    private static final String ROUTETABLE = "route_table";
    private static final String ROUTETRAINID = "trainId";

    private static final String STATIONTABLE = "station_table";
    private static final String STATIONCODE = "stationCode";
    private static final String STATIONNAME = "stationName";

    private static final String MON = "mon";
    private static final String TUE = "tue";
    private static final String WED = "wed";
    private static final String THU = "thu";
    private static final String FRI = "fri";
    private static final String SAT = "sat";
    private static final String SUN = "sun";

    /*private Observable<String> updateObservable;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();*/

    private List<String> queries = new ArrayList<>();

    public static MySQLiteOpenHelper getInstance(Context context) {
        if (mySQLiteOpenHelper == null)
            mySQLiteOpenHelper = new MySQLiteOpenHelper(context);
        return mySQLiteOpenHelper;
    }

    private MySQLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, VERSION);

        if (android.os.Build.VERSION.SDK_INT >= 17) {
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        } else {
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        }
        this.context = context;

        boolean mDataBaseExist = checkDataBase();
        Logger.d(mDataBaseExist + "");
        if (!mDataBaseExist) {
            mDataBase = this.getReadableDatabase();
            this.close();
            try {
                //Copy the database from assests
                copyDataBase();
                Logger.d("createDatabase database created");
            } catch (IOException mIOException) {
                throw new Error("ErrorCopyingDataBase");
            }
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    private void copyDataBase() throws IOException {
        InputStream mInput = context.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream mOutput = new FileOutputStream(outFileName);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer)) > 0) {
            mOutput.write(mBuffer, 0, mLength);
        }
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    private boolean checkDataBase() {
        File dbFile = new File(DB_PATH + DB_NAME);
        Logger.d("dbFile" + dbFile + "   " + dbFile.exists());

        String query = "select * from db_information";

        try {
            openDataBase();
            Cursor cursor = mDataBase.rawQuery(query, null);
            cursor.close();
            close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean openDataBase() throws SQLException {
        String mPath = DB_PATH + DB_NAME;
        //Log.v("mPath", mPath);
        mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.OPEN_READWRITE);
        //mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        return mDataBase != null;
    }

    @Override
    public synchronized void close() {
        if (mDataBase != null)
            mDataBase.close();
        super.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void checkAndUpdate(Train train) {
        openDataBase();
        String query = "select * from " + TRAINTABLE + " where " + TRAINNO + " =" + train.getTrainNo();
        try {
            Cursor cursor = mDataBase.rawQuery(query, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String trainNo = cursor.getString(cursor.getColumnIndexOrThrow(TRAINNO));
                    String trainName = cursor.getString(cursor.getColumnIndexOrThrow(TRAINNAME));
                    boolean sun = cursor.getInt(cursor.getColumnIndexOrThrow(SUN)) == 1;
                    boolean mon = cursor.getInt(cursor.getColumnIndexOrThrow(MON)) == 1;
                    boolean tue = cursor.getInt(cursor.getColumnIndexOrThrow(TUE)) == 1;
                    boolean wed = cursor.getInt(cursor.getColumnIndexOrThrow(WED)) == 1;
                    boolean thu = cursor.getInt(cursor.getColumnIndexOrThrow(THU)) == 1;
                    boolean fri = cursor.getInt(cursor.getColumnIndexOrThrow(FRI)) == 1;
                    boolean sat = cursor.getInt(cursor.getColumnIndexOrThrow(SAT)) == 1;
                    TrainDay trainDay = train.getTrainDay();

                    boolean check = false;


                    if (sun != trainDay.isSun()) {
                        Logger.d("NON MATCH SUNDAY" + trainNo);
                        check = true;
                    } else if (mon != trainDay.isMon()) {
                        Logger.d("NON MATCH MONDAY" + trainNo);
                        check = true;
                    } else if (tue != trainDay.isTue()) {
                        Logger.d("NON MATCH TUESDAY" + trainNo);
                        check = true;
                    } else if (wed != trainDay.isWed()) {
                        Logger.d("NON MATCH WEDNESDAY" + trainNo);
                        check = true;
                    } else if (thu != trainDay.isThu()) {
                        Logger.d("NON MATCH THURSDAY" + trainNo);
                        check = true;
                    } else if (fri != trainDay.isFri()) {
                        Logger.d("NON MATCH FRIDAY" + trainNo);
                        check = true;
                    } else if (sat != trainDay.isSat()) {
                        Logger.d("NON MATCH SATURDAY" + trainNo);
                        check = true;
                    }

                    if (check) {
                        int s = (trainDay.isSun()) ? 1 : 0;
                        int m = (trainDay.isMon()) ? 1 : 0;
                        int t = (trainDay.isTue()) ? 1 : 0;
                        int w = (trainDay.isWed()) ? 1 : 0;
                        int th = (trainDay.isThu()) ? 1 : 0;
                        int f = (trainDay.isFri()) ? 1 : 0;
                        int sa = (trainDay.isSat()) ? 1 : 0;
                        ContentValues values = new ContentValues();
                        values.put(SUN, s);
                        values.put(MON, m);
                        values.put(TUE, t);
                        values.put(WED, w);
                        values.put(THU, th);
                        values.put(FRI, f);
                        values.put(SAT, sa);
                        String where = TRAINNO + "=?";
                        String[] whereArgs = new String[]{String.valueOf(trainNo)};
                        mDataBase.update(TRAINTABLE, values, where, whereArgs);
                    }
                    close();
                } while (cursor.moveToNext());
            } else {
                Logger.d("NONAVAILABILITY " + train.getTrainNo());
                TrainDay trainDay = train.getTrainDay();
                int sun = 0, mon = 0, tue = 0, wed = 0, thu = 0, fri = 0, sat = 0;
                if (trainDay.isSun())
                    sun = 1;
                if (trainDay.isMon())
                    mon = 1;
                if (trainDay.isTue())
                    tue = 1;
                if (trainDay.isWed())
                    wed = 1;
                if (trainDay.isThu())
                    thu = 1;
                if (trainDay.isFri())
                    fri = 1;
                if (trainDay.isSat())
                    sat = 1;
                String updateQuery = "Insert into "
                        + TRAINTABLE
                        + " ("
                        + TRAINNO + ","
                        + TRAINNAME + ","
                        + SUN + "," + MON + "," + TUE + "," + WED + "," + THU + "," + FRI + "," + SAT + ")"
                        + " values (" + train.getTrainNo() + ","
                        + "\""
                        + train.getTrainName() + "\","
                        + sun + ","
                        + mon + ","
                        + tue + ","
                        + wed + ","
                        + thu + ","
                        + fri + ","
                        + sat
                        + ")";
                openDataBase();
                mDataBase.execSQL(updateQuery);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    public void checkAndUpdateTrain(Train train) {
        openDataBase();
        try {
            String updateStationQry = "select * from " + ROUTETABLE + " r inner join(select _id from " + TRAINTABLE + " where trainNO=?)t on r.trainId=t._id;";
            String[] args = {train.getTrainNo()};
            Cursor cursor = mDataBase.rawQuery(updateStationQry, args);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    public int getStationId(String stationCode) {
        openDataBase();
        try {
            int stationId = 0;
            String query = "select _id from " + STATIONTABLE + " where " + STATIONCODE + "=\"" + stationCode.trim() + "\"";
            Cursor cursor = mDataBase.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    stationId = cursor.getInt(cursor.getColumnIndexOrThrow(TRAINID));
                } while (cursor.moveToNext());
            }
            cursor.close();
            return stationId;
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            close();
        }
        return 0;
    }


}
