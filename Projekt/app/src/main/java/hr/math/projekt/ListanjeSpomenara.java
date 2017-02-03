package hr.math.projekt;

import android.app.Activity;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ListanjeSpomenara extends AppCompatActivity {

    int trenStr = 1;
    int maxStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listanje_spomenara);

        TextView txt = (TextView) findViewById(R.id.listanjenaslov);
        Typeface font = Typeface.createFromAsset(getAssets(), "Sacramento-Regular.ttf");
        txt.setTypeface(font);

        maxStr = brPitanja();
        dohvatiPitanje(trenStr);
        dohvatiOdgovore(trenStr);

        //swipe kontrola
        LinearLayout tl = (LinearLayout)findViewById(R.id.spomenar);
        tl.setOnTouchListener(new OnSwipeTouchListener(ListanjeSpomenara.this) {
            public void onSwipeRight() {
                prethodnaStr();
            }

            public void onSwipeLeft() {
                sljedecaStr();
            }
        });

        //ako je logiran provjeri jel odgovorio
        Globals g = Globals.getInstance();
        if( g.isLogged() )
        {
            ProvjeriOdgovore(g.getId());
        }

    }

    public void dodajPitanje(String s) //možda da bude bool?
    {
        DBAdapter db = new DBAdapter(this);

        //---add a question---
        db.open();
        long id;
        id = db.insertQuestion(s);

        db.close();
    }

    public void dohvatiPitanja() //bool //to samo u popisu //ovdje se nepotrebno ponavlja
    {
        DBAdapter db = new DBAdapter(this);
        //--get all questions---
        db.open();
        Cursor c = db.getAllQuestions();
        if (c.moveToFirst()) {
            do {
                Prikazi(c);
            } while (c.moveToNext());
        }
        db.close();

    }

    public void dohvatiPitanje(int i) //bool //dodati mozda provjeru trazimo li ok indeks
    {
        DBAdapter db = new DBAdapter(this);
        //---get a question---
        db.open();
        Cursor c = db.getQuestion(i);
        if (c.moveToFirst())
            Prikazi(c);
        else
            Toast.makeText(this, "No question found", Toast.LENGTH_LONG).show();
        db.close();
    }

    public void promjeniPitanje(int br_p, String s) //bzvz mozda ali da ne brisem sad, mozda zatreba
    {
        DBAdapter db = new DBAdapter(this);
        //---update pitanje---
        db.open();
        if (db.updateQuestion(br_p, s))
            Toast.makeText(this, "Update successful.", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this, "Update failed.", Toast.LENGTH_LONG).show();
        db.close();
    }

    public int brPitanja()
    {
        DBAdapter db = new DBAdapter(this);
        //--get all questions---
        db.open();
        Cursor c = db.getAllQuestions();
        return c.getCount();
    }

    public void obrisiPitanje( int i ) //vraca bool?
    {
        DBAdapter db = new DBAdapter(this);
        //---delete a question---
        db.open();
        if (db.deleteQuestion(1))
            Toast.makeText(this, "Delete successful.", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this, "Delete failed.", Toast.LENGTH_LONG).show();
        db.close();
    }

    public void obrisiPitanja()
    {
        DBAdapter db = new DBAdapter(this);
        //---delete a question---
        db.open();
        if (db.deleteQuestions())
            Toast.makeText(this, "Delete successful.", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this, "Delete failed.", Toast.LENGTH_LONG).show();
        db.close();
    }

    //funkcija za ispis pitanja
    public void Prikazi(Cursor c)
    {
        //za sad //jer nemam sta pametnije

        TextView tv = (TextView)findViewById(R.id.pitanje);
        Typeface font = Typeface.createFromAsset(getAssets(), "Sacramento-Regular.ttf");
        tv.setTypeface(font);
        tv.append(c.getString(1) + "\n");
    }

    public void dohvatiOdgovore(int i) //bool //dodati mozda provjeru trazimo li ok indeks
    {
        DBAdapter db = new DBAdapter(this);
        db.open();
        Cursor c = db.getAnswers(i);
        if (c.moveToFirst()  )
        {
            do {
                if( c.getInt(2) == i )
                    PrikaziOdg(c);
            } while (c.moveToNext());
        }
        else
            Toast.makeText(this, "Trenutno ovo pitanje nije odgovoreno", Toast.LENGTH_LONG).show();
        db.close();
    }

    //funkcija za ispis odgovora
    public void PrikaziOdg(Cursor c)
    {
        TextView tv = (TextView)findViewById(R.id.ispis);
        tv.append(c.getString(1) + " " + c.getString(3) + "\n");

        //ispis svega iz baze odg,ako zatreba
        //tv.append(c.getString(0) + " " + c.getString(1) + " " + c.getString(2) + " " + c.getString(3)  + "\n");
    }

    public void ProvjeriOdgovore( int id )
    {
        DBAdapter db = new DBAdapter(this);
        db.open();

        //ako nije odgovorio omogući mu
        if ( !db.isAswered(id, trenStr) )
        {
           // TextView textView = (TextView)findViewById(R.id.unesi);
            EditText editText = (EditText)findViewById(R.id.unesi_odg);
            Button btn = (Button)findViewById(R.id.unesi_btn);
          // textView.setVisibility(View.VISIBLE);
            editText.setVisibility(View.VISIBLE);
            btn.setVisibility(View.VISIBLE);
        }
        else //ako je već odgovorio sakrij formu za odgovor (i kad odgovori)
        {
           // TextView textView = (TextView)findViewById(R.id.unesi);
            EditText editText = (EditText)findViewById(R.id.unesi_odg);
            Button btn = (Button)findViewById(R.id.unesi_btn);
           // textView.setVisibility(View.GONE);
            editText.setVisibility(View.GONE);
            btn.setVisibility(View.GONE);
            editText.setText("");
        }
        db.close();
    }



    //listanje
    public void prethodnaStr(  )
    {
        if( trenStr > 1 )
        {
            --trenStr;
            TextView tv = (TextView) findViewById(R.id.ispis);
            tv.setText("");
            TextView tv2 = (TextView) findViewById(R.id.pitanje);
            tv2.setText("");

            dohvatiPitanje(trenStr);
            dohvatiOdgovore(trenStr);

            Globals g = Globals.getInstance();
            if( g.isLogged() )
                ProvjeriOdgovore(g.getId());
        }

    }

    public void osvjezi()
    {
        TextView tv = (TextView) findViewById(R.id.ispis);
        tv.setText("");
        TextView tv2 = (TextView) findViewById(R.id.pitanje);
        tv2.setText("");

        dohvatiPitanje(trenStr);
        dohvatiOdgovore(trenStr);

        Globals g = Globals.getInstance();
        if( g.isLogged() )
            ProvjeriOdgovore(g.getId());
    }


    public void sljedecaStr(  )
    {
        if( trenStr < maxStr )
        {
            ++trenStr;
            TextView tv = (TextView) findViewById(R.id.ispis);
            tv.setText("");
            TextView tv2 = (TextView) findViewById(R.id.pitanje);
            Typeface font = Typeface.createFromAsset(getAssets(), "Sacramento-Regular.ttf");
            tv2.setTypeface(font);
            tv2.setText("");

            dohvatiPitanje(trenStr);
            dohvatiOdgovore(trenStr);

            Globals g = Globals.getInstance();
            if( g.isLogged() )
                ProvjeriOdgovore(g.getId());
        }

    }

    public void PosaljiOdg(View view)
    {
        DBAdapter db = new DBAdapter(this);
        db.open();

        EditText editText = (EditText)findViewById(R.id.unesi_odg);
        String s =  editText.getText().toString();

        Globals g = Globals.getInstance();
        long i = db.insertAnswer(g.getId(), g.getUsername(), trenStr, s);
        if( i != 0 )
            osvjezi(); //mozda osvjezi kako god a u ovom toast za ubaceno pitanje
        hideKeyboard();

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

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view == null)
            view = new View(this);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
