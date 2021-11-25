package ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.iotashopping.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import FireStore.FireStoreClass;
import model.User;
import util.BaseActivity;

public class RegistrationActivity extends BaseActivity {
    EditText name,email,password;
    private FirebaseAuth auth;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        getSupportActionBar().hide();
        name=findViewById(R.id.Name);
        email=findViewById(R.id.Email);
        password=findViewById(R.id.Password);
        auth=FirebaseAuth.getInstance();
        Button signup=findViewById(R.id.signUp);
        sharedPreferences=this.getSharedPreferences("Iota shopping", Context.MODE_PRIVATE);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sign_up();
            }
        });

    }
    public void  sign_in(View view){
        Intent intent =new Intent(RegistrationActivity.this, LoginActivity.class);
        startActivity(intent);
    }
    public void sign_up(){
        String user_name=name.getText().toString();
        String user_email=email.getText().toString();
        String user_password=password.getText().toString();
        sharedPreferences.edit().putString("user_name",user_name).apply();
        if(TextUtils.isEmpty(user_email)){
            //Toast.makeText(RegistrationActivity.this, "Enter Email", Toast.LENGTH_SHORT).show();
            showErrorSnackBar("Enter Email",true);
            return;
        }
        if(TextUtils.isEmpty(user_name)){
            //Toast.makeText(RegistrationActivity.this, "Enter User Name", Toast.LENGTH_SHORT).show();
            showErrorSnackBar("Enter User Name",true);
            return;
        }
        if(TextUtils.isEmpty(user_password)){
            //Toast.makeText(RegistrationActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
            showErrorSnackBar("Enter password",true);
            return;
        }
        if(user_password.length()<6){
            //Toast.makeText(RegistrationActivity.this, "Password TOO Short!!!", Toast.LENGTH_SHORT).show();
            showErrorSnackBar("Password TOO Short!!!",true);
        }
        showProgressDialog("please wait");
        auth.createUserWithEmailAndPassword(user_email,user_password).addOnCompleteListener(RegistrationActivity.this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    FirebaseUser firebaseUser=task.getResult().getUser();
                    User user=new User(firebaseUser.getUid().toString(),user_name,user_email,"",0,"",0);
                   /* Intent intent =new Intent(RegistrationActivity.this, MainActivity.class);
                    intent.putExtra("use_id",firebaseUser.getUid());
                    intent.putExtra("email",user_email);
                    startActivity(intent);*/
                    FireStoreClass fireStoreClass=new FireStoreClass();
                    fireStoreClass.registerUser(RegistrationActivity.this,user);
                    //Toast.makeText(RegistrationActivity.this, "Successfully registered "+task.getException(), Toast.LENGTH_SHORT).show();
                    //finish();
                }else{
                    //Toast.makeText(RegistrationActivity.this, "Registration Failed!!", Toast.LENGTH_SHORT).show();
                    hideProgressDialog();
                    showErrorSnackBar("Registration Failed!!",true);
                }
            }
        });

    }
    public void userRegistrationSuccess() {

        // Hide the progress dialog
        hideProgressDialog();

        // TODO Step 5: Replace the success message to the Toast instead of Snackbar.
        Toast.makeText(
                this,
        (R.string.register_success),
                Toast.LENGTH_SHORT
        ).show();


        /**
         * Here the new user registered is automatically signed-in so we just sign-out the user from firebase
         * and send him to Intro Screen for Sign-In
         */
        FirebaseAuth.getInstance().signOut();
        // Finish the Register Screen
        finish();
    }

}