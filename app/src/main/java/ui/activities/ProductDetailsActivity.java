package ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;

import com.example.iotashopping.R;

import FireStore.FireStoreClass;
import model.Products;
import util.BaseActivity;
import util.GlideLoader;

public class ProductDetailsActivity extends BaseActivity {

    String product_id="";
    ImageView product_image;
    TextView product_title;
    TextView product_price;
    TextView product_description;
    TextView product_quantity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        Intent intent=getIntent();
        if(intent.hasExtra("product_id")){
            product_id=intent.getStringExtra("product_id");
            //Toast.makeText(this, product_id, Toast.LENGTH_SHORT).show();
        }
        setupActionbar();
        product_image=findViewById(R.id.iv_product_detail_image);
        product_title=findViewById(R.id.tv_product_details_title);
        product_price=findViewById(R.id.tv_product_details_price);
        product_description=findViewById(R.id.tv_product_details_description);
        product_quantity=findViewById(R.id.tv_product_details_stock_quantity);
        getProductDetails();

    }
    public void getProductDetails(){
        showProgressDialog("Please wait....");
        FireStoreClass fireStoreClass=new FireStoreClass();
        fireStoreClass.getProductDetailsFromFirestore(this,product_id);

    }
    public void getProductDetailsSuccess(Products products){
           hideProgressDialog();
        GlideLoader glideLoader=new GlideLoader(this);
        Uri product_image_uri=Uri.parse(products.getImage());
        glideLoader.load_product_picture(product_image,product_image_uri);
        product_title.setText(products.getProductTitle());
        product_price.setText(String.valueOf(products.getProductPrice()));
        product_description.setText(products.getProductDescription());
        product_quantity.setText(String.valueOf(products.getProductQuantity()));
    }
    private void setupActionbar() {
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar_product_details_activity);
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