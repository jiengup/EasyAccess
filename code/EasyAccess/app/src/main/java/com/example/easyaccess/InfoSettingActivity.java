package com.example.easyaccess;

import android.Manifest;
import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.easyaccess.utils.ActivityCollector;
import com.example.easyaccess.utils.PhotoUtils;
import com.example.easyaccess.utils.ToastUtil;
import com.example.easyaccess.ui.ItemGroup;
import com.example.easyaccess.ui.RoundImageView;
import com.example.easyaccess.ui.TitleLayout;

import java.io.FileNotFoundException;

import static android.provider.MediaStore.EXTRA_OUTPUT;


public class InfoSettingActivity extends AppCompatActivity implements View.OnClickListener {
    private ItemGroup ig_name;
    //private LoginUser




    private ItemGroup ig_sign;
    private ItemGroup ig_pass;
    private LinearLayout ll_portrait;
    private ToastUtil mToast = new ToastUtil();


    private RoundImageView ri_portrati;
    private Uri imageUri;  //拍照功能的地址
    private static final int TAKE_PHOTO = 1;
    private static final int FROM_ALBUMS = 2;
    private PopupWindow popupWindow;
    private String imagePath;  //从相册中选的地址
    private PhotoUtils photoUtils = new PhotoUtils();

    private static final int EDIT_NAME = 3;
    private static final int EDIT_SIGN = 4;
    private static final int EDIT_PASS = 5;
    private TitleLayout titleLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        setContentView(R.layout.activity_person_info);

        ig_name = findViewById(R.id.ig_name);
        ig_pass = findViewById(R.id.ig_pass);
        ig_sign = findViewById(R.id.ig_sign);
        ll_portrait = findViewById(R.id.ll_portrait);
        ri_portrati = findViewById(R.id.ri_portrait);
        titleLayout = findViewById(R.id.tl_title);

        ig_name.setOnClickListener(this);
        ig_pass.setOnClickListener(this);
        ig_sign.setOnClickListener(this);
        ll_portrait.setOnClickListener(this);

        //设置点击保存的逻辑
        titleLayout.getTextView_forward().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //loginUser.update()



                mToast.showMsg(InfoSettingActivity.this,"保存成功");
                finish();
            }
        });

        initInfo();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        //如果是退出则loginUser的数据重新初始化（也就是不保存数据库）





        ActivityCollector.removeActivity(this);
    }

    public void onClick(View v){
        switch (v.getId()){

            //点击修改头像的逻辑
            case R.id.ll_portrait:
                //展示选择框，并设置选择框的监听器
                show_popup_windows();
                break;
            //点击修改名字的逻辑
            case R.id.ig_name:
                Intent intent  = new Intent(InfoSettingActivity.this, EditName.class);
                startActivityForResult(intent, EDIT_NAME);
                break;
            case R.id.ig_pass:
                Intent intent2  = new Intent(InfoSettingActivity.this, EditPass.class);
                startActivityForResult(intent2, EDIT_PASS);
                break;
            case R.id.ig_sign:
                Intent intent3  = new Intent(InfoSettingActivity.this, EditSign.class);
                startActivityForResult(intent3, EDIT_SIGN);
                break;
            default:
                break;

        }
    }
    //处理拍摄照片回调
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode,data);
        switch (requestCode){
            //拍照得到图片
            case TAKE_PHOTO:
                if(resultCode == RESULT_OK){
                    try {
                        //将拍摄的图片展示并更新数据库
                        Bitmap bitmap = BitmapFactory.decodeStream((getContentResolver().openInputStream(imageUri)));
                        ri_portrati.setImageBitmap(bitmap);
                        //loginUser.setPortrait(photoUtils.bitmap2byte(bitmap));

                    }catch (FileNotFoundException e){
                        e.printStackTrace();
                    }
                }
                break;
            //从相册中选择图片
            case FROM_ALBUMS:
                if(resultCode == RESULT_OK){
                    //判断手机版本号
                    if(Build.VERSION.SDK_INT >= 19){
                        imagePath =  photoUtils.handleImageOnKitKat(this, data);
                    }else {
                        imagePath = photoUtils.handleImageBeforeKitKat(this, data);
                    }
                }
                if(imagePath != null){
                    //将拍摄的图片展示并更新数据库
                    Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                    ri_portrati.setImageBitmap(bitmap);
                    //loginUser.setPortrait(photoUtils.bitmap2byte(bitmap));




                }else{
                    Log.d("head","没有找到图片");
                }
                break;
            //如果是编辑名字，则修改展示
            case EDIT_NAME:
                if(resultCode == RESULT_OK){
                    //ig_name.getContentEdt().setText(loginUser.getName());




                }
                break;
            case EDIT_PASS:
                if(resultCode == RESULT_OK){
                    //ig_name.getContentEdt().setText(loginUser.getPass());




                }
                break;
            case EDIT_SIGN:
                if(resultCode == RESULT_OK){
                    //ig_name.getContentEdt().setText(loginUser.getSign());




                }
                break;
            default:
                break;
        }
    }
    //从数据库中初始化数据并展示
    private void initInfo(){










    }


    //展示修改头像的选择框，并设置选择框的监听器
    private void show_popup_windows(){
        RelativeLayout layout_photo_selected = (RelativeLayout) getLayoutInflater().inflate(R.layout.photo_select,null);
        if(popupWindow==null){
            popupWindow = new PopupWindow(layout_photo_selected, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
        }
        //显示popupwindows
        popupWindow.showAtLocation(layout_photo_selected, Gravity.CENTER, 0, 0);
        //设置监听器
        TextView take_photo =   layout_photo_selected.findViewById(R.id.take_photo);
        TextView from_albums =  layout_photo_selected.findViewById(R.id.from_albums);
        LinearLayout cancel =  layout_photo_selected.findViewById(R.id.cancel);
        //拍照按钮监听
        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(popupWindow != null && popupWindow.isShowing()) {
                    imageUri = photoUtils.take_photo_util(InfoSettingActivity.this, "com.example.easyaccess.fileprovider", "output_image.jpg");
                    //调用相机，拍摄结果会存到imageUri也就是outputImage中
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    intent.putExtra(EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, TAKE_PHOTO);
                    //去除选择框
                    popupWindow.dismiss();
                }
            }
        });
        //相册按钮监听
        from_albums.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //申请权限
                if(ContextCompat.checkSelfPermission(InfoSettingActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(InfoSettingActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }else {
                    //打开相册
                    Intent intent = new Intent("android.intent.action.GET_CONTENT");
                    intent.setType("image/*");
                    startActivityForResult(intent, FROM_ALBUMS);
                }
                //去除选择框
                popupWindow.dismiss();
            }
        });
        //取消按钮监听
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }
        });
    }
}
