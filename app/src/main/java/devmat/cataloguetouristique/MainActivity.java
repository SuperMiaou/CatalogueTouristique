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
import android.widget.SearchView;
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
                    String map = etabli.getJSONObject(i).getString("map");

                    Etablissement eta = new Etablissement(id, name, adress, price, number, picture, content, ouverture, map);
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
