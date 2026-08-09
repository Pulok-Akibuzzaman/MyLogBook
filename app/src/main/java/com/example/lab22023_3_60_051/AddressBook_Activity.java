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

import com.example.lab22023_3_60_051.R;

import java.util.ArrayList;

public class AddressBook_Activity extends Activity {

    private ListView lvAddressBook;
    private Button btnExit, btnAddNew;
    private ArrayList<Contact> contactList;
    private ActivityAdapter adapter;
    private EventDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_book);

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
                startActivity(i);
            }
        });

        // Handle clicking on a contact to view/edit it
        lvAddressBook.setOnItemClickListener((parent, view, position, id) -> {
            Contact c = contactList.get(position);
            Intent i = new Intent(AddressBook_Activity.this, AddressDetails_Activity.class);
            i.putExtra("CONTACT_EMAIL", c.email);
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
                                db.deleteContact(c.email);

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
    protected void onResume() {
        super.onResume();
        loadContacts();
    }

    private void loadContacts() {
        contactList.clear();
        Cursor cursor = db.getAllContacts();
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
}
