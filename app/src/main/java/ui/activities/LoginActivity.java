package ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.iotashopping.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import FireStore.FireStoreClass;
import model.User;
import util.BaseActivity;

public class LoginActivity extends BaseActivity {
    EditText email,password;
    private FirebaseAuth mauth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        email=findViewById(R.id.Email);
        password=findViewById(R.id.Password);
        mauth=FirebaseAuth.getInstance();
        /*if(mauth.getCurrentUser()!=null){
            Intent intent =new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }*/
    }
    public void  sign_up(View view){
        Intent intent =new Intent(LoginActivity.this, RegistrationActivity.class);
        startActivity(intent);
    }
    public void sign_in(View view){
        String user_email=email.getText().toString();
        String user_password=password.getText().toString();
        if(TextUtils.isEmpty(user_email)){
            Toast.makeText(LoginActivity.this, "Enter Email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(user_password)){
            Toast.makeText(LoginActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
            return;
        }
        showProgressDialog("please wait");
        mauth.signInWithEmailAndPassword(user_email,user_password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                           /* Intent intent =new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("user_id",FirebaseAuth.getInstance().getCurrentUser().getUid());
                            intent.putExtra("email",user_email);
                            startActivity(intent);*/
                            FireStoreClass fireStoreClass=new FireStoreClass();
                            fireStoreClass.getUserDetails(LoginActivity.this);
                            Toast.makeText(LoginActivity.this, "LogIn successfull " +task.getException(), Toast.LENGTH_SHORT).show();

                        }else {
                            hideProgressDialog();
                            Toast.makeText(LoginActivity.this, "LogIn failed!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    public void userLoggedInSuccess(User user) {
        hideProgressDialog();
        // Print the user details in the log as of now.
        Log.i("First Name: ", user.getFirstName());
        Log.i("Email: ", user.getEmail());
        long mobile=user.getMobile();
        Log.i("mobile", String.valueOf(mobile));

        if (user.getProfileCompleted() == 0) {
            // If the user profile is incomplete then launch the UserProfileActivity.
            Intent intent =new  Intent(LoginActivity.this, UserProfileActivity.class);
            intent.putExtra("user details",user);
            startActivity(intent);
        } else {
            // Redirect the user to Main Screen after log in.
            startActivity(new Intent(LoginActivity.this, DashBoardActivity.class));
        }
        // Redirect the user to Main Screen after log in.

        finish();
    }
}