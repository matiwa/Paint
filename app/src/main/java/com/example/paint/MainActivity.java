package com.example.paint;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.graphics.Color;
import android.os.Bundle;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.kyanogen.signatureview.SignatureView;

import android.os.Environment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    int defaultColor;
    SignatureView signatureView;
    ImageButton imgClear,imgClearAll,imgSave,imgExit;
    View colorView;
    SeekBar seekBar,sred,sgreen,sblue;
    TextView txtPenSize,txtred,txtgreen,txtblue;

    private static String fileName,filename;
    File path=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/myPaintings");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeComponents();
        askPermission();
        SimpleDateFormat format=new
                SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String date=format.format(new Date());
        fileName=path+"/"+date+".png";
        filename=path+"/"+date+".jpg";
        if(!path.exists()){
            path.mkdirs();
        }

        defaultColor=ContextCompat.getColor(getApplicationContext(),R.color.
                colorPrimary);
    }

    private void openColorPicker() {

        signatureView.setPenColor(Color.rgb(Integer.parseInt(txtred.getText(
                ).toString()),
                Integer.parseInt(txtgreen.getText().toString()),
                Integer.parseInt(txtblue.getText().toString())));

        colorView.setBackgroundColor(Color.rgb(Integer.parseInt(txtred.getText().toString()),
                Integer.parseInt(txtgreen.getText().toString()),
                Integer.parseInt(txtblue.getText().toString())));
    }

    private void initializeComponents(){
        signatureView=findViewById(R.id.signature_view);
        seekBar=findViewById(R.id.penSize);
        txtPenSize=findViewById(R.id.txtPenSize);
        sred=findViewById(R.id.sred);
        txtred=findViewById(R.id.txtred);
        sgreen=findViewById(R.id.sgreen);
        txtgreen=findViewById(R.id.txtgreen);
        sblue=findViewById(R.id.sblue);
        txtblue=findViewById(R.id.txtblue);
        colorView=findViewById(R.id.colorView);
        openColorPicker();
        imgClear=findViewById(R.id.btnClear);
        imgClearAll=findViewById(R.id.btnClearAll);
        imgSave=findViewById(R.id.btnSave);
        imgExit=findViewById(R.id.btnExit);
        seekBar.setOnSeekBarChangeListener(new
        SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txtPenSize.setText(progress+"dp");
                signatureView.setPenSize(progress);
                seekBar.setMax(300);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sred.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txtred.setText(progress+"");
                openColorPicker();
                seekBar.setMax(255);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sgreen.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txtgreen.setText(progress+"");
                openColorPicker();
                seekBar.setMax(255);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sblue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txtblue.setText(progress+"");
                openColorPicker();
                seekBar.setMax(255);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        imgClear.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                txtred.setText("255");
                txtgreen.setText("255");
                txtblue.setText("255");

                signatureView.setPenColor(Color.rgb(Integer.parseInt(txtred.getText().toString()),
                        Integer.parseInt(txtgreen.getText().toString()),
                        Integer.parseInt(txtblue.getText().toString())));

                colorView.setBackgroundColor(Color.rgb(Integer.parseInt(txtred.getText().toString()),
                        Integer.parseInt(txtgreen.getText().toString()),
                        Integer.parseInt(txtblue.getText().toString())));
            }
        });
        imgClearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signatureView.clearCanvas();
            }
        });
        imgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!signatureView.isBitmapEmpty()){
                    try {
                        saveImage();
                    } catch (IOException e) {
                        e.printStackTrace();

                        Toast.makeText(getApplicationContext(),"Couldn't Save!",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        imgExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void saveImage() throws IOException {
        File file=new File(fileName);

        Bitmap bitmap=signatureView.getSignatureBitmap();

        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,0,bos);
        byte[]bitmapData=bos.toByteArray();

        FileOutputStream fos=new FileOutputStream(file);
        fos.write(bitmapData);
        fos.flush();
        fos.close();

        //jpg
        File file2=new File(filename);
        Bitmap bitmap2=signatureView.getSignatureBitmap();
        bitmap2.compress(Bitmap.CompressFormat.JPEG,0,bos);
        FileOutputStream fos2=new FileOutputStream(file2);
        fos2.write(bitmapData);
        fos2.flush();
        fos2.close();

        Toast.makeText(getApplicationContext(),"Painting Saved!",Toast.LENGTH_LONG).show();
    }

    private void askPermission(){

        Dexter.withContext(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        Toast.makeText(getApplicationContext(),"Granted!",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list,
                                                                   PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }
}