package com.app.dlc.dlc.model;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Guillaume on 24/04/2016.
 */
public class InfosDistributeur {
    public static int id;
    public static String nom;
    public static String addresse;
    public static String horaireOuverture;
    public static String horaireFermerture;
    public static String image;
    public static String voie;
    public static String ville;
    public static String codePostal;

    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        InfosDistributeur.id = id;
    }

    public static String getNom() {
        return nom;
    }

    public static void setNom(String nom) {
        InfosDistributeur.nom = nom;
    }

    public static String getAddresse() {
        return addresse;
    }

    public static void setAddresse(String addresse) {
        InfosDistributeur.addresse = addresse;
    }

    public static String getHoraireOuverture() {
        return horaireOuverture;
    }

    public static void setHoraireOuverture(String horaireOuverture) {
        InfosDistributeur.horaireOuverture = horaireOuverture;
    }

    public static String getHoraireFermerture() {
        return horaireFermerture;
    }

    public static void setHoraireFermerture(String horaireFermerture) {
        InfosDistributeur.horaireFermerture = horaireFermerture;
    }

    public static String getImage() {
        return image;
    }

    public static void setImage(String image) {
        InfosDistributeur.image = image;
    }

    public static String getVoie() {
        return voie;
    }

    public static void setVoie(String voie) {
        InfosDistributeur.voie = voie;
    }

    public static String getVille() {
        return ville;
    }

    public static void setVille(String ville) {
        InfosDistributeur.ville = ville;
    }

    public static String getCodePostal() {
        return codePostal;
    }

    public static void setCodePostal(String codePostal) {
        InfosDistributeur.codePostal = codePostal;
    }

    public static void updateInfo()
    {
        JSONObject productDetails = new JSONObject();
        try {


            productDetails.put("nom", getNom());
            productDetails.put("ville", getVille());
            productDetails.put("voie", getVoie());
            productDetails.put("codepostal", getCodePostal());
            productDetails.put("horaireOuverture", getHoraireOuverture());
            productDetails.put("horaireFermerture", getHoraireFermerture());
            productDetails.put("image", getImage());


        String JsonResponse = null;
        String JsonDATA = String.valueOf(productDetails);
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String requeteUrl = "https://dlcapi.herokuapp.com/api/Distributeurs/" + LoggedInUser.getId() + "?access_token=" + LoggedInUser.getToken();

        URL url = new URL(requeteUrl);
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        //definit le type de requete (POST/GET)
        urlConnection.setRequestMethod("PUT");
        urlConnection.setRequestProperty("Content-Type", "application/json");
        urlConnection.setRequestProperty("Accept", "application/json");
        //"ecrit" le json dans les header de la requete
        Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
        writer.write(JsonDATA);
        writer.close();
        urlConnection.connect();
        int response = urlConnection.getResponseCode(); // recupere le code de reponse suite a la requete effectuee
        String resultheader = Integer.toString(response);
        InputStream inputStream;
        if (response != 200) // si l utlisateur est authentifie
        {
            // recupere la reponse du server en l occurence le json definissant le token, le user id,...
            BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder jsonResponse = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                jsonResponse.append(line);
            }
            //resultheader=parseResultError(jsonResponse.toString()); // parse le json

        }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.d("Erreur", e.getLocalizedMessage());
        }
    }


}
