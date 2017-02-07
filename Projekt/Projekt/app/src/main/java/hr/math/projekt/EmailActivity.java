package hr.math.projekt;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class EmailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        TextView txt = (TextView) findViewById(R.id.kontaktfnaslov);
        Typeface font = Typeface.createFromAsset(getAssets(), "Sacramento-Regular.ttf");
        txt.setTypeface(font);
    }

    public void PosaljiMail(View view)
    {
        EditText s =(EditText)findViewById(R.id.Subjekt);
        String subject = s.getText().toString();
        EditText m =(EditText)findViewById(R.id.Poruka);
        String message = m.getText().toString();
        String[] to = {"martinabarisic7@gmail.com"};
        String[] cc = {"iva.sovic3@gmail.com", "terezija.cosic@gmail.com"};
        sendEmail(to, cc, subject, message);

    }

    private void sendEmail(String[] emailAddresses, String[] carbonCopies,
                           String subject, String message)
    {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        String[] to = emailAddresses;
        String[] cc = carbonCopies;
        emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
        emailIntent.putExtra(Intent.EXTRA_CC, cc);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);
        emailIntent.setType("message/rfc822");
        startActivity(Intent.createChooser(emailIntent, "Email"));
    }

    public void Pregled(View view)
    {
        Intent intent = new Intent(this, ListanjeSpomenara.class);
        startActivity(intent);
    }

    public void Popis(View view)
    {
        Intent intent = new Intent(this, PopisPitanja.class);
        startActivity(intent);
    }

    public void PopisKorisnika(View view)
    {
        Intent intent = new Intent(this, PopisKorisnika.class);
        startActivity(intent);
    }

    public void KontaktForma(View view)
    {
        Intent intent = new Intent(this, EmailActivity.class);
        startActivity(intent);
    }

    public void log(View view)
    {
        Globals g = Globals.getInstance();
        //ako je netko ulogiran ponudi mu da se odjavi
        if( g.isLogged() ) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.logout_message)
                    .setTitle(R.string.logout_title);

            builder.setPositiveButton(R.string.logout_ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Globals g = Globals.getInstance();
                    g.logout();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            builder.setNegativeButton(R.string.logout_cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }

            });

            AlertDialog dialog = builder.create();
            dialog.show();

        }
        //ako nitko nije ulogiran ponudi mu login
        else{
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

    }
}
