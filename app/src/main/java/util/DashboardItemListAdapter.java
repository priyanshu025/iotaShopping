package util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iotashopping.R;

import java.util.ArrayList;

import model.Products;
import ui.activities.ProductDetailsActivity;

public class DashboardItemListAdapter extends RecyclerView.Adapter<DashboardItemListAdapter.MyViewHolder> {
    Context context;
    ArrayList<Products> productsArrayList;

    public DashboardItemListAdapter(Context context, ArrayList<Products> productsArrayList) {
        this.context = context;
        this.productsArrayList = productsArrayList;
    }

    @NonNull
    @Override
    public DashboardItemListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.item_dashboard_layout,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardItemListAdapter.MyViewHolder holder, int position) {
        Products products=productsArrayList.get(position);
        GlideLoader glideLoader=new GlideLoader(context);
        if(holder instanceof DashboardItemListAdapter.MyViewHolder){
            Uri image_uri=Uri.parse(products.getImage());
            glideLoader.load_product_picture(holder.getItem_imageView(),image_uri);
            holder.getTitle_textView().setText(products.getProductTitle());
            holder.getPrice_textView().setText("Price: " + String.valueOf(products.getProductPrice()));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, ProductDetailsActivity.class);
                    intent.putExtra("product_id",products.getProduct_id());
                    intent.putExtra("product_owner_id",products.getUser_id());
                    context.startActivity(intent);
                }
            });
        }
    }



    @Override
    public int getItemCount() {
        return productsArrayList.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView item_imageView;
        TextView title_textView;
        TextView price_textView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            item_imageView=itemView.findViewById(R.id.iv_dashboard_item_image);
            title_textView=itemView.findViewById(R.id.tv_dashboard_item_title);
            price_textView=itemView.findViewById(R.id.tv_dashboard_item_price);
        }

        public ImageView getItem_imageView() {
            return item_imageView;
        }

        public TextView getTitle_textView() {
            return title_textView;
        }

        public TextView getPrice_textView() {
            return price_textView;
        }
    }

}
