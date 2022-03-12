package com.example.projetandroid;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

public class Ajouter extends AppCompatActivity {

    // Déclaration du timePicker
    private TimePicker tempsCuisson;

    // Déclaration des EditText
    private EditText nomPlat;
    private EditText temperature;

    // Déclaration des boutons
    private Button effacer;
    private Button ajouter;

    // Déclaration des variables
    private int temperatureCuisson;

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
     * Méthode permettant d'appeler la feneêtre de dialogue en cas d'erreur
     */
    public void alerte() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.erreurTitre)
                .setMessage(R.string.texte)
                .setNeutralButton(R.string.btnRetour, null)
                .show();
    }

    /**
     * Méthode permettant de réinitilialiser chaque input de la vue
     */
    public void clicEffacer(View bouton) {

        // On efface le texte entré dans les editText
        nomPlat.setText("");
        temperature.setText("");

        // On remet le timePicker à 40 minutes
        tempsCuisson.setHour(00);
        tempsCuisson.setMinute(40);

    }

    public void clicAjouter(View bouton) {

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
                }
            }

        }
    }
}
