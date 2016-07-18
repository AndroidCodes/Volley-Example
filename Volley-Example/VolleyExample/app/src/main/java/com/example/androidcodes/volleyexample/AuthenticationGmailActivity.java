package com.example.androidcodes.volleyexample;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class AuthenticationGmailActivity extends AppCompatActivity {

    private static String CLIENT_ID = "262003905374-elvgrman1ltj8280ahbi027kff46gl8s.apps.googleusercontent.com";
    // Use your own client id
    private static String CLIENT_SECRET = "YDapcXbFeMZSHd31P_sEhMsL";
    // Use your own client secret
    private static String REDIRECT_URI = "http://localhost";
    private static String GRANT_TYPE = "authorization_code";
    private static String TOKEN_URL = "https://accounts.google.com/o/oauth2/token";
    private static String OAUTH_URL = "https://accounts.google.com/o/oauth2/auth";
    private static String OAUTH_SCOPE = "email%20profile";

    String Code;
    WebView web_auth;
    Activity activity;
    String tok;
    //    private KProgressHUD pDialog;
    private JSONObject jsonObject;
    private String status, message;
    private JSONObject resultObject;
    //    private SharedPreferencesUtility preferencesUtility;
//    private AppDbAdapter dbAdapter;
    private JSONArray resultArray;

    private static String readResponse(InputStream is) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] data = new byte[2048];
        int len = 0;
        while ((len = is.read(data, 0, data.length)) >= 0) {
            bos.write(data, 0, len);
        }
        return new String(bos.toByteArray(), "UTF-8");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication_gmail);

        activity = AuthenticationGmailActivity.this;

//        preferencesUtility = new SharedPreferencesUtility(activity);
//
//        pDialog = KProgressHUD.create(this)
//                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
//                .setDimAmount(0.5f);
//        pDialog.setCancellable(true);
//        pDialog.show();

        web_auth = (WebView) findViewById(R.id.web_auth);

        web_auth.getSettings().setJavaScriptEnabled(true);
        web_auth.loadUrl(OAUTH_URL + "?redirect_uri=" + REDIRECT_URI
                + "&response_type=code&client_id=" + CLIENT_ID
                + "&scope=" + OAUTH_SCOPE);

        web_auth.setWebViewClient(new WebViewClient() {

            boolean authComplete = false;
            Intent resultIntent = new Intent();
            String authCode;

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if (url.contains("?code=") && authComplete != true) {
                    Uri uri = Uri.parse(url);
                    authCode = uri.getQueryParameter("code");
                    Log.i("", "CODE: " + authCode);
                    authComplete = true;
                    resultIntent.putExtra("code", authCode);
                    AuthenticationGmailActivity.this.setResult(Activity.RESULT_OK, resultIntent);
                    setResult(Activity.RESULT_CANCELED, resultIntent);

                    Code = authCode;

//                    SharedPreferences.Editor edit = pref.edit();
//                    edit.putString("Code", authCode);
//                    edit.commit();
//                    auth_dialog.dismiss();

                    TokenGet();
//                    Toast.makeText(getApplicationContext(),
//                            "Authorization Code is: " + authCode,
//                            Toast.LENGTH_SHORT).show();
                } else

                    view.loadUrl(url);

                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

//                pDialog.dismiss();

                if (url.contains("?code=") && authComplete != true) {

                } else if (url.contains("error=access_denied")) {
                    Log.i("", "ACCESS_DENIED_HERE");
                    resultIntent.putExtra("code", authCode);
                    authComplete = true;
                    setResult(Activity.RESULT_CANCELED, resultIntent);
//                    Toast.makeText(getApplicationContext(),Error
//                            "Error Occured", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void TokenGet() {

        final ProgressDialog pDialog;

        pDialog = new ProgressDialog(this);

        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);

        pDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, TOKEN_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                System.out.println(" Google Auth -->> " + response.toString());

                if (response != null) {
                    pDialog.dismiss();

                    try {

                        pDialog.dismiss();

                        JSONObject json = new JSONObject(response);
//                        Toast.makeText(getApplicationContext(), json.toString(),
//                                Toast.LENGTH_SHORT).show();

                        tok = json.getString("access_token");
                        String expire = json.getString("expires_in");
                        //String refresh = json.getString("refresh_token");
                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                try {
                                    fetchNameFromProfileServer(tok);
                                } catch (IOException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        }).start();

                        Log.d("Token Access", tok);
                        Log.d("Expire", expire);
                        //Log.d("Refresh", refresh);

                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();

                }


            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        pDialog.dismiss();


                    }
                })

        {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("code", Code);
                params.put("client_id", CLIENT_ID);
                params.put("client_secret", CLIENT_SECRET);
                params.put("redirect_uri", REDIRECT_URI);
                params.put("grant_type", GRANT_TYPE);

                return params;
            }

        };

        request.setRetryPolicy(new

                DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        Volley.newRequestQueue(this).add(request);
    }

    private void fetchNameFromProfileServer(String token) throws IOException,
            JSONException {

        URL url = new URL(
                "https://www.googleapis.com/oauth2/v1/userinfo?access_token=" + token);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        int sc = con.getResponseCode();
        Log.e("sc" + con.getResponseMessage(), "" + sc);
        if (sc == 200) {
            InputStream is = con.getInputStream();
            final String GOOGLE_USER_DATA = readResponse(is);
            is.close();

            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
//                    Toast.makeText(getApplicationContext(), GOOGLE_USER_DATA, Toast.LENGTH_SHORT).show();

                    try {
                        JSONObject obj = new JSONObject(GOOGLE_USER_DATA);
                        String email = obj.getString("email");
                        String name = obj.getString("name");

                        Toast.makeText(AuthenticationGmailActivity.this, "success", Toast.LENGTH_SHORT).show();
                        // registerUser(email, name);
                        startActivity(new Intent(AuthenticationGmailActivity.this, MainActivity.class));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
            return;
        } else if (sc == 401) {

            return;

        } else {

            return;
        }
    }

    private void registerUser(final String strEmail, final String strName) {

//        pDialog = KProgressHUD.create(this)
//                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
//                .setDimAmount(0.5f);
//        pDialog.setCancellable(true);
//        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, common.Server_URL + common.SIGNUP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //  pDialog.dismiss();
                            jsonObject = new JSONObject(response.toString());

                            status = jsonObject.optString("success");
                            message = jsonObject.optString("message");

                            if (status.equals("1")) {

                                System.out.println(" jsonArray -->> " + jsonObject.optJSONArray("result"));

                                resultObject = jsonObject.optJSONObject("result");

                                System.out.println(" memberId -->> " + resultObject.optString("memberId"));
//
//                                preferencesUtility.setEmail(resultObject.optString("email"));
//                                preferencesUtility.setUserName(resultObject.optString("name"));
//                                preferencesUtility.setUserId(resultObject.optString("id"));
//                                preferencesUtility.setPassword("");
//                                preferencesUtility.setVerifiy(true);
//                                preferencesUtility.setAccessToken(tok);
//                                preferencesUtility.setLoginType("gmail");
//                                preferencesUtility.setString("loginOwner", "true");
//                                preferencesUtility.setString("loginPostright", "true");
//                                preferencesUtility.setString("loginFollower", "true");
//                                dbAdapter = new AppDbAdapter(activity);
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

                            }

                        } catch (Exception e) {


                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (status.equals("1")) {

                                    Intent intent1 = new Intent(activity, MainActivity.class);
                                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    activity.startActivity(intent1);
                                    // activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                    activity.finish();

                                } else {

                                    if (message.equals("exception")) {

                                        Toast.makeText(AuthenticationGmailActivity.this, "no internet", Toast.LENGTH_SHORT).show();

                                    } else {

                                        Toast.makeText(AuthenticationGmailActivity.this, "My bulletin", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            }
                        });

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(AuthenticationGmailActivity.this, "unexpected response", Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(common.KEY_USERNAME, strName);
                params.put(common.KEY_EMAIL, strEmail);
                params.put(common.KEY_PASSWORD, "");
                params.put(common.KEY_PHONE_NUMBER, "1234567890");
                params.put(common.KEY_LOGIN_TYPE, "fbLogin");
                params.put(common.KEY_DEVICE_ID, "hi");

                System.out.println("PARAMETERS::::::::::::" + params.toString());

                return params;
            }

        };

//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(stringRequest);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        Volley.newRequestQueue(activity).add(stringRequest);
    }
}
