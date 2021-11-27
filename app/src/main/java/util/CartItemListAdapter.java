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

import model.CartItem;

public class CartItemListAdapter extends RecyclerView.Adapter<CartItemListAdapter.MyViewHolder> {
    private Context context;
    ArrayList<CartItem> list;
    public CartItemListAdapter(Context context,ArrayList<CartItem> arrayList) {
        this.context=context;
        this.list=arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.item_cart_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
      CartItem cartItem=list.get(position);
      GlideLoader glideLoader=new GlideLoader(context);
      if(holder instanceof MyViewHolder ) {
          Uri cart_image_uri=Uri.parse(cartItem.getImage());
          glideLoader.load_product_picture(holder.getCartItemImage(),cart_image_uri);
          holder.getCartItemTitle().setText(cartItem.getTitle());
          holder.getCartItemPrice().setText("₹"+String.valueOf(cartItem.getPrice()));
          holder.getCartQuantity().setText("₹"+String.valueOf(cartItem.getCart_quantity()));
      }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class  MyViewHolder extends RecyclerView.ViewHolder{
        ImageView cartItemImage;
        TextView cartItemTitle;
        TextView cartItemPrice;
        TextView cartQuantity;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cartItemImage=itemView.findViewById(R.id.iv_cart_item_image);
            cartItemTitle=itemView.findViewById(R.id.tv_cart_item_title);
            cartItemPrice=itemView.findViewById(R.id.tv_cart_item_price);
            cartQuantity=itemView.findViewById(R.id.tv_cart_quantity);
        }

        public ImageView getCartItemImage() {
            return cartItemImage;
        }

        public void setCartItemImage(ImageView cartItemImage) {
            this.cartItemImage = cartItemImage;
        }

        public TextView getCartItemTitle() {
            return cartItemTitle;
        }

        public void setCartItemTitle(TextView cartItemTitle) {
            this.cartItemTitle = cartItemTitle;
        }

        public TextView getCartItemPrice() {
            return cartItemPrice;
        }

        public void setCartItemPrice(TextView cartItemPrice) {
            this.cartItemPrice = cartItemPrice;
        }

        public TextView getCartQuantity() {
            return cartQuantity;
        }

        public void setCartQuantity(TextView cartQuantity) {
            this.cartQuantity = cartQuantity;
        }
    }
}
