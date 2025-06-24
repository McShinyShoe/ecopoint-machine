package net.shinyshoe.ecopointmachine;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;

public class ConfirmActivity extends AppCompatActivity {


    Button yesBtn, noBtn;
    ImageView photoPreview;
    String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_confirm);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        yesBtn = findViewById(R.id.yes_btn);
        noBtn = findViewById(R.id.no_btn);
        photoPreview = findViewById(R.id.image_preview);

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
            Intent intent = new Intent(ConfirmActivity.this, ResultActivity.class);
            intent.putExtra("CODE", code);
            startActivity(intent);
        });

        noBtn.setOnClickListener(v -> finish());
    }
}