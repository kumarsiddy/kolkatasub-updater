package freakydevelopers.contorloverad.Utils;

import okhttp3.OkHttpClient;

/**
 * Created by PURUSHOTAM on 1/17/2018.
 */

public class MyOkHttpClient extends OkHttpClient {

    private static MyOkHttpClient myOkHttpClient;

    private MyOkHttpClient() {
        super();
    }

    public static MyOkHttpClient getInstance() {
        if (myOkHttpClient == null)
            myOkHttpClient = new MyOkHttpClient();
        return myOkHttpClient;
    }
}
