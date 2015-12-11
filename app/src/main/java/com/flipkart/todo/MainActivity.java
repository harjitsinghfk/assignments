package com.flipkart.todo;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        switchToHomepageFragment("date");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch(item.getItemId()){
            case R.id.title:
                Toast.makeText(getBaseContext(), "Sorting by Titles", Toast.LENGTH_SHORT).show();
                switchToHomepageFragment("title");
                break;

            case R.id.priority:
                Toast.makeText(getBaseContext(), "Sorting by Priority", Toast.LENGTH_SHORT).show();
                switchToHomepageFragment("priority");
                break;

            case R.id.dueDate:
                Toast.makeText(getBaseContext(), "Sorting by Due Date", Toast.LENGTH_SHORT).show();
                switchToHomepageFragment("date");
                break;

        }
        return true;

    }

    void switchToHomepageFragment(String sortOptionSeleted) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction;
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
        Homepage homepage;
        homepage = new Homepage();
        homepage.setSortType(sortOptionSeleted);
        fragmentTransaction.replace(R.id.main_layout, homepage, "HPF");
        fragmentTransaction.commit();

    }
}
