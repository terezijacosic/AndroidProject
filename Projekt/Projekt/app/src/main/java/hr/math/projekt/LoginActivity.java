package hr.math.projekt;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    @InjectView(R.id.input_email) EditText _emailText;
    @InjectView(R.id.input_password) EditText _passwordText;
    @InjectView(R.id.btn_login) Button _loginButton;
    @InjectView(R.id.link_signup) TextView _signupLink;
    @InjectView(R.id.link_gost) TextView _gostLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });

        _gostLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Main activity
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                //startActivityForResult(intent, REQUEST_SIGNUP);
                startActivity(intent);
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

       /* final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Provjera...");
        progressDialog.show(); */


        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();


        DBAdapter db = new DBAdapter(this);
        db.open();
        Cursor c = db.getAllContacts();
        int x = 0;
        if (c.moveToFirst())
        {
            do {
                if(email.equals(c.getString(c.getColumnIndex("email"))) && password.equals(c.getString(c.getColumnIndex("password"))))
                {
                    x = 1;
                    onLoginSuccess(c.getInt(c.getColumnIndex("_id")), c.getString(c.getColumnIndex("name")));
                }

            } while (c.moveToNext());
        }
        if( x == 0 )
            onLoginFailed();

        db.close();
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess(int id, String name) {
        _loginButton.setEnabled(true);
        Globals g = Globals.getInstance();
        g.setId(id);
        g.setUsername(name);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Neuspješna prijava!", Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
    }


    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("Unesite ispravnu email adresu!");
            valid = false;}
        else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("Lozinka mora imati 4 - 10 brojki/slova!");
            valid = false;}
        else {
            _passwordText.setError(null);
        }

        return valid;
    }
}