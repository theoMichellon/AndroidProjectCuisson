package com.example.projetandroid;

import static android.media.CamcorderProfile.get;
import static com.example.projetandroid.OutilCuisson.chaineEspace;
import static com.example.projetandroid.OutilCuisson.transformeEnChaine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class Afficher extends AppCompatActivity implements AdapterView.OnItemClickListener{

    /** Nom du fichier contenant les pays de l'union européenne */
    private static final String NOM_FICHIER = "cuisson.txt";

    /**
     * Tag utilisé dans les messages de log. Les messages de log sont affichés en cas
     * de problème lors de l'accès au fichier
     */
    private static final String TAG = "RecetteTP";

    public ListView List_element;

    public TextView titre_list;

    public ArrayList<HashMap<String, String>> listItem;

    /** Liste des recettes présentés par l'application */
    private ArrayList<String> list_recette;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.afficher);

        List_element = findViewById(R.id.List_element);
        titre_list = findViewById(R.id.titre_list);

        /* *********************** */
        /* Affichage titre colonne */
        titre_list.setText("Plat" + chaineEspace(10) + "Durée" + chaineEspace(6) + "Degrés"+ chaineEspace(2));


        String premierelement = transformeEnChaine("Pizza", 0, 22, 50) ;
        String secondElement = transformeEnChaine("Gratin dauphinois", 0, 50, 180);
        String troisiemeElement = transformeEnChaine("Tarte aux pommes", 0, 40, 205);


        String[] elementCuisson = {"Pizza | 0 h 22 | 205","Gratin dauphinois | 0 h 50 | 180", "Tarte aux pommes | 0 h 40 | 205"};

        /* ********************* */
        //Création de la ArrayList qui nous permettra de remplir la listView
        listItem = new ArrayList<HashMap<String, String>>();

        //On déclare la HashMap qui contiendra les informations pour un item
        HashMap<String, String> map;

        //Création d'une HashMap pour insérer les informations du premier item de notre listView
        map = new HashMap<String, String>();


        /* ********** */
        list_recette = new ArrayList<>();
        String recetteLu;
        final String SEPARATEUR = ";";
        try {
            InputStreamReader fichier =
                    new InputStreamReader(openFileInput(NOM_FICHIER));
            BufferedReader fichierTexte = new BufferedReader(fichier);

            while ( (recetteLu = fichierTexte.readLine())!= null ) {

                String ligne[] = recetteLu.split(SEPARATEUR);
                String nomPlat =ligne[0];
                int heures = Integer.parseInt(ligne[1]);
                int minutes = Integer.parseInt(ligne[2]);
                int température = Integer.parseInt(ligne[3]);
                String truc = transformeEnChaine(nomPlat, heures, minutes, température) ;

                map = new HashMap<String, String>();
                map.put("ligne", truc);
                listItem.add(map);
            }
            fichier.close();
        } catch (FileNotFoundException e) {
            Log.e(TAG, "Le fichier " + NOM_FICHIER + " n'existe pas.");
        } catch (IOException e) {
            Log.e(TAG, "Problème de lecture dans le fichier " + NOM_FICHIER);
        }


        //on insère un élément titre que l'on récupérera dans le textView titre créé dans le fichier affichageitem.xml
        map.put("ligne", premierelement);
        //enfin on ajoute cette hashMap dans la arrayList
        listItem.add(map);

        //On refait la manip plusieurs fois avec des données différentes pour former les items de notre ListView
        map = new HashMap<String, String>();
        map.put("ligne", secondElement);
        listItem.add(map);

        map = new HashMap<String, String>();
        map.put("ligne", troisiemeElement);
        listItem.add(map);

        //Création d'un SimpleAdapter qui se chargera de mettre les items présents dans notre list (listItem) dans la vue affichageitem
        SimpleAdapter mSchedule = new SimpleAdapter (this.getBaseContext(), listItem, R.layout.affichage_item, new String[] {"ligne"},
                new int[] {R.id.ligne});

        //On attribue à notre listView l'adapter que l'on vient de créer
        List_element.setAdapter(mSchedule);

        registerForContextMenu(List_element);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        new MenuInflater(this).inflate(R.menu.menu_context_item, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo information =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        // selon l'option sélectionnée dans le menu, on réalise le traitement adéquat
        switch(item.getItemId()) {
            case R.id.option1: // suppression de l'article courant
                // on supprime de l'adaptateur l'article courant
                //adaptateur.remove(listeAchat.get(information.position));
                break;
            case R.id.option2: // voir thermostat
                int i = information.position;
                //String valeur =  listItem.get(i);
                System.out.print("La positiion est :" +i + " et la valeur est :" );

                AlerteMessage("Pizza", 30);
                break;
            case R.id.option3: // annuler : retour à la list principale
                break;

        }
        return (super.onContextItemSelected(item));
    }

    private void AlerteMessage(String Plat, int temperature) {
        String message = getString(R.string.ad_message, Plat, temperature, calculTempérature(temperature));

        new AlertDialog.Builder(this)
                .setTitle(R.string.ad_titre)
                .setMessage(message)
                .setNeutralButton(R.string.ad_texteRetour, null)
                .show();
    }

    private int calculTempérature(int temperature){
        int[] pallierTemperature = {
                30,
                60,
                90,
                120,
                150,
                180,
                210,
                240,
                270
        };

        if(temperature < pallierTemperature[0]){
            return 1;
        } else if(temperature < pallierTemperature[1]){
            return 2;
        } else if(temperature < pallierTemperature[2]){
            return 3;
        } else if(90 <= temperature && temperature < pallierTemperature[3]){
            return 4;
        } else if(temperature < pallierTemperature[4]){
            return 5;
        } else if(temperature < pallierTemperature[5] ){
            return 6;
        } else if(temperature < pallierTemperature[6]){
            return 7;
        } else if(temperature < pallierTemperature[7] ){
            return 8;
        } else if(temperature < pallierTemperature[8]) {
            return 9;
        } else if(temperature <= pallierTemperature[9]){
            return 10;
        }
        return -1;
    }
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        // on affiche le nom du pays sélectionné dans le label
        String messageToast = "" + listItem.get(position);
        Toast.makeText(this, messageToast, Toast.LENGTH_LONG)
                .show();
        System.out.print(messageToast);
    }
}