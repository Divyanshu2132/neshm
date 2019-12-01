package com.androhome.neshm.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.androhome.neshm.ProfileActivity;
import com.androhome.neshm.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class profileFragment extends Fragment {

    private RelativeLayout profileInfoRelativeLayout;
    private TextView profileInfoTextView;
    private TextView profileInfoSymbol;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initView(view);
        listeners();
        return view;
    }

    private void listeners() {
        profileInfoRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        profileInfoTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        profileInfoSymbol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initView(View view) {
        profileInfoTextView = view.findViewById(R.id.personalInfoText);
        profileInfoRelativeLayout = view.findViewById(R.id.personalInfoRelativeLayout);
        profileInfoSymbol = view.findViewById(R.id.personalInfoSymbol);
    }

}
