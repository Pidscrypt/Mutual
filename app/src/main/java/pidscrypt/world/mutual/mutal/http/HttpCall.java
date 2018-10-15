package pidscrypt.world.mutual.mutal.http;

import java.util.HashMap;

/**
 * Created by PidScrypt on 2/3/2018.
 */

public class HttpCall {

    public static final int GET = 1;
    public static final int POST = 2;

    private String url;
    private int methodType;
    private HashMap<String,String> params = null;
    private boolean FOR_FILE = false;

    public void setFOR_FILE(boolean FOR_FILE) {
        this.FOR_FILE = FOR_FILE;
    }

    public boolean isFOR_FILE() {
        return FOR_FILE;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getMethodType() {
        return methodType;
    }

    public void setMethodType(int methodType) {
        this.methodType = methodType;
    }

    public HashMap<String, String> getParams() {
        return params;
    }

    public void setParams(HashMap<String, String> params) {
        this.params = params;
    }
}
