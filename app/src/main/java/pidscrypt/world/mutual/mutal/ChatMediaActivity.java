package pidscrypt.world.mutual.mutal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import pidscrypt.world.mutual.mutal.Adapters.AlbumRecyclerAdapter;
import pidscrypt.world.mutual.mutal.api.Album;

public class ChatMediaActivity extends AppCompatActivity {

    private AlbumRecyclerAdapter albumRecyclerAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_media);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.album_recycler);

        albumRecyclerAdapter = new AlbumRecyclerAdapter(getPhotoAlbums());
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(albumRecyclerAdapter);
    }

    private List<Album> getPhotoAlbums(){
        List<Album> albumList = new ArrayList<>();
        albumList.add(new Album(R.drawable.dami));
        albumList.add(new Album(R.drawable.dami_1));
        albumList.add(new Album(R.drawable.olili_tellems));
        return albumList;
    }
}
