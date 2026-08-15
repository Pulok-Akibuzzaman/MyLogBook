package com.example.lab22023_3_60_051;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import android.os.AsyncTask;
import java.util.List;

import com.example.lab22023_3_60_051.R;

import java.util.ArrayList;

public class AddressBook_Activity extends Activity {

    private ListView lvAddressBook;
    private Button btnExit, btnAddNew;
    private ArrayList<Contact> contactList;
    private ActivityAdapter adapter;
    private EventDB db;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_book);

        currentUserId = getIntent().getStringExtra("USER-ID");
        if (currentUserId == null) {
            currentUserId = getSharedPreferences("UserPrefs", MODE_PRIVATE).getString("LAST-USER-ID", null);
        }

        db = new EventDB(this);
        lvAddressBook = findViewById(R.id.lvAddressBook);
        btnExit = findViewById(R.id.btnExit);
        btnAddNew = findViewById(R.id.btnAddNew);

        contactList = new ArrayList<>();
        adapter = new ActivityAdapter(this, contactList);
        lvAddressBook.setAdapter(adapter);

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Disable Auto Login on Sign Out
                getSharedPreferences("UserPrefs", MODE_PRIVATE)
                        .edit()
                        .putBoolean("REM-LOGIN", false)
                        .apply();

                // Return to Login
                Intent i = new Intent(AddressBook_Activity.this, LoginPage_Activity.class);
                startActivity(i);
                finish();
            }
        });

        btnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddressBook_Activity.this, AddressDetails_Activity.class);
                i.putExtra("USER-ID", currentUserId);
                startActivity(i);
            }
        });

        // Handle clicking on a contact to view/edit it
        lvAddressBook.setOnItemClickListener((parent, view, position, id) -> {
            Contact c = contactList.get(position);
            Intent i = new Intent(AddressBook_Activity.this, AddressDetails_Activity.class);
            i.putExtra("CONTACT_EMAIL", c.email);
            i.putExtra("USER-ID", currentUserId);
            startActivity(i);
        });




        // Handle long-click to delete a contact
        lvAddressBook.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Contact c = contactList.get(position);

                // Show confirmation dialog
                new AlertDialog.Builder(AddressBook_Activity.this)
                        .setTitle("Delete Contact")
                        .setMessage("Are you sure you want to delete " + c.name + "?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Delete from database
                                db.deleteContact(currentUserId, c.email);

                                // Remove from list and refresh
                                contactList.remove(position);
                                adapter.notifyDataSetChanged();

                                Toast.makeText(AddressBook_Activity.this,
                                        "Contact deleted successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();

                return true;
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        String keys[] = {"action", "sid", "semester"};
        String values[] = {"restore", "2023-3-60-051", "2026-3"};
        httpRequest(keys, values);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadContacts();
    }

    private void loadContacts() {
        contactList.clear();
        Cursor cursor = db.getAllContacts(currentUserId);
        while (cursor.moveToNext()) {
            String email = cursor.getString(0);
            String name = cursor.getString(1);
            String phone = cursor.getString(2);
            String dob = cursor.getString(3);
            String present = cursor.getString(4);
            String permanent = cursor.getString(5);
            String imageUri = cursor.getString(6);
            contactList.add(new Contact(email, name, email, phone, dob, present, permanent, imageUri));
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }

    private void httpRequest(final String keys[], final String values[]) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                String url = "https://www.muthosoft.com/univ/cse489/index.php";
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
                    updateLocalDBByServerData(data);
                }
            }
        }.execute();
    }

    private void updateLocalDBByServerData(String data) {
        System.out.println("found: " + data);
        if (currentUserId == null) return;
        try {
            JSONObject jo = new JSONObject(data);
            if (jo.has("key-value")) {
                JSONArray ja = jo.getJSONArray("key-value");
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject summary = ja.getJSONObject(i);
                    String uniqueKey = summary.getString("key");
                    String rowValue = summary.getString("value");
                    String[] fields = rowValue.split("::");
                    if (fields.length >= 7) {
                        String name = fields[0];
                        String email = fields[1];
                        String phone = fields[2];
                        String dob = fields[3];
                        String present = fields[4];
                        String permanent = fields[5];
                        String imageUri = fields[6].equals("null") ? null : fields[6];
                        db.insertContact(currentUserId, name, email, phone, dob, present, permanent, imageUri);
                    }
                }
                loadContacts();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
