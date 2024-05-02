package com.example.epoka;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity {

    // Déclaration des variables
    String util_id;
    String util_mdp;
    TextView bienvenueId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialisation des variables
        util_id = getIntent().getStringExtra("util_id");
        util_mdp = getIntent().getStringExtra("util_mdp");
        bienvenueId = findViewById(R.id.bienvenueNom);

        // Appel de la méthode bienvenue
        bienvenue(util_id);

        // Récupération du bouton et ajout d'un écouteur d'événements
        Button btnQuitter= findViewById(R.id.Quitter);
        btnQuitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Appel de la méthode Quitter
                Quitter();
            }
        });

        Button btnAjoutMission = findViewById(R.id.AjoutMission);
        btnAjoutMission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Appel de la méthode Quitter
                AjoutMission();
            }
        });
    }

    private void bienvenue(String id) {

        String urlServiceWeb = "http://" + getString(R.string.server) + "/epoka/bienvenue.php?util_id=" + id ;
        String resultat = getServerDataTexteBrut(urlServiceWeb);

        if (resultat != null && !resultat.isEmpty()) {
            // Définition du texte dans le TextView
            bienvenueId.setText("Bonjour " + resultat);
        } else {
            Toast.makeText(getApplicationContext(), "Une erreur s'est produite", Toast.LENGTH_LONG).show();
        }
    }

    private void Quitter() {
        finish(); // Ferme l'activité en cours
        System.exit(0); // Ferme complètement l'application
    }

    private void AjoutMission() {
        // Création de l'Intent avec l'ID et le mdp en extra
        Intent intent = new Intent(getApplicationContext(), MissionActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("util_id", util_id); // Ajout de l'ID comme extra
        intent.putExtra("util_mdp", util_mdp); // Ajout du mdp comme extra
        startActivity(intent);
    }

    private String getServerDataTexteBrut(String urlString) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            // Lire la réponse du serveur
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Renvoyer les données brutes du serveur
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
