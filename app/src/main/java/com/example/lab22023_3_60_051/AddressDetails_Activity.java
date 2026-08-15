package com.example.lab22023_3_60_051;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.os.AsyncTask;
import java.util.ArrayList;
import java.util.List;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class AddressDetails_Activity extends AppCompatActivity {
    private EditText etName, etEmail, etPhone, etDob, etPresentAddress, etPermanentAddress;
    private ImageView ivProfile;
    private Button btnCancel, btnSave;
    private EventDB db;
    private String existingEmail = null;
    private String imageUri = null;
    private String userId = null;

    private final ActivityResultLauncher<String> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    imageUri = uri.toString();
                    ivProfile.setImageURI(uri);
                    // Persist permission for the URI if needed, but for lab usually not required unless reboot
                    getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new EventDB(this);
        setContentView(R.layout.activity_address_details);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etDob = findViewById(R.id.etDob);
        etPresentAddress = findViewById(R.id.etPresentAddress);
        etPermanentAddress = findViewById(R.id.etPermanentAddress);
        ivProfile = findViewById(R.id.ivProfile);

        ivProfile.setOnClickListener(v -> imagePickerLauncher.launch("image/*"));

        userId = getIntent().getStringExtra("USER-ID");

        // Check if we are editing an existing contact
        existingEmail = getIntent().getStringExtra("CONTACT_EMAIL");
        if (existingEmail != null) {
            loadContactData(existingEmail);
        }

        btnCancel = findViewById(R.id.btnCancel);
        btnSave = findViewById(R.id.btnSave);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //on pressing Save:
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accessFieldsData();
            }
        });
    }
    private void loadContactData(String email) {
        Cursor cursor = db.getAllContacts(userId);
        while (cursor.moveToNext()) {
            if (cursor.getString(0).equals(email)) {
                etEmail.setText(cursor.getString(0));
                etName.setText(cursor.getString(1));
                etPhone.setText(cursor.getString(2));
                etDob.setText(cursor.getString(3));
                etPresentAddress.setText(cursor.getString(4));
                etPermanentAddress.setText(cursor.getString(5));
                imageUri = cursor.getString(6);
                if (imageUri != null) {
                    ivProfile.setImageURI(Uri.parse(imageUri));
                }
                etEmail.setEnabled(false); // Email is PK, don't allow changing it during edit
                break;
            }
        }
        cursor.close();
    }
    private void accessFieldsData() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String dob = etDob.getText().toString().trim();
        String presentAddress = etPresentAddress.getText().toString().trim();
        String permanentAddress = etPermanentAddress.getText().toString().trim();

        if(name.length() < 4 || name.length() > 40){
            Toast.makeText(this, "Name must be between 4-40 characters", Toast.LENGTH_LONG).show();
            return;
        }

        if(!isValidEmailAddress(email)){
            Toast.makeText(this, "Invalid email address", Toast.LENGTH_LONG).show();
            return;
        }

        if(phone.length() < 11 || phone.length() > 14){
            Toast.makeText(this, "Phone number must be between 11-14 digits", Toast.LENGTH_LONG).show();
            return;
        }

        if(dob.length() < 8){
            Toast.makeText(this, "Invalid Date of Birth", Toast.LENGTH_LONG).show();
            return;
        }

        if(presentAddress.length() < 4 || presentAddress.length() > 100){
            Toast.makeText(this, "Present Address must be between 4-100 characters", Toast.LENGTH_LONG).show();
            return;
        }

        if(permanentAddress.length() < 4 || permanentAddress.length() > 100){
            Toast.makeText(this, "Permanent Address must be between 4-100 characters", Toast.LENGTH_LONG).show();
            return;
        }

        if (existingEmail != null) {
            db.updateContact(userId, name, email, phone, dob, presentAddress, permanentAddress, imageUri);
        } else {
            db.insertContact(userId, name, email, phone, dob, presentAddress, permanentAddress, imageUri);
        }

        // Prepare data for remote backup
        String uniqueKey = email;
        String rowValue = name + "::" + email + "::" + phone + "::" + dob + "::" + presentAddress + "::" + permanentAddress + "::" + (imageUri != null ? imageUri : "null");
        String keys[] = {"action", "sid", "semester", "key", "value"};
        String values[] = {"backup", "2023-3-60-051", "2026-3", uniqueKey, rowValue};
        httpRequest(keys, values);

        Toast.makeText(this, "Information Saved Successfully", Toast.LENGTH_SHORT).show();
        finish();
    }
    public boolean isValidEmailAddress(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void httpRequest(final String keys[], final String values[]) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                String url = "https://www.muthosoft.com/univ/cse489/key_value.php";
                try {
                    String data = RemoteAccess.getInstance().makeHttpRequest(url, "POST", keys, values);
                    return data;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            protected void onPostExecute(String data) {
                if (data != null) {
                    Toast.makeText(getApplicationContext(), data, Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }
}
