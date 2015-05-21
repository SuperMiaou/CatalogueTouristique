package devmat.cataloguetouristique;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.util.XmlDom;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    ArrayAdapter<String> myAdapter;
    ArrayList<Etablissement> etablissements;
    ListView etablissementList;
    AQuery query;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    public static final String PREFS_NAME = "MyPrefsFile";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final String TAG = "GCMRelated";
    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    String regid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        //boolean dialogShown = settings.getBoolean("dialogShown", false);

        etablissementList = (ListView) findViewById(R.id.etablissement);

        etablissementList.setOnItemClickListener(this);

        query = new AQuery(this);

        asyncJson();

        if (settings.getBoolean("dialogShown", true)) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Autorisez-vous HOLLY Week à vous envoyer des notifications?");
            builder.setMessage("Les notifications peuvent inclure alertes, sons et pastilles d'icônes. Vous pouvez les configurer dans les réglages.");
            builder.setPositiveButton("Accepter", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (checkPlayServices()) {
                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                        regid = getRegistrationId(getApplicationContext());

                        if (regid.isEmpty()) {
                            //button.setEnabled(false);
                            new RegisterApp(getApplicationContext(), gcm, getAppVersion(getApplicationContext())).execute();
                        } else {
                            Toast.makeText(getApplicationContext(), "Device already Registered", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.i(TAG, "No valid Google Play Services APK found.");
                    }
                    dialog.cancel();
                }
            })
                    .setNegativeButton("Refuser", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("dialogShown", false);
            editor.commit();
        }

    }

    public void asyncJson(){

        //perform a Google search in just a few lines of code

        String url = "http://172.31.1.215:8888/CatalogueTouristique/Perpignan.json";
        query.ajax(url, JSONObject.class, this, "jsonCallback");

    }

    public void jsonCallback(String url, JSONObject json, AjaxStatus status){
        if(json != null){
            JSONArray etabli = json.optJSONArray("etablissements");

            etablissements = new ArrayList<>();
            for(int i = 0; i < etabli.length(); i++) {
                try {
                    int id = etabli.getJSONObject(i).getInt("id");
                    String name = etabli.getJSONObject(i).getString("name");
                    String adress = etabli.getJSONObject(i).getString("adress");
                    String price = etabli.getJSONObject(i).getString("price");
                    String number = etabli.getJSONObject(i).getString("number");
                    String picture = etabli.getJSONObject(i).getString("picture");
                    String content = etabli.getJSONObject(i).getString("content");
                    String ouverture = etabli.getJSONObject(i).getString("ouverture");
                    String site = etabli.getJSONObject(i).getString("site");
                    String map = etabli.getJSONObject(i).getString("map");

                    Etablissement eta = new Etablissement(id, name, adress, price, number, picture, content, ouverture, site, map);
                    etablissements.add(eta);

                } catch (JSONException e) {
                    Log.e("Catch ViewEtablissement", "Exception" + e.getMessage());
                    e.printStackTrace();
                }
            }

            //Log.d("Result:", etabli.toString());
            EtablissementAdapter adapter = new EtablissementAdapter(this, etablissements);
            etablissementList.setAdapter(adapter);

            //Log.d("Result:", json.toString());

        } else{
                Log.d("Error", json.toString());
        }

    }


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Getting SearchView from XML layout by id defined there - my_search_view in this case
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        // Getting id for 'search_plate' - the id is part of generate R file,
        // so we have to get id on runtime.
        int searchPlateId = searchView.getContext().getResources().getIdentifier("android:id/search_plate", null, null);
        // Getting the 'search_plate' LinearLayout.
        View searchPlate = searchView.findViewById(searchPlateId);
        // Setting background of 'search_plate' to earlier defined drawable.
        searchPlate.setBackgroundResource(R.drawable.textfield_searchview_holo_light);

        return super.onCreateOptionsMenu(menu);
    }
*/

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Gets the current registration ID for application on GCM service.
     * <p>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     *         registration ID.
     */
    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(getApplicationContext());
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getGCMPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return getSharedPreferences(MainActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent detail = new Intent(this, DetailEtablissement.class);
        detail.putExtra("etablissements", etablissements.get(position));
        startActivity(detail);
    }
}
