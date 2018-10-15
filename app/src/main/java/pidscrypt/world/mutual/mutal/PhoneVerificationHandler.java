package pidscrypt.world.mutual.mutal;

import android.os.AsyncTask;
import android.text.BoringLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneVerificationHandler extends AsyncTask<String, Void, Boolean> {
    @Override
    protected Boolean doInBackground(String... mobile) {
        //the callback to detect the verification status
        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                //Getting the code sent by SMS
                String code = phoneAuthCredential.getSmsCode();

                //sometime the code is not detected automatically
                //in this case the code will be null
                //so user has to manually enter the code
                verifyComplete(code);

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                //Toast.makeText(VerifyPhoneActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                responseRecieved(false);
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                codeSent(s);
            }
        };


        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                String.valueOf(mobile[0]),
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);

        return false;
    }

    public void codeSent(String s) {

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        startTimer();
    }

    public void startTimer(){

    }

    @Override
    protected void onPostExecute(Boolean res) {
        super.onPostExecute(res);
        responseRecieved(res);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    public void responseRecieved(Boolean res){

    }

    public void verifyComplete(String code){

    }
}
