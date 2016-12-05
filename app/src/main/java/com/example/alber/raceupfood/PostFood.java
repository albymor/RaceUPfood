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

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by alber on 04/12/2016.
 */
public class PostFood extends AsyncTask<String, Void, String> {

    Context c;
    boolean requestStatus = false;

    //Contructor
    // @param application context
    public PostFood(Context context) {
        c = context;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected String doInBackground(String... params) {

        byte[] postDataBytes = {};

        String urlString = params[0]; // URL to call
        String foodString = params[1]; //String to POST

        try {
            postDataBytes = foodString.getBytes("UTF-8"); //convert in byte array
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {

            URL url = new URL(urlString);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            urlConnection.setDoOutput(true);
            urlConnection.getOutputStream().write(postDataBytes);

            System.out.println("Ok inviato");
            requestStatus = true;


        } catch (Exception e) {

            e.printStackTrace();
            System.out.println("Qualcosa è andato storto");
            requestStatus = false;


        }

        return null;
    }


    @Override
    protected void onPostExecute(String result) {
        //Update the UI
        if (requestStatus){
            Toast.makeText(c, "Ordine inviato con successo", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(c, "OPS! Qualcosa è andato storto", Toast.LENGTH_LONG).show();
        }
    }



}
