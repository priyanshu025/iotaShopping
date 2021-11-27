package ui.activities.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iotashopping.R;
import com.example.iotashopping.databinding.FragmentDashboardBinding;

import java.util.ArrayList;

import FireStore.FireStoreClass;
import model.Products;
import ui.activities.CartListActivity;
import ui.activities.SettingsActivity;
import util.BaseFragment;
import util.DashboardItemListAdapter;

public class DashboardFragment extends BaseFragment {

    //private DashboardViewModel dashboardViewModel;
     private FragmentDashboardBinding binding;
     View root;
     RecyclerView recyclerView;
    TextView textView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.dashboard_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
         super.onOptionsItemSelected(item);
        switch(item.getItemId()){
            case R.id.settings:{
                Intent intent=new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
                return true;
            }
            case R.id.cart:{
                Intent intent=new Intent(getActivity(), CartListActivity.class);
                startActivity(intent);
                return true;
            }
            default: return false;
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        /*dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);*/

    /*binding = FragmentDashboardBinding.inflate(inflater, container, false);
    View root = binding.getRoot();*/

        /*final TextView textView = binding.textDashboard;*/
        /*dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        /*textView.setText("This is Dashboard Fragment");*/
        root=inflater.inflate(R.layout.fragment_dashboard,container,false);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        getDashboardListFromFirestore();
    }

    public void getDashboardListFromFirestore(){
        showProgressDialog("Please wait...");
        FireStoreClass fireStoreClass=new FireStoreClass();
        fireStoreClass.getDashboardList(this);
    }
    public void SuccessProductListFromFirestore(ArrayList<Products> productsArrayList){
        hideProgressDialog();
      /*  FireStoreClass fireStoreClass=new FireStoreClass();
        for(Products i:productsArrayList) {
            fireStoreClass.UpdateProductDetails(requireActivity(), i);
        }*/
        recyclerView=root.findViewById(R.id.rv_dashboard_items);
        textView=root.findViewById(R.id.tv_no_dashboard_items_found);
        if(productsArrayList.size()>0){
            recyclerView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.INVISIBLE);
            recyclerView.setLayoutManager(new GridLayoutManager(requireActivity(),2));
            recyclerView.setHasFixedSize(true);
            DashboardItemListAdapter dashboardItemListAdapter=new DashboardItemListAdapter(requireActivity(),productsArrayList);
            recyclerView.setAdapter(dashboardItemListAdapter);
        }else{
            recyclerView.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.VISIBLE);
        }
    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}