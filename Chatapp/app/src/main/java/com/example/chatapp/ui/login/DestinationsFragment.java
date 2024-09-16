package com.example.chatapp.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.chatapp.R;

public class DestinationsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_step2_destinations, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText destinationsEditText = view.findViewById(R.id.edit_text_destinations);
        Button nextButton = view.findViewById(R.id.btn_next_destinations);

        nextButton.setOnClickListener(v -> {
            // Save selected destinations and navigate to next fragment
            ((Add_Trip) getActivity()).setDestinations(destinationsEditText.getText().toString());
            ((Add_Trip) getActivity()).goToNextPage();
        });
    }
}
