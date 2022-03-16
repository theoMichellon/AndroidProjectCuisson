package com.example.projetandroid;

import static com.example.projetandroid.OutilCuisson.chaineEspace;
import static com.example.projetandroid.OutilCuisson.transformeEnChaine;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class Afficher extends Fragment{

    /** Nom du fichier contenant les pays de l'union européenne */
    private static final String NOM_FICHIER = "cuisson.txt";

    /**
     * Tag utilisé dans les messages de log. Les messages de log sont affichés en cas
     * de problème lors de l'accès au fichier
     */
    private static final String TAG = "RecetteTP";

    // Déclaration de la vue associée
    private View vueDuFragment;


    public ListView List_element;

    public TextView titre_list;

    public ArrayList<HashMap<String, String>> listItem;

    /** Liste des recettes présentés par l'application */
    private ArrayList<String> list_recette;;

    public Afficher() {
        // Required empty public constructor
    }

    /**
     * Cette méthode est une "factory" : son rôle est de créer une nouvelle instance
     * du fragment de type FragmentUn
     * @return A new instance of fragment FragmentUn.
     */
    public static Afficher newInstance() {
        Afficher fragment = new Afficher();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        vueDuFragment = inflater.inflate(R.layout.afficher, container, false);

        List_element = vueDuFragment.findViewById(R.id.List_element);
        titre_list = vueDuFragment.findViewById(R.id.titre_list);

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
        /*try {
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
        }*/


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
        SimpleAdapter mSchedule = new SimpleAdapter (this.getContext(), listItem, R.layout.affichage_item, new String[] {"ligne"},
                new int[] {R.id.ligne});

        //On attribue à notre listView l'adapter que l'on vient de créer
        List_element.setAdapter(mSchedule);

        registerForContextMenu(List_element);

        return vueDuFragment;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        new MenuInflater(getActivity()).inflate(R.menu.menu_context_item, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo information =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        // selon l'option sélectionnée dans le menu, on réalise le traitement adéquat
        switch(item.getItemId()) {
            case R.id.option1: // suppression de la ligne courrante
                // on supprime de l'adaptateur l'article courant
                //adaptateur.remove(listeAchat.get(information.position));
                break;
            case R.id.option2: // voir thermostat
                // récupération de la ligne de l'item
                String s = "" + listItem.get(information.position);
                // récupération du nom de plat et de la température
                int maxCharPlat = 27;  // nbre de caractères max pour plat
                int maxCharDegre = 4;  // nbre de caractères max pour degrées
                int length = s.length(); // récupération taille de la ligne
                String nomPlat = s.substring(7, maxCharPlat).trim();
                String degree = s.substring(length -maxCharDegre, length-1);
                // transformation String to int de la température
                int temperature = Integer.parseInt(degree.trim());
                // lancement message
                AlerteMessage(nomPlat, temperature);
                break;
            case R.id.option3: // annuler : retour à la list principale
                break;
        }
        return (super.onContextItemSelected(item));
    }

    /**
     * Méthode qui permet d'afficher l'alerte dialogue pour voir
     * le réglage du thermostat
     *
     * @param Plat : nom du plat
     * @param temperature : température du plat
     */
    private void AlerteMessage(String Plat, int temperature) {
        // création du message à afficher
        String message = getString(R.string.ad_message, Plat, temperature, calculTempérature(temperature));
        //lancement de l'alerte Dialogue
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.ad_titre)
                .setMessage(message)
                .setNeutralButton(R.string.ad_texteRetour, null)
                .show();
    }

    /**
     * Méthode qui permet de savoir quel thermostat utiliser selon la tempéréature
     *
     * @param temperature : température du plat
     * @return un int qui correspond au thermostat
     */
    private int calculTempérature(int temperature){
        // tableau des différents pallier pour le thermostat
        int[] pallierTemperature = {30,60,90,120,150,180,210,240,270};
        // retourne la valeur du thermostat
        if(temperature < pallierTemperature[0]){
            return 1;
        } else if(temperature < pallierTemperature[1]){
            return 2;
        } else if(temperature < pallierTemperature[2]){
            return 3;
        } else if(temperature < pallierTemperature[3]){
            return 4;
        } else if(temperature < pallierTemperature[4]){
            return 5;
        } else if(temperature < pallierTemperature[5] ){
            return 6;
        } else if(temperature < pallierTemperature[6]){
            return 7;
        } else if(temperature < pallierTemperature[7]){
            return 8;
        } else if(temperature < pallierTemperature[8]){
            return 9;
        } else if(temperature <= pallierTemperature[9]){
            return 10;
        }
        return 0;
    }
}