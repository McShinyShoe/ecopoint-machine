package net.shinyshoe.ecopointmachine;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.Random;

public class ResultActivity extends AppCompatActivity {
    private ImageView qrImageView;
    private Button backButton;

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

        setContentView(R.layout.activity_result);

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

        TextView codeTextView = findViewById(R.id.result_code_text);
        qrImageView = findViewById(R.id.result_qr);
        backButton = findViewById(R.id.backButton);

        Intent intent = getIntent();
        String code = intent.getStringExtra("CODE");

        codeTextView.setText(getString(R.string.result_code_replacement, code));

        generateQrCode("https://ecopoint.kevin-andreas.com/scan-qr?code=" + code);

        backButton.setOnClickListener(v -> {
            Animation fadeShrink = AnimationUtils.loadAnimation(ResultActivity.this, R.anim.fade_shrink);
            backButton.startAnimation(fadeShrink);

            fadeShrink.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
                    // Optional: hide the FrameLayout
                    backButton.setVisibility(View.INVISIBLE);

                    Intent intent = new Intent(ResultActivity.this, MainActivity.class);
                    startActivity(intent);

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
        qrImageView.setVisibility(View.VISIBLE);
        backButton.setVisibility(View.VISIBLE);
        Animation growFade = AnimationUtils.loadAnimation(this, R.anim.fade_grow);
        qrImageView.startAnimation(growFade);
        backButton.startAnimation(growFade);
    }

    private String generateSixDigitCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // 100000 to 999999
        return String.valueOf(code);
    }

    private void generateQrCode(String text) {
        try {
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.encodeBitmap(text, BarcodeFormat.QR_CODE, 400, 400);
            qrImageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void goBackToHome(android.view.View view) {
        finishAffinity(); // closes all previous activities
        startActivity(new Intent(this, MainActivity.class));
    }
}