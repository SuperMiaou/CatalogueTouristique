package devmat.cataloguetouristique;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.util.XmlDom;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    ArrayList<Etablissement> etablissements;

    ListView etablissementList;
    AQuery query;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etablissementList = (ListView) findViewById(R.id.etablissement);

        etablissementList.setOnItemClickListener(this);

        query = new AQuery(this);

        asyncJson();

    }

    public void asyncJson(){

        //perform a Google search in just a few lines of code

        String url = "http://172.31.1.63:8888/EXERCICES_IMERIR/CatalogueTouristique/Perpignan.json";
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
                    String price = etabli.getJSONObject(i).getString("price");
                    String number = etabli.getJSONObject(i).getString("number");
                    String adress = etabli.getJSONObject(i).getString("adress");
                    String content = etabli.getJSONObject(i).getString("content");
                    String ouverture = etabli.getJSONObject(i).getString("ouverture");
                    String picture = etabli.getJSONObject(i).getString("picture");

                    Etablissement eta = new Etablissement(id, name, price, number, adress, content, ouverture, picture);
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
        if (id == R.id.action_refresh) {
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
