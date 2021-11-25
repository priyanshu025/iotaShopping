package ui.activities;

import static com.example.iotashopping.R.drawable.ic_vector_edit;
import static ui.activities.UserProfileActivity.READ_STORAGE_PERMISSION_CODE;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.iotashopping.R;

import FireStore.FireStoreClass;
import model.Products;
import util.BaseActivity;
import util.GlideLoader;
import util.ImageChooser;

public class AddProductsActivity extends BaseActivity implements View.OnClickListener {
    ImageChooser imageChooser=new ImageChooser();
    GlideLoader glideLoader=new GlideLoader(this);
    Uri product_image_uri;
    String product_image_url;
    ImageView product_image;
    ImageView add_image;
    EditText et_product_title;
    EditText et_product_price;
    EditText et_product_quantity;
    EditText et_product_description;
    Button add_product_btn;
    FireStoreClass fireStoreClass;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_products);
        setupActionbar();
        product_image=findViewById(R.id.product_image);
        add_image=findViewById(R.id.add_product_image);
        et_product_title=findViewById(R.id.product_title);
        et_product_price=findViewById(R.id.product_price);
        et_product_description=findViewById(R.id.product_description);
        et_product_quantity=findViewById(R.id.product_quantity);
        add_product_btn=findViewById(R.id.add_product_btn);
        sharedPreferences=this.getSharedPreferences("Iota shopping", Context.MODE_PRIVATE);
        fireStoreClass=new FireStoreClass();
        add_image.setOnClickListener(this::onClick);
        add_product_btn.setOnClickListener(this::onClick);

    }
    private void setupActionbar() {
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar_add_products_activity);
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

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.add_product_image){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //showErrorSnackBar("You already have the storage permission.", false);

                imageChooser.setImage_picker(this);

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_STORAGE_PERMISSION_CODE);
            }

        }
       if(v.getId()==R.id.add_product_btn){
           if(validate_product_details()) {
               upload_product_image();
           }

       }
    }
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
        if(requestCode==imageChooser.IMAGE_REQUEST_CODE){
            if(resultCode==RESULT_OK){
                try {
                    add_image.setImageDrawable(ContextCompat.getDrawable(this, ic_vector_edit));
                    product_image_uri = data.getData();
                    glideLoader.load_user_picture(product_image, product_image_uri);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
    public boolean validate_product_details(){
        if(product_image_uri==null){
            showErrorSnackBar(getResources().getString(R.string.err_select_product_image),true);
            return false;
        }else if(TextUtils.isEmpty(et_product_title.getText().toString())){
            showErrorSnackBar(getResources().getString(R.string.err_enter_product_title),true);
            return false;
        }else if(TextUtils.isEmpty(et_product_price.getText().toString())){
            showErrorSnackBar(getResources().getString(R.string.err_enter_product_price),true);
            return false;
        }else if(TextUtils.isEmpty(et_product_description.getText().toString())){
            showErrorSnackBar(getResources().getString(R.string.err_enter_product_description),true);
            return false;
        }else if(TextUtils.isEmpty(et_product_quantity.getText().toString())){
            showErrorSnackBar(getResources().getString(R.string.err_enter_product_quantity),true);
            return false;
        }else{
            return true;
        }
    }
    public void upload_product_image(){
        showProgressDialog("Please wait...");
        showErrorSnackBar("Your product details are valid", false);
        fireStoreClass.uploadImage(product_image_uri,this,"product_image");
    }
    public void uploadImageSuccess(String url){
        //hideProgressDialog();
        product_image_url=url;
        //Toast.makeText(UserProfileActivity.this, url, Toast.LENGTH_SHORT).show();
        //updateUserProfileDetails();
        uploadProductDetails();

    }
    public void uploadProductDetails(){

       String user_name=sharedPreferences.getString("user_name","");
        Log.i("user_name",user_name);
        Products products=new Products(fireStoreClass.getCurrentUserID(),
                user_name,
                " ",
                product_image_url,
                et_product_title.getText().toString(),
                et_product_description.getText().toString(),
                Integer.parseInt(et_product_quantity.getText().toString()),
                Integer.parseInt(et_product_price.getText().toString())
                );

        fireStoreClass.UpdateProductDetails(AddProductsActivity.this,products);

    }
    public void uploadProductSuccess(){
        hideProgressDialog();
        Toast.makeText(AddProductsActivity.this, "Your Product Upload Successful", Toast.LENGTH_SHORT).show();
        finish();
    }
}