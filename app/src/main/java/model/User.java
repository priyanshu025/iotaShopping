package model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    private String id;
    private String firstName;
    private String email;
    private String image;
    private long mobile;
    private String gender;
    private int profileCompleted;

    public User( String id,  String firstName,  String email,  String image, long mobile,  String gender, int profileCompleted) {
        this.id = id;
        this.firstName = firstName;
        this.email = email;
        this.image = image;
        this.mobile = mobile;
        this.gender = gender;
        this.profileCompleted = profileCompleted;

    }
    public User(){

    }

    public String getId() {
        return id;
    }
    public final String getFirstName() {
        return this.firstName;
    }

    public final String getEmail() {
        return this.email;
    }

    public final String getImage() {
        return this.image;
    }

    public final long getMobile() {
        return this.mobile;
    }

    public final String getGender() {
        return this.gender;
    }

    public final int getProfileCompleted() {
        return this.profileCompleted;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
         dest.writeString(firstName);
         dest.writeString(email);
         dest.writeString(image);
         dest.writeLong(mobile);
         dest.writeString(gender);
         dest.writeInt(profileCompleted);

    }
    public static final Parcelable.Creator<User> CREATOR
            = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in.readString(),in.readString(),in.readString(), in.readString(),in.readLong(),in.readString(),in.readInt());
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

   /* private User(Parcel in) {
        id=in.readString();
        firstName = in.readString();
         email=in.readString();

    }*/
}
