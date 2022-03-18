package com.example.projetandroid;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

/**
 * Cette classe activité gère 3 fragments qui seront affichés via un ViewPager
 * Le ViewPager gère le défilement entre les 3 fragments, défilement effectué
 * lorsque l'utilisteur fait un "glisser".
 * Le 1er fragment est codé de manière à générer un nombre aléatoire, les 2 suivants
 * affichent seulement un texte
 * @author Tatiana Borgi
 * @author Théo Michellon
 */
public class MainActivity extends AppCompatActivity implements Ajouter.EcouteurGeneration {

    // Déclaration du string permettant de gérer la recette à ajouter
    private String recetteAGerer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
         * on récupère un accès sur le ViewPager défini dans la vue (le layout)
         * ainsi que sur le TabLayout qui gèrera les onglets
         */
        ViewPager2 pager = findViewById(R.id.activity_main_viewpager);
        TabLayout gestionnaireOnglet = findViewById(R.id.tab_layout);

        /*
         * on associe au ViewPage un adaptateur (c'est lui qui organise le défilement
         * entre les fragments à afficher)
         */
        pager.setAdapter(new AdaptateurPage(this)) ;

        /*
         * On regroupe dans un tableau les intitulés des boutons d'onglet
         */
        String[] titreOnglet = {getString(R.string.onglet_afficher),
                getString(R.string.onglet_ajouter)};

        /*
         * On crée une instance de type TabLayoutMediator qui fera le lien entre
         * le gestionnaire de pagination et le gestionnaire des onglets
         * La méthode onConfigureTab permet de préciser quel initulé de bouton d'onglets
         * correspond à tel ou tel onglet, selon la position de celui-ci
         * L'instance TabLayoutMediator est attachée à l'activité courante
         *
         */
        new TabLayoutMediator(gestionnaireOnglet, pager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override public void onConfigureTab(TabLayout.Tab tab, int position) {
                        tab.setText(titreOnglet[position]);
                    }
                }).attach();
    }

    public String getRecetteAGerer() {
        return recetteAGerer;
    }

    @Override
    public void recevoirRecette(String recette) {
        /* on récupère, via le FragmentManager, un accès au fragment deux.
         * En interne, ce fragment a l'identifiant "f1". Cet identifiant est attributé
         * automatiquement par Android
         */
        Afficher fragmentAModifier =
                (Afficher) getSupportFragmentManager().findFragmentByTag("f1");
        /* Si l'utilisateur n'a pas encore activé l'onglet numéro 2, le fragment f1 n'existe pas
         * encore. Dans ce cas, fragmentAModifier est égal à null. On ne peut donc pas lui
         * envoyer le nombre aléatoire à afficher
         */
        if (fragmentAModifier != null) {
            fragmentAModifier.mettreAJourRecette(recetteAGerer);
        }
    }
}
