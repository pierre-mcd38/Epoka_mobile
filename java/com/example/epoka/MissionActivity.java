package com.example.epoka;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MissionActivity extends Activity {

    String util_id;
    String util_mdp;
    EditText dateDepart;
    EditText dateRetour;
    Spinner ville;
    int idCom;
    List<LibelleEtNo> list = new ArrayList<LibelleEtNo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission);

        // Initialisation des variables
        util_id = getIntent().getStringExtra("util_id");
        util_mdp = getIntent().getStringExtra("util_mdp");

        dateDepart = findViewById(R.id.dateDepart);
        dateRetour = findViewById(R.id.dateRetour);

        // Récupération des données JSON depuis le service web
        String jsonString = getServerDataTexteBrut("http://" + getString(R.string.server) + "/epoka/commune.php");

        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            // Extraction des noms des villes
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String nom = jsonObject.getString("com_nom");
                int id = jsonObject.getInt("com_id");
                list.add(new LibelleEtNo(nom, id));
            }
            // Création de l'adaptateur avec la liste triée
            ArrayAdapter<LibelleEtNo> adapter = new ArrayAdapter<LibelleEtNo>(this,
                    android.R.layout.simple_spinner_item, list);
            // Initialisation du Spinner
            ville = (Spinner) findViewById(R.id.listeVille);
            ville.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
            // Gérer l'erreur de parsing JSON
        }


        // Récupération du bouton et ajout d'un écouteur d'événements
        Button btnConnexion = findViewById(R.id.ajoutMission);
        btnConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Appel de la méthode connexion
                ajoutMission(dateDepart.getText().toString(), dateRetour.getText().toString());
            }
        });

    }

    public class LibelleEtNo {
        public String libelle;
        public int no;

        public LibelleEtNo (String unLibelle, int unNo) {
            libelle = unLibelle;
            no = unNo;
        }

        @Override
        public String toString() {
            return libelle;
        }
    }


    private void ajoutMission(String dateDepart, String dateRetour) {
        if (dateDepart.isEmpty() || dateRetour.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }
        LibelleEtNo libelleEtNo = (LibelleEtNo) ville.getSelectedItem();
        idCom = libelleEtNo.no; // Récupérer la valeur 'no'

        String urlServiceWeb = "http://" + getString(R.string.server) + "/epoka/mission.php?mis_idUtil=" + util_id + "&mis_idCom=" + idCom + "&mis_dDepart=" + dateDepart +  "&mis_dRetour=" + dateRetour;
        String resultat = getServerDataTexteBrut(urlServiceWeb);

        if (resultat.equals("")) {
            Toast.makeText(this, "Mission insérée avec succès", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Erreur lors de l'insertion. Raison technique : " + resultat, Toast.LENGTH_LONG).show();
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
