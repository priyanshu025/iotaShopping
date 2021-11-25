package util;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.iotashopping.R;

public class GlideLoader {
    Context context;
    public GlideLoader(Context context){
         this.context=context;
    }
    public void load_user_picture(ImageView imageView, Uri imageuri){
        try {
            Glide.with(context).load(imageuri).centerCrop().placeholder(R.drawable.ic_user_placeholder).into(imageView);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void load_product_picture(ImageView imageView, Uri imageuri){
        try {
            Glide.with(context).load(imageuri).centerCrop().into(imageView);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
