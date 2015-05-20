package devmat.cataloguetouristique;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by rcdsm on 18/05/15.
 */
public class EtablissementAdapter extends BaseAdapter {
    Context context;
    ArrayList<Etablissement> etablissements;

    LayoutInflater inflater;

    public EtablissementAdapter(Context context, ArrayList<Etablissement> etablissements) {
        this.context = context;
        this.etablissements = etablissements;

        inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return etablissements.size();
    }

    @Override
    public Object getItem(int position) {
        return etablissements.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView==null) {
            convertView = inflater.inflate(R.layout.etablissement_item, null);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.etabliTitle);
            holder.price = (TextView) convertView.findViewById(R.id.price);
            holder.picture = (ImageView) convertView.findViewById(R.id.picture);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }


        // téléchargement async de l'image...
        final ImageView imgView = holder.picture;
        new AsyncTask<String,Void,Bitmap>() {

            @Override
            protected Bitmap doInBackground(String... params) {
                String url = params[0];

                return null;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);

                imgView.setImageBitmap(bitmap);
            }
        }.execute(etablissements.get(position).getPicture());

        try {
            Uri imgUri = Uri.parse(etablissements.get(position).getPicture());
            Log.d("Url image", "Uri: " + imgUri.toString());
            holder.picture.setImageURI(imgUri);
        } catch (Exception e) {
            Log.d("Url image", "Erreur");
        }

        holder.title.setText(etablissements.get(position).getName());
        holder.price.setText(etablissements.get(position).getPrice());
        //holder.picture.setImageURI(imgUri);

        return convertView;
    }

    class ViewHolder {
        public TextView title;
        public TextView price;
        public ImageView picture;
    }
}
