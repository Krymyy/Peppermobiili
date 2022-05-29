package pepper.mobile.peppermobileapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

public class ScannerActivity extends AppCompatActivity {

    TextView txt;
    CodeScanner codeScanner;
    CodeScannerView codeScannerView;
    Button gameBtn;
    String  playerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        Intent i = getIntent();

        playerName = i.getStringExtra("Player");

        gameBtn = (Button) findViewById(R.id.gameButton);
        txt = (TextView) findViewById(R.id.textView);
        codeScannerView = (CodeScannerView) findViewById(R.id.scannerView);
        codeScanner = new CodeScanner(this, codeScannerView);

        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txt.setText(result.getText());
                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestCamera();
    }

    private void requestCamera() {
        codeScanner.startPreview();

    gameBtn.setOnClickListener(v -> {
        Intent i = new Intent(getApplicationContext(), GameActivity.class);
        i.putExtra("Player",playerName);
        startActivity(i);
        });

    }
}

