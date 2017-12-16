package com.cog.ananv.Others;

/**
 * Created by test on 31/10/17.
 */

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

public class JsonParse {
    public JsonParse() {
    }

    public List<Suggest> getParseJsonWCF(String sName) {
        List<Suggest> ListData = new ArrayList<Suggest>();
        try {
            String temp = sName.replace(" ", "%20");
            URL js = new URL("http://demo.cogzideltemplates.com/anan/mobile/search/search_by_username/username/bala" + temp);

            URLConnection jc = js.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(jc.getInputStream()));
            String line = reader.readLine();

            JSONArray itemArray = new JSONArray(line);
            for (int i = 0; i < itemArray.length(); i++) {
                String name = itemArray.getString(i);
                Log.e("json", i + "=" + name);

            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        return ListData;

    }

}
