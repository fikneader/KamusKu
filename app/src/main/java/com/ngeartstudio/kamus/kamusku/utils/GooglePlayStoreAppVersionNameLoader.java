package com.ngeartstudio.kamus.kamusku.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;

import org.jsoup.Jsoup;

public class GooglePlayStoreAppVersionNameLoader extends AsyncTask<String, Void, String> {

    private String currentVersion = "";
    private WSCallerVersionListener mWsCallerVersionListener;
    private boolean isVersionAvailabel;
    private boolean isAvailableInPlayStore;
    @SuppressLint("StaticFieldLeak")
    private Context mContext;
    private String mStringCheckUpdate = "";

    public GooglePlayStoreAppVersionNameLoader(Context mContext, WSCallerVersionListener callback) {
        mWsCallerVersionListener = callback;
        this.mContext = mContext;
    }

    @Override
    protected String doInBackground(String... urls) {
        try {
            isAvailableInPlayStore = true;
            if (isNetworkAvailable(mContext)) {
                mStringCheckUpdate = Jsoup.connect("https://play.google.com/store/apps/details?id=" + mContext.getPackageName()+ "&hl=en")
                        .timeout(10000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select("div.hAyfc:nth-child(4) > span:nth-child(2) > div:nth-child(1) > span:nth-child(1)")
                        .first()
                        .ownText();
                return mStringCheckUpdate;
            }

        } catch (Throwable e) {
            isAvailableInPlayStore = false;
            return mStringCheckUpdate;
        }
        return mStringCheckUpdate;
    }

    @Override
    protected void onPostExecute(String string) {
        if (isAvailableInPlayStore) {
            checkApplicationCurrentVersion();
            isVersionAvailabel = !currentVersion.equalsIgnoreCase(string);
            mWsCallerVersionListener.onGetResponse(isVersionAvailabel);
        }
    }

    /**
     * Method to check current app version
     */
    public void checkApplicationCurrentVersion() {
        PackageManager packageManager = mContext.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        currentVersion = packageInfo.versionName;
    }

    /**
     * Method to check internet connection
     */
    public boolean isNetworkAvailable(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
