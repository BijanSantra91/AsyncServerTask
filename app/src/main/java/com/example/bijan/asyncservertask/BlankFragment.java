package com.example.bijan.asyncservertask;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SecureCacheResponse;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends Fragment {

    //8. initialized
    Button click;
    TextView data;
    MyTask myTask;

    //9. for internet connection test
    public boolean checkInternet(){
        ConnectivityManager manager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo == null || networkInfo.isConnected() == false){
            return false;
        }
        return true;
    }
    //7. creting AsyncTask
    public class MyTask extends AsyncTask<String, Void, String>{
        //11. declare everything do
        URL myUrl;
        HttpURLConnection connection;
        InputStream inputStream;
        InputStreamReader inputStreamReader;
        BufferedReader bufferedReader;
        String line;
        StringBuilder result;

        @Override
        protected String doInBackground(String... strings) {
            try {
                myUrl = new URL(strings[0]);
                connection = (HttpURLConnection) myUrl.openConnection();
                inputStream = connection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);

                //we are now reading data from bufferedReader
                result = new StringBuilder();
                line = bufferedReader.readLine();
                while (line != null){
                    result.append(line);
                    line = bufferedReader.readLine();
                }
                //return the completeData
                return  result.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }catch (SecurityException e){
                e.printStackTrace();
                return "NO INTERNET";
            }

            return "SOME THING WENT WRONG";
        }

        @Override
        protected void onPostExecute(String s) {
            data.setText(s);
            super.onPostExecute(s);
        }
    }

    public BlankFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_blank, container, false);

        click = (Button) v.findViewById(R.id.click);
        data = (TextView) v.findViewById(R.id.data);
        myTask = new MyTask();

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//              10.internate avilable or not
                if (checkInternet()){
                    if (myTask.getStatus() == AsyncTask.Status.RUNNING || myTask.getStatus() == AsyncTask.Status.FINISHED){
                        Toast.makeText(getActivity(), "ALREADY RUNNING PLEASE WAIT", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    myTask.execute("http://skillgun.com");
                }
                else {
                    Toast.makeText(getActivity(), "NETWORK IS NOT AVAILABLE", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }

}
