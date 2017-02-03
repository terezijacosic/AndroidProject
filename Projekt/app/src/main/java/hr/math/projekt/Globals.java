package hr.math.projekt;

import android.app.Application;

/**
 * Created by isovic on 1/24/17.
 */
public class Globals extends Application {
    private static Globals instance = null;

    // Global variable
    private boolean log = false;
    private int username_id = -1;
    private String username;

    // Restrict the constructor from being instantiated
    private Globals(){}

    public boolean isLogged()
    { return log; }

    public void setId(int d){
        this.username_id=d;
        this.log = true;
    }

    public int getId(){
        return this.username_id;
    }

    public void setUsername(String s) //to vjerojatno nece ostati kasnije jer nema potrebe pamtiti username?
            //osim ako ga nećemo ispisivati negdje u kutu ili izborniku
    {
        this.username=s;
    }
    public String getUsername(){
        return this.username;
    }

    public static synchronized Globals getInstance(){
        if(instance==null)
        {
            instance=new Globals();
        }
        return instance;
    }

    public void logout()
    {
        this.log = false;
    }
}