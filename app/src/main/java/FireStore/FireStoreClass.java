package FireStore;

import android.app.Activity;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import org.jetbrains.annotations.NotNull;

import activities.LoginActivity;
import activities.RegistrationActivity;
import model.User;

public class FireStoreClass {
    private FirebaseFirestore mFireStore=FirebaseFirestore.getInstance();
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
            public final void onSuccess(DocumentSnapshot document) {
                Log.i(activity.getClass().getSimpleName(), document.toString());
                Object var10000 = document.toObject(User.class);
                User user = (User)var10000;
                Activity var3 = activity;
                if (var3 instanceof LoginActivity) {
                    ((LoginActivity)activity).userLoggedInSuccess(user);
                }
            }
        })).addOnFailureListener((OnFailureListener)(new OnFailureListener() {
            public final void onFailure(@NotNull Exception e) {

                Activity var2 = activity;
                if (var2 instanceof LoginActivity) {
                    ((LoginActivity)activity).hideProgressDialog();
                }

                Log.e(activity.getClass().getSimpleName(), "Error while getting user details.", (Throwable)e);
            }
        }));
    }

}
