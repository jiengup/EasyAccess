package com.example.easyaccess;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

import java.util.Random;

public class RegisterActivity extends AppCompatActivity {
    private EditText mEtID;
    private EditText mEtIDCode;
    private EditText mEtEmail;
    private EditText mEtCode;
    private Button mBtnGetCode;
    private EditText mEtPassword;
    private Button mBtnFinish;
    private String code;
    private Thread thread;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    int leftWaitTime;
    private Message message;
    private static final int MSG_COUNT_DOWN = 0x0001;


    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg){
            super.handleMessage(msg);
            switch(msg.what){
                case MSG_COUNT_DOWN:
                    if(leftWaitTime > 0){
                        leftWaitTime -= 1;
                        mBtnGetCode.setText(leftWaitTime + "s后重新获取");
                        message = mHandler.obtainMessage(MSG_COUNT_DOWN);
                        mHandler.sendMessageDelayed(message, 1000);
                    }
                    else{
                        mBtnGetCode.setText("获取验证码");
                        mBtnGetCode.setEnabled(true);
                    }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEtID = findViewById(R.id.et_id);
        mEtIDCode = findViewById(R.id.et_id_code);
        mEtEmail = findViewById(R.id.et_email);
        mEtCode = findViewById(R.id.et_code);
        mBtnGetCode = findViewById(R.id.btn_get_code);
        mEtPassword = findViewById(R.id.et_password);
        mBtnFinish = findViewById(R.id.btn_finish);

        mSharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        //对发送验证码按钮设置监听事件
        mBtnGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEtEmail.getText().toString();
                if(email.equals("")){
                    ToastUtil.showMsg(getApplicationContext(), "请输入邮箱");
                }else{
                    boolean isValid = ValidateUtils.validate(email);
                    if(isValid){
                        code = generateCode(6);
                        leftWaitTime = 10;
                        mBtnGetCode.setEnabled(false);
                        message = Message.obtain();
                        message.what = MSG_COUNT_DOWN;
                        mHandler.sendMessage(message);
                        //发送邮件
                        sendCode(email, code, new VolleyCallback() {
                            @Override
                            public void onSuccessResponse(String result) {
                                try{
                                    JSONObject response = new JSONObject(result);
                                    int ret = response.getInt("ret");
                                    String desc = response.getString("desc");
                                    if(ret == 2){
                                        ToastUtil.showMsg(getApplicationContext(), desc);
                                        finish();
                                    }else{
                                        ToastUtil.showMsg(getApplicationContext(), desc);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }else{
                        ToastUtil.showMsg(getApplicationContext(), "邮箱格式有误");
                    }
                }
            }
        });

        mBtnFinish.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String id = mEtID.getText().toString();
                String idCode = mEtIDCode.getText().toString();
                String email = mEtEmail.getText().toString();
                String password = mEtPassword.getText().toString();
                String inputCode = mEtCode.getText().toString();
                if(id.equals(""))
                    ToastUtil.showMsg(getApplicationContext(), "请输入学号/工号");
                else{
                    if(idCode.equals(""))
                        ToastUtil.showMsg(getApplicationContext(), "请输入身份证后六位");
                    else{
                        if(email.equals(""))
                            ToastUtil.showMsg(getApplicationContext(), "请输入邮箱");
                        else{
                            if(inputCode.equals(""))
                                ToastUtil.showMsg(getApplicationContext(), "请输入验证码");
                            else{
                                if(password.equals(""))
                                    ToastUtil.showMsg(getApplicationContext(), "请输入密码");
                                else{
                                    if(password.length() < 6)
                                        ToastUtil.showMsg(getApplicationContext(), "密码至少为6位");
                                    else{
                                        if(inputCode.equals(code)){
                                            register(id, idCode, email, password);
                                            ToastUtil.showMsg(getApplicationContext(), "注册成功");
                                            finish();
                                        }else{
                                            ToastUtil.showMsg(getApplicationContext(), "验证码错误");
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    //生成一个验证码
    public String generateCode(int length){
        //length：验证码的长度
        StringBuilder result = new StringBuilder();
        Random random = new Random();
        for(int i=0; i<length; i++){
            result.append(random.nextInt(10));
        }
        return result.toString();
    }

    public void register(String id, String idCode, String email, String password){
        // TODO: validate id and idcode then return the result to decide registerable
        String url = "http://127.0.0.1:8000/myauth/register";
        try{
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            JSONObject jsonBody = new JSONObject();

            jsonBody.put("id", id);
            jsonBody.put("idCode", idCode);
            jsonBody.put("email", email);
            jsonBody.put("password", password);

            JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>(){

                @Override
                public void onResponse(JSONObject jsonObject) {
                    Log.d("onResponse", jsonObject.toString());
                }
            }, new Response.ErrorListener(){

                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.e("onErrorResponse", volleyError.toString());
                }
            });
            queue.add(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendCode(String email, String code, final VolleyCallback callback){
        String s;
        String url = "http://127.0.0.1:8000/myauth/send_auth_code";
        try{
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            JSONObject jsonBody = new JSONObject();

            jsonBody.put("email", email);
            jsonBody.put("code", code);

            JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>(){

                @Override
                public void onResponse(JSONObject jsonObject) {
                    Log.d("onResponse", jsonObject.toString());
                    callback.onSuccessResponse(jsonObject.toString());
                }
            }, new Response.ErrorListener(){

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

    public interface VolleyCallback{
        void onSuccessResponse(String result);
    }
}
