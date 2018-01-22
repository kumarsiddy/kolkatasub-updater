package freakydevelopers.contorloverad.Database;

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

import freakydevelopers.contorloverad.Utils.Logger;

/**
 * Created by PURUSHOTAM on 1/18/2018.
 **/

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "database";
    private static final int VERSION = 100;
    private final String DB_PATH;
    private final Context context;
    private SQLiteDatabase mDataBase;

    public MySQLiteOpenHelper(Context context) {
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
            Cursor cursor = this.getReadableDatabase().rawQuery(query, null);
            cursor.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean openDataBase() throws SQLException {
        String mPath = DB_PATH + DB_NAME;
        //Log.v("mPath", mPath);
        mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
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
}
