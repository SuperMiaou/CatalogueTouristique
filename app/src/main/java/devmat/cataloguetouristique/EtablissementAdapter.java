package devmat.cataloguetouristique;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import devmat.cataloguetouristique.models.Etablissement;

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
        return etablissements.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView==null) {
            convertView = inflater.inflate(R.layout.etab_item, null);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.etablTitle);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.title.setText(etablissements.get(position).getName());

        return convertView;
    }

    class ViewHolder {
        public TextView title;
    }
}
