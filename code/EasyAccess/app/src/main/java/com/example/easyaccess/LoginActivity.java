package com.example.easyaccess;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.easyaccess.utils.ToastUtil;
import com.example.easyaccess.utils.ValidateUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private Button mBtnRegister;
    private EditText mEtEmail;
    private EditText mEtPassword;
    private Button mBtnLogin;
    private SharedPreferences mSharePreferences;
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEtEmail = findViewById(R.id.et_email);
        mEtPassword = findViewById(R.id.et_password);
        mBtnLogin = findViewById(R.id.btn_login);

        mBtnRegister = findViewById(R.id.btn_register);
        mBtnRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        mSharePreferences = getSharedPreferences("user", MODE_PRIVATE);
        mEditor = mSharePreferences.edit();
        mBtnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final String email = mEtEmail.getText().toString();
                String password = mEtPassword.getText().toString();
                ToastUtil.showMsg(getApplicationContext(), email + "|" + password);
                if (email.equals("")) {
                    ToastUtil.showMsg(getApplicationContext(), "请输入邮箱");
                } else if (password.equals("")) {
                    ToastUtil.showMsg(getApplicationContext(), "请输入密码");
                } else if (!ValidateUtils.validate(email)) {
                    ToastUtil.showMsg(getApplicationContext(), "邮箱格式不正确");
                } else if (password.length() < 6) {
                    ToastUtil.showMsg(getApplicationContext(), "密码至少为6位");
                } else {
                    login(email, password, new VolleyCallback() {
                        @Override
                        public void onSuccessResponse(String result) {
                            try {
                                JSONObject response = new JSONObject(result);
                                int ret = response.getInt("ret");
                                String desc = response.getString("desc");
                                if (ret == 1) {
                                    ToastUtil.showMsg(getApplicationContext(), desc);
                                } else {
                                    String username = response.getString("username");
                                    String head_portrait_url = response.getString("head_portrait");
                                    ToastUtil.showMsg(getApplicationContext(), desc);

                                    //TODO:
                                    mEditor.putString("email", email);
                                    mEditor.putString("usename", username);
                                    mEditor.putString("head_portrait_url", head_portrait_url);
                                    mEditor.apply();

                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }

    private void login(String email, String password, final VolleyCallback callback) {
        String s;
        String url = "http://127.0.0.1:8000/myauth/login";
        try {
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            JSONObject jsonBody = new JSONObject();

            jsonBody.put("email", email);
            jsonBody.put("password", password);

            JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject jsonObject) {
                    callback.onSuccessResponse(jsonObject.toString());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.e("onErrorResponse", volleyError.toString());
                    Toast.makeText(getApplicationContext(), "Error: " + volleyError.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            queue.add(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public interface VolleyCallback {
        void onSuccessResponse(String result);
    }
}
