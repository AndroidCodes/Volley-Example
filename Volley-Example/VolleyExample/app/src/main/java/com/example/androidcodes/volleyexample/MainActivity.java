package com.example.androidcodes.volleyexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtEmail, edtName, edtPassword, edtCPassword;
    private Button btnSignup, btnLogin, gmail, fblogin;
    private String strEmail, strPassword, strName, strCPassword;
    private JSONObject jsonObject, resultObject;
    private String status, message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtName = (EditText) findViewById(R.id.edtName);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtCPassword = (EditText) findViewById(R.id.edtCPassword);

        btnSignup = (Button) findViewById(R.id.btnSignup);
        btnSignup.setOnClickListener(this);

        gmail = (Button) findViewById(R.id.gmail);
        gmail.setOnClickListener(this);

        fblogin = (Button) findViewById(R.id.fblogin);
        fblogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {


            case R.id.btnSignup:


                strName = edtName.getText().toString().trim();
                strEmail = edtEmail.getText().toString().trim();
                strPassword = edtPassword.getText().toString().trim();
                strCPassword = edtCPassword.getText().toString().trim();


                String message = isLoginValidate();

                if (message.equalsIgnoreCase("true")) {


                    if (NetworkStatus.getConnectivityStatus(MainActivity.this)) {

                        registerUser();

                    } else {

                        Toast.makeText(MainActivity.this, "no internet", Toast.LENGTH_SHORT).show();
                    }

                } else {

                    Toast.makeText(MainActivity.this, "My bulletin", Toast.LENGTH_SHORT).show();

                }
                break;
            case R.id.gmail:

                startActivity(new Intent(MainActivity.this, AuthenticationGmailActivity.class));

                break;
            case R.id.fblogin:

                startActivity(new Intent(MainActivity.this, AuthenticationFBActivity.class));

                break;


        }
    }


    private String isLoginValidate() {

        if (strName.length() == 0
                || strName.equalsIgnoreCase("")) {

            return "please enter username";

        } else if (!common.ValidEmail(strEmail)) {

            return "please enter email";

        } else if (strPassword.length() == 0

                || strPassword.equalsIgnoreCase("")) {

            return "please enter password";

        } else if (strPassword.length() < 5 || strPassword.length() > 15) {

            return "password between 5 and 15 chars.";

        } else if (strCPassword.equals("")) {

            return "please enter confirm password";

        } else if (!strPassword.equals(strCPassword)) {

            return "password and confirm do not match";

        } else {

            return "true";

        }

    }

    private void registerUser() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, common.Server_URL + common.SIGNUP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            jsonObject = new JSONObject(response.toString());

                            System.out.println(" res -->> " + response.toString());

                            status = jsonObject.optString("success");
                            message = jsonObject.optString("message");

                            if (status.equals("1")) {

                                System.out.println(" jsonArray -->> " + jsonObject.optJSONArray("result"));

                                resultObject = jsonObject.optJSONObject("result");

                                System.out.println(" memberId -->> " + resultObject.optString("memberId"));

                            }

                        } catch (Exception e) {


                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (status.equals("1")) {

                                    Toast.makeText(MainActivity.this, "Sign Up successful", Toast.LENGTH_SHORT).show();
                                    finish();
                                    startActivity(new Intent(MainActivity.this, LoginActivity.class));

                                } else {

                                    if (message.equals("exception")) {

                                        Toast.makeText(MainActivity.this, "unexpected response", Toast.LENGTH_SHORT).show();


                                    } else {
                                        Toast.makeText(MainActivity.this, "my bulletin", Toast.LENGTH_SHORT).show();

                                    }
                                }

                            }
                        });
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                        System.out.println("Error>>>>>>>>> " + error.getMessage());

                        if (error instanceof NoConnectionError) {
                            System.out.println("NoConnectionError>>>>>>>>>");

                        } else if (error instanceof AuthFailureError) {
                            System.out.println("AuthFailureError>>>>>>>>>");

                        } else if (error instanceof ServerError) {
                            System.out.println("ServerError>>>>>>>>>");

                        } else if (error instanceof NetworkError) {
                            System.out.println("NetworkError>>>>>>>>>");

                        } else if (error instanceof ParseError) {
                            System.out.println("ParseError>>>>>>>>>");

                        } else if (error instanceof TimeoutError) {
                            System.out.println("TimeoutError>>>>>>>>>");

                        }

                        Toast.makeText(MainActivity.this, "unexpected response", Toast.LENGTH_SHORT).show();


                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(common.KEY_USERNAME, strName);
                params.put(common.KEY_EMAIL, strEmail);
                params.put(common.KEY_PASSWORD, strPassword);
                params.put(common.KEY_PHONE_NUMBER, "1234567890");
                params.put(common.KEY_LOGIN_TYPE, "customLogin");
                params.put(common.KEY_DEVICE_ID, "hi");
                return params;
            }
        };

//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(stringRequest);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        Volley.newRequestQueue(MainActivity.this).add(stringRequest);
    }
}
