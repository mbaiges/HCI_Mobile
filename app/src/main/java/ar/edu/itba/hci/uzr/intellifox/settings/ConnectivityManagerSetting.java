package ar.edu.itba.hci.uzr.intellifox.settings;

import android.app.AppComponentFactory;
import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

import java.lang.ref.WeakReference;
import java.net.ConnectException;
import java.net.InetAddress;

import ar.edu.itba.hci.uzr.intellifox.R;

public class ConnectivityManagerSetting {

    private static WeakReference<AppCompatActivity> currentActivity;
    private static ConnectivityManager cm;
    private static ConnectivityManagerSetting instance;
    private static boolean isItTheFirstTimeWeAreShowingTheMessage;

    private ConnectivityManagerSetting(){

    }

    public static ConnectivityManagerSetting getInstance(){
        if(cm == null && currentActivity != null){
            Context auxContext = currentActivity.get().getApplicationContext();
            if(auxContext != null){
                cm = (ConnectivityManager)auxContext.getSystemService(Context.CONNECTIVITY_SERVICE);
                instance = new ConnectivityManagerSetting();
            }
        }
        return instance;
    }

    public static void setActivity(AppCompatActivity activity){
        currentActivity = new WeakReference<>(activity);
        isItTheFirstTimeWeAreShowingTheMessage = true;
    }
    
    public boolean isNetworkConnected() {
        return (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected());
    }

    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            Log.v("CM", "alta coneccion con" + ipAddr.toString());
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }

    public void noInternetError(){
        if(isItTheFirstTimeWeAreShowingTheMessage){
            displayConnectionErrorMessage(R.string.no_internet);
            isItTheFirstTimeWeAreShowingTheMessage = false;
        }
    }

    public void noConnectionError(){
        if(isItTheFirstTimeWeAreShowingTheMessage){
            displayConnectionErrorMessage(R.string.no_connection);
            isItTheFirstTimeWeAreShowingTheMessage = false;
        }
    }

    private void displayConnectionErrorMessage(Integer msgResource){
        AppCompatActivity auxActivity = currentActivity.get();
        if(auxActivity != null){
            View parentLayout = auxActivity.findViewById(android.R.id.content);
            Snackbar snackbar = Snackbar.make(parentLayout, auxActivity.getApplicationContext().getResources().getString(msgResource), Snackbar.LENGTH_INDEFINITE);
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(ContextCompat.getColor(auxActivity.getApplicationContext(), R.color.warning));
            snackbar.setAction("Dismiss.", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Call your action method here
                    snackbar.dismiss();
                }
            });

            TextView textView = (TextView)sbView.findViewById(com.google.android.material.R.id.snackbar_text);
            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_bell_off, 0, 0, 0);
            textView.setCompoundDrawablePadding(auxActivity.getResources().getDimensionPixelOffset(R.dimen.snackbar_icon_padding));

            snackbar.show();

        }
    }
}

