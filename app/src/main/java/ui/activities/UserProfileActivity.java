package ui.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.iotashopping.R;

import java.util.HashMap;

import FireStore.FireStoreClass;
import model.User;
import util.BaseActivity;
import util.GlideLoader;
import util.ImageChooser;

public class UserProfileActivity extends BaseActivity implements View.OnClickListener {
    ImageChooser imageChooser=new ImageChooser();
    ImageView imageView;
    GlideLoader glideLoader=new GlideLoader(this);
    Button button_submit;
    EditText mobile;
    RadioButton rb_male;
    User user_details;
    Uri image;
    String imageURL;
    public static final int READ_STORAGE_PERMISSION_CODE = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        EditText et_firstname=findViewById(R.id.et_first_name);
        EditText et_lastname=findViewById(R.id.et_last_name);
        EditText et_email=findViewById(R.id.et_email);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Intent intent=getIntent();
        user_details=new User();
        if(intent.hasExtra("user details")) {
           user_details= intent.getParcelableExtra("user details");
        }
        et_firstname.setEnabled(false);
        et_firstname.setText(user_details.getFirstName());
        et_email.setEnabled(false);
        et_email.setText(user_details.getEmail());
        imageView=findViewById(R.id.iv_user_photo);
        button_submit=findViewById(R.id.btn_submit);
        mobile=findViewById(R.id.et_mobile_number);
        rb_male=findViewById(R.id.rb_male);
        imageView.setOnClickListener((this::onClick));
         button_submit.setOnClickListener(this::onClick);

    }

    @Override
    public void onClick(View v) {
        if (v != null) {
            if (v.getId() == R.id.iv_user_photo) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    //showErrorSnackBar("You already have the storage permission.", false);

                    imageChooser.setImage_picker(this);

                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_STORAGE_PERMISSION_CODE);
                }
            }
            if (v.getId() == R.id.btn_submit) {

                if (validateUserDetails()) {
                    showProgressDialog("please wait");
                    if (image != null) {
                        FireStoreClass fireStoreClass = new FireStoreClass();
                        fireStoreClass.uploadImage(image, UserProfileActivity.this);
                    }else{
                        updateUserProfileDetails();
                    }
                }

            }
        }
    }
        public final void userProfileUpdateSuccess( ) {
        this.hideProgressDialog();
        Toast.makeText((Context)this, "user details updated", Toast.LENGTH_SHORT).show();
        this.startActivity(new Intent((Context)this, DashBoardActivity.class));
        this.finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==READ_STORAGE_PERMISSION_CODE){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                //showErrorSnackBar("The storage permission is granted.", false);
                imageChooser.setImage_picker(this);
            }else{
                Toast.makeText(
                        this,
                        R.string.read_storage_permission_denied,
                        Toast.LENGTH_LONG
                ).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if(requestCode==imageChooser.getImageRequestCode()){
                try {
                     image=data.getData();
                    //imageView.setImageURI(image);
                    glideLoader.load_user_picture(imageView,image);
                    /*FireStoreClass fireStoreClass=new FireStoreClass();
                    fireStoreClass.uploadImage(image,UserProfileActivity.this);*/
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
    public boolean validateUserDetails(){
        String m_number=mobile.getText().toString();
        if(TextUtils.isEmpty(m_number)){
            showErrorSnackBar("please enter moble number",true);
            return false;
        }
        return true;

    }
    public void updateUserProfileDetails(){
            FireStoreClass fireStoreClass=new FireStoreClass();
            HashMap userHashMap=new HashMap();
            String m_number=mobile.getText().toString();
            String Gender=rb_male.isChecked()?"MALE":"FEMALE";

            if(!m_number.isEmpty()){
                userHashMap.put("mobile", Long.parseLong(m_number));
            }
            userHashMap.put("gender",Gender);
            if(image!=null){
                String user_image=image.toString();
                userHashMap.put("image",user_image);
            }

            //showProgressDialog("please wait");
        String profile="1";
        userHashMap.put("profileCompleted", Integer.parseInt(profile));
        fireStoreClass.updateUserProfileData(this,userHashMap);
            showErrorSnackBar("your details are valid, you can update them :) ",false);

    }

    public void uploadImageSuccess(String url){
        //hideProgressDialog();
        imageURL=url;
        //Toast.makeText(UserProfileActivity.this, url, Toast.LENGTH_SHORT).show();
        updateUserProfileDetails();

    }
}