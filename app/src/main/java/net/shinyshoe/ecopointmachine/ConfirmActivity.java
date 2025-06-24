package net.shinyshoe.ecopointmachine;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;

public class ConfirmActivity extends AppCompatActivity {


    Button yesBtn, noBtn;
    ImageView photoPreview;
    String imagePath;
    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            getWindow().setAttributes(lp);
        }

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_confirm);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        yesBtn = findViewById(R.id.yes_btn);
        noBtn = findViewById(R.id.no_btn);
        photoPreview = findViewById(R.id.image_preview);
        frameLayout = findViewById(R.id.frame_image_preview);

        imagePath = getIntent().getStringExtra("imageUri");
        if (imagePath != null) {
            File imageFile = new File(Uri.parse(imagePath).getPath());
            Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            photoPreview.setImageBitmap(bitmap);
        }

        yesBtn.setOnClickListener(v -> {
//            Log.d("API", "PARSING SAMPAH: ");
//            String sampahId1 = APIController.getSampahId(APIController.postSampah("kaca", "420", 69));
//            String sampahId2 = APIController.getSampahId(APIController.postSampah("plastik", "9", 10));
//            Log.d("API", "SAMPAH 1: " + sampahId1);
//            Log.d("API", "SAMPAH 2: " + sampahId2);
//            APIController.ensureRegistered("MachineA");
//            String machineID = APIController.getMesinIdByName("MachineA");
//            Log.d("API", "MACHINE ID: " + machineID);
//            String[] sampahList = {sampahId1, sampahId2};
//            String code = APIController.getKodeVerifikasi(APIController.postPermintaan(machineID, sampahList));
            String code = "87142";
            Log.d("API", "CODE: " + code);

            Animation fadeShrink = AnimationUtils.loadAnimation(ConfirmActivity.this, R.anim.fade_shrink);
            frameLayout.startAnimation(fadeShrink);
            yesBtn.startAnimation(fadeShrink);
            noBtn.startAnimation(fadeShrink);

            fadeShrink.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
                    // Optional: hide the FrameLayout
                    frameLayout.setVisibility(View.INVISIBLE);
                    yesBtn.setVisibility(View.INVISIBLE);
                    noBtn.setVisibility(View.INVISIBLE);

                    Intent intent = new Intent(ConfirmActivity.this, ResultActivity.class);
                    intent.putExtra("CODE", code);
                    startActivity(intent);

                    // Optional: remove default transition
                    overridePendingTransition(0, 0);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {}
            });
        });






        noBtn.setOnClickListener(v -> {
            Animation fadeShrink = AnimationUtils.loadAnimation(ConfirmActivity.this, R.anim.fade_shrink);
            frameLayout.startAnimation(fadeShrink);
            yesBtn.startAnimation(fadeShrink);
            noBtn.startAnimation(fadeShrink);

            fadeShrink.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
                    // Optional: hide the FrameLayout
                    frameLayout.setVisibility(View.INVISIBLE);
                    yesBtn.setVisibility(View.INVISIBLE);
                    noBtn.setVisibility(View.INVISIBLE);

                    finish();

                    // Optional: remove default transition
                    overridePendingTransition(0, 0);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {}
            });
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        frameLayout.setVisibility(View.VISIBLE);
        yesBtn.setVisibility(View.VISIBLE);
        noBtn.setVisibility(View.VISIBLE);
        Animation growFade = AnimationUtils.loadAnimation(this, R.anim.fade_grow);
        frameLayout.startAnimation(growFade);
        yesBtn.startAnimation(growFade);
        noBtn.startAnimation(growFade);
    }
}