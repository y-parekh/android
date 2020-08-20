package com.yogenp.qrscanner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button scanBtn;
    final String TAG = "QRTest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scanBtn = findViewById(R.id.btnScan);
        scanBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        scanCode();
    }

    public void scanCode() {

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(CaptureAct.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scanning");
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        final IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                final String response = result.getContents().toLowerCase();
                final String action = getAction(response);
                builder.setMessage(response);
                builder.setTitle("Scanned Results");
                builder.setPositiveButton(action, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        parseAction(action, response, dialog);

                    }
                }).setNegativeButton("Scan Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        scanCode();

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                Toast.makeText(this, "No Results", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private String getAction(String response) {
        if (response.contains("http")) {
            return "Open Browser";
        } else if (response.startsWith("mailto") || response.startsWith("matmsg")) {
            return "Open Email";
        }
        return "Copy to Clipboard";
    }

    private void parseAction(String action, String response, DialogInterface dialog) {
        action = action.toLowerCase();
        if (action.contains("browser")) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(response));
            startActivity(browserIntent);
        } else if (action.contains("email")) {
            Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
            intent.setType("*/*");

            if (response.startsWith("mailto")) {
                String[] response_array = response.split(":");
                String[] email = {response_array[1]};
                intent.putExtra(Intent.EXTRA_EMAIL, email);
            } else {
                String[] response_array = response.split(";");
                String[] email = {response_array[0].split(":")[2]};
                String subject = response_array[1].split(":")[1];
                String text = response_array[2].split(":")[1];

                intent.putExtra(Intent.EXTRA_EMAIL, email);
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                intent.putExtra(Intent.EXTRA_TEXT, text);
            }

            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }

        } else {
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("CodeText", response);
            clipboardManager.setPrimaryClip(clipData);
            Toast.makeText(getApplicationContext(), "Copied to Clipboard", Toast.LENGTH_SHORT).show();
            dialog.cancel();
        }
    }
}