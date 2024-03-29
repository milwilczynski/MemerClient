package controllers;

import android.content.SharedPreferences;
import android.util.Log;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import connection.handlers.Host;
import entities.Meme;
import entities.TenMemes;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetTenMemes {
    private final SharedPreferences prefs;
    private boolean isSuccess = false;
    private int page;
    private List<Meme> meme;

    public List<Meme> getMemeList(){
        return this.meme;
    }

    public GetTenMemes(SharedPreferences prefs, int page){
        this.prefs = prefs;
        this.page = page;
        this.meme = new ArrayList<>();
        isSuccess = doMagic();
    }

    private boolean doMagic(){
        try{
            OkHttpClient client = new OkHttpClient();
            String url = "http://"+ Host.IP + ":8080/getPictures?page=" + Integer.toString(page);
            Request request = new Request.Builder()
                    .addHeader("Authorization","Bearer "+ prefs.getString("token",""))
                    .url(url)
                    .build();
            Response response = null;
            try {
                response = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(response.isSuccessful()){
                final String myResponse = response.body().string();
                if(response.code() == 200){
                    TenMemes tm = new TenMemes(myResponse);
                    meme = tm.getListMemes();
                    return true;
                }
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean succeed(){
        return isSuccess;
    }
}

