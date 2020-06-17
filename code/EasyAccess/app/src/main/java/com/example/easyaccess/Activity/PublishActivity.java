package com.example.easyaccess.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.easyaccess.R;
import com.example.easyaccess.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

public class PublishActivity extends AppCompatActivity {
    private EditText etTitle;
    private EditText etDesc;
    private EditText etTel;
    private EditText etEmail;
    private EditText etTag;
    private SharedPreferences mSharedPreferences;
    private RequestQueue requestQueue;
    private Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        mSharedPreferences = this.getSharedPreferences("user", Context.MODE_PRIVATE);
        etTitle = findViewById(R.id.ed_title);
        etDesc = findViewById(R.id.ed_desc);
        etTel = findViewById(R.id.ed_tel);
        etEmail = findViewById(R.id.ed_email);
        etTag = findViewById(R.id.ed_tag);
        btnSend = findViewById(R.id.btn_publish);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etTitle.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "请输入标题", Toast.LENGTH_SHORT).show();
                }
                else if(etDesc.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "请输入描述", Toast.LENGTH_SHORT).show();
                }
                else if(etTel.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "请输入标题", Toast.LENGTH_SHORT).show();
                }
                else if(etEmail.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "请输入标题", Toast.LENGTH_SHORT).show();
                }
                else{
                    publish(new VolleyCallback() {
                        @Override
                        public void onSuccessResponse(String result) {
                            try{
                                JSONObject response = new JSONObject(result);
                                int code = response.getInt("code");
                                if(code == 0){
                                    Toast.makeText(getApplicationContext(), "发布成功", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(getApplicationContext(), "发布失败！", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    finish();
                }
            }
        });
    }
    public void publish(final VolleyCallback callback){
        String url = Constants.TEAMWORK_URL + "publish";
        try{
            JSONObject jsonBody = new JSONObject();

            jsonBody.put("publisher_email", mSharedPreferences.getString("email", "null"));
            jsonBody.put("title", etTitle.getText());
            jsonBody.put("desc", etDesc.getText());
            jsonBody.put("tel", etTel.getText());
            jsonBody.put("email", etEmail.getText());
            jsonBody.put("tags", etTag.getText());

            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    callback.onSuccessResponse(response.toString());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Error" + error.toString(), Toast.LENGTH_LONG).show();
                }
            });
            requestQueue.add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public interface VolleyCallback {
        void onSuccessResponse(String result);
    }
}
