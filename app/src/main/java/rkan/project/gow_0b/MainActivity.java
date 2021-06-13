package rkan.project.gow_0b;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager2.widget.ViewPager2;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity implements Serializable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ViewPager2 brochureBasketPager = findViewById(R.id.basket_brochure_swiper);
        final MainSwipeAdapter mainSwipeAdapter =
                new MainSwipeAdapter(getSupportFragmentManager(), this);
        brochureBasketPager.setAdapter(mainSwipeAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("MainActivity", "Main activity destroyed");
    }
}