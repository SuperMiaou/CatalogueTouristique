package devmat.cataloguetouristique;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;

import devmat.cataloguetouristique.models.Etablissement;
import io.realm.Realm;

/**
 * Created by rcdsm on 18/05/15.
 */
public class EtablissementAPI {

    private static EtablissementAPI instance;

    private Context context;

    public static void createInstance(Context appContext) {
        instance = new EtablissementAPI(appContext);
    }

    public static EtablissementAPI getInstance() {
        return instance;
    }

    private EtablissementAPI(Context appContext) {
        this.context = appContext;
    }

    public void viewEtablissementAPI(final APIListener listener) {

        final AQuery aq = new AQuery(context);

        aq.ajax("http://172.31.1.215:8888/CatalogueTouristique/etablissement", JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {

                try {
                    Log.d("Etablissement", json.toString());
                    Log.d("Url :", url);

                    if (json.getString("success").equals("true")) {

                        Realm realm = Realm.getInstance(context);
                        realm.beginTransaction();

                        JSONArray etablissements = json.getJSONArray("etablissement");

                        for (int i = 0; i < etablissements.length(); i++) {

                            realm.createOrUpdateObjectFromJson(Etablissement.class, etablissements.getJSONObject(i));

                        }

                        realm.commitTransaction();

                        Log.d("ViewEtablissement", "ViewEtablissement ok");

                        listener.callback();

                    } else {
                        Log.e("Error etablissements api", json.toString());

                    }
                } catch (Exception e) {
                    Log.e("Catch ViewEtablissement", "Exception" + e.getMessage());
                }
            }
        });
    }

    public interface APIListener {
        void callback();
    }
}
