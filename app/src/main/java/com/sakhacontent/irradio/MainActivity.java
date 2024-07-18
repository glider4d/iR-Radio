package com.sakhacontent.irradio;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.JsonReader;
import android.util.JsonToken;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.sakhacontent.irradio.Services.OnClearFromRecentService;

import java.io.IOException;
import java.io.StringReader;


public class MainActivity extends AppCompatActivity implements Playable{//, Runnable {

//ssk11    ImageButton play;
//ssk11    TextView title;
//    Handler handler = new Handler();

    NotificationManager notificationManager;


    int position = 0;
    boolean isPlaying = false;
    boolean isConnected = false;
    final String offlineMessageHtml = "DEFINE THIS";
    final String timeoutMessageHtml = "DEFINE THIS";
    private WebView mywebView;
    private void InitializeWebView(){
        setContentView(R.layout.activity_main);

        mywebView =(WebView)findViewById(R.id.webview);
        //mywebView.setWebViewClient(new WebViewClient());
//        mywebView.setWebChromeClient(new WebChromeClient()); //this
        mywebView.setWebViewClient(new MyWebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            public void onLoadResource(WebView view, String url) {
                // Check to see if there is a progress dialog
//                if (progressDialog == null) {
//                    // If no progress dialog, make one and set message
//                    progressDialog = new ProgressDialog(activity);
//                    progressDialog.setMessage("Loading please wait...");
//                    progressDialog.show();
//
//                    // Hide the webview while loading
//                    webview.setEnabled(false);
//                }
            }

            public void onPageFinished(WebView view, String url) {


                // Page is done loading;
                // hide the progress dialog and show the webview
//                if (progressDialog.isShowing()) {
//                    progressDialog.dismiss();
//                    progressDialog = null;
//                    webview.setEnabled(true);
//                }
            }

//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                if (isConnected) {
//                    // return false to let the WebView handle the URL
//                    return false;
//                } else {
//                    // show the proper "not connected" message
//                    view.loadData(offlineMessageHtml, "text/html", "utf-8");
//                    // return true if the host application wants to leave the current
//                    // WebView and handle the url itself
//                    return true;
//                }
//            }
            @Override
            public void onReceivedError (WebView view, int errorCode,
                                         String description, String failingUrl) {
                if (errorCode == ERROR_TIMEOUT) {
                    view.stopLoading();  // may not be needed
                    view.loadData(timeoutMessageHtml, "text/html", "utf-8");
                }
            }



        });


//        mywebView.loadUrl("https://radio.dataworld.pro/");
        mywebView.loadUrl("https://radioir.ru/");
        isConnected = isConnected(this.getBaseContext());
        if (!isConnected) {
//            Toast.makeText(this.getBaseContext(), "You are offline ", Toast.LENGTH_SHORT).show();

//            String unencodedHtml =
//                    "<html><body>'%23' is the percent code for ‘#‘ </body></html>";
//            String encodedHtml = Base64.encodeToString(unencodedHtml.getBytes(),
//                    Base64.NO_PADDING);
//            mywebView.loadData(encodedHtml, "text/html", "base64");

        }
        WebSettings webSettings=mywebView.getSettings();

        webSettings.setJavaScriptEnabled(true);





    }

    public static boolean isConnected(Context context) {



        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (null != cm) {
            NetworkInfo info = cm.getActiveNetworkInfo();


            return (info != null && info.isConnected());
        }
        return false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createChannel();
            registerReceiver(broadcastReceiver, new IntentFilter("TRACKS_TRACKS"));
            startService(new Intent(getBaseContext(), OnClearFromRecentService.class));
        }

        InitializeWebView();

        InitializeNotification();
//        run();
        /*
        run();*/

    }
    private void InitializeNotification(){
        //ssk11
        CreateNotification.createNotification(MainActivity.this, R.drawable.ic_baseline_play_arrow_38);

        isPlaying = false;

    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CreateNotification.CHANNEL_ID,
                    "KOD Dev", NotificationManager.IMPORTANCE_LOW);

            notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null){
                notificationManager.createNotificationChannel(channel);
            }
        }
    }



    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getExtras().getString("actionname");

            switch (action){
                case CreateNotification.ACTION_PREVIUOS:
                    onTrackPrevious();
                    break;
                case CreateNotification.ACTION_PLAY:
                    if (!isPause){
                        onTrackPause();
                    } else {
                        onPlay();
                    }
                    break;
                case CreateNotification.ACTION_NEXT:
                    onTrackNext();
                    break;
            }
        }
    };

    @Override
    public void onTrackPrevious() {

//        position--;
//        CreateNotification.createNotification(MainActivity.this, tracks.get(position),
//                R.drawable.ic_baseline_pause, position, tracks.size()-1);
//        title.setText(tracks.get(position).getTitle());

    }



    @Override
    public void onTrackPause() {
        loadJavascript("document.querySelector('.loading')");

    }

    @Override
    public void onTrackNext() {


    }

    @Override
    public void onPlay() {
        mywebView.loadUrl("javascript:document.querySelector('.play').click()");
        CreateNotification.createNotification(MainActivity.this, R.drawable.ic_baseline_pause_38);
        isPlaying = true;
        isPause = false;

    }

    @Override
    protected void onStop() {
        super.onStop();  // Always call the superclass method first

        System.out.println("ON_STOP");
//        CreateNotification.createNotification(MainActivity.this, R.drawable.ic_baseline_pause);
//        checkStatus();
//        if (isPause){
//            CreateNotification.createNotification(MainActivity.this, R.drawable.ic_play_arrow);
//
//        } else {
//            CreateNotification.createNotification(MainActivity.this, R.drawable.ic_baseline_pause);
//        }
        //Toast.makeText(getApplicationContext(), "onStop called", Toast.LENGTH_LONG).show();
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        System.out.println("ON_CHANGE_FOCUS");
        if (!hasFocus)
        {
            checkStatus();
//            checkStatus();
//            if (isPause){
//                CreateNotification.createNotification(MainActivity.this, R.drawable.ic_play_arrow);
//
//            } else {
//                CreateNotification.createNotification(MainActivity.this, R.drawable.ic_baseline_pause);
//            }
//            Toast.makeText(getApplicationContext(), "onWindowFocusChange " + hasFocus, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            notificationManager.cancelAll();
        }

        unregisterReceiver(broadcastReceiver);
    }

    private void callbackfrompause(){
        mywebView.loadUrl("javascript:document.querySelector('.play').click()");
        CreateNotification.createNotification(MainActivity.this, R.drawable.ic_baseline_play_arrow_38);
        isPlaying = false;
        isPause = true;
    }

    private void CallBackPauseFromCheckStatus(){
        CreateNotification.createNotification(MainActivity.this, R.drawable.ic_baseline_play_arrow_38);
    }

    private void CallbBackPlayFromCheckStatus(){
        CreateNotification.createNotification(MainActivity.this, R.drawable.ic_baseline_pause_38);
    }


    public static boolean isTest = false;
    public static boolean isNull1 = true;
    public void loadJavascript(String javascript) {
        isNull1 = true;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // In KitKat+ you should use the evaluateJavascript method
            ValueCallback<String> callback = new ValueCallback<String>() {
                @TargetApi(Build.VERSION_CODES.HONEYCOMB)
                @Override
                public void onReceiveValue(String s) {
                    isTest = false;
                    isNull1 = true;
                    JsonReader reader = new JsonReader(new StringReader(s));

                    // Must set lenient to parse single values
                    reader.setLenient(true);

                    try {
                        if(reader.peek() != JsonToken.NULL) {
                            isNull1 = false;
                            isTest = true;

                            if(reader.peek() == JsonToken.STRING) {
                                String msg = reader.nextString();
                                if(msg != null) {
//                                    Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                                }
                            }
                        } else {
                            callbackfrompause();
                        }
                    } catch (IOException e) {
//                        Log.e("TAG", "MainActivity: IOException", e);
                    } finally {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            // NOOP
                        }

                    }
                }
            };
            mywebView.evaluateJavascript(javascript, callback);
        } else {

            mywebView.loadUrl("javascript:"+javascript);

        }

    }





    private boolean isPause = true;


    public void checkStatus() {
        String javascript = "homePage.audio.paused";
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // In KitKat+ you should use the evaluateJavascript method
            ValueCallback<String> callback = new ValueCallback<String>() {
                @TargetApi(Build.VERSION_CODES.HONEYCOMB)
                @Override
                public void onReceiveValue(String s) {
                    JsonReader reader = new JsonReader(new StringReader(s));
                    // Must set lenient to parse single values
                    reader.setLenient(true);
                    try {
                        if(reader.peek() != JsonToken.NULL) {
                            if (s.equals("true")){
                                isPause = true;
                                CallBackPauseFromCheckStatus();
                            } else if ( s.equals("false")){
                                isPause = false;
                                CallbBackPlayFromCheckStatus();
                            }
                        } else {
                        }
                    } catch (IOException e) {
//                        Log.e("TAG", "MainActivity: IOException", e);
                    } finally {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            // NOOP
                        }

                    }
                }
            };
            mywebView.evaluateJavascript(javascript, callback);
        } else {

            mywebView.loadUrl("javascript:"+javascript);

        }

    }




//    @Override
//    public void run() {
//        handler.postDelayed(this, 500);
//        while (true)
//        System.out.println("RUUUUUUUUUUUUUN");
        /*
        handler.postDelayed(this, 500);
        checkStatus();
        if (isPause){
            CreateNotification.createNotification(MainActivity.this, R.drawable.ic_baseline_pause);
        } else {
            CreateNotification.createNotification(MainActivity.this, R.drawable.ic_play_arrow);
        }*/
    //homePage.audio.paused
//    }


    private class MyWebViewClient extends WebViewClient {

        String unencodedHtml =
                "<html><body style='background-color: black;'>" +
                        "<h3 style = 'color: white;position:fixed; top:50%; left:50%; text-align:center;vertical-align: middle;transform: translate(-50%, -50%);'>" +
                        "   не удается связаться с сервером" +
                        "</h3></body></html>";
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
//            view.loadD\

//            Toast.makeText(MainActivity.this, "Unexpected error occurred.Reload page again1.", Toast.LENGTH_SHORT).show();

            String unencodedHtml1 =
                    "<html><body style='background-color: black;'>" +
                            "<h3 style = 'color: white;position:fixed; top:50%; left:50%; text-align:center;vertical-align: middle;transform: translate(-50%, -50%);'>" +
                            "   не удается связаться с сервером1" +
                            "</h3></body></html>";

            String encodedHtml = Base64.encodeToString(unencodedHtml.getBytes(),
                    Base64.NO_PADDING);




//            Toast.makeText(MainActivity.this, " error ."  + error.getErrorCode() + " " + error.getDescription(), Toast.LENGTH_SHORT).show();
            if (error.getErrorCode() == -2)
                view.loadData(encodedHtml, "text/html", "base64");

        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);


            String unencodedHtml2 =
                    "<html><body style='background-color: black;'>" +
                            "<h3 style = 'color: white;position:fixed; top:50%; left:50%; text-align:center;vertical-align: middle;transform: translate(-50%, -50%);'>" +
                            "   не удается связаться с сервером 2" +
                            "</h3></body></html>";

//            Toast.makeText(MainActivity.this, "Unexpected error occurred.Reload page again2.", Toast.LENGTH_SHORT).show();
            String encodedHtml = Base64.encodeToString(unencodedHtml2.getBytes(),
                    Base64.NO_PADDING);
            view.loadData(encodedHtml, "text/html", "base64");
//            String unencodedHtml =
//                    "<html><body>error2</body></html>";
//            String encodedHtml = Base64.encodeToString(unencodedHtml.getBytes(),
//                    Base64.NO_PADDING);
//            view.loadData(encodedHtml, "text/html", "base64");

        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
//            Toast.makeText(MainActivity.this, "Unexpected SSL error occurred.Reload page again3.", Toast.LENGTH_SHORT).show();

            String unencodedHtml3 =
                    "<html><body style='background-color: black;'>" +
                            "<h3 style = 'color: white;position:fixed; top:50%; left:50%; text-align:center;vertical-align: middle;transform: translate(-50%, -50%);'>" +
                            "   не удается связаться с сервером 3" +
                            "</h3></body></html>";
            String encodedHtml = Base64.encodeToString(unencodedHtml3.getBytes(),
                    Base64.NO_PADDING);
            view.loadData(encodedHtml, "text/html", "base64");
//            String unencodedHtml =
//                    "<html><body>error3</body></html>";
//            String encodedHtml = Base64.encodeToString(unencodedHtml.getBytes(),
//                    Base64.NO_PADDING);
//            view.loadData(encodedHtml, "text/html", "base64");

        }

        private boolean appInstalledOrNot(String uri) {
            PackageManager pm = getPackageManager();
            try {
                pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
                return true;
            } catch (PackageManager.NameNotFoundException e) {
            }

            return false;
        }


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

            final Uri uri = request.getUrl();

//            Toast.makeText(MainActivity.this, "app installed." + request.getUrl().toString(), Toast.LENGTH_SHORT).show();
//
//            String headerReceiver = "";// Replace with your message.
//            String bodyMessageFormal = "";// Replace with your message.
//            String whatsappContain = headerReceiver + bodyMessageFormal;


            /*
            String trimToNumner = "+79245697061"; //10 digit number
            Intent intent = new Intent ( Intent.ACTION_VIEW );
            intent.setData ( Uri.parse ( "https://wa.me/" + trimToNumner + "/?text=" + "" ) );*/
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            startActivity ( intent );
            return true;
            /*
            PackageManager pm =  getPackageManager();

            try
            {
                // Raise exception if whatsapp doesn't exist
                PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);

                Intent waIntent = new Intent(Intent.ACTION_SEND);
                waIntent.setType("text/plain");
                waIntent.setPackage("com.whatsapp");
                waIntent.putExtra(Intent.EXTRA_TEXT, "YOUR TEXT");
                startActivity(waIntent);
            }
            catch (PackageManager.NameNotFoundException e)
            {
                Toast.makeText(MainActivity.this, "Please install whatsapp app", Toast.LENGTH_SHORT)
                        .show();
            }
            return true;
            */
            /*
            Toast.makeText(MainActivity.this, "app installed.", Toast.LENGTH_SHORT).show();
            Intent intent2 = new Intent(Intent.ACTION_VIEW, uri);
            startActivity( intent2 );

            return true;
            */
            /*
            if( URLUtil.isNetworkUrl(uri.toString()) ) {
                Toast.makeText(MainActivity.this, "isNetworkUrl. = " + uri, Toast.LENGTH_SHORT).show();
                return false;
            }
            if (appInstalledOrNot(uri.toString())) {
                Toast.makeText(MainActivity.this, "app installed.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity( intent );
            } else {
                Toast.makeText(MainActivity.this, "app is not installed.", Toast.LENGTH_SHORT).show();
                // do something if app is not installed
            }
            return true;*/

        }

    }
}


