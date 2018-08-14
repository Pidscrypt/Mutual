package pidscrypt.world.mutual.mutal;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UserDetailsActivity extends AppCompatActivity {

    private Button btn_go_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        btn_go_home = (Button) findViewById(R.id.btn_go_home);

        btn_go_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chats = new Intent(UserDetailsActivity.this, LandingActivity.class);
                chats.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(chats);
            }
        });

    }


}
