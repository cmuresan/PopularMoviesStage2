package com.example.android.popularmovies.main;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.android.popularmovies.R;

public class MainActivity extends AppCompatActivity {

    private static final int NUMBER_OF_COLUMNS = 3;
    private MoviesAdapter moviesAdapter;
    private boolean isPopularSelected = true;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupBottomNavigation();
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.action_popular:
                        fragment = PopularMoviesFragment.newInstance();
                        break;
                    case R.id.action_top_rated:
                        fragment = TopRatedMoviesFragment.newInstance();
                        break;
                    case R.id.action_favorites:
                        fragment = FavoriteMoviesFragment.newInstance();
                        break;
                    default:
                        fragment = PopularMoviesFragment.newInstance();
                        break;
                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, fragment);
                transaction.commit();
                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.action_popular);
    }
}
