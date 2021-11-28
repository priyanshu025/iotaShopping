package util;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iotashopping.R;

import java.util.ArrayList;
import java.util.HashMap;

import FireStore.FireStoreClass;
import model.CartItem;
import ui.activities.CartListActivity;

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
        FireStoreClass fireStoreClass=new FireStoreClass();
      GlideLoader glideLoader=new GlideLoader(context);
      if(holder instanceof MyViewHolder ) {
          Uri cart_image_uri=Uri.parse(cartItem.getImage());
          glideLoader.load_product_picture(holder.getCartItemImage(),cart_image_uri);
          holder.getCartItemTitle().setText(cartItem.getTitle());
          holder.getCartItemPrice().setText("₹"+String.valueOf(cartItem.getPrice()));
          holder.getCartQuantity().setText("₹"+String.valueOf(cartItem.getCart_quantity()));
          if (cartItem.getCart_quantity() == 0) {
              holder.getAdd_cart_item().setVisibility(View.GONE);
              holder.getRemove_cart_item().setVisibility(View.GONE);
              holder.getCartQuantity().setText("Out of Stock");
              holder.getCartQuantity().setTextColor(ContextCompat.getColor(context,R.color.colorSnackBarError));

          } else {
              holder.getAdd_cart_item().setVisibility(View.VISIBLE);
              holder.getRemove_cart_item().setVisibility(View.VISIBLE);
              holder.getCartQuantity().setTextColor(ContextCompat.getColor(context,R.color.colorSecondaryText));

          }
          holder.getDelete_cart_item().setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  if(context instanceof CartListActivity)
                      ((CartListActivity) context).showProgressDialog("please wait...");

                  fireStoreClass.removeItemFromCart(context,cartItem.getId());
              }
          });
         holder.getRemove_cart_item().setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if (cartItem.getCart_quantity()==1) {
                     fireStoreClass.removeItemFromCart(context, cartItem.getId());
                 } else {

                     int cartQuantity= cartItem.getCart_quantity();
                     HashMap hashMap=new HashMap();
                     hashMap.put("cart_quantity",(cartQuantity-1));

                     if (context instanceof CartListActivity) {
                         ((CartListActivity) context).showProgressDialog("Please wait....");
                     }
                     fireStoreClass.update_cart_list(context,cartItem.getId(),hashMap);
                 }
             }
         });
         holder.getAdd_cart_item().setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 int cartQuantity= cartItem.getCart_quantity();
                  if(cartQuantity<cartItem.getStock_quantity()){
                      HashMap hashMap=new HashMap();
                      hashMap.put("cart_quantity",(cartQuantity+1));
                      if(context instanceof CartListActivity)
                          ((CartListActivity) context).showProgressDialog("Please wait....");
                      fireStoreClass.update_cart_list(context,cartItem.getId(),hashMap);
                  }else{
                      if (context instanceof CartListActivity) {
                          ((CartListActivity) context).showErrorSnackBar(" You can not add more than stock quantity",true);
                      }
                  }

             }
         });
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
        ImageView add_cart_item;
        ImageView remove_cart_item;
        ImageView delete_cart_item;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cartItemImage=itemView.findViewById(R.id.iv_cart_item_image);
            cartItemTitle=itemView.findViewById(R.id.tv_cart_item_title);
            cartItemPrice=itemView.findViewById(R.id.tv_cart_item_price);
            cartQuantity=itemView.findViewById(R.id.tv_cart_quantity);
            add_cart_item=itemView.findViewById(R.id.ib_add_cart_item);
            remove_cart_item=itemView.findViewById(R.id.ib_remove_cart_item);
            delete_cart_item=itemView.findViewById(R.id.ib_delete_cart_item);
        }

        public ImageView getCartItemImage() {
            return cartItemImage;
        }

        public void setCartItemImage(ImageView cartItemImage) {
            this.cartItemImage = cartItemImage;
        }

        public ImageView getDelete_cart_item() {
            return delete_cart_item;
        }

        public void setDelete_cart_item(ImageView delete_cart_item) {
            this.delete_cart_item = delete_cart_item;
        }

        public ImageView getAdd_cart_item() {
            return add_cart_item;
        }

        public void setAdd_cart_item(ImageView add_cart_item) {
            this.add_cart_item = add_cart_item;
        }

        public ImageView getRemove_cart_item() {
            return remove_cart_item;
        }

        public void setRemove_cart_item(ImageView remove_cart_item) {
            this.remove_cart_item = remove_cart_item;
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
