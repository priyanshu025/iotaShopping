package ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.core.content.ContextCompat;

import com.example.iotashopping.R;

import FireStore.FireStoreClass;
import model.CartItem;
import model.Products;
import util.BaseActivity;
import util.GlideLoader;

public class ProductDetailsActivity extends BaseActivity implements View.OnClickListener{

    String product_id="";
    String product_owner_id="";
    ImageView product_image;
    TextView product_title;
    TextView product_price;
    TextView product_description;
    TextView product_quantity;
    Button add_to_cart_button;
    Button go_to_cart_button;
    Products mProducts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        Intent intent=getIntent();
        if(intent.hasExtra("product_id")){
            product_id=intent.getStringExtra("product_id");
            //Toast.makeText(this, product_id, Toast.LENGTH_SHORT).show();
        }
        if(intent.hasExtra("product_owner_id")){
            product_owner_id=intent.getStringExtra("product_owner_id");
        }
        setupActionbar();
        product_image=findViewById(R.id.iv_product_detail_image);
        product_title=findViewById(R.id.tv_product_details_title);
        product_price=findViewById(R.id.tv_product_details_price);
        product_description=findViewById(R.id.tv_product_details_description);
        product_quantity=findViewById(R.id.tv_product_details_stock_quantity);
        add_to_cart_button=findViewById(R.id.btn_add_to_cart);
        go_to_cart_button=findViewById(R.id.btn_go_to_cart);
        FireStoreClass fireStoreClass=new FireStoreClass();
        if(product_owner_id.equals(fireStoreClass.getCurrentUserID())){
            add_to_cart_button.setVisibility(View.INVISIBLE);
            go_to_cart_button.setVisibility(View.INVISIBLE);
        }else{
            add_to_cart_button.setVisibility(View.VISIBLE);
        }
        getProductDetails();
        add_to_cart_button.setOnClickListener(this::onClick);
        go_to_cart_button.setOnClickListener(this::onClick);

    }
    public void getProductDetails(){
        showProgressDialog("Please wait....");
        FireStoreClass fireStoreClass=new FireStoreClass();
        fireStoreClass.getProductDetailsFromFirestore(this,product_id);

    }
    public void getProductDetailsSuccess(Products products){
        mProducts=products;
       // hideProgressDialog();
        FireStoreClass fireStoreClass=new FireStoreClass();
        GlideLoader glideLoader=new GlideLoader(this);
        Uri product_image_uri=Uri.parse(products.getImage());
        glideLoader.load_product_picture(product_image,product_image_uri);
        product_title.setText(products.getProductTitle());
        product_price.setText(String.valueOf(products.getProductPrice()));
        product_description.setText(products.getProductDescription());
        product_quantity.setText(String.valueOf(products.getProductQuantity()));
        if(products.getProductQuantity()==0){
            hideProgressDialog();

            // Hide the AddToCart button if the item is already in the cart.
            add_to_cart_button.setVisibility(View.GONE);
            product_quantity.setText("Out of Stock");
            product_quantity.setTextColor(ContextCompat.getColor(this,R.color.colorSnackBarError));
           /* btn_add_to_cart.visibility = View.GONE

            tv_product_details_stock_quantity.text =
                    resources.getString(R.string.lbl_out_of_stock)

            tv_product_details_stock_quantity.setTextColor(
                    ContextCompat.getColor(
                            this@ProductDetailsActivity,
            R.color.colorSnackBarError
                )
            )*/
        }else {
            if (fireStoreClass.getCurrentUserID().equals(mProducts.getUser_id())) {
                hideProgressDialog();
            } else {
                fireStoreClass.checkItemExistInCart(this, product_id);
            }
        }
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
    public void add_to_Cart(){
        FireStoreClass fireStoreClass=new FireStoreClass();
        CartItem cartItem=new CartItem(
                fireStoreClass.getCurrentUserID(),
                mProducts.getProduct_id(),
                mProducts.getProductTitle(),
                mProducts.getProductPrice(),
                mProducts.getImage(),
                1,
                mProducts.getProductQuantity(),
                ""
        );
        showProgressDialog("please wait...");
        fireStoreClass.add_to_cart_Firestore(this,cartItem);

    }
    public void add_to_cartSuccess(){
        hideProgressDialog();
        Toast.makeText(this, "Product Added To Cart Successfully", Toast.LENGTH_SHORT).show();
        add_to_cart_button.setVisibility(View.INVISIBLE);
        go_to_cart_button.setVisibility(View.VISIBLE);

    }
    public void itemExistInCart(){
        hideProgressDialog();
        add_to_cart_button.setVisibility(View.INVISIBLE);
        go_to_cart_button.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
       if(v.getId()==R.id.btn_add_to_cart){
           add_to_Cart();
       }
       if(v.getId()==R.id.btn_go_to_cart){
           Intent intent=new Intent(this,CartListActivity.class);
           startActivity(intent);
           finish();
       }
    }
}