package com.example.chatapp.ui.login;

import static android.app.PendingIntent.getActivity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.chatapp.R;
import com.example.chatapp.ui.login.Add_Trip;

import java.util.Calendar;

public class DatesFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_step1_dates, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DatePicker startDatePicker = view.findViewById(R.id.date_picker_start);
        DatePicker endDatePicker = view.findViewById(R.id.date_picker_end);
        Button nextButton = view.findViewById(R.id.btn_next_dates);

        nextButton.setOnClickListener(v -> {
            // Retrieve the selected start date
            int startYear = startDatePicker.getYear();
            int startMonth = startDatePicker.getMonth();
            int startDay = startDatePicker.getDayOfMonth();

            // Retrieve the selected end date
            int endYear = endDatePicker.getYear();
            int endMonth = endDatePicker.getMonth();
            int endDay = endDatePicker.getDayOfMonth();

            // Convert to milliseconds since epoch
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.set(startYear, startMonth, startDay, 0, 0, 0);
            startCalendar.set(Calendar.MILLISECOND, 0);
            long startDate = startCalendar.getTimeInMillis();

            Calendar endCalendar = Calendar.getInstance();
            endCalendar.set(endYear, endMonth, endDay, 0, 0, 0);
            endCalendar.set(Calendar.MILLISECOND, 0);
            long endDate = endCalendar.getTimeInMillis();

            // Save selected dates and navigate to next fragment
            ((Add_Trip) getActivity()).setTravelDates(startDate, endDate);
            ((Add_Trip) getActivity()).goToNextPage();
        });
    }
}
