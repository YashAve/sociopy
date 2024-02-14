package enlightment.yash.sociopy.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import enlightment.yash.sociopy.R;
import enlightment.yash.sociopy.activities.PostActivity;

public class HomeFragment extends Fragment {

    private FloatingActionButton postFAB;

    public HomeFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initializeViews(view);

        postFAB.setOnClickListener(v -> {
            requireActivity().startActivity(new Intent(requireActivity(), PostActivity.class));
            requireActivity().overridePendingTransition(R.anim.sllide_in_up, R.anim.sllide_out_up);
        });

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onInflate(@NonNull Context context, @NonNull AttributeSet attrs, @Nullable Bundle savedInstanceState) {
        super.onInflate(context, attrs, savedInstanceState);
    }

    private void initializeViews(View view) {
        postFAB = view.findViewById(R.id.addPostFAB);
    }
}
