package com.example.projetandroid;

import static com.example.projetandroid.OutilCuisson.chaineEspace;
import static com.example.projetandroid.OutilCuisson.transformeEnChaine;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class Afficher extends AppCompatActivity{

    public ListView List_element;

    public TextView titre_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.afficher);

        List_element = findViewById(R.id.List_element);
        titre_list = findViewById(R.id.titre_list);

        // affichage élément en style colonne :
        // constante taille colonne - element.length

        // plat = 18
        // durrée = 8
        // degrés = 4
        /* *********************** */
        /* Affichage titre colonne */
        /* *********************** */
        //        Plat      Durée    Degrés
        titre_list.setText(chaineEspace(8) +"Plat" + chaineEspace(6) + "Durée" + chaineEspace(4) + "Degrés");


        String premierelement = transformeEnChaine("Pizza", 0, 22, -50) ;
        String secondElement = transformeEnChaine("Gratin dauphinois", 0, 50, 180);
        String troisiemeElement = transformeEnChaine("Tarte aux pommes", 0, 40, 205);


        String[] elementCuisson = {"Pizza | 0 h 22 | 205","Gratin dauphinois | 0 h 50 | 180", "Tarte aux pommes | 0 h 40 | 205"};

        //String[] elementCuisson = {};
        //SimpleAdapter adapter = new SimpleAdapter(this.getBaseContext(), listItem, R.layout.affichageitem,
        //new String[] {"text1"}, new int = R.id.text1);

        //ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, elementCuisson);

        /* ********************* */
        //Création de la ArrayList qui nous permettra de remplir la listView
        ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();

        //On déclare la HashMap qui contiendra les informations pour un item
        HashMap<String, String> map;

        //Création d'une HashMap pour insérer les informations du premier item de notre listView
        map = new HashMap<String, String>();
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
        SimpleAdapter mSchedule = new SimpleAdapter (this.getBaseContext(), listItem, R.layout.affichage_item,
                new String[] {"ligne"}, new int[] {R.id.ligne});

        //On attribue à notre listView l'adapter que l'on vient de créer
        List_element.setAdapter(mSchedule);

    }
}