package devmat.cataloguetouristique;

import android.content.Context;

import java.util.ArrayList;

import devmat.cataloguetouristique.models.Etablissement;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by rcdsm on 18/05/15.
 */
public class EtablissementManager {

    protected Realm realm;

    public EtablissementManager(Context context) {
        realm = Realm.getInstance(context);

        getListEtablissements();
    }

    public ArrayList<Etablissement> getListEtablissements() {

        RealmResults<Etablissement> query = realm.where(Etablissement.class).findAll();

        ArrayList<Etablissement> etablissements = new ArrayList<>();

        for(Etablissement etablissement : query) {
            etablissements.add(etablissement);
        }

        return etablissements;
    }
}
