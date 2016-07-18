package com.example.androidcodes.volleyexample;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

public class AuthenticationFBActivity extends AppCompatActivity {


    protected WebView mainWebView;
    Activity activity;
    FBConnection fbConnection;
    String message, status;
    private Context mContext;
    private WebView mWebviewPop;
    private WebView wv1;
    //    private KProgressHUD pDialog;
    private JSONObject jsonObject;
    private JSONObject resultObject;
    //    private SharedPreferencesUtility preferencesUtility;
//    private AppDbAdapter dbAdapter;
    private JSONArray resultArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication_fb);

        activity = AuthenticationFBActivity.this;

        fbConnection = new FBConnection();
        String url = fbConnection.getFBAuthUrl();

        mainWebView = (WebView) findViewById(R.id.web_auth);

//        pDialog = KProgressHUD.create(this)
//                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
//                .setDimAmount(0.5f);
//        pDialog.setCancellable(true);
//        pDialog.show();
//
//        preferencesUtility = new SharedPreferencesUtility(AuthenticationFBActivity.this);

        //Cookie manager for the webview
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);

        //Settings
        WebSettings webSettings = mainWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportMultipleWindows(true);

        mainWebView.setWebViewClient(new MyCustomWebViewClient());
        mainWebView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);

        mainWebView.setWebChromeClient(new MyCustomChromeClient());
        mainWebView.loadUrl(url);

        mContext = this.getApplicationContext();

    }

    private class MyCustomWebViewClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            //pDialog.dismiss();

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            String host = Uri.parse(url).getHost();


            if (host.equals("m.facebook.com") || host.equals("www.facebook.com")) {
                return false;
            }

            if (url.contains("code")) {
                final Uri uri = Uri.parse(url);


                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        final String accessToken = fbConnection.getAccessToken(uri.getQueryParameter("code"));

                        FBGraph fbGraph = new FBGraph(accessToken);
                        String graph = fbGraph.getFBGraph();
                        final Map<String, String> fbProfileData = fbGraph.getGraphData(graph);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                System.out.println("facebook --> " + fbProfileData.get("email").toString() + " -- " + fbProfileData.get("name").toString());
                            }
                        });
                    }
                }).start();

            } else

                view.loadUrl(url);
            /*Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);*/
            return true;
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler,
                                       SslError error) {

        }
    }

    private class MyCustomChromeClient extends WebChromeClient {

        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog,
                                      boolean isUserGesture, Message resultMsg) {
            mWebviewPop = new WebView(mContext);
            mWebviewPop.setVerticalScrollBarEnabled(false);
            mWebviewPop.setHorizontalScrollBarEnabled(false);
            mWebviewPop.setWebViewClient(new MyCustomWebViewClient());
            mWebviewPop.getSettings().setJavaScriptEnabled(true);
            mWebviewPop.getSettings().setSavePassword(false);
            mWebviewPop.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
            transport.setWebView(mWebviewPop);
            resultMsg.sendToTarget();

            return true;
        }

        @Override
        public void onCloseWindow(WebView window) {

        }

    }

//    private void registerUser(final String strEmail, final String strName, final String accessToken) {
//
//        pDialog = KProgressHUD.create(this)
//                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
//                .setDimAmount(0.5f);
//        pDialog.setCancellable(true);
//        pDialog.show();
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, StaticDataUtility.Server_URL + StaticDataUtility.SIGNUP,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//
//                        try {
//
//                            System.out.println("RESPONSE OF FB>>>>" + response);
//
//                            pDialog.dismiss();
//
//                            jsonObject = new JSONObject(response.toString());
//
//                            status = jsonObject.optString("success");
//                            message = jsonObject.optString("message");
//
//                            if (status.equals("1")) {
//
//                                resultObject = jsonObject.optJSONObject("result");
//
//                                preferencesUtility.setEmail(resultObject.optString("email"));
//                                preferencesUtility.setUserName(resultObject.optString("name"));
//                                preferencesUtility.setUserId(resultObject.optString("id"));
//                                preferencesUtility.setPassword("");
//                                preferencesUtility.setVerifiy(true);
//                                preferencesUtility.setAccessToken(accessToken);
//                                preferencesUtility.setLoginType("fb");
//                                preferencesUtility.setString("loginOwner", "true");
//                                preferencesUtility.setString("loginPostright", "true");
//                                preferencesUtility.setString("loginFollower", "true");
//                                dbAdapter = new AppDbAdapter(AuthenticationFBActivity.this);
//
//                                resultArray = resultObject.optJSONArray("boardList");
//
//                                for (int i = 0; i < resultArray.length(); i++) {
//
//                                    resultObject = resultArray.getJSONObject(i);
//
//                                    BoardMasterJSON boardMasterJSON = new BoardMasterJSON(resultObject.toString());
//
//                                    dbAdapter.insertData(boardMasterJSON);
//
//                                }
//
//                            }
//
//                        } catch (Exception e) {
//
//
//                        }
//
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//
//                                if (status.equals("1")) {
//                                    Intent intent1 = new Intent(activity,MainActivity.class);
//                                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                    activity.startActivity(intent1);
//                                    activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//                                    activity.finish();
//
//                                } else {
//
//                                    if(message.equals("exception")) {
//                                        CommonUtility.showAlertDialog(activity, getString(R.string.unexpected_response),
//                                                getString(R.string.app_name));
//                                    }
//                                    else
//                                    {
//                                        CommonUtility.showAlertDialog(activity, message,
//                                                getString(R.string.app_name));
//                                    }
//
//                                }
//                            }
//                        });
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//
//                        CommonUtility.showAlertDialog(activity, getString(R.string.unexpected_response),
//                                getString(R.string.app_name),true);
//
//                    }
//                }) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put(StaticDataUtility.KEY_USERNAME, strName);
//                params.put(StaticDataUtility.KEY_EMAIL, strEmail);
//                params.put(StaticDataUtility.KEY_PASSWORD, "");
//                params.put(StaticDataUtility.KEY_PHONE_NUMBER, "1234567890");
//                params.put(StaticDataUtility.KEY_LOGIN_TYPE, "fbLogin");
//                params.put(StaticDataUtility.KEY_DEVICE_ID, preferencesUtility.getRegId());
//
//                System.out.println("PARAMETERS::::::::::::" + params.toString());
//
//                return params;
//            }
//
//        };
//
////        RequestQueue requestQueue = Volley.newRequestQueue(this);
////        requestQueue.add(stringRequest);
//
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//        // Adding request to request queue
//        Volley.newRequestQueue(AuthenticationFBActivity.this).add(stringRequest);
//    }
}