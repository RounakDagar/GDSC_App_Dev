package com.example.chatapp;

import static androidx.core.graphics.drawable.DrawableCompat.applyTheme;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.chatapp.ui.login.LoginActivity;
import com.example.chatapp.ui.login.Settings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class User_Information extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private TextView username, status;
    private ImageView logout, pfp;
    private ProgressBar load;
    private DatabaseReference databaseReference;
    private String imgpfp;
    private ActivityResultLauncher<String> getContentLauncher;
    private TextView setting;
    private Switch darkmode;
    private static final String PREFS_NAME = "AppPreferences";
    private static final String DARK_MODE_KEY = "dark_mode";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);

        // Initialize views
        username = findViewById(R.id.username);
        status = findViewById(R.id.status);
        logout = findViewById(R.id.logout);
        pfp = findViewById(R.id.pfp);
        load = findViewById(R.id.progressBar2);
        setting = findViewById(R.id.setting);
        darkmode=findViewById(R.id.switch1);
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isDarkMode = prefs.getBoolean(DARK_MODE_KEY, true);
        darkmode.setChecked(isDarkMode);
        applyTheme(isDarkMode);


        darkmode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Save theme preference
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(DARK_MODE_KEY, isChecked);
            editor.apply();

            // Apply the selected theme
            applyTheme(isChecked);
        });



        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(User_Information.this, Settings.class);
                startActivity(in);

            }
        });






        // Show ProgressBar before data retrieval
        load.setVisibility(View.VISIBLE);

        // Set up listeners for TextViews
        username.setOnClickListener(v -> showEditDialog("username", username));
        status.setOnClickListener(v -> showEditDialog("status", status));

        // Set up activity result launcher for image selection
        getContentLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri uri) {
                if (uri != null) {
                    uploadImageToFirebaseStorage(uri);
                }
            }
        });

        // Set up click listener for profile picture
        pfp.setOnClickListener(v -> openGallery());

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        String userId = getUserId();

        if (userId.isEmpty()) {
            Log.e("User_Information", "No user is currently signed in");
            return;
        }

        // Set up database reference
        databaseReference = database.getReference("user").child(userId);
        myRef = database.getReference().child("user").child(userId);

        // Fetch user data from Firebase
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    populateUserData(dataSnapshot);
                } else {
                    Log.e("User_Information", "No data found for the user");
                }
                // Hide ProgressBar after data is loaded
                load.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FirebaseError", "Error retrieving data: " + databaseError.getMessage());
                // Hide ProgressBar on error
                load.setVisibility(View.GONE);
                Toast.makeText(User_Information.this, "Failed to load data.", Toast.LENGTH_SHORT).show();
            }
        });

        // Set up logout button click listener
        logout.setOnClickListener(view -> showDialog());
    }

    // Get the current user ID
    private String getUserId() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        return (currentUser != null) ? currentUser.getUid() : "";
    }

    // Open gallery to select an image
    private void openGallery() {
        Log.d("User_Information", "openGallery called");
        getContentLauncher.launch("image/*");
    }

    // Upload selected image to Firebase Storage
    private void uploadImageToFirebaseStorage(Uri imageUri) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference photoRef = storageRef.child("profile_pics/" + UUID.randomUUID().toString() + ".jpg");

        UploadTask uploadTask = photoRef.putFile(imageUri);
        uploadTask.addOnSuccessListener(taskSnapshot -> photoRef.getDownloadUrl().addOnSuccessListener(uri -> {
            String newPhotoUrl = uri.toString();
            updateProfilePicUrl(newPhotoUrl);
        }).addOnFailureListener(e -> {
            Log.e("UploadError", "Failed to get image URL.", e);
            Toast.makeText(User_Information.this, "Failed to get image URL.", Toast.LENGTH_SHORT).show();
        })).addOnFailureListener(e -> {
            Log.e("UploadError", "Failed to upload image.", e);
            Toast.makeText(User_Information.this, "Failed to upload image.", Toast.LENGTH_SHORT).show();
        });
    }

    // Update profile picture URL in Firebase Database
    private void updateProfilePicUrl(String newPhotoUrl) {
        String userId = getUserId();

        if (userId.isEmpty()) {
            Log.e("User_Information", "No user is currently signed in");
            return;
        }

        DatabaseReference dbrf = database.getReference().child("user").child(userId).child("profilepic");
        dbrf.setValue(newPhotoUrl).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Glide.with(User_Information.this)
                        .load(newPhotoUrl)
                        .placeholder(R.drawable.profile)
                        .error(R.drawable.pfpp)
                        .transform(new CircleCrop())
                        .into(pfp);
                Toast.makeText(User_Information.this, "Profile picture updated.", Toast.LENGTH_SHORT).show();
            } else {
                Log.e("UpdateError", "Failed to update profile picture.");
                Toast.makeText(User_Information.this, "Failed to update profile picture.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Show logout confirmation dialog
    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("Do you really want to logout?");

        builder.setPositiveButton("Yes", (dialog, which) -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(User_Information.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Show dialog to edit user information
    private void showEditDialog(String field, TextView textView) {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialoglayout, null);

        CardView cardView = dialogView.findViewById(R.id.card_view);
        cardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));

        EditText editText = dialogView.findViewById(R.id.Change);
        Button cancelButton = dialogView.findViewById(R.id.cancel);
        Button saveButton = dialogView.findViewById(R.id.save);
        TextView title = dialogView.findViewById(R.id.Title);

        editText.setText(textView.getText().toString());
        title.setText("Edit " + field);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
        dialog.show();

        cancelButton.setOnClickListener(v -> dialog.dismiss());

        saveButton.setOnClickListener(v -> {
            String inputText = editText.getText().toString();
            if (!inputText.isEmpty()) {
                updateField(field, inputText);
                textView.setText(inputText);
            }
            dialog.dismiss();
        });
    }

    // Update user information in Firebase Database
    private void updateField(String field, String value) {
        String userId = getUserId();

        if (userId.isEmpty()) {
            Log.e("User_Information", "No user is currently signed in");
            return;
        }

        DatabaseReference fieldRef = database.getReference("user").child(userId).child(field);
        fieldRef.setValue(value).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(User_Information.this, "Updated " + field, Toast.LENGTH_SHORT).show();
            } else {
                Log.e("UpdateError", "Failed to update " + field);
                Toast.makeText(User_Information.this, "Failed to update " + field, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Populate user data from DataSnapshot
    private void populateUserData(DataSnapshot dataSnapshot) {
        String email = dataSnapshot.child("mail").getValue(String.class);
        String name = dataSnapshot.child("username").getValue(String.class);
        String statuss = dataSnapshot.child("status").getValue(String.class);
        imgpfp = dataSnapshot.child("profilepic").getValue(String.class);

        TextView mail = findViewById(R.id.email);
        mail.setText(email);
        username.setText(name);
        status.setText(statuss);

        retrieveAndDisplayImage();
    }

    // Retrieve and display profile picture
    private void retrieveAndDisplayImage() {
        if (imgpfp != null && !imgpfp.isEmpty()) {
            Glide.with(this)
                    .load(imgpfp)
                    .placeholder(R.drawable.profile)
                    .error(R.drawable.pfpp)
                    .transform(new CircleCrop())
                    .into(pfp);
        }
    }

    private void applyTheme(boolean isDarkMode) {
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}
