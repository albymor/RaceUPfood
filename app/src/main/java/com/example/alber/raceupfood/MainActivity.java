/*

DO WHAT THE FUCK YOU WANT TO PUBLIC LICENSE 
                    Version 2, December 2004 

 Copyright (C) 2016 Alberto Morato  

 Everyone is permitted to copy and distribute verbatim or modified 
 copies of this license document, and changing it is allowed as long 
 as the name is changed. 

            DO WHAT THE FUCK YOU WANT TO PUBLIC LICENSE 
   TERMS AND CONDITIONS FOR COPYING, DISTRIBUTION AND MODIFICATION 

  0. You just DO WHAT THE FUCK YOU WANT TO.
  
  */


package com.example.alber.raceupfood;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URLEncoder;


public class MainActivity extends ListActivity {

    Button bottoneInvia;
    EditText nomeText;

    PostFood food = new PostFood();

    String tipoPanino, nome,  tipoPaninoRaw;

    String[] ingredienti ={
            "PORCHETTA",
            "SOPRESSA",
            "LATTUGA",
            "COTTO",
            "CRUDO",
            "SPECK",
            "POMODORO",
            "RADICCHIO" ,
            "FORMAGGIO",
            "+ Salsa rosa"
    };
    SparseBooleanArray selectedIngredients;

    String stringaIngredienti = "";
    String listaIngr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        final ListView listview= getListView();
        listview.setChoiceMode(listview.CHOICE_MODE_MULTIPLE);
        listview.setTextFilterEnabled(true);

        listview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });

        bottoneInvia = (Button) findViewById(R.id.buttonInvia);
        nomeText = (EditText) findViewById(R.id.nome);

        bottoneInvia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stringaIngredienti ="";
                listaIngr = "";
                selectedIngredients = listview.getCheckedItemPositions();
                for (int ii = 0; ii < ingredienti.length; ii++) {
                    if(selectedIngredients.get(ii)){
                        listaIngr += ingredienti[ii] + " ";
                        stringaIngredienti += "entry.1566187531="+ingredienti[ii] + "&";
                    }

                }

                nome= nomeText.getText().toString();
                if(nome.length()>0){
                    nome="entry.1169803445=" + nome +"&";
                }

                if(stringaIngredienti.length()==0 || stringaIngredienti.length()==0 || tipoPanino.length()==0) {
                    Toast.makeText(getApplicationContext(), "Non hai inserito tutte le info richieste", Toast.LENGTH_LONG).show();

                }
                else{
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            postData();
                        }
                    });
                    alertDialog.setNegativeButton("NO", null);
                    alertDialog.setTitle("Invia ordine");
                    alertDialog.setMessage("Ciao " + nomeText.getText().toString() + "\n stai per ordinare " + tipoPaninoRaw + " con: \n" + listaIngr);
                    alertDialog.create().show();
                }


            }
        });


        setListAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_checked, ingredienti));


        //postData();
        //<input type="hidden" name="entry.1796092198" jsname="L9xHkb" value="Opzione 1">


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void postData(){
        String url = "https://docs.google.com/forms/d/e/1FAIpQLSc_N_SLmX6rAjMB7eBUQFGvt2hFvbQW358xge8-EsFJOT7l_g/formResponse";

        //<input type="hidden" name="entry.1632641272" jsname="L9xHkb" disabled="" value="Opzione 1">

        //<input type="hidden" name="entry.1796092198" jsname="L9xHkb" value = "Opzione 1">

        String data = nome+tipoPanino+stringaIngredienti;
        data=data.substring(0,data.length()-1);
        System.out.println(data);
        food.execute(url, data);


    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.paninoGrande:
                if (checked) {
                    tipoPanino = "entry.112685935=Panino Grande&";
                    tipoPaninoRaw = "Panino Grande";
                    break;
                }
            case R.id.paninoPiccolo:
                if (checked){
                    tipoPanino = "entry.112685935=Panino Piccolo&";
                    tipoPaninoRaw = "Panino Piccolo";
                    break;
                }
            case R.id.paninoToast:
                if (checked) {
                    tipoPanino = "entry.112685935=Toast&";
                    tipoPaninoRaw = "Toast";
                    break;
                }
        }
    }


}
