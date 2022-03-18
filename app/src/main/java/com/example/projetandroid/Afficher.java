package com.example.projetandroid;

import static com.example.projetandroid.OutilCuisson.chaineEspace;
import static com.example.projetandroid.OutilCuisson.thermostat;
import static com.example.projetandroid.OutilCuisson.transformeEnChaine;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

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

    private ListView List_element;

    public TextView titre_list;

    public static ArrayList<String> listItem;

    /**
     * Adaptateur permettant de gérer la liste affichée
     */
    private ArrayAdapter<String> adaptateur;

    /**
     * Constructeur vide
     */
    public Afficher() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        String recetteLu;
        try {
            InputStreamReader fichier =
                    new InputStreamReader(getActivity().openFileInput(NOM_FICHIER));
            BufferedReader fichierTexte = new BufferedReader(fichier);

            while ( (recetteLu = fichierTexte.readLine())!= null ) {
                adaptateur.add(recetteLu);
            }
            fichier.close();
        } catch (FileNotFoundException e) {
            Log.e(TAG, "Le fichier " + NOM_FICHIER + " n'existe pas.");
        } catch (IOException e) {
            Log.e(TAG, "Problème de lecture dans le fichier " + NOM_FICHIER);
        }
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
        // déclaration fragement
        vueDuFragment = inflater.inflate(R.layout.afficher, container, false);

        // récupération des éléments
        List_element = vueDuFragment.findViewById(R.id.List_element);
        titre_list = vueDuFragment.findViewById(R.id.titre_list);

        /* Affichage titre colonne */
        titre_list.setText(chaineEspace(7) + "Plat" + chaineEspace(10) + "Durée" + chaineEspace(6) + "Degrés"+ chaineEspace(2));

        // données affichage brut
        String premierelement = transformeEnChaine("Pizza", 0, 22, 50) ;
        String secondElement = transformeEnChaine("Gratin dauphinois", 0, 50, 180);
        String troisiemeElement = transformeEnChaine("Tarte aux pommes", 0, 40, 205);


        /* ********************* */
        listItem = new ArrayList<String>();
        adaptateur = new ArrayAdapter<String>(this.getContext(), R.layout.affichage_item, listItem);

        List_element.setAdapter(adaptateur);
        // on précise qu'un menu est associé à la liste qui correspond à l'activité
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
                adaptateur.remove(listItem.get(information.position));
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
        String message = getString(R.string.ad_message, Plat, temperature, thermostat(temperature));
        //lancement de l'alerte Dialogue
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.ad_titre)
                .setMessage(message)
                .setNeutralButton(R.string.ad_texteRetour, null)
                .show();
    }

    /**
     *
     * @param recette
     */
    public void mettreAJourRecette(String recette) {
        //null
    }


}