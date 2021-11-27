package model;

import android.os.Parcel;
import android.os.Parcelable;

public class CartItem implements Parcelable {
    private String  user_id ;
    private String  product_id;
    private String title;
    private int price;
    private String image;
    private int cart_quantity;
    private int  stock_quantity;
    private String id;
    public CartItem(String user_id, String product_id, String title, int price, String image, int cart_quantity, int stock_quantity, String id) {
        this.user_id = user_id;
        this.product_id = product_id;
        this.title = title;
        this.price = price;
        this.image = image;
        this.cart_quantity = cart_quantity;
        this.stock_quantity = stock_quantity;
        this.id = id;
    }
    public CartItem(){}

    public String getUser_id() {
        return user_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getTitle() {
        return title;
    }

    public int getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }

    public int getCart_quantity() {
        return cart_quantity;
    }

    public int getStock_quantity() {
        return stock_quantity;
    }

    public String getId() {
        return id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setCart_quantity(int cart_quantity) {
        this.cart_quantity = cart_quantity;
    }

    public void setStock_quantity(int stock_quantity) {
        this.stock_quantity = stock_quantity;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(user_id);
        dest.writeString(product_id);
        dest.writeString(title);
        dest.writeInt(price);
        dest.writeString(image);
        dest.writeInt(cart_quantity);
        dest.writeInt(stock_quantity);
        dest.writeString(id);
    }
    public static final Parcelable.Creator<CartItem> CREATOR
            = new Parcelable.Creator<CartItem>() {
        public CartItem createFromParcel(Parcel in) {
            return new CartItem(in.readString(),in.readString(),in.readString(), in.readInt(),in.readString(),in.readInt(),in.readInt(),in.readString());
        }

        public CartItem[] newArray(int size) {
            return new CartItem[size];
        }
    };
}
