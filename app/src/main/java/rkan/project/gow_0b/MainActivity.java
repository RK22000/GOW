package rkan.project.gow_0b;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.jetbrains.annotations.NotNull;

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
        TabLayout bbTabs = findViewById(R.id.bbTabLayout);
        new TabLayoutMediator(bbTabs, brochureBasketPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull @NotNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText("Brochure");
                        tab.setIcon(R.drawable.ic_brochure);
                        break;
                    default:
                        tab.setText("Basket");
                        tab.setIcon(R.drawable.ic_basket);
                }
            }
        }).attach();
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