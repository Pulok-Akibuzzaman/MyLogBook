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
        
        // Try to get data from Intent first
        Intent i = this.getIntent();
        boolean isUserRemembered;
        if(i.hasExtra("USER-ID")){
            userId = i.getStringExtra("USER-ID");
            userName = i.getStringExtra("USER-NAME");
            pass = i.getStringExtra("PASS");
            phone = i.getStringExtra("PHONE");
            email = i.getStringExtra("EMAIL");
            isUserRemembered = i.getBooleanExtra("REM-USER", false);
        } else {
            // Fallback to SharedPreferences
            userId = sp.getString("USER-ID", null);
            userName = sp.getString("USER-NAME", "");
            pass = sp.getString("PASS", "");
            phone = sp.getString("PHONE", "");
            email = sp.getString("EMAIL", "");
            isUserRemembered = sp.getBoolean("REM-USER", false);
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
    //Method
    private void accessFieldsData(){
        String userId= etUserID.getEditableText().toString().trim();
        String password= etPassword.getEditableText().toString().trim();
        boolean isRememberUserIdChecked = cbRememberUser.isChecked() ;
        boolean isRememberLogin = cbRememberLogin.isChecked() ;

        if(userId.length()<4 || userId.length()>6){
            Toast.makeText(this,"UserId must have 4 to 6 digits", Toast.LENGTH_LONG).show();
            return;
        }

        if(password.length()<6 ){
            Toast.makeText(this,"Password must be at least 6 characters",Toast.LENGTH_LONG).show();
            return;
        }

        if(this.userId == null || !this.userId.equals(userId)){
            Toast.makeText(this, "User Id didn't match", Toast.LENGTH_LONG).show();
            return;
        }

        if(this.pass == null || !this.pass.equals(password)){
            Toast.makeText(this, "Password didn't match", Toast.LENGTH_LONG).show();
            return;
        }

        // Update preferences
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("REM-LOGIN", isRememberLogin);
        editor.putBoolean("REM-USER", isRememberUserIdChecked);
        editor.apply();

       Intent i = new Intent(this, AddressBook_Activity.class);
        i.putExtra("USER-ID",userId);
        i.putExtra("USER-NAME", userName);
        i.putExtra("PASS", password);
        i.putExtra("EMAIL", email);
        i.putExtra("PHONE", phone);
        startActivity(i);
        finishAffinity();


    }
}