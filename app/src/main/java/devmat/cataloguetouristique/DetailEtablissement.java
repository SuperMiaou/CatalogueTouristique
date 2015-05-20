package devmat.cataloguetouristique;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by rcdsm on 19/05/15.
 */
public class DetailEtablissement extends ActionBarActivity {

    Etablissement etablissement;

    TextView title, ouverture, content;
    ImageButton call, map, mail;
    ImageView picture;

    AQuery aq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.getSupportActionBar().setTitle("Détail de l'établissement");

        picture = (ImageView) findViewById(R.id.picture);
        call = (ImageButton) findViewById(R.id.call);
        map = (ImageButton) findViewById(R.id.map);
        mail = (ImageButton) findViewById(R.id.mail);
        title = (TextView) findViewById(R.id.title);
        ouverture = (TextView) findViewById(R.id.date);
        content = (TextView) findViewById(R.id.content);

        etablissement = (Etablissement) getIntent().getSerializableExtra("etablissements");

        aq = new AQuery(this);

        title.setText(etablissement.getName());
        ouverture.setText(etablissement.getOuverture());
        content.setText(etablissement.getContent());


        if(etablissement.getPicture()!=null && etablissement.getPicture().length()>0) {
            aq.id(picture).image(etablissement.getPicture());
        }

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+etablissement.getNumber()));
                startActivity(callIntent);
            }
        });

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(etablissement.getMap());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"chante.matthieu34@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "subject");
                intent.putExtra(Intent.EXTRA_TEXT, "mail body");
                startActivity(Intent.createChooser(intent, ""));
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
