package hr.math.projekt;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class PopisKorisnika extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popis_korisnika);

        TextView txt = (TextView) findViewById(R.id.tkojeodgovorio);
        Typeface font = Typeface.createFromAsset(getAssets(), "Sacramento-Regular.ttf");
        txt.setTypeface(font);

        dohvatiKorisnike();
    }

    public void dohvatiKorisnike() //bool
    {
        DBAdapter db = new DBAdapter(this);
        //--get all questions---
        db.open();
        Cursor c = db.getAllContacts();
        if (c.moveToFirst())
        {
            do {
                Prikazi(c);
            } while (c.moveToNext());
        }
        db.close();
    }

    //funkcija za ispis
    public void Prikazi(Cursor c)
    {
        TextView tv = (TextView)findViewById(R.id.ispisK);
        tv.append(c.getString(0) + " " + c.getString(1) + "\n");
        //ispis svega
        //tv.append(c.getString(0) + " " + c.getString(1) + " " + c.getString(2) + " " + c.getString(3) + " " + c.getString(4) + "\n");
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
