package com.example.projetandroid;

import static com.example.projetandroid.OutilCuisson.transformeEnChaine;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Ajouter extends Fragment implements View.OnClickListener {

    // Déclaration de la vue
    private View vueDuFragment;

    // Déclaration de l'écouteur
    private EcouteurGeneration activiteRecette;

    // Déclaration du fichier dans lequel les écritures vont se faire
    private static final String NOM_FICHIER = "cuisson.txt";

    // Déclaration du StringBuilder permettant de construire le string à add
    private String recette;

    // Déclaration du timePicker
    private TimePicker tempsCuisson;

    // Déclaration des EditText
    private EditText nomPlat;
    private EditText temperature;

    // Déclaration des variables permettant de récupérer les valeurs
    private String plat;
    private String messsageToast;
    private int temperatureCuisson;
    private int heure,
            minutes;

    /**
     * Constructeur vide
     */
    public Ajouter() {
        // Required empty public constructor
    }

    /**
     * Ecouteur
     */
    public interface EcouteurGeneration {
        void recevoirRecette(String recette);
    }

    /**
     * Cette méthode est une "factory" : son rôle est de créer une nouvelle instance
     * du fragment de type FragmentUn
     * @return A new instance of fragment FragmentUn.
     */
    public static Ajouter newInstance() {
        Ajouter fragment = new Ajouter();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context contexte) {
        super.onAttach(contexte);
        // contexte est l'activité parente du fragment, donc l'activité principale
        activiteRecette = (EcouteurGeneration) contexte;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // On récupère la vue (le layout) associée au fragment un
        vueDuFragment = inflater.inflate(R.layout.ajouter, container, false);

        // On récupère un accès sur les widgets de la vue
        tempsCuisson = (TimePicker) vueDuFragment.findViewById(R.id.tempsCuisson);

        nomPlat = (EditText) vueDuFragment.findViewById(R.id.txtPlat);
        temperature = (EditText) vueDuFragment.findViewById(R.id.txtTemp);

        // Ecouteur permettant de détecter un clic sur un bouton
        vueDuFragment.findViewById(R.id.btnValider).setOnClickListener(this);
        vueDuFragment.findViewById(R.id.btnEffacer).setOnClickListener(this);

        // Formatage du spinner en h24 et non AM/PM
        tempsCuisson.setIs24HourView(true);
        tempsCuisson.setMinute(40);
        tempsCuisson.setHour(00);

        return vueDuFragment;
    }

    /**
     * Méthode permettant de créer la feneêtre de dialogue qui serra
     * appelé en cas d'erreur de saisie de la part de l'utilisateur
     */
    public void alerte() {
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.erreurTitre)
                .setMessage(R.string.texte)
                .setNeutralButton(R.string.btnRetour, null)
                .show();
    }

    /**
     * Méthode permettant de créer une chaîne et de l'insérer dans un fichier
     */
    public void persistance() {

        /* Ajout dans le fichier textes des nouvelles données */

        try {
            // déclaration et création de l'objet fichier
            FileOutputStream fichier = getActivity().openFileOutput("cuisson.txt", Context.MODE_PRIVATE);

            for (String recette : Afficher.listItem) {
                fichier.write(recette.getBytes());
            }

            //fichier.close();

        } catch (IOException ex) {
            System.out.println("Problème d'accès au fichier");
        }


    }


    /**
     * Méthode permettant de réinitilialiser chaque input de la vue à son état
     * intial.
     */
    public void effacer() {

        // On efface le texte entré dans les editText
        nomPlat.setText("");
        temperature.setText("");

        // On remet le timePicker à 40 minutes
        tempsCuisson.setHour(00);
        tempsCuisson.setMinute(40);

    }

    /**
     * Méthode permettant de vérifier que tous les champs ont été saisis
     * correctement.
     * Affichage d'un message d'erreur si jamais il y a une erreur de saisie
     * Sinon insertion dans un fichier texte de la nouvelle recette ajouté et
     * affichage d'un message toast validant l'action.
     */
    public void ajouter() {
        if (!nomPlat.getText().toString().isEmpty()) {
            if (nomPlat.getText().toString().indexOf('|') != -1) {
                alerte();
            } else if (tempsCuisson.getMinute() == 0
                    && tempsCuisson.getHour() == 0) {
                alerte();
            } else if (tempsCuisson.getHour() >= 9) {
                alerte();
            } else if (!temperature.getText().toString().isEmpty()) {
                temperatureCuisson = Integer.parseInt(temperature.getText()
                        .toString());
                if (temperatureCuisson < 1 || temperatureCuisson > 300) {
                    alerte();
                } else {
                    plat = nomPlat.getText().toString();
                    heure = tempsCuisson.getHour();
                    minutes = tempsCuisson.getMinute();

                    // On prépare le message à afficher dans le toast en cas de validation
                    messsageToast = String.format(getResources()
                            .getString(R.string.ajoute), plat);

                    Toast.makeText(getActivity(), messsageToast, Toast.LENGTH_LONG)
                            .show();

                    /* On insère toutes les valeurs dans le stringBuilder */
                    recette = transformeEnChaine(plat,heure, minutes, temperatureCuisson);
                    Afficher.listItem.add(recette);

                    persistance();

                }
            } else {
                alerte();
            }
        } else {
            alerte();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnEffacer:
                effacer();
                break;

            case R.id.btnValider:
                ajouter();
                break;
        }
    }
}