package ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iotashopping.R;

import java.util.ArrayList;

import FireStore.FireStoreClass;
import model.CartItem;
import model.Products;
import util.BaseActivity;
import util.CartItemListAdapter;

public class CartListActivity extends BaseActivity {

    RecyclerView recyclerView;
    TextView noItem;
    TextView sub_total;
    TextView shipping_charge;
    TextView Total_amount;
    LinearLayout ll;
    ArrayList<Products> mArrayList=new ArrayList<>();
    ArrayList<CartItem> mCartList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list);
        setupActionbar();
        recyclerView=findViewById(R.id.rv_cart_items_list);
        noItem=findViewById(R.id.tv_no_cart_item_found);
        sub_total=findViewById(R.id.tv_sub_total);
        shipping_charge=findViewById(R.id.tv_shipping_charge);
        Total_amount=findViewById(R.id.tv_total_amount);
        ll=findViewById(R.id.ll_checkout);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //getCartItem();
        getProductList();
    }

    public void getCartItem(){
       // showProgressDialog("Please wait....");
        FireStoreClass fireStoreClass=new FireStoreClass();
        fireStoreClass.getCartList(this);
    }
    public void cartListSuccess(ArrayList<CartItem> Item){
        FireStoreClass fireStoreClass=new FireStoreClass();
        hideProgressDialog();
       /* HashMap hashMap=new HashMap();
        for(CartItem i: Item){
            hashMap.put("user_id",i.getUser_id());
            hashMap.put("product_id",i.getProduct_id());
            hashMap.put("title",i.getTitle());
            hashMap.put("price",i.getPrice());
            hashMap.put("image",i.getImage());
            hashMap.put("cart_quantity",i.getCart_quantity());
            hashMap.put("stock_quantity",i.getStock_quantity());
            hashMap.put("id",i.getId());
            fireStoreClass.update_cart_list(this,i.getId(),hashMap);
        }*/
        for (Products i:mArrayList) {
            for (CartItem j:Item) {
                if (i.getProduct_id()== j.getProduct_id()) {

                    j.setStock_quantity(i.getProductQuantity());

                    if (i.getProductQuantity() == 0){
                        //cart.cart_quantity = product.stock_quantity
                                j.setCart_quantity(i.getProductQuantity());
                    }
                }
            }
        }
        mCartList=Item;
        if(mCartList.size()>0){
            recyclerView.setVisibility(View.VISIBLE);
            ll.setVisibility(View.VISIBLE);
            noItem.setVisibility(View.INVISIBLE);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setHasFixedSize(true);
            CartItemListAdapter cartItemListAdapter=new CartItemListAdapter(this,Item);
            recyclerView.setAdapter(cartItemListAdapter);
            Double subTotal=0.0;
            for(CartItem i:mCartList){
                int available_quantity=i.getStock_quantity();
                if(available_quantity>0) {
                    Double price = Double.valueOf(i.getPrice());
                    Integer quantity = i.getCart_quantity();
                    subTotal += (price * quantity);
                }
            }
            sub_total.setText("₹"+String.valueOf(subTotal));
            shipping_charge.setText("₹10");
            if (subTotal > 0) {
                ll.setVisibility(View.VISIBLE);

                Double total = subTotal + 10;
                Total_amount.setText("₹" + String.valueOf(total));
            } else {
                ll.setVisibility(View.INVISIBLE);
            }
        }else{
            recyclerView.setVisibility(View.INVISIBLE);
            ll.setVisibility(View.INVISIBLE);
            noItem.setVisibility(View.VISIBLE);
        }
    }
    private void setupActionbar() {
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar_cart_list_activity);
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
    public void upate_cart_list_success(){
        hideProgressDialog();
        Toast.makeText(this, "Cart List Item Updated", Toast.LENGTH_SHORT).show();
        getCartItem();
    }
    public void getAllProductsSuccess(ArrayList<Products> arrayList){
        //hideProgressDialog();
        mArrayList=arrayList;
        getCartItem();
    }
    private void  getProductList() {

        // Show the progress dialog.
        showProgressDialog("Please wait...");
        FireStoreClass fireStoreClass=new FireStoreClass();
        fireStoreClass.getAllProductList(this);
    }
    public void itemRemovedSuccess() {

        hideProgressDialog();

        Toast.makeText(
                this,
       "Item removed Successfully",
                Toast.LENGTH_SHORT
        ).show();
        getCartItem();
    }
}