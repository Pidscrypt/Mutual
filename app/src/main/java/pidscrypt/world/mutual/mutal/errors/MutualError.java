package pidscrypt.world.mutual.mutal.errors;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

public class MutualError implements Error {

    private String msg = null;
    private Context cxt;

    public MutualError(){

    }
    public void setMessage(String msg) {
        this.msg = msg;
    }

    public String getMessage(){
        return this.msg;
    }

    @Override
    public void show() {

    }
}
