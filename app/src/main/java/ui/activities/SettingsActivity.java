package ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;

import com.example.iotashopping.R;
import com.google.firebase.auth.FirebaseAuth;

import FireStore.FireStoreClass;
import model.User;
import util.BaseActivity;
import util.GlideLoader;

public class SettingsActivity extends BaseActivity implements View.OnClickListener {
    User UserDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setupActionbar();
        Button btn_logout=findViewById(R.id.btn_logout);
        TextView ed_text=findViewById(R.id.tv_edit);
        btn_logout.setOnClickListener(this::onClick);
        ed_text.setOnClickListener(this::onClick);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserDetails();
    }

    private void setupActionbar() {
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar_settings_activity);
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

    private final void getUserDetails() {

        this.showProgressDialog("Please Wait");
        (new FireStoreClass()).getUserDetails((Activity) this);
    }
    public void UserDetailSuccess(User user){
        UserDetails=user;
        hideProgressDialog();
        GlideLoader glideLoader=new GlideLoader(this);
        String image= user.getImage();
        Uri imageUri=Uri.parse(image);
        ImageView imageView=findViewById(R.id.iv_user_photo);
        glideLoader.load_user_picture(imageView,imageUri);
        TextView name=findViewById(R.id.tv_name);
        name.setText(user.getFirstName());
        TextView email=findViewById(R.id.tv_email);
        TextView gender=findViewById(R.id.tv_gender);
        email.setText(user.getEmail());
        gender.setText(user.getGender());
        TextView mobile=findViewById(R.id.tv_mobile_number);
        String phone=String.valueOf(user.getMobile());
        mobile.setText(phone);
    }

    @Override
    public void onClick(View v) {
        if(v!=null){
            if(v.getId() == R.id.btn_logout){
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(this,LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
            if(v.getId()==R.id.tv_edit){
                Intent intent=new Intent(this,UserProfileActivity.class);
                intent.putExtra("user details",UserDetails);
                startActivity(intent);
            }
        }
    }
}
