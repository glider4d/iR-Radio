package com.sakhacontent.irradio;

import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class JavaScriptCallbackInterface {
    public static String ArtistName = "";
    @JavascriptInterface
    public void calledFromJs(){
        Log.d("JS","calledFromJS");
//        Toast.makeText(null, "calledFromJs", Toast.LENGTH_LONG).show();
    }

    @JavascriptInterface
    public void getArtistName(String name){
        ArtistName = name;
    }
}
