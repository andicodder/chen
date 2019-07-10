package com.lilliemountain.chen;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.lilliemountain.chen.pojo.Keys;

import java.text.SimpleDateFormat;
import java.util.Date;


import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;


public class QRCodeActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    String emailAddress="";
    ImageView imageView;
    TextView date;
    ProgressBar progressBar2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        db=FirebaseDatabase.getInstance();
        mAuth=FirebaseAuth.getInstance();
        imageView=findViewById(R.id.qrcode);
        progressBar2=findViewById(R.id.progressBar2);
        date=findViewById(R.id.date);

        emailAddress=mAuth.getCurrentUser().getEmail();
        emailAddress=emailAddress.replace("@","at").replace(".","dot");
        db.getReference("lilliemountain-college").child("keys").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds :
                        dataSnapshot.getChildren()) {
                    Keys keys = ds.getValue(Keys.class);
                    String todayStr=new SimpleDateFormat("dd MM yyyy").format(new Date());
                    if(keys.getDate().equals(todayStr))
                    {
                        String key=keys.getKey();
                        AESUtils aesUtils=new AESUtils(key.getBytes());
                        try {
                            String encrypt=aesUtils.encrypt(emailAddress);
                            Bitmap encryptBM=encodeAsBitmap(encrypt);
                            imageView.setImageBitmap(encryptBM);
                            date.setText("Date :"+keys.getDate());
                            progressBar2.setVisibility(View.GONE);
                            findViewById(R.id.attendance).setVisibility(View.VISIBLE);
                            findViewById(R.id.idcard).setVisibility(View.VISIBLE);
                            findViewById(R.id.textView).setVisibility(View.VISIBLE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        findViewById(R.id.attendance).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QRCodeActivity.this,AttendanceActivity.class).putExtra("emailAddress",emailAddress));
            }
        });
        findViewById(R.id.idcard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QRCodeActivity.this,ProfileActivity.class).putExtra("emailAddress",emailAddress));
            }
        });
        progressBar2.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chen, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.developer:
                startActivity(new Intent(QRCodeActivity.this,AboutDeveloperActivity.class));
                return true;
            case R.id.logoff:
                mAuth.signOut();
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, 512, 512, null);
        } catch (IllegalArgumentException iae) {
            Log.e( "encodeAsBitmap: ",iae.getMessage() );
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, 512, 0, 0, w, h);
        return bitmap;
    }

}
