package ui.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
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
    TextView tv_title;
    EditText et_firstname;
    EditText et_lastname;
    RadioButton rb_male_btn;
    RadioButton rb_female_btn;
    User user_details;
    Uri image;
    String imageURL;
    public static final int READ_STORAGE_PERMISSION_CODE = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        et_firstname=findViewById(R.id.et_first_name);
        et_lastname=findViewById(R.id.et_last_name);
        EditText et_email=findViewById(R.id.et_email);
        imageView=findViewById(R.id.iv_user_photo);
        button_submit=findViewById(R.id.btn_submit);
        mobile=findViewById(R.id.et_mobile_number);
        rb_male_btn=findViewById(R.id.rb_male);
        rb_female_btn=findViewById(R.id.rb_female);
        tv_title=findViewById(R.id.tv_title);
        //getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Intent intent=getIntent();
        //user_details=new User();
        if(intent.hasExtra("user details")) {
           user_details= intent.getParcelableExtra("user details");
        }
        if(user_details.getProfileCompleted()==0){
            tv_title.setText("Profile Complete");
            et_firstname.setEnabled(false);
            et_firstname.setText(user_details.getFirstName());
            et_email.setEnabled(false);
            et_email.setText(user_details.getEmail());
        }else{
            tv_title.setText("Edit Profile");
            setupActionbar();
             glideLoader.load_user_picture(imageView,Uri.parse(user_details.getImage()));
             et_firstname.setText(user_details.getFirstName());
           // et_lastname.setText(mUserDetails.lastName)
            et_email.setEnabled(false);
            et_email.setText(user_details.getEmail());

            if (user_details.getMobile() != 0L) {
                mobile.setText(String.valueOf(user_details.getMobile()));
            }
            Log.i("gender",user_details.getGender());
            if(TextUtils.equals(user_details.getGender(),"FEMALE") ) {
                rb_female_btn.setChecked(true);
                //rb_male.setChecked(false);
            }

            if(TextUtils.equals(user_details.getGender(),"MALE") ) {
                rb_male_btn.setChecked(true);
            }
        }


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
                        fireStoreClass.uploadImage(image, UserProfileActivity.this,"user_profile");
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
            String first_name= et_firstname.getText().toString();
            if(first_name!=user_details.getFirstName()){
                userHashMap.put("firstName",first_name);
            }
            String m_number=mobile.getText().toString();
            String Gender=rb_male_btn.isChecked()?"MALE":"FEMALE";

            if(!m_number.isEmpty() && m_number!=String.valueOf(user_details.getMobile())){
                userHashMap.put("mobile", Long.parseLong(m_number));
            }
            if(!Gender.isEmpty() && Gender!=user_details.getGender()) {
                userHashMap.put("gender", Gender);
            }
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
    private void setupActionbar() {
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar_user_profile_activity);
        setSupportActionBar(toolbar);
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_vector_chevron_left_24);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}