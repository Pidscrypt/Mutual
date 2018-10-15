package pidscrypt.world.mutual.mutal;

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.annotation.SuppressLint;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import pidscrypt.world.mutual.mutal.api.MutualDateFormat;
import pidscrypt.world.mutual.mutal.media.ShadowLayout;

public class ImageViewActivity extends AppCompatActivity {

    private static final TimeInterpolator sDecelerator = new DecelerateInterpolator();
    private static final TimeInterpolator sAccelerator = new AccelerateInterpolator();
    private static final int ANIM_DURATION = 500;

    private BitmapDrawable bitmapDrawable;
    private ColorMatrix colorMatrix = new ColorMatrix();
    ColorDrawable mBackgroud;
    int mLeftDelta;
    int mTopDelta;
    float mWidthScale;
    float mHeightScale;
    private ImageView imageView;
    private FrameLayout topLevelLayout;
    private ShadowLayout mShadowLayout;
    private int mOriginalOrientation;
    private ScaleGestureDetector scaleGestureDetector;
    private Matrix matrix;
    Toolbar mToolbar;
    private int toolbarVisible = 1;
    private float translateBar = 0;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_landing,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);


        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.bringToFront();

        topLevelLayout = (FrameLayout) findViewById(R.id.toplevelLayout);
        imageView = (ImageView) findViewById(R.id.imageView);



        matrix = new Matrix();
        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());


        Bundle bundle = getIntent().getExtras();
        final int thumbnailTop = bundle.getInt("top");
        final int thumbnailLeft = bundle.getInt("left");
        final int thumbnailWidth = bundle.getInt("width");
        final int thumbnailHeight = bundle.getInt("height");
        final String fileLocation = bundle.getString("fileLocation");
        final long timeSent = bundle.getLong("timeSent");
        final String sentBy = bundle.getString("sentBy");
        mOriginalOrientation = bundle.getInt("orientation");

        mToolbar.setTitle(sentBy);
        mToolbar.setSubtitle(DateFormat.format(MutualDateFormat.SHORT,timeSent));

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.avatar_contact)
                .error(R.drawable.bg_outline_gray)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(this).setDefaultRequestOptions(requestOptions).load(fileLocation).thumbnail(0.5f).into(imageView);

        mBackgroud = new ColorDrawable(Color.BLACK);
        topLevelLayout.setBackground(mBackgroud);

        if(savedInstanceState == null){
            ViewTreeObserver observer = imageView.getViewTreeObserver();
            observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    imageView.getViewTreeObserver().removeOnPreDrawListener(this);

                    int[] screenLocation = new int[2];
                    imageView.getLocationOnScreen(screenLocation);
                    mLeftDelta = thumbnailLeft - screenLocation[0];
                    mTopDelta = thumbnailTop - screenLocation[1];

                    mWidthScale = (float) thumbnailWidth / imageView.getWidth();
                    mHeightScale = (float) thumbnailHeight / imageView.getHeight();

                    runEnterAnimation();

                    return true;
                }
            });
        }

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final float touchDownY = motionEvent.getY(), touchUp;
                float touchNewY;
                scaleGestureDetector.onTouchEvent(motionEvent);
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        showOptions();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        touchNewY = motionEvent.getY();
                        float diff = touchNewY - touchDownY;
                        imageView.setTranslationY(diff);
                        float alpha = diff == 0 ? 1: 1 / diff;
                        topLevelLayout.setAlpha(alpha);
                        break;
                    case MotionEvent.ACTION_UP:
                        touchUp = motionEvent.getY();
                        if((touchUp - touchDownY) > 80){
                            finish();
                        }else{
                            imageView.setTranslationY(0);
                            topLevelLayout.setAlpha(1);
                        }
                        break;
                    default:
                        break;
                }

                return false;
            }
        });
    }

    private void showOptions(){
        mToolbar.animate().setDuration(ANIM_DURATION).alpha(toolbarVisible).translationY(translateBar).setInterpolator(sDecelerator).withEndAction(new Runnable() {
            @Override
            public void run() {
                toolbarVisible = toolbarVisible == 1 ? 0 : 1;
                translateBar = translateBar == 0 ? -mToolbar.getBottom() : 0;
            }
        });
    }


    private String getPath(Uri uri) {
        String scheme = uri.getScheme();

        if ("file".equalsIgnoreCase(scheme)) return uri.getPath();
        else if ("content".equalsIgnoreCase(scheme)) return getFilePathFromMedia(uri);

        return "";
    }

    private String getFilePathFromMedia(Uri uri) {
        String data = "_data";
        String[] projection = { data };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndex(data);
        if (cursor.moveToFirst()) {
            String filePath = cursor.getString(column_index);
            cursor.close();
            return filePath;
        }
        return "";
    }

    private void runEnterAnimation() {
        final long duration = (long) (ANIM_DURATION);

        imageView.setPivotX(0);
        imageView.setPivotY(0);
        imageView.setScaleX(mWidthScale);
        imageView.setScaleY(mHeightScale);
        imageView.setTranslationX(mLeftDelta);
        imageView.setTranslationY(mTopDelta);

        imageView.animate().
                setDuration(duration).
                scaleX(1).
                scaleY(1).
                translationX(0).translationY(0).
                setInterpolator(sDecelerator).
                withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        showOptions();
                    }
                });

        ObjectAnimator bgAnim = ObjectAnimator.ofInt(mBackgroud, "alpha", 0, 255);
        bgAnim.setDuration(duration);
        bgAnim.start();
/*
        ObjectAnimator shadowAnim = ObjectAnimator.ofFloat(mShadowLayout, "shadowDepth",0);
        shadowAnim.setDuration(duration);
        shadowAnim.start();*/
    }

    public void runExitAnimation(final Runnable endAction){
        final long duration = (long) ANIM_DURATION;

        final boolean fadeOut;
        if(getResources().getConfiguration().orientation != mOriginalOrientation){
            imageView.setPivotX(imageView.getWidth() / 2);
            imageView.setPivotY(imageView.getHeight() / 2);

            mLeftDelta = 0;
            mTopDelta = 0;
            fadeOut = true;
        }else{
            fadeOut = false;
        }

        imageView.animate().
                setDuration(duration)
                .scaleX(mWidthScale).scaleY(mHeightScale).
                translationX(mLeftDelta).translationY(mTopDelta).
                withEndAction(endAction);
        if(fadeOut){
            imageView.animate().alpha(0);
        }

        ObjectAnimator bgAnim = ObjectAnimator.ofInt(mBackgroud, "alpha", 255, 0);
        bgAnim.setDuration(duration);
        bgAnim.start();
    }

    @Override
    public void onBackPressed() {
        runExitAnimation(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,0);
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float sf = detector.getScaleFactor();
            sf = Math.max(0.1f, Math.min(sf,0.5f));
            matrix.setScale(sf, sf);
            imageView.setImageMatrix(matrix);
            return true;
        }
    }
}
