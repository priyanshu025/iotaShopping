package ui.activities.Orders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.example.iotashopping.databinding.FragmentOrdersBinding;

public class OrdersFragment extends Fragment {

   // private NotificationsViewModel notificationsViewModel;
private FragmentOrdersBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
       /* notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);
*/
    binding = FragmentOrdersBinding.inflate(inflater, container, false);
    View root = binding.getRoot();

        final TextView textView = binding.textNotifications;
       /* notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        textView.setText("This is Orders Fragment");
        return root;
    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}