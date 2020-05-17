package com.example.easyaccess;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.easyaccess.ui.news.CommentAdapter;
import com.example.easyaccess.ui.news.CommentItem;
import com.example.easyaccess.utils.Constants;
import com.example.easyaccess.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Comment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentActivity extends AppCompatActivity {
    private CommentAdapter mAdapter;
    private RequestQueue requestQueue;
    private List<CommentItem> list;
    private SharedPreferences mSharedPreferences;
    private TextView tv;
    private LinearLayout showStar;
    private LinearLayout showInput;
    private ImageView imageStar;
    private EditText et;
    private Button btnSend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        mSharedPreferences = this.getSharedPreferences("user", Context.MODE_PRIVATE);
        tv = findViewById(R.id.comment_text);
        showStar = findViewById(R.id.show_star);
        showInput = findViewById(R.id.show_input);
        imageStar = findViewById(R.id.check_Is_Checked);
        et = findViewById(R.id.comment_input);
        btnSend = findViewById(R.id.send_comment);

        ListView listView = findViewById(R.id.comment_display_item);
        requestQueue = Volley.newRequestQueue(this);
        list = new ArrayList<CommentItem>();
        putData(String.valueOf(getIntent().getStringExtra("news_id")));
        SharedPreferences msp = getSharedPreferences("user", MODE_PRIVATE);
        boolean isLogin;
        if(!msp.getString("email", "null").equals("null")){
            isLogin = true;
        }
        else{
            isLogin = false;
        }
        mAdapter = new CommentAdapter(this, requestQueue, list, isLogin);
        listView.setAdapter(mAdapter);
        listView.setEmptyView((TextView)findViewById(R.id.nocommentshow));
        if(mSharedPreferences.getString("email", "null").equals("null")){
            showStar.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "无法发表评论，请先登录", Toast.LENGTH_LONG).show();
        }
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String replyContent;
                replyContent = et.getText().toString();
                if(replyContent.equals("")){
                    Toast.makeText(getApplicationContext(),"请输入评论内容",Toast.LENGTH_LONG).show();
                }
                else{
                    CommentItem commentItem = new CommentItem();
                    commentItem.setNickname(mSharedPreferences.getString("username", "null"));
                    commentItem.setHeadUrl(Constants.BASE_URL + mSharedPreferences.getString("headPortrait", ""));
                    commentItem.setContent(replyContent);
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    commentItem.setReleaseTime(df.format(new Date()));
                    list.add(commentItem);
                    String url = Constants.NEWS_URL +
                            "add_comment" + "?_id=" +
                            String.valueOf(getIntent().getStringExtra("news_id")) + "&user_email="
                            + mSharedPreferences.getString("email", "null") +
                            "&comment_text=" + replyContent;
                    try{
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    int code = response.getInt("code");
                                    if (code == 0) {
                                        Toast.makeText(getBaseContext(), "发表成功", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getBaseContext(), "发表失败", Toast.LENGTH_SHORT).show();
                                    }
                                    mAdapter.notifyDataSetChanged();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(),"获取失败",Toast.LENGTH_LONG).show();
                            }
                        });
                        requestQueue.add(jsonObjectRequest);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    et.setText("");
                    InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(et.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStar.setVisibility(View.GONE);
                showInput.setVisibility(View.VISIBLE);
                et.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.showSoftInput(et, 0);
            }
        });
        btnSend.setEnabled(false);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() > 0){
                    btnSend.setEnabled(true);
                }else{
                    btnSend.setEnabled(false);
                }
            }
        });
    }

    private void putData(String news_id){
        String url = Constants.NEWS_URL + "get_comment_list/" + "?_id=" + news_id;
        System.out.println(url);
        try{
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        int code = response.getInt("code");
                        if (code == 0) {
                            JSONArray data = response.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                CommentItem commentItem = new CommentItem();
                                JSONObject item = data.getJSONObject(i);
                                commentItem.setNickname(item.getString("nickname"));
                                commentItem.setHeadUrl(Constants.BASE_URL + item.getString("head_url"));
                                commentItem.setReleaseTime(item.getString("release_time"));
                                commentItem.setContent(item.getString("content"));
                                commentItem.setTotalStars(item.getInt("total_stars"));
                                list.add(commentItem);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "网络错误", Toast.LENGTH_LONG).show();
                        }
                        mAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(),"获取失败",Toast.LENGTH_LONG).show();
                }
            });
            requestQueue.add(jsonObjectRequest);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
