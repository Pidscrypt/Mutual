package pidscrypt.world.mutual.mutal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

public class VerifyPhoneActivity extends AppCompatActivity {

    //These are the objects needed
    //It is the verification id that will be sent to the user
    private String mVerificationId;

    //The edittext to input the code
    private EditText editTextCode;

    //firebase auth object
    private FirebaseAuth mAuth;
    private TextView tv_timer, mobile_to_verify;
    private String mobile;
    private Button btn_resend_sms;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);

        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();

        //initializing objects
        mAuth = FirebaseAuth.getInstance();
        editTextCode = findViewById(R.id.editTextCode);
        tv_timer = (TextView) findViewById(R.id.timer);
        mobile_to_verify = (TextView) findViewById(R.id.mobile);
        btn_resend_sms = (Button) findViewById(R.id.btn_resend_sms);


        startAnimation();


        //getting mobile number from the previous activity
        //and sending the verification code to the number
        Intent intent = getIntent();
        mobile = intent.getStringExtra("mobile");

        mobile_to_verify.setText(mobile);

        sendVerificationCode(mobile);


        //if the automatic sms detection did not work, user can also enter the code manually
        //so adding a click listener to the button
        findViewById(R.id.buttonSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = editTextCode.getText().toString().trim();
                if (code.isEmpty() || code.length() < 6) {
                    editTextCode.setError("Enter valid code");
                    editTextCode.requestFocus();
                    return;
                }

                //verifying the code entered manually
                verifyVerificationCode(code);
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
        editTextCode.clearAnimation();
        editTextCode.startAnimation(anim);

    }

    //the method is sending verification code
    //the country id is concatenated
    //you can take the country id as user input as well
    private void sendVerificationCode(final String mobile) {


        @SuppressLint("HandlerLeak") final Handler h = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }
        };


        final CountDownTimer timer = new CountDownTimer(90000, 1000) {
            int minute = 1, ticks = 0;
            @Override
            public void onTick(long l) {
                ticks += 1;
                minute = ((ticks == 30) && (minute != 0) && (l >= 60000))?(minute -= 1):minute;

                //@todo: update ui for seconds\
                int minutes = Integer.parseInt(Long.toString(l/60000));
                int seconds = Integer.parseInt(Long.toString((l%60000)/1000));

                if(l > 60000){
                    tv_timer.setTextColor(getResources().getColor(R.color.green));
                }else if((l <= 60000) && (l >= 30000)){
                    tv_timer.setTextColor(getResources().getColor(R.color.orange));
                }else{
                    tv_timer.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
                tv_timer.setText(minutes + ":"+seconds);
            }

            @Override
            public void onFinish() {
                //@todo: show resend code button
                btn_resend_sms.setTextColor(getResources().getColor(R.color.colorPrimaryLight));
                btn_resend_sms.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sendVerificationCode(mobile);
                        /*Intent back = new Intent(VerifyPhoneActivity.this, PhoneAuthActivity.class);
                        back.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(back);*/
                    }
                });
            }
        };

        Runnable verifyRunnable = new Runnable() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void run() {
                synchronized (this){
                    new PhoneVerificationHandler(){
                        @Override
                        public void responseRecieved(Boolean res) {
                            super.responseRecieved(res);
                            Toast.makeText(VerifyPhoneActivity.this, res?"Loading ...":"Connection failed!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void verifyComplete(String code) {
                            super.verifyComplete(code);
                            if (code != null) {
                                editTextCode.setText(code);
                                timer.cancel();
                                //verifying the code
                                verifyVerificationCode(code);
                            }
                        }

                        @Override
                        public void codeSent(String s) {
                            super.codeSent(s);
                            //storing the verification id that is sent to the user
                            mVerificationId = s;
                        }

                        @Override
                        public void startTimer() {
                            super.startTimer();
                            timer.start();
                        }
                    }.execute(mobile);
                }

                h.sendEmptyMessage(0);
            }
        };

        Thread verifyThread = new Thread(null, verifyRunnable);
        verifyThread.start();

        //startTimer();
    }

    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(VerifyPhoneActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //verification successful we will start the profile activity
                            Intent intent = new Intent(VerifyPhoneActivity.this, EnterUserActivity.class);
                            intent.putExtra("phone",mobile);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        } else {

                            //verification unsuccessful.. display an error message

                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }

                            Snackbar snackbar = Snackbar.make(findViewById(R.id.parent), message, Snackbar.LENGTH_LONG);
                            snackbar.setAction("Dismiss", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                            snackbar.show();
                        }
                    }
                });
    }

}