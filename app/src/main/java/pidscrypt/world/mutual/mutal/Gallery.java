package pidscrypt.world.mutual.mutal;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridLayout;
import android.widget.GridView;

import java.util.ArrayList;

import pidscrypt.world.mutual.mutal.Adapters.GalleryImageAdapter;

public class Gallery extends AppCompatActivity {

    private ArrayList<String> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView gallery = (RecyclerView) findViewById(R.id.galleryGridView);

        GalleryImageAdapter galleryViewAdapter = new GalleryImageAdapter(this);
        gallery.setHasFixedSize(true);
        gallery.setItemAnimator(new DefaultItemAnimator());
        gallery.setLayoutManager(new GridLayoutManager(this, 2));
        gallery.setAdapter(galleryViewAdapter);

    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }*/
}
