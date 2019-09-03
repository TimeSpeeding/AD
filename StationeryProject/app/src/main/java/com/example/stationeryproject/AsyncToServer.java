package com.example.stationeryproject;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Debug;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;



public class AsyncToServer extends AsyncTask<Command, Void, JSONArray> {
    String data="";
    IServerResponse callback;

    protected JSONArray doInBackground(Command... cmds) {
        Command cmd = cmds[0];
        this.callback = cmd.callback;

        JSONArray jsonArr = null;
        StringBuilder response = new StringBuilder();

        try {
            URL url = new URL(cmd.endPt);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");

            // send data
            if (cmd.data != null) {

                DataOutputStream outstream = new DataOutputStream(conn.getOutputStream());
                outstream.writeBytes(cmd.data.toString());
                outstream.flush();
                outstream.close();
            }

            // receive response
            InputStream inputStream = new BufferedInputStream(conn.getInputStream());
            BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
            for (String line; (line = r.readLine()) != null; ) {
                response.append(line).append('\n');
            }
            data=response.toString();
            Log.d("LOG",data);
            if(response.toString() == "")
                return null;
            try {
                jsonArr = new JSONArray(response.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonArr;
    }

    protected void onPostExecute(JSONArray jsonArray) {
        if (jsonArray != null)
            this.callback.onServerResponse(jsonArray);
    }

    public interface IServerResponse {
        void onServerResponse(JSONArray jsonArray);
    }
}
