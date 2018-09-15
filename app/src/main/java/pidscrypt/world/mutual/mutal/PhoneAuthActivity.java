package pidscrypt.world.mutual.mutal;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.sql.Time;

import pidscrypt.world.mutual.mutal.Database.CountryData;

import static java.lang.Thread.sleep;

public class PhoneAuthActivity extends AppCompatActivity {
    private EditText editTextMobile;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);
        /*ActionBar actionBar = getSupportActionBar();
        actionBar.hide();*/

        spinner = findViewById(R.id.spinnerCountries);
        spinner.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, CountryData.countryNames));

        editTextMobile = (EditText) findViewById(R.id.editTextMobile);

        findViewById(R.id.buttonContinue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = CountryData.countryAreaCodes[spinner.getSelectedItemPosition()];
                String mobile = editTextMobile.getText().toString().trim();

                if(mobile.isEmpty() || mobile.length() < 9){
                    editTextMobile.setError("Enter a valid mobile");
                    editTextMobile.requestFocus();
                    return;
                }

                if(mobile.startsWith("0")){
                    mobile = mobile.substring(1,mobile.length());
                }

                String phoneNumber = "+" + code + mobile;

                Intent intent = new Intent(PhoneAuthActivity.this, VerifyPhoneActivity.class);
                intent.putExtra("mobile", phoneNumber);
                startActivity(intent);
            }
        });
    }


    public void startAnimation(){
        int wait = 0;

        try{
            while(wait <= 3500){
                wait += 100;
                sleep(100);
            }
        }catch(InterruptedException ex){
            // exc code here
        }
        Animation anim = AnimationUtils.loadAnimation(this,R.anim.widen_out);
        anim.reset();
        editTextMobile.clearAnimation();
        editTextMobile.startAnimation(anim);

    }

    public static void startIntent(Context context){
        Intent intent = new Intent(context, PhoneAuthActivity.class);
        context.startActivity(intent);
    }

    public static void startIntent(Context context, int flags){
        Intent intent = new Intent(context, PhoneAuthActivity.class);
        intent.setFlags(flags);
        context.startActivity(intent);
    }


}