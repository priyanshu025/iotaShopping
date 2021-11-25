package model;

import android.os.Parcel;
import android.os.Parcelable;

public class Products implements Parcelable {
    private  String  user_id;
    private  String user_name;
    private  String product_id;
    private String image;
    private  String productTitle;
    private  String productDescription;
    private  int productQuantity;
    private int ProductPrice;
    public Products( String user_id,  String user_name,  String product_id,  String image, String productTitle,  String productDescription, int productQuantity,int productPrice) {
        this.product_id = product_id;
        this.user_id=user_id;
        this.user_name=user_name;
        this.image = image;
        this.productDescription=productDescription;
        this.productTitle=productTitle;
        this.productQuantity=productQuantity;
        this.ProductPrice=productPrice;

    }
    public static Creator<Products> getCREATOR() {
        return CREATOR;
    }
    public Products(){}

    public  String getProductTitle(){
        return this.productTitle;
    }
    public  String getProductDescription(){
        return this.productDescription;
    }
    public  int getProductPrice(){
        return this.ProductPrice;
    }
    public  int getProductQuantity(){
        return this.productQuantity;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setProduct_id(String id) {
        this.product_id = id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public void setProductPrice(int productPrice) {
        ProductPrice = productPrice;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getImage() {
        return image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(product_id);
        dest.writeString(user_name);
        dest.writeString(user_id);
        dest.writeString(image);
        dest.writeInt(ProductPrice);
        dest.writeString(productDescription);
        dest.writeInt(productQuantity);
        dest.writeString(productTitle);
    }
    public static final Parcelable.Creator<Products> CREATOR
            = new Parcelable.Creator<Products>() {
        public Products createFromParcel(Parcel in) {
            return new Products(in.readString(),in.readString(),in.readString(), in.readString(),in.readString(),in.readString(),in.readInt(),in.readInt());
        }

        public Products[] newArray(int size) {
            return new Products[size];
        }
    };
    public Products(Parcel in) {
        product_id=in.readString();
        user_name = in.readString();
        user_id=in.readString();
        image=in.readString();
        productTitle=in.readString();
        productDescription=in.readString();
        productQuantity=in.readInt();
        ProductPrice=in.readInt();
    }

}
