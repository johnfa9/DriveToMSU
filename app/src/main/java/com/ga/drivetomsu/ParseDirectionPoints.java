package com.ga.drivetomsu;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.os.AsyncTask;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ParseDirectionPoints extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
    MapCallback dirCallback;
    String directionMode = "driving";

    public ParseDirectionPoints(Context mContext, String directionMode) {
        this.dirCallback = (MapCallback) mContext;
        this.directionMode = directionMode;
    }

    // Parsing the data in non-ui thread
    @Override
    protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

        JSONObject jObject;
        List<List<HashMap<String, String>>> routes = null;

        try {
            jObject = new JSONObject(jsonData[0]);
            JSONParser parser = new JSONParser();
            // Starts parsing data
            routes = parser.parse(jObject);
            Log.d("travellog", "Processing routes");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return routes;
    }

    // Runs in user screen after parsing
    @Override
    protected void onPostExecute(List<List<HashMap<String, String>>> result) {
        ArrayList<LatLng> points;
        PolylineOptions lineValues = null;
        // Traverss the routes
        for (int i = 0; i < result.size(); i++) {
            points = new ArrayList<>();
            lineValues = new PolylineOptions();
            // Fetching route
            List<HashMap<String, String>> path = result.get(i);
            // Fetch all the points in each route
            for (int j = 0; j < path.size(); j++) {
                HashMap<String, String> point = path.get(j);
                double lat = Double.parseDouble(point.get("lat"));
                double lng = Double.parseDouble(point.get("lng"));
                LatLng position = new LatLng(lat, lng);  //new coordinate
                points.add(position);
            }
            // Add the points in the route to LineValues
            lineValues.addAll(points);
            lineValues.width(18);
            lineValues.color(Color.RED);

            Log.d("travellog", "onPostExecute lineValues decoded");
        }

        // Draw polyline in the Map
        if (lineValues != null) {
            dirCallback.onComplete(lineValues);

        } else {
            Log.d("travellog", "nothing drawn");
        }
    }
}
