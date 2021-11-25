package util;

import android.content.Context;
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

public class MyProductListAdapter extends RecyclerView.Adapter<MyProductListAdapter.MyViewHolder> {
    Context context;
    ArrayList<Products> productsArrayList;

    public MyProductListAdapter(Context context, ArrayList<Products> productsArrayList) {
        this.context = context;
        this.productsArrayList = productsArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.item_list_layout,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Products products=productsArrayList.get(position);
        GlideLoader glideLoader=new GlideLoader(context);
        if(holder instanceof MyViewHolder){
            Uri image_uri=Uri.parse(products.getImage());
            glideLoader.load_product_picture(holder.getItem_imageView(),image_uri);
            holder.getTitle_textView().setText(products.getProductTitle());
            holder.getPrice_textView().setText("Price: " + String.valueOf(products.getProductPrice()));
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
            item_imageView=itemView.findViewById(R.id.iv_item_image);
            title_textView=itemView.findViewById(R.id.tv_item_name);
            price_textView=itemView.findViewById(R.id.tv_item_price);
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
