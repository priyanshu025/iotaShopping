package util;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.iotashopping.R;

public class BaseFragment extends Fragment {
     private Dialog progressDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_base, container, false);
    }
    public void showProgressDialog(String mess){
        progressDialog=new Dialog(requireActivity());
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
}