package pidscrypt.world.mutual.mutal.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import pidscrypt.world.mutual.mutal.R;

public class GalleryImageAdapter extends RecyclerView.Adapter<GalleryImageAdapter.ImageViewHolder> {
    private Context context;
    private ArrayList<String> images;

    public GalleryImageAdapter(Context context) {
        this.context = context;
        this.images = getAllShownImagesPaths();
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.grid_item_layout,viewGroup,false);
            return new ImageViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder viewHolder, int i) {

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.drawable.bg_outline_gray)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context).setDefaultRequestOptions(requestOptions).load(images.get(i)).thumbnail(0.5f).into(viewHolder.image);

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    private ArrayList<String> getAllShownImagesPaths(){
        Uri uri;
        Cursor cursor;
        int column_index_data,  folder_name;
        ArrayList<String> allImages = new ArrayList<String>();
        String absoluteImagePath = null;
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
        cursor = context.getContentResolver().query(uri, projection, null, null, null);
        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

        while(cursor.moveToNext()){
            absoluteImagePath  = cursor.getString(column_index_data);
            allImages.add(absoluteImagePath);
            Log.d("image_log", absoluteImagePath);
        }
        return allImages;
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        public ImageViewHolder(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.image);
        }
    }
}
