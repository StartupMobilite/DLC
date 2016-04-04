package com.app.dlc.dlc;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class create_account extends AppCompatActivity {
    RadioGroup radGrp;
    private Button btn_register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        btn_register = (Button) findViewById(R.id.btn_register);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Toast.makeText(getApplicationContext(), "Consommateur", Toast.LENGTH_LONG).show();
            }
        });



        radGrp = (RadioGroup) findViewById(R.id.radioType);
        //int checkedRadioButtonID = radGrp.getCheckedRadioButtonId();
        radGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            public void onCheckedChanged(RadioGroup group, int checkedId) {
               /* RadioButton checkedRadioButton = (RadioButton)radGrp.findViewById(checkedId);
                boolean isChecked = checkedRadioButton.isChecked();
                if (isChecked)
                {
                    // Changes the textview's text to "Checked: example radiobutton text"
                    //tv.setText("Checked:" + checkedRadioButton.getText());
                    Toast.makeText(getApplicationContext(),checkedRadioButton.getText() , Toast.LENGTH_LONG).show();


                }
*/
                if (checkedId == R.id.radioConsommateur) {
                    //some code
                    Toast.makeText(getApplicationContext(), "Consommateur", Toast.LENGTH_LONG).show();

                } else if (checkedId == R.id.radioDistributeur) {
                    //some code
                    Toast.makeText(getApplicationContext(), "Distributeur", Toast.LENGTH_LONG).show();

                }

            }

        });


        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

}
