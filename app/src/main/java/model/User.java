package model;

import org.jetbrains.annotations.NotNull;

public class User {

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

}
