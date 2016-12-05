/*###########################################################################//

MIT License

Copyright (c) 2016 Alberto Morato

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

//###########################################################################*/


package com.example.alber.raceupfood;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
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
import android.widget.Toast;


public class MainActivity extends ListActivity {

    Button sendButton;
    EditText nameText;
    String sandwichType, name,  sandwichTypeRaw;
    SparseBooleanArray selectedIngredients;
    String ingredientsString = "";
    String ingredientsStringRaw = "";

    PostFood food;

    String[] ingridients ={
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




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        sendButton = (Button) findViewById(R.id.buttonInvia);
        nameText = (EditText) findViewById(R.id.nome);

        food = new PostFood(getApplicationContext());

        final ListView listview= getListView();
        listview.setChoiceMode(listview.CHOICE_MODE_MULTIPLE);
        listview.setTextFilterEnabled(true);

        /**
         * Disable scrollView in ListView
         */
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

        /**
         * Populate ListView
         */
        setListAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_checked, ingridients));


        /**
         * On click Send Button
         */
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ingredientsString ="";
                ingredientsStringRaw = "";
                selectedIngredients = listview.getCheckedItemPositions();
                for (int ii = 0; ii < ingridients.length; ii++) {
                    if(selectedIngredients.get(ii)){
                        ingredientsStringRaw += ingridients[ii] + " ";
                        ingredientsString += "entry.1566187531="+ ingridients[ii] + "&";
                    }

                }

                name = nameText.getText().toString(); //get name from EditText
                if(name.length()>0){
                    name ="entry.1169803445=" + name +"&"; //and append the entry string for POST
                }

                if(ingredientsString.length()==0 || ingredientsString.length()==0 || sandwichType.length()==0) {
                    Toast.makeText(getApplicationContext(), "Non hai inserito tutte le info richieste", Toast.LENGTH_LONG).show(); //Some fields are empty

                }
                else{
                    //All ok let send the order: ask for confirm with alertDialog
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            postData();
                        }
                    });
                    alertDialog.setNegativeButton("NO", null);
                    alertDialog.setTitle("Invia ordine");
                    alertDialog.setMessage("Ciao " + nameText.getText().toString() + "\nstai per ordinare " + sandwichTypeRaw + " con: \n" + ingredientsStringRaw);
                    alertDialog.create().show();
                }


            }
        });
    }

    /**
     * Get the checked RadioButton
     * @param view
     */
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.paninoGrande:
                if (checked) {
                    sandwichType = "entry.112685935=Panino Grande&";
                    sandwichTypeRaw = "Panino Grande";
                    break;
                }
            case R.id.paninoPiccolo:
                if (checked){
                    sandwichType = "entry.112685935=Panino Piccolo&";
                    sandwichTypeRaw = "Panino Piccolo";
                    break;
                }
            case R.id.paninoToast:
                if (checked) {
                    sandwichType = "entry.112685935=Toast&";
                    sandwichTypeRaw = "Toast";
                    break;
                }
        }
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

    /**
     * Prepare the string and POST the data to Google Form
     */
    public void postData(){

        //URL to the Google Form
        String url = "https://docs.google.com/forms/d/e/1FAIpQLSc_N_SLmX6rAjMB7eBUQFGvt2hFvbQW358xge8-EsFJOT7l_g/formResponse";

        //Compose the string
        String data = name + sandwichType + ingredientsString;

        //Delete the last character &
        data=data.substring(0,data.length()-1);
        System.out.println(data);

        //POST data
        food.execute(url, data);


    }




}
