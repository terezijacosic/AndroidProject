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
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    static int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Typeface font = Typeface.createFromAsset(getAssets(), "Sacramento-Regular.ttf");
        TextView txt = (TextView) findViewById(R.id.dobrodosli);
        txt.setTypeface(font);

        //ako nema nista u bazi korisnika popunit ce sve baze
        popuniBazu();


        if( count == 0 ){
            ++count;
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        TextView t = (TextView)findViewById(R.id.ispis);
        Globals g = Globals.getInstance();
        //provjera logirane osobe
        if( g.isLogged() ) {
            t.setText("\nBok, "+ g.getUsername() + "\n");
            t.setTypeface(font);

        }
        else
        {
            t.setText("\nBok, gost! :)");
            t.setTypeface(font);
        }
    }



    public void popuniBazu()
    {
        DBAdapter db = new DBAdapter(this);
        db.open();
        if( db.getAllContacts().getCount() == 0 ) {
            popuniPitanja();
            popuniOdgovore();
            popuniKorisnike();
        }
    }

    public void popuniPitanja() //možda da bude bool da znamo jel uspjesno ili ne?
    {
        DBAdapter db = new DBAdapter(this);
        //---add a question---
        db.open();
        long id;
        id = db.insertQuestion("Kako se zoveš?");
        id = db.insertQuestion("Gdje živiš?");
        id = db.insertQuestion("Koliko imaš godina?");
        id = db.insertQuestion("Boja očiju?");
        id = db.insertQuestion("Najbolji prijatelj?");
        id = db.insertQuestion("Imaš li simpatiju?");
        id = db.insertQuestion("Najdraže jelo?");
        db.close();
    }

    public void popuniOdgovore() //možda da bude bool da znamo jel uspjesno ili ne?
    {
        DBAdapter db = new DBAdapter(this);

        //---add a question---
        db.open();
        long id;
        id = db.insertAnswer(1,"Ana", 1, "Ana");
        id = db.insertAnswer(1, "Ana", 2, "Zagreb" );
        id = db.insertAnswer(1, "Ana", 3, "15");
        id = db.insertAnswer(1, "Ana", 4, "Plava");
        id = db.insertAnswer(1, "Ana", 5, "Petar");
        id = db.insertAnswer(1, "Ana", 6, "Da");
        id = db.insertAnswer(1, "Ana", 7, "Pizza");

        id = db.insertAnswer(2, "Pero", 1, "Petar");
        id = db.insertAnswer(2, "Pero", 2, "Zadar");
        id = db.insertAnswer(2, "Pero", 3, "10");
        db.close();
    }

    public void popuniKorisnike()
    {
        DBAdapter db = new DBAdapter(this);

        db.open();
        long id;
        id = db.insertContact("Ana", "ana@ana.hr", "pass", 0);
        id = db.insertContact("Pero", "pero@pero.hr", "pass", 0);
        id = db.insertContact("admin", "admin@admin.hr", "admin", 1);
        db.close();
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

    public void kontaktForma(View view)
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
