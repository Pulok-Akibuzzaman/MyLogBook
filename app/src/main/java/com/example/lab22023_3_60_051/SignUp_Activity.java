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

public class SignUp_Activity extends Activity {
    private EditText etUserID, etUserName, etEmail, etPhone, etPassword, etConfirmPassword;
    private CheckBox cbRememberUser, cbRememberLogin;
    private Button btnExit, btnAlreadyHave, btnGo;

    private SharedPreferences sp;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sp = this.getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userId = sp.getString("USER-ID", "NOT-CREATED");
        boolean showSignUp = getIntent().getBooleanExtra("SHOW_SIGNUP", false);

        if (!userId.equals("NOT-CREATED") && !showSignUp) {
            String userName = sp.getString("USER-NAME", "");
            String pass = sp.getString("PASS", "");
            String email = sp.getString("EMAIL", "");
            String phone = sp.getString("PHONE", "");
            boolean isLoginRemembered = sp.getBoolean("REM-LOGIN", false);

            Intent i;
            if (isLoginRemembered) {
                i = new Intent(this, AddressBook_Activity.class);
            } else {
                i = new Intent(this, LoginPage_Activity.class);
            }
            
            boolean isUserRemembered = sp.getBoolean("REM-USER", false);
            i.putExtra("REM-USER", isUserRemembered);
            i.putExtra("USER-ID", userId);
            i.putExtra("USER-NAME", userName);
            i.putExtra("PASS", pass);
            i.putExtra("EMAIL", email);
            i.putExtra("PHONE", phone);
            
            startActivity(i);
            finish();
            return;
        }

        setContentView(R.layout.activity_sign_up);
        //Edit Text Fields
        etUserID = findViewById(R.id.etUserID);
        etUserName = findViewById(R.id.etUserName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        //Checkbox
        cbRememberLogin = findViewById(R.id.cbRememberLogin);
        cbRememberUser = findViewById(R.id.cbRememberUser);
        //Button
        btnExit = findViewById(R.id.btnExit);
        btnGo = findViewById(R.id.btnGo);
        btnAlreadyHave = findViewById(R.id.btnAlreadyHave);
        //On Pressing Exit
        btnExit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                System.out.println("The Exit Button was pressed");
                finish();
            }
        });
        //On Pressing Go
        btnGo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                System.out.println("The Go Button was pressed");
                accessFieldsData();
            }
        });
        //On Pressing Already Have an Account
        btnAlreadyHave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent i = new Intent(SignUp_Activity.this, LoginPage_Activity.class);
                startActivity(i);
                finishAffinity();
            }
        });
    }
    //Method
    private void accessFieldsData(){
        String userId= etUserID.getEditableText().toString().trim();
        String userName= etUserName.getEditableText().toString().trim();
        String email= etEmail.getEditableText().toString().trim();
        String phone= etPhone.getEditableText().toString().trim();
        String password= etPassword.getEditableText().toString().trim();
        String confirmPassword = etConfirmPassword.getEditableText().toString().trim();
        boolean isRememberUserIdChecked = cbRememberUser.isChecked() ;
        boolean isRememberLogin = cbRememberLogin.isChecked() ;

        if(userId.length()<4 || userId.length()>6){
            Toast.makeText(this,"UserId must have 4 to 6 digits", Toast.LENGTH_LONG).show();
            return;
        }
        if(userName.length()<4 || userName.length()>20){
            Toast.makeText(this,"Username must have 4-20 characters",Toast.LENGTH_LONG).show();
            return;
        }
        if(!isValidEmail(email)){
            Toast.makeText(this,"Invalid Email Address",Toast.LENGTH_LONG).show();
            return;
        }
        if(phone.length()<11 || phone.length()>14 ){
            Toast.makeText(this,"Phone number must be 11-14 digits",Toast.LENGTH_LONG).show();
            return;
        }
        if(password.length()<6 ){
            Toast.makeText(this,"Password must be at least 6 characters",Toast.LENGTH_LONG).show();
            return;
        }
        if(!password.equals(confirmPassword)){
            Toast.makeText(this,"Re-Type Password. Password is Incorrect",Toast.LENGTH_LONG).show();
            return;
        }
        //
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("USER-ID", userId);
        editor.putString("USER-NAME", userName );
        editor.putString("PASS", password);
        editor.putString("EMAIL",email );
        editor.putString("PHONE",phone );
        editor.putBoolean("REM-LOGIN", isRememberLogin);
        editor.putBoolean("REM-USER",isRememberUserIdChecked);
        editor.apply();

        //
        Intent i = new Intent(this, AddressBook_Activity.class);
        i.putExtra("USER-ID", userId);
        i.putExtra("USER-NAME", userName);
        i.putExtra("PASS", password);
        i.putExtra("EMAIL", email);
        i.putExtra("PHONE", phone);
        startActivity(i);
        finishAffinity();

    }

    // Method to check email validity
    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
