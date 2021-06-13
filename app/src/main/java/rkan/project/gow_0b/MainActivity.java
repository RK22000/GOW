package rkan.project.gow_0b;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager2.widget.ViewPager2;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity implements Serializable {

    private ViewPager2 brochureBasketPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        brochureBasketPager = findViewById(R.id.basket_brochure_swiper);
        final MainSwipeAdapter mainSwipeAdapter =
                new MainSwipeAdapter(getSupportFragmentManager(), this);
        brochureBasketPager.setAdapter(mainSwipeAdapter);
        Toolbar toolbar = findViewById(R.id.appBar);
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            /**
             * This method will be invoked when a menu item is clicked if the item itself did
             * not already handle the event.
             *
             * @param item {@link MenuItem} that was clicked
             * @return <code>true</code> if the event was handled, <code>false</code> otherwise.
             */
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.basket_option:
                        brochureBasketPager.setCurrentItem(1);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if (brochureBasketPager.getCurrentItem() == 1) {
            brochureBasketPager.setCurrentItem(0);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("MainActivity", "Main activity destroyed");
    }
}