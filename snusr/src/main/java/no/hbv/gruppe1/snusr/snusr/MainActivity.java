package no.hbv.gruppe1.snusr.snusr;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private final String PREFS_NAME = "PrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        DatabaseHelper db = new DatabaseHelper(this);
        if (settings.getBoolean("first_time", true)){
            // Kode som skal kjøres første gang appen tas i bruk.
            // Opprett database
            // Ved testing av appen kan det være greit å slette appen fra mobilen
            // om det er gjort endringer i databasen, og lignende.
            db.putDummyData();
            // Setter first_time til false, denne koden kjøres aldri igjen.
            settings.edit().putBoolean("first_time", false).apply();
        }
        Intent intent = new Intent(this, NavigationDrawer.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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


}
