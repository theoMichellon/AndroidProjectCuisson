package com.example.projetandroid;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

/**
 * Cette classe activité gère 3 fragments qui seront affichés via un ViewPager
 * Le ViewPager gère le défilement entre les 3 fragments, défilement effectué
 * lorsque l'utilisteur fait un "glisser".
 * Le 1er fragment est codé de manière à générer un nombre aléatoire, les 2 suivants
 * affichent seulement un texte
 * @author Tatiana Borgi
 * @author Théo Michellon
 */
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // on récupère un accès sur le ViewPager défini dans la vue (le layout)
        ViewPager2 pager = findViewById(R.id.activity_main_viewpager);

        /*
         * on associe au ViewPage un adaptateur (c'est lui qui organise le défilement
         * entre les fragments à afficher)
         */
        pager.setAdapter(new AdaptateurPage(this)) ;
    }
}
