package pidscrypt.world.mutual.mutal;

import android.support.design.circularreveal.CircularRevealGridLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Friend_Chat extends AppCompatActivity {

    String[] gridViewStrings = {"Camera", "Gallery", "Audio", "Contact", "Video", "File"};
    int[] gridViewImageId = {
            R.drawable.attach_camera, R.drawable.attach_gallery,
            R.drawable.attach_audio, R.drawable.attach_contact,
            R.drawable.attach_video, R.drawable.attach_document,
    };

    private CircularRevealGridLayout chatOptionsGridLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend__chat);

        chatOptionsGridLayout = (CircularRevealGridLayout) findViewById(R.id.chat_options_grid);
        CustomGridViewActivity adapterViewAndroid = new CustomGridViewActivity(Friend_Chat.this, gridViewStrings, gridViewImageId);
        chatOptionsGridLayout.setFadingEdgeLength(0);
        //chatOptionsGridLayout.setAdapter(adapterViewAndroid);
    }
}
