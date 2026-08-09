package com.example.lab22023_3_60_051;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginPage_Activity extends Activity {
    private EditText etUserID, etPassword;
    private CheckBox cbRememberUser, cbRememberLogin;
    private Button btnNoAccount, btnExit, btnGo;
    private String userId, userName, pass, phone, email;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        sp = this.getSharedPreferences("UserPrefs", MODE_PRIVATE);
        EventDB db = new EventDB(this);
        
        // Get the User ID from Intent (SignUp) or Preferences (Last login)
        userId = getIntent().getStringExtra("USER-ID");
        if (userId == null) {
            userId = sp.getString("LAST-USER-ID", null);
        }

        boolean isUserRemembered = sp.getBoolean("REM-USER", false);

        // If we have a userId, load the rest from the Database
        if (userId != null) {
            android.database.Cursor cursor = db.getUser(userId);
            if (cursor.moveToFirst()) {
                userName = cursor.getString(1);
                email = cursor.getString(2);
                phone = cursor.getString(3);
                pass = cursor.getString(4);
            }
            cursor.close();
        }

        //Edit Text Fields
        etUserID = findViewById(R.id.etUserID);
        etPassword = findViewById(R.id.etPassword);

        if (isUserRemembered && userId != null) {
            etUserID.setText(userId);
        }
        
        //Checkbox
        cbRememberLogin = findViewById(R.id.cbRememberLogin);
        cbRememberUser = findViewById(R.id.cbRememberUser);
        
        // Set checkbox states from prefs
        cbRememberUser.setChecked(isUserRemembered);
        cbRememberLogin.setChecked(sp.getBoolean("REM-LOGIN", false));

        //Button
        btnExit = findViewById(R.id.btnExit);
        btnGo = findViewById(R.id.btnGo);
        btnNoAccount = findViewById(R.id.btnNoAccount);

        //On Pressing Exit
        btnExit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                System.out.println("The Exit Button was pressed");
                finish();
            }
        });
        btnGo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                System.out.println("The Go Button was pressed");
                accessFieldsData();
            }
        });
        //On Pressing Already Have an Account
        btnNoAccount.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent i = new Intent(LoginPage_Activity.this,SignUp_Activity.class);
                i.putExtra("SHOW_SIGNUP", true);
                startActivity(i);
                finish();
            }
        });


    }
    private void accessFieldsData(){
        String typedUserId= etUserID.getEditableText().toString().trim();
        String password= etPassword.getEditableText().toString().trim();
        boolean isRememberUserIdChecked = cbRememberUser.isChecked() ;
        boolean isRememberLogin = cbRememberLogin.isChecked() ;

        if(typedUserId.length()<4 || typedUserId.length()>6){
            Toast.makeText(this,"UserId must have 4 to 6 digits", Toast.LENGTH_LONG).show();
            return;
        }

        if(password.length()<6 ){
            Toast.makeText(this,"Password must be at least 6 characters",Toast.LENGTH_LONG).show();
            return;
        }

        // Verify from DB
        EventDB db = new EventDB(this);
        android.database.Cursor cursor = db.getUser(typedUserId);
        if (!cursor.moveToFirst()) {
            Toast.makeText(this, "User ID not found", Toast.LENGTH_LONG).show();
            cursor.close();
            return;
        }

        String dbPass = cursor.getString(4);
        if (!dbPass.equals(password)) {
            Toast.makeText(this, "Password didn't match", Toast.LENGTH_LONG).show();
            cursor.close();
            return;
        }

        // If login successful, update 'this' fields for the intent
        this.userId = typedUserId;
        this.userName = cursor.getString(1);
        this.email = cursor.getString(2);
        this.phone = cursor.getString(3);
        cursor.close();

        // Update preferences for last session
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("LAST-USER-ID", typedUserId);
        editor.putBoolean("REM-LOGIN", isRememberLogin);
        editor.putBoolean("REM-USER", isRememberUserIdChecked);
        editor.apply();

        Intent i = new Intent(this, AddressBook_Activity.class);
        i.putExtra("USER-ID", typedUserId);
        startActivity(i);
        finishAffinity();
    }
}