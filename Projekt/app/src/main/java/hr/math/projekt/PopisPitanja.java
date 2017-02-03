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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class PopisPitanja extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popis_pitanja);

        TextView txt = (TextView) findViewById(R.id.svapitanja);
        Typeface font = Typeface.createFromAsset(getAssets(), "Sacramento-Regular.ttf");
        txt.setTypeface(font);

        dohvatiPitanja();

        Globals g = Globals.getInstance();
        if( g.isLogged() ){
            DBAdapter db = new DBAdapter(this);
            db.open();

            if( db.isAdmin( g.getId() ))
            {
                unosPitanja();
            }
        }
    }


    public void dohvatiPitanja() //bool
    {
        DBAdapter db = new DBAdapter(this);
        //--get all questions---
        db.open();
        Cursor c = db.getAllQuestions();
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
        //za sad
        TextView tv = (TextView)findViewById(R.id.ispis);
        tv.append(c.getString(0) + " " + c.getString(1) + "\n");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_popis_pitanja, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void unosPitanja()
    {
       // TextView textView = (TextView)findViewById(R.id.unesi_p);
        EditText editText = (EditText)findViewById(R.id.unesi_pitanje);
        Button btn = (Button)findViewById(R.id.unesi_pit);
        //textView.setVisibility(View.VISIBLE);
        editText.setVisibility(View.VISIBLE);
        btn.setVisibility(View.VISIBLE);
        editText.setText("");
    }

    public void PosaljiPitanje( View view )
    {
        DBAdapter db = new DBAdapter(this);
        db.open();

        EditText editText = (EditText)findViewById(R.id.unesi_pitanje);
        String s =  editText.getText().toString();

        long i = db.insertQuestion(s);
        if( i != 0 ) {
            TextView tv = (TextView)findViewById(R.id.ispis);
            tv.setText("");
            dohvatiPitanja(); //mozda osvjezi kako god a u ovom toast za ubaceno pitanje
        }

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
