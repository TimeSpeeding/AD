package com.example.stationeryproject;

import org.json.JSONArray;
import org.json.JSONObject;


public class Command {
    protected AsyncToServer.IServerResponse callback;
    protected String context;
    protected String endPt;
    protected JSONArray data;

    Command(AsyncToServer.IServerResponse callback,
            String context, String endPt, JSONArray data)
    {
        this.callback = callback;
        this.context = context;
        this.endPt = endPt;
        this.data = data;
    }
}
