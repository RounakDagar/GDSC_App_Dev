package com.example.chatapp.ui.login;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.chatapp.R;
import com.google.android.material.tabs.TabLayout;

public class Add_Trip extends AppCompatActivity {

    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private TabLayout tabLayout;
    private long startDate, endDate;
    private String destinations, activities;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);

        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);

        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        // Bind the TabLayout with the ViewPager
        tabLayout.setupWithViewPager(viewPager);
    }

    public void setTravelDates(long startDate, long endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void setDestinations(String destinations) {
        this.destinations = destinations;
    }

    public void setActivities(String activities) {
        this.activities = activities;
    }

    public void goToNextPage() {
        int nextItem = viewPager.getCurrentItem() + 1;
        if (nextItem < pagerAdapter.getCount()) {
            viewPager.setCurrentItem(nextItem);
        }
    }

    public void finishItineraryCreation() {
        // Save or process the final itinerary
        // For example: show a summary or navigate to another screen
    }
}
