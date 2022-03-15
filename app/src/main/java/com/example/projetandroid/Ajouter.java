package com.example.projetandroid;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class Ajouter extends AppCompatActivity {

    // Déclaration du fichier dans lequel les écritures vont se faire
    private static final String NOM_FICHIER = "cuisson.txt";

    // Déclaration du StringBuilder permettant de construire le string à add
    private StringBuilder recette = new StringBuilder();

    // Déclaration du timePicker
    private TimePicker tempsCuisson;

    // Déclaration des EditText
    private EditText nomPlat;
    private EditText temperature;

    // Déclaration des boutons
    private Button effacer;
    private Button ajouter;

    // Déclaration des variables permettant de récupérer les valeurs
    private String plat;
    private String messsageToast;
    private int temperatureCuisson;
    private int heure,
            minutes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ajouter);

        // On récupère un accès sur les widgets de la vue
        tempsCuisson = (TimePicker) findViewById(R.id.tempsCuisson);

        nomPlat = (EditText) findViewById(R.id.txtPlat);
        temperature = (EditText) findViewById(R.id.txtTemp);

        effacer = (Button) findViewById(R.id.btnEffacer);
        ajouter = (Button) findViewById(R.id.btnValider);

        // Formatage du spinner en h24 et non AM/PM
        tempsCuisson.setIs24HourView(true);
        tempsCuisson.setMinute(40);
        tempsCuisson.setHour(00);
    }

    /**
     * Méthode permettant de créer la feneêtre de dialogue qui serra
     * appelé en cas d'erreur de saisie de la part de l'utilisateur
     */
    public void alerte() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.erreurTitre)
                .setMessage(R.string.texte)
                .setNeutralButton(R.string.btnRetour, null)
                .show();
    }

    /**
     *
     */
    public void persistance(String plat, int heure, int minutes, int temperature) {

        /* On insère toutes les valeurs dans le stringBuilder */
        recette.append(plat);
        recette.append(';');
        recette.append(heure);
        recette.append(';');
        recette.append(minutes);
        recette.append(';');
        recette.append(temperature);

        /* Ajout dans le fichier textes des nouvelles données */

        try {
            String coucou = "coucou";
            // déclaration et création de l'objet fichier
            // OutputStreamWriter fichier = new OutputStreamWriter(this.openFileOutput("cuisson.txt"));
            FileOutputStream fichier = openFileOutput("cuisson.txt", Context.MODE_PRIVATE);
            fichier.write(coucou.getBytes());
            fichier.close();

        } catch (IOException ex) {
            System.out.println("Problème d'accès au fichier");
        }
    }


    /**
     * Méthode permettant de réinitilialiser chaque input de la vue à son état
     * intial.
     * @param bouton bouton sur lequel on clique pour appeler cette méthode
     */
    public void clicEffacer(View bouton) {

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
     *
     * @param bouton bouton sur lequel on clique pour appeler cette méthode
     */
    public void clicAjouter(View bouton) {

        // On prépare le message à afficher dans le toast en cas de validation
        messsageToast = String.format(getResources()
                .getString(R.string.ajoute), plat);

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

                    persistance(plat, heure, minutes, temperatureCuisson);
                    Toast.makeText(this, messsageToast, Toast.LENGTH_LONG)
                            .show();
                }
            } else {
                alerte();
            }
        } else {
            alerte();
        }
    }
}