package com.example.chatapp.ui.login;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.chatapp.R;

public class ActivitiesFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_step3_activities, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText activitiesEditText = view.findViewById(R.id.edit_text_activities);
        Button finishButton = view.findViewById(R.id.btn_finish);

        finishButton.setOnClickListener(v -> {
            // Save selected activities and finish
            ((Add_Trip) getActivity()).setActivities(activitiesEditText.getText().toString());
            ((Add_Trip) getActivity()).finishItineraryCreation();
        });
    }
}
