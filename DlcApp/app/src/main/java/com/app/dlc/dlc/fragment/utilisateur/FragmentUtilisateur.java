package com.app.dlc.dlc.fragment.utilisateur;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.text.TextWatcher;
import android.widget.TextView;
import android.widget.Toast;

import com.app.dlc.dlc.R;
import com.app.dlc.dlc.activity.login;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentUtilisateur extends Fragment {
    //private Toolbar toolbar;
    private EditText inputName, input_email, input_password;
    private TextInputLayout input_layout_email, input_layout_password;
    private Button btnSignUp;
    private TextView tvlogin;
    String requestResult;
    private ProgressDialog dialog;




    public FragmentUtilisateur() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


    }

  /*  private void submitForm() {
        if (!validateName()) {
            return;
        }

        if (!validateEmail()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }

        Toast.makeText(getActivity(), "Thank You!", Toast.LENGTH_SHORT).show();
    }*/

   /*  private boolean validateName() {
        if (inputName.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError(getString(R.string.err_msg_name));
            requestFocus(inputName);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }

   private boolean validateEmail() {
        String email = input_email.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(inputEmail);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        if (inputPassword.getText().toString().trim().isEmpty()) {
            inputLayoutPassword.setError(getString(R.string.err_msg_password));
            requestFocus(inputPassword);
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }*/

   /* private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_name:
                    validateName();
                    break;
                case R.id.input_email:
                    validateEmail();
                    break;
                case R.id.input_password:
                    validatePassword();
                    break;
            }
        }
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        dialog = new ProgressDialog(getActivity());
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Creation du compte en cours...");
        View myFragmentView = inflater.inflate(R.layout.fragment_utilisateur, container, false);

        input_layout_email = (TextInputLayout) myFragmentView.findViewById(R.id.input_layout_email);
        input_layout_password = (TextInputLayout) myFragmentView.findViewById(R.id.input_layout_password);
        input_email = (EditText) myFragmentView.findViewById(R.id.input_email);
        input_password = (EditText) myFragmentView.findViewById(R.id.input_password);
        btnSignUp = (Button) myFragmentView.findViewById(R.id.btn_signup);
        tvlogin =(TextView)myFragmentView.findViewById(R.id.link_signup);
        //btnSignUp = (Button) getView().findViewById(R.id.btn_signup);

        /*inputName.addTextChangedListener(new MyTextWatcher(inputName));
        inputEmail.addTextChangedListener(new MyTextWatcher(inputEmail));
        inputPassword.addTextChangedListener(new MyTextWatcher(inputPassword));
*/


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getActivity(), "Clicked!", Toast.LENGTH_SHORT).show();
                if(validation()){
                    SignInAsyncTask signInAsyncTask = new SignInAsyncTask();
                    signInAsyncTask.execute("");
                }
                //submitForm();

            }
        });

        tvlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, 0);
            }
        });
        return myFragmentView;
        //return inflater.inflate(R.layout.fragment_utilisateur, container, false);
    }

    public class SignInAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }



        @Override
        protected String doInBackground(String... params) {

            try{
                signInRequest(params[0]);
                //return "OK";
            }
            catch (Exception e)
            {
                Log.d("Erreur",e.getLocalizedMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String result ) {

            //spinner.setAdapter(dataAdapter);
            dialog.dismiss(); //masque le dialg box

            try{
                if(requestResult=="OK")
                {
                    Toast.makeText(getContext(), "Votre compte a ete cree vous pouvez vous identifer",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getContext(), login.class);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    //startActivityForResult(intent, 0); // execution de l intent
                    startActivity(intent);

                }
                else if(requestResult =="Exists")
                {
                    Toast.makeText(getContext(), "Cette addresse est deja utilisee pour un autre compte",Toast.LENGTH_LONG).show();
                    input_layout_email.setError("Adresse email deja utilisee");

                }
                else
                {
                    Toast.makeText(getContext(), "erreur",Toast.LENGTH_LONG).show();
                    //Intent intent = new Intent(getContext(), login.class);
                    //startActivity(intent);


                }
            }
            catch (Exception e)
            {
                String s = e.getLocalizedMessage();
                Log.d("Erreur",e.getLocalizedMessage());
            }



        }
    }




    private String signInRequest(String requestUrl) throws IOException {
        String s;
        String resultheader="";
        JSONObject productDetails = new JSONObject();
        try {

            /*productDetails.put("voie", input_voie.getText());
            productDetails.put("ville", input_ville.getText());
            productDetails.put("codepostal", input_zip.getText());
            productDetails.put("horaireOuverture", tv_horaireOuverture.getText());
            productDetails.put("horaireFermerture", tv_horaireFermerture.getText());
            productDetails.put("email", input_email.getText());
            productDetails.put("nom", input_name.getText());
            productDetails.put("password", input_password.getText());
*/
            productDetails.put("email", input_email.getText().toString());
            productDetails.put("password", input_password.getText().toString());
            productDetails.put("ville", "Non renseigne");
            productDetails.put("codepostal", "75000");

            /*productDetails.put("email", "20 rue fontarabie");
            productDetails.put("password", "Paris");*/
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        try
        {
            String JsonResponse = null;
            String JsonDATA = String.valueOf(productDetails);
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String requeteUrl;


            requeteUrl = "https://dlcapi.herokuapp.com/api/utilisateurs";

            URL url = new URL(requeteUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            //definit le type de requete (POST/GET)
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            //"ecrit" le json dans les header de la requete
            Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
            writer.write(JsonDATA);
            writer.close();
            urlConnection.connect();
            int response = urlConnection.getResponseCode(); // recupere le code de reponse suite a la requete effectuee

            InputStream inputStream ;
            if (response == 200|| response==503){
                requestResult=resultheader="OK";
                return resultheader;
            }

            else if(response==422){
                requestResult=resultheader="Exists";
                return resultheader;
            }

            else
            {
                // recupere la reponse du server en l occurence le json definissant le token, le user id,...
                BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder jsonResponse = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    jsonResponse.append(line);
                }
                requestResult=resultheader=parseResult(jsonResponse.toString()); // parse le json

            }

            //return resultheader;
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            String str;
            Log.d("Erreur",e.getLocalizedMessage()) ;
            str=e.getLocalizedMessage().toString();
            str=e.getMessage().toString();
        }

        return resultheader;


    }

    private String parseResult(String result) {
        // parsage du json
        try {
            JSONObject response = new JSONObject(result);
            JSONObject error = response.getJSONObject("error");
            String name=error.getString("name");
            String message=error.getString("message");
            return message;

            //LoggedInUser.id=response.getString("userId");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "Error";
    }

    private boolean validation() {
        boolean validationPass = true;
        if (TextUtils.isEmpty(input_email.getText()) || !android.util.Patterns.EMAIL_ADDRESS.matcher(input_email.getText()).matches()
                ) {

            input_layout_email.setError("Addresse email incorrecte");
            validationPass = false;
        }
        else{
            input_layout_email.setErrorEnabled(false);
        }



        if (input_password.getText().toString().trim().isEmpty() || input_password.getText().length()<5) {
            input_layout_password.setError("Entrez un mot de passe d'au moins 5 caracteres");
            validationPass = false;
        }
        else {
            input_layout_password.setErrorEnabled(false);
        }

        return validationPass;
    }

}
