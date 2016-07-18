package com.example.androidcodes.volleyexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnLogin;
    private EditText edtEmail, edtPassword;
    private String strEmail, strPassword;
    private JSONObject jsonObject, resultObject;
    private String status, message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:

                strEmail = edtEmail.getText().toString().trim();
                strPassword = edtPassword.getText().toString().trim();

                String message = isLoginValidate();

                if (message.equalsIgnoreCase("true")) {
                    if (NetworkStatus.getConnectivityStatus(LoginActivity.this)) {

                        loginUser();

                    } else {

                        Toast.makeText(LoginActivity.this, "no internet", Toast.LENGTH_SHORT).show();
                    }

                } else {

                    Toast.makeText(LoginActivity.this, "My bulletin", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    private String isLoginValidate() {

        if (!common.ValidEmail(strEmail)) {

            return "please enter email";

        } else if (strPassword.length() <= 0 || strPassword.equalsIgnoreCase("")) {

            return "please enter password";
        }
//        else if (common.getRegId().equals("")) {
//
//            return "Your device doesn't generate device id because of internet connection problem.\nClose application and start again.";
//
//        }
        else {

            return "true";
        }
    }

    private void loginUser() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, common.Server_URL + common.SIGNIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            System.out.println("response login-->> " + response.toString());

                            jsonObject = new JSONObject(response.toString());

                            status = jsonObject.optString("success");
                            message = jsonObject.optString("message");

                            if (status.equals("1")) {

                                resultObject = jsonObject.optJSONObject("result");

                                System.out.println("email --> " + resultObject.optString("email"));

//                                preferencesUtility.setEmail(resultObject.optString("email"));
//                                preferencesUtility.setUserName(resultObject.optString("name"));
//                                preferencesUtility.setUserId(resultObject.optString("id"));
//                                preferencesUtility.setPassword(strPassword);
//                                preferencesUtility.setVerifiy(true);
//                                preferencesUtility.setLoginType("cl");
//                                preferencesUtility.setString("loginOwner", "true");
//                                preferencesUtility.setString("loginPostright", "true");
//                                preferencesUtility.setString("loginFollower", "true");
//
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


                            }

                        } catch (Exception e) {

//                            progressWheel.setVisibility(View.GONE);
//                            btnLogin.setVisibility(View.VISIBLE);
//
//                            btn_board_glogin.setEnabled(true);
//                            btn_board_fblogin.setEnabled(true);
//
//                            link_signup.setEnabled(true);
//                            link_forgot_pass.setEnabled(true);

                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (status.equals("1")) {

                                    Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();

                                    // startActivity(new Intent(getApplicationContext(), MainActivity.class));

                                    finish();

                                } else {

//                                    link_signup.setEnabled(true);
//                                    link_forgot_pass.setEnabled(true);
//
//                                    btn_board_glogin.setEnabled(true);
//                                    btn_board_fblogin.setEnabled(true);
//
//                                    progressWheel.setVisibility(View.GONE);
//                                    btnLogin.setVisibility(View.VISIBLE);

                                    if (message.equals("exception")) {

                                        Toast.makeText(LoginActivity.this, "unexpected response", Toast.LENGTH_SHORT).show();

                                    } else {

                                        Toast.makeText(LoginActivity.this, "my bulletin", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            }
                        });
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

//                        link_signup.setEnabled(true);
//                        link_forgot_pass.setEnabled(true);
//
//                        btn_board_glogin.setEnabled(true);
//                        btn_board_fblogin.setEnabled(true);
//
//                        progressWheel.setVisibility(View.GONE);
//                        btnLogin.setVisibility(View.VISIBLE);

                        Toast.makeText(LoginActivity.this, "unexpected response", Toast.LENGTH_SHORT).show();

                        // Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_LONG).show();


                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(common.KEY_EMAIL, strEmail);
                params.put(common.KEY_LOGIN_TYPE, "customLogin");
                params.put(common.KEY_PASSWORD, strPassword);
                params.put(common.KEY_DEVICE_ID, "hi");
                System.out.println(" login param --> " + params.toString());

                return params;
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(LoginActivity.this).add(stringRequest);
    }

}
