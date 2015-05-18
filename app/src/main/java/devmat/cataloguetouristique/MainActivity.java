package devmat.cataloguetouristique;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import java.util.ArrayList;

import devmat.cataloguetouristique.models.Etablissement;


public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    ArrayAdapter<String> myAdapter;
    ArrayList<Etablissement> etablissementArray;
    EtablissementManager etablissementManager;
    ListView listEtablissements;
    EtablissementAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etablissementManager = new EtablissementManager(this);

        EtablissementAPI.createInstance(this.getApplicationContext());
        listEtablissements = (ListView) findViewById(R.id.listEtabli);

        listEtablissements.setOnItemClickListener(this);


        myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        listEtablissements.setAdapter(myAdapter);
        listEtablissements.setTextFilterEnabled(true);

        showEtablissements();
    }

    public void showEtablissements() {
        adapter = new EtablissementAdapter(this, etablissementManager.getListEtablissements());
        listEtablissements.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        refreshListView();

        adapter = new EtablissementAdapter(this, etablissementManager.getListEtablissements());

        listEtablissements.setOnItemClickListener(this);
        listEtablissements.setAdapter(adapter);
        listEtablissements.setTextFilterEnabled(true);

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
            refreshListView();
        }

        return super.onOptionsItemSelected(item);
    }

    private void refreshListView() {
        EtablissementAPI.getInstance().viewEtablissementAPI(new EtablissementAPI.APIListener() {
            @Override
            public void callback() {
                etablissementArray = etablissementManager.getListEtablissements();
                EtablissementAdapter adapter = new EtablissementAdapter(MainActivity.this, etablissementArray);

                listEtablissements.setOnItemClickListener(MainActivity.this);
                listEtablissements.setAdapter(adapter);
                listEtablissements.setTextFilterEnabled(true);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
