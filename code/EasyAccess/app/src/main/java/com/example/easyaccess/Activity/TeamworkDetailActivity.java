package com.example.easyaccess.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.easyaccess.R;
import com.example.easyaccess.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

public class TeamworkDetailActivity extends AppCompatActivity {
    private TextView tvTitle;
    private ImageView head;
    private TextView userName;
    private TextView pubTime;
    private TextView desc;
    private TextView tel;
    private TextView email;
    private Button toTel;
    private Button toEmail;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teamwork_detail);
        tvTitle = findViewById(R.id.teamwork_title);
        head = findViewById(R.id.teamwork_head);
        userName = findViewById(R.id.teamwork_username);
        pubTime = findViewById(R.id.teamwork_pubtime);
        desc = findViewById(R.id.teamwork_desc);
        tel = findViewById(R.id.teamwork_tel);
        email = findViewById(R.id.teamwork_email);
        toTel = findViewById(R.id.btn_to_tel);
        toEmail = findViewById(R.id.btn_to_mail);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        Intent intent = getIntent();
        final String teamworkId = intent.getStringExtra("teamwork_id");
        initView(teamworkId);


        toTel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("clicking!");
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + tel.getText()));
                startActivity(intent);
            }
        });
        toEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data=new Intent(Intent.ACTION_SENDTO);
                data.setData(Uri.parse("mailto:" + email.getText()));
                data.putExtra(Intent.EXTRA_SUBJECT, "这是标题");
                data.putExtra(Intent.EXTRA_TEXT, "这是内容");
                startActivity(data);
            }
        });
    }


    public void initView(String teamworkId){
        String url = Constants.TEAMWORK_URL + "get_wanted_detail/" + "?_id=" + teamworkId;
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //System.out.println(response.toString());
                try{
                    int code = response.getInt("code");
                    if(code == 0){
                        tvTitle.setText(response.getString("title"));
                        Glide.with(head.getContext()).load(Constants.BASE_URL + response.getString("head")).into(head);
                        userName.setText(response.getString("user_name"));
                        pubTime.setText("发布于：" + response.getString("pub_time"));
                        desc.setText("        " + response.getString( "desc"));
                        tel.setText(response.getString("tel"));
                        email.setText(response.getString("email"));
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "获取项目数据错误", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error" + error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public interface VolleyCallback {
        void onSuccessResponse(String result);
    }
}
