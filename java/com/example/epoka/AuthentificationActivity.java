package com.example.epoka;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AuthentificationActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentification);

        // Recherche des éléments EditText
        EditText util_id = findViewById(R.id.util_id);
        EditText util_mdp = findViewById(R.id.util_mdp);

        // Récupération du bouton et ajout d'un écouteur d'événements
        Button btnConnexion = findViewById(R.id.btnConnexion);
        btnConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Appel de la méthode connexion
                connexion(util_id.getText().toString(), util_mdp.getText().toString());
            }
        });
    }

    private void connexion(String id, String mdp) {
        if (id.isEmpty() || mdp.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        String urlServiceWeb = "http://" + getString(R.string.server) + "/epoka/connexion.php?util_id=" + id + "&util_mdp=" + mdp;
        String resultat = getServerDataTexteBrut(urlServiceWeb);

        if (!resultat.equals("")) {
            if (resultat.equals("authentification réussie")) {
                Toast.makeText(getApplicationContext(), "Authentification réussie", Toast.LENGTH_LONG).show();
                // Création de l'Intent avec l'ID et le mdp en extra
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("util_id", id); // Ajout de l'ID comme extra
                intent.putExtra("util_mdp", mdp); // Ajout du mdp comme extra
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "Utilisateur ou mot de passe incorrect", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Une erreur s'est produite lors de l'authentification", Toast.LENGTH_LONG).show();
        }
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
