package pidscrypt.world.mutual.mutal;

import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import pidscrypt.world.mutual.mutal.api.Album;

public class AlbumRecyclerAdapter extends RecyclerView.Adapter<AlbumRecyclerAdapter.AlbumViewHolder> {

    List<Album> albumList = new ArrayList<>();

    public AlbumRecyclerAdapter(List<Album> albumList) {
        this.albumList = albumList;
    }

    @Override
    public AlbumViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chat_media_row_layout,viewGroup,false);

        return new AlbumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AlbumViewHolder viewHolder, int i) {
        viewHolder.img.setImageResource(albumList.get(i).getImig_id());
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public static class AlbumViewHolder extends RecyclerView.ViewHolder{

        ImageView img;

        public AlbumViewHolder(@NonNull View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.album);
        }
    }
}
