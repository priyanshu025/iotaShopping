package FireStore;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

import model.CartItem;
import model.Products;
import model.User;
import ui.activities.AddProductsActivity;
import ui.activities.CartListActivity;
import ui.activities.LoginActivity;
import ui.activities.ProductDetailsActivity;
import ui.activities.Products.ProductsFragment;
import ui.activities.RegistrationActivity;
import ui.activities.SettingsActivity;
import ui.activities.UserProfileActivity;
import ui.activities.dashboard.DashboardFragment;

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
    public final void UpdateProductDetails(@NotNull final Activity activity, Products productsInfo) {

        this.mFireStore.collection("Products").document().set(productsInfo, SetOptions.merge()).addOnSuccessListener(
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        if(activity instanceof AddProductsActivity)
                           ((AddProductsActivity)activity).uploadProductSuccess();
                    }
                }
        ).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (activity instanceof AddProductsActivity)
                     ((AddProductsActivity)activity).hideProgressDialog();
                e.printStackTrace();
            }
        });

    }
    public void getProductsList(final Fragment fragment){
        mFireStore.collection("Products")
                .whereEqualTo("user_id",this.getCurrentUserID())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        //Log.i("Product List",queryDocumentSnapshots.getDocuments().toString());
                        ArrayList<Products> productList=new ArrayList<>();
                        for(DocumentSnapshot i:queryDocumentSnapshots){
                            Products products=i.toObject(Products.class);
                            Log.i("product_id",products.getProduct_id()+ "  " +products.getProductTitle());
                             String product_id=i.getId();
                            products.setProduct_id(product_id);
                            Log.i("product_id",products.getProduct_id()+ "  " +products.getProductTitle());
                            productList.add(products);
                        }
                        if(fragment instanceof ProductsFragment)
                            ((ProductsFragment)fragment).SuccessProductListFromFirestore(productList);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(fragment instanceof ProductsFragment){
                    ((ProductsFragment)fragment).hideProgressDialog();
                    e.printStackTrace();
                }
            }
        });

    }

    public void getDashboardList( Fragment fragment){
        mFireStore.collection("Products")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        //Log.i("Product List",queryDocumentSnapshots.getDocuments().toString());
                        ArrayList<Products> productList=new ArrayList<>();
                        for(DocumentSnapshot i:queryDocumentSnapshots){
                            Products products=i.toObject(Products.class);
                            //Log.i("product_id",products.getProduct_id()+ "  " +products.getProductTitle());
                            String product_id=i.getId();
                            products.setProduct_id(product_id);
                            //Log.i("product_id",products.getProduct_id()+ "  " +products.getProductTitle());
                            productList.add(products);
                        }

                        if(fragment instanceof DashboardFragment)
                            ((DashboardFragment)fragment).SuccessProductListFromFirestore(productList);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(fragment instanceof DashboardFragment)
                    ((DashboardFragment)fragment).hideProgressDialog();
                e.printStackTrace();
            }
        });

    }
    public void completeProductDetails(Fragment fragment, HashMap userHashMap,String id) {
        // Collection Name
        mFireStore.collection("Products")
                // Document ID against which the data to be updated. Here the document id is the current logged in user id.
                .document(id)
                // A HashMap of fields which are to be updated.
                .update(userHashMap).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                if (fragment instanceof ProductsFragment) {
                    ((ProductsFragment)fragment).ProductUpdateSuccess();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (fragment instanceof ProductsFragment) {
                    ((ProductsFragment)fragment).hideProgressDialog();
                }
                e.printStackTrace();
            }
        });

    }
    public void deleteProductfromfirestore(ProductsFragment fragment,String product_id ){
        mFireStore.collection("Products").document(product_id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                ((ProductsFragment)fragment).productDeleteSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
    public void getProductDetailsFromFirestore(ProductDetailsActivity activity, String product_id){
        mFireStore.collection("Products").document(product_id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Products products=documentSnapshot.toObject(Products.class);
                activity.getProductDetailsSuccess(products);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
              activity.hideProgressDialog();
              e.printStackTrace();
            }
        });
    }
    public void add_to_cart_Firestore(ProductDetailsActivity activity, CartItem cartItem){
        mFireStore.collection("Cart Item").document().set(cartItem,SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        activity.add_to_cartSuccess();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                activity.hideProgressDialog();
                e.printStackTrace();
            }
        });
    }
    public void update_cart_list(Context activity,String cart_id,HashMap hashMap){
        mFireStore.collection("Cart Item")
                .document(cart_id)
                .update(hashMap)
                .addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        if(activity instanceof CartListActivity){
                            ((CartListActivity) activity).upate_cart_list_success();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(activity instanceof CartListActivity)
                    ((CartListActivity) activity).hideProgressDialog();
                e.printStackTrace();
            }
        });
    }
    public void checkItemExistInCart(ProductDetailsActivity activity,String product_id){
        mFireStore.collection("Cart Item")
                .whereEqualTo("user_id",getCurrentUserID())
                .whereEqualTo("product_id",product_id)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots.size()>0) {
                            activity.itemExistInCart();
                        }else{
                            activity.hideProgressDialog();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                activity.hideProgressDialog();
                e.printStackTrace();
            }
        });
    }
    public void getCartList(Activity activity){
        mFireStore.collection("Cart Item")
                .whereEqualTo("user_id",getCurrentUserID())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        ArrayList<CartItem> arrayList=new ArrayList<>();
/*
                        Toast.makeText(activity, queryDocumentSnapshots.getDocuments().toString(), Toast.LENGTH_SHORT).show();
*/                       Log.i("Document",queryDocumentSnapshots.getDocuments().toString());
                        for(DocumentSnapshot i:queryDocumentSnapshots.getDocuments()){
                            CartItem cartItem=i.toObject(CartItem.class);
                            cartItem.setId(i.getId());
                            Log.i("cart item id",cartItem.getId());
                            arrayList.add(cartItem);
                        }
                        if(activity instanceof CartListActivity){
                            ((CartListActivity) activity).cartListSuccess(arrayList);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(activity instanceof CartListActivity)
                    ((CartListActivity) activity).hideProgressDialog();
                e.printStackTrace();
            }
        });
    }
    public void getAllProductList(CartListActivity activity){
        mFireStore.collection("Products")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                      ArrayList<Products> arrayList=new ArrayList<>();
                      for(DocumentSnapshot i:queryDocumentSnapshots.getDocuments()){
                         Products products=i.toObject(Products.class);
                         products.setProduct_id(i.getId());
                         arrayList.add(products);
                      }
                      activity.getAllProductsSuccess(arrayList);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                activity.hideProgressDialog();
                e.printStackTrace();
            }
        });
    }
    public void removeItemFromCart(Context activity, String cart_id) {

        // Cart items collection name
        mFireStore.collection("Cart Item")
                .document(cart_id) // cart id
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        if(activity instanceof CartListActivity){
                                  ((CartListActivity) activity).itemRemovedSuccess();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(activity instanceof CartListActivity)
                     ((CartListActivity) activity).hideProgressDialog();
                e.printStackTrace();
            }
        });
                /*.addOnSuccessListener {

            // TODO Step 6: Notify the success result of the removed cart item from the list to the base class.
            // START
            // Notify the success result of the removed cart item from the list to the base class.
            when (context) {
                is CartListActivity -> {
                    context.itemRemovedSuccess()
                }
            }
            // END
        }*/
            /*.addOnFailureListener { e ->

                // Hide the progress dialog if there is any error.
                when (context) {
            is CartListActivity -> {
                context.hideProgressDialog()
            }
        }
            Log.e(
                    context.javaClass.simpleName,
                    "Error while removing the item from the cart list.",
                    e
            )*/
        }

}
