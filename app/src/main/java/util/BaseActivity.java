package util;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.iotashopping.R;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

public class BaseActivity extends AppCompatActivity {
    private Dialog progressDialog;
    private boolean doublePressed=false;
    public final void showErrorSnackBar(@NotNull String message, boolean errorMessage) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        if (errorMessage) {
            snackBarView.setBackgroundColor(ContextCompat.getColor((Context)this, R.color.colorSnackBarError));
        } else {
            snackBarView.setBackgroundColor(ContextCompat.getColor((Context)this,R.color.colorSnackBarSuccess));
        }

        snackbar.show();
    }
    public void showProgressDialog(String mess){
         progressDialog=new Dialog((Context)this);
         progressDialog.setContentView(R.layout.progess_dialogue);
         TextView pd=progressDialog.findViewById(R.id.tv_progress_text);
         pd.setText(mess);
         progressDialog.setCancelable(false);
         progressDialog.setCanceledOnTouchOutside(false);
         progressDialog.show();
    }
    public void hideProgressDialog(){
        progressDialog.dismiss();
    }

    public  void doubleBackToExit(){
        if(doublePressed){
            super.onBackPressed();
            return;
        }
        this.doublePressed=true;
        Toast.makeText(this, "Please Click Back Again To Exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doublePressed=false;
            }
        },2000);
    }
}
