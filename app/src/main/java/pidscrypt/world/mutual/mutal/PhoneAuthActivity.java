package pidscrypt.world.mutual.mutal;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import de.hdodenhof.circleimageview.CircleImageView;
import pidscrypt.world.mutual.mutal.Database.CountryData;
import pidscrypt.world.mutual.mutal.services.MutualPermission;

import static java.lang.Thread.sleep;

public class PhoneAuthActivity extends AppCompatActivity {
    private EditText editTextMobile, country_code;
    private Spinner spinner;
    private CircleImageView flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);
        /*ActionBar actionBar = getSupportActionBar();
        actionBar.hide();*/

        spinner = findViewById(R.id.spinnerCountries);
        ArrayAdapter<String> countries = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, CountryData.countryNames);
        //spinner.setSelection(countries.getPosition("Uganda"), true);
        spinner.setAdapter(countries);
        spinner.setSelection(countries.getPosition("Uganda"), true);

        editTextMobile = (EditText) findViewById(R.id.editTextMobile);
        country_code = (EditText) findViewById(R.id.country_code);
        flag = (CircleImageView) findViewById(R.id.flag);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String c_code = CountryData.countryAreaCodes[i];
                country_code.setText(c_code);
                flag.setImageResource(CountryData.countryFlags[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        MutualPermission mutualPermission = new MutualPermission(PhoneAuthActivity.this, 1);
        if (!mutualPermission.check(new String[]{Manifest.permission.READ_PHONE_STATE, "Allow Mutual Messenger to access your phone state."})) {
            mutualPermission.request(Manifest.permission.READ_PHONE_STATE);
        }
/*
        if (!mutualPermission.check(new String[]{Manifest.permission.READ_SMS, "Allow Mutual Messenger to access your incoming messages"})) {
            mutualPermission.request(Manifest.permission.READ_SMS);
        }*/

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            editTextMobile.setText(((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getLine1Number());
        }


        findViewById(R.id.buttonContinue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = CountryData.countryAreaCodes[spinner.getSelectedItemPosition()];
                String mobile = editTextMobile.getText().toString().trim();

                if (mobile.isEmpty() || mobile.length() < 9) {
                    editTextMobile.setError("Enter a valid mobile");
                    editTextMobile.requestFocus();
                    return;
                }

                if (mobile.startsWith("0")) {
                    mobile = mobile.substring(1, mobile.length());
                }

                String phoneNumber = "+" + code + mobile;

                Intent intent = new Intent(PhoneAuthActivity.this, VerifyPhoneActivity.class);
                intent.putExtra("mobile", phoneNumber);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //@TODO: put my phone number into the telephone box
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }

            }
        }
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