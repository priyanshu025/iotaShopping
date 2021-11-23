package FireStore;

import android.app.Activity;
import android.content.ContentResolver;
import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import ui.activities.AddProductsActivity;
import ui.activities.LoginActivity;
import ui.activities.RegistrationActivity;
import ui.activities.SettingsActivity;
import ui.activities.UserProfileActivity;
import model.User;

public class FireStoreClass {
    private FirebaseFirestore mFireStore=FirebaseFirestore.getInstance();
    private FirebaseStorage storage= FirebaseStorage.getInstance();
    public String getFileExtension(Uri uri,Activity activity){
        ContentResolver contentResolver=activity.getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    public void uploadImage(Uri uri,Activity activity,String image_type){
        StorageReference storageReference=storage.getReference().child(
                image_type + System.currentTimeMillis() + "." + getFileExtension(uri,activity)
        );
        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                      taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                          @Override
                          public void onSuccess(Uri uri) {
                              String url=uri.toString();
                              Log.i("url",url);
                              if(activity  instanceof UserProfileActivity){
                                  ((UserProfileActivity) activity).uploadImageSuccess(url);
                              }
                              if (activity instanceof AddProductsActivity){
                                  ((AddProductsActivity) activity).uploadImageSuccess(url);
                              }
                          }
                      }).addOnFailureListener(new OnFailureListener() {
                          @Override
                          public void onFailure(@NonNull Exception e) {
                              if(activity instanceof UserProfileActivity){
                                  ((UserProfileActivity) activity).hideProgressDialog();
                              }
                              if(activity instanceof AddProductsActivity){
                                  ((AddProductsActivity) activity).hideProgressDialog();
                              }
                              Log.e(activity.getClass().getSimpleName(),e.getMessage(),e);
                              e.printStackTrace();
                          }
                      });
            }
        });
    }
    public final void registerUser(@NotNull final RegistrationActivity activity, @NotNull User userInfo) {

        this.mFireStore.collection("users").document(userInfo.getId()).set(userInfo, SetOptions.merge()).addOnSuccessListener((OnSuccessListener)(new OnSuccessListener() {
            // $FF: synthetic method
            // $FF: bridge method
            public void onSuccess(Object var1) {
                this.onSuccess((Void)var1);
            }
            public final void onSuccess(Void it) {
                activity.userRegistrationSuccess();
            }
        })).addOnFailureListener((OnFailureListener)(new OnFailureListener() {
            public final void onFailure(@NotNull Exception e) {

                activity.hideProgressDialog();
                Log.e(activity.getClass().getSimpleName(), "Error while registering the user.", (Throwable)e);
            }
        }));
    }
    public final String getCurrentUserID() {
        FirebaseUser currentUser= FirebaseAuth.getInstance().getCurrentUser();
        String currentUserID = "";
        if (currentUser != null) {
            currentUserID  = currentUser.getUid();
        }
        return currentUserID;
    }
    public final void getUserDetails(@NotNull final Activity activity) {

        this.mFireStore.collection("users").document(this.getCurrentUserID()).get().addOnSuccessListener((OnSuccessListener)(new OnSuccessListener() {
            // $FF: synthetic method
            // $FF: bridge method
            public void onSuccess(Object var1) {
                this.onSuccess((DocumentSnapshot)var1);
            }
            public final void onSuccess(@NonNull DocumentSnapshot document) {
                Log.i(activity.getClass().getSimpleName(), document.toString());
                Object var10000 = document.toObject(User.class);
                User user = (User)var10000;
                Activity var3 = activity;
                if (var3 instanceof LoginActivity) {
                    ((LoginActivity)activity).userLoggedInSuccess(user);
                }
                if(var3 instanceof SettingsActivity){
                    ((SettingsActivity)activity).UserDetailSuccess(user);
                }
            }
        })).addOnFailureListener((OnFailureListener)(new OnFailureListener() {
            public final void onFailure(@NotNull Exception e) {

                Activity var2 = activity;
                if (var2 instanceof LoginActivity) {
                    ((LoginActivity)activity).hideProgressDialog();
                }
                if(var2 instanceof SettingsActivity){
                    ((SettingsActivity) activity).hideProgressDialog();
                }

                Log.e(activity.getClass().getSimpleName(), "Error while getting user details.", (Throwable)e);
            }
        }));
    }
    public void updateUserProfileData(Activity activity, HashMap userHashMap) {
        // Collection Name
        mFireStore.collection("users")
                // Document ID against which the data to be updated. Here the document id is the current logged in user id.
                .document(getCurrentUserID())
                // A HashMap of fields which are to be updated.
                .update(userHashMap).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                if (activity instanceof UserProfileActivity) {
                    ((UserProfileActivity) activity).userProfileUpdateSuccess();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (activity instanceof UserProfileActivity) {
                    ((UserProfileActivity) activity).hideProgressDialog();
                }
            }
        });

    }
}
