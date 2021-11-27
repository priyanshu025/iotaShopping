package ui.activities.Products;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iotashopping.R;
import com.example.iotashopping.databinding.FragmentProductsBinding;

import java.util.ArrayList;
import java.util.HashMap;

import FireStore.FireStoreClass;
import model.Products;
import ui.activities.AddProductsActivity;
import util.BaseFragment;
import util.MyProductListAdapter;

public class ProductsFragment extends BaseFragment {

    //private HomeViewModel homeViewModel;
    View root;
    RecyclerView recyclerView;
    TextView textView;
    private FragmentProductsBinding binding;
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.addproducts_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch(item.getItemId()){
            case R.id.action_add_product:{
                Intent intent=new Intent(getActivity(), AddProductsActivity.class);
                startActivity(intent);
                return true;
            }
            default: return false;
        }
    }
    public void SuccessProductListFromFirestore(ArrayList<Products> productsArrayList){
        hideProgressDialog();
       FireStoreClass fireStoreClass=new FireStoreClass();
        HashMap hashMap=new HashMap();
        for(Products i:productsArrayList) {
            hashMap.put("user_id",i.getUser_id());
            hashMap.put("user_name","priyanshu");
            hashMap.put("product_id",i.getProduct_id());
            hashMap.put("image",i.getImage());
            hashMap.put("productTitle",i.getProductTitle());
            hashMap.put("productDescription",i.getProductDescription());
            hashMap.put("productQuantity",i.getProductQuantity());
            hashMap.put("ProductPrice",i.getProductPrice());
            String product_id= (String) hashMap.get("product_id");
            fireStoreClass.completeProductDetails(this,hashMap,product_id);
        }

        //Toast.makeText(requireActivity(), product_id, Toast.LENGTH_SHORT).show();
        Activity activity=getActivity();
        if(isAdded() && activity!=null) {
            recyclerView = root.findViewById(R.id.my_product_items);
            textView = root.findViewById(R.id.tv_no_product_found);
            if (productsArrayList.size() > 0) {
                recyclerView.setVisibility(View.VISIBLE);
                textView.setVisibility(View.INVISIBLE);
                recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
                recyclerView.setHasFixedSize(true);
                MyProductListAdapter myProductListAdapter = new MyProductListAdapter(requireActivity(), productsArrayList,this);
                recyclerView.setAdapter(myProductListAdapter);
            } else {
                recyclerView.setVisibility(View.INVISIBLE);
                textView.setVisibility(View.VISIBLE);
            }
        }
    }
    public void getProductListFromFirestore(){
        showProgressDialog("Please wait...");
        FireStoreClass fireStoreClass=new FireStoreClass();
        fireStoreClass.getProductsList(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getProductListFromFirestore();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        /*homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);*/

    //binding = FragmentProductsBinding.inflate(inflater, container, false);
     //root = binding.getRoot();
        root=inflater.inflate(R.layout.fragment_products,container,false);


        /*homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        return root;
    }
    public void deleteProduct(String product_id){
        Toast.makeText(requireActivity(), "You are now ready to delete product", Toast.LENGTH_SHORT).show();
        showAlertDialog(product_id);
    }
    public void showAlertDialog(String product_id){
            new AlertDialog.Builder(requireActivity())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Delete")
                .setMessage("Are You Sure You Want To Delete?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showProgressDialog("Please wait....");
                        FireStoreClass fireStoreClass=new FireStoreClass();
                        fireStoreClass.deleteProductfromfirestore(ProductsFragment.this,product_id);
                        dialog.dismiss();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setCancelable(false).show();

    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void ProductUpdateSuccess() {
        this.hideProgressDialog();
        Toast.makeText(requireActivity(), "Products updated", Toast.LENGTH_SHORT).show();
    }

    public void productDeleteSuccess() {
        hideProgressDialog();
        Toast.makeText(
                requireActivity(),
                "Your product deleted successfully",
                Toast.LENGTH_SHORT
        ).show();
        getProductListFromFirestore();
    }
}