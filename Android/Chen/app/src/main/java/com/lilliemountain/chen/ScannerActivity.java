package com.lilliemountain.chen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lilliemountain.chen.pojo.Keys;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


import static android.Manifest.permission.CAMERA;

public class ScannerActivity extends AppCompatActivity implements  QRCodeReaderView.OnQRCodeReadListener {

    private static final int REQUEST_CAMERA = 1;
    private FirebaseDatabase db;
    private FirebaseAuth mAuth;
    String emailAddress="";
    String docPath;
    AESUtils aesUtils;
    String todayStr;
    private QRCodeReaderView qrCodeReaderView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        db=FirebaseDatabase.getInstance();
        mAuth=FirebaseAuth.getInstance();
        emailAddress=mAuth.getCurrentUser().getEmail();
        Chen.initializeInstance(this);
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.M) {
            if (checkPermission()) {
                qrCodeReaderView = findViewById(R.id.qrdecoderview);
                qrCodeReaderView.setOnQRCodeReadListener(ScannerActivity.this);
                // Use this function to enable/disable decoding
                qrCodeReaderView.setQRDecodingEnabled(true);

                // Use this function to change the autofocus interval (default is 5 secs)
                qrCodeReaderView.setAutofocusInterval(2000L);

                // Use this function to enable/disable Torch
                qrCodeReaderView.setTorchEnabled(true);

                // Use this function to set front camera preview
                qrCodeReaderView.setFrontCamera();

                // Use this function to set back camera preview
                qrCodeReaderView.setBackCamera();
            } else {
                requestPermission();
            }
        }
//        colRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if(task.isSuccessful())
//                {
//                    QuerySnapshot doc=task.getResult();
//                    for (DocumentSnapshot ds:
//                            doc.getDocuments()) {
//                        if(ds.exists())
//                        {
//                            SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy");
//                            SimpleDateFormat format2=new SimpleDateFormat("MMMM_yyyy");
//                            Timestamp ts= (Timestamp) ds.get("date");
//                            Date timestampDate=ts.toDate();
//                            docPath=format2.format(timestampDate).toLowerCase();
//                            Log.e( "docPath: ",docPath );
//                            todayStr= format.format(new Date());
//                            String serverDateStr=format.format(timestampDate);
//                            if(todayStr.equals(serverDateStr))
//                            {
//                                String key=ds.get("key").toString();
//                                 aesUtils=new AESUtils(key.getBytes());
//                            }
//                            else {
//                                Snackbar.make(qrCodeReaderView,"No QR Code Generated Yet",Snackbar.LENGTH_LONG).show();
//                            }
//                        }
//                    }
//                }
//            }
//        });
        db.getReference("lilliemountain-college").child("keys").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds :
                        dataSnapshot.getChildren()) {
                    Keys keys = ds.getValue(Keys.class);
                    SimpleDateFormat format=new SimpleDateFormat("dd MM yyyy");
//                    SimpleDateFormat format2=new SimpleDateFormat("MMMM_yyyy");

                    Date timestampDate;
                    try {
                        timestampDate = format.parse(keys.getDate());

                    todayStr= format.format(new Date());
                    String serverDateStr=format.format(timestampDate);
                    if(todayStr.equals(serverDateStr))
                    {
                        String key=keys.getKey();
                         aesUtils=new AESUtils(key.getBytes());
                    }
                    else {
                        Snackbar.make(qrCodeReaderView,"No QR Code Generated Yet",Snackbar.LENGTH_LONG).show();
                    }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private boolean checkPermission() {
        return ( ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA ) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CAMERA);
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted){
                        Toast.makeText(getApplicationContext(), "Permission Granted, Now you can access camera", Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(getApplicationContext(), "Permission Denied, You cannot access and camera", Toast.LENGTH_LONG).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(CAMERA)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{CAMERA},
                                                            REQUEST_CAMERA);
                                                }
                                            }
                                        });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(ScannerActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
    @Override
    protected void onResume() {
        super.onResume();
        qrCodeReaderView.startCamera();
    }


    @Override
    protected void onPause() {
        super.onPause();
        qrCodeReaderView.stopCamera();
    }
    void putAttendance(String emailAddress){
        emailAddress=emailAddress.replace("@","at");
        emailAddress=emailAddress.replace(".","dot");
        String monthyear=new SimpleDateFormat("MMMM_yyyy").format(new Date());
        db.getReference("lilliemountain-college").child("attendance").child(monthyear).child(emailAddress).child(todayStr).setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    new AlertDialog.Builder(ScannerActivity.this)
                                        .setTitle("Scan Result")
                                        .setMessage("Marked Attendance!")
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                findViewById(R.id.imageButton).setVisibility(View.GONE);
                                                dialog.dismiss();
                                            }
                                        })
                                        .create()
                                        .show();
                }
            }
        });
//        final DocumentReference docRef = db.collection("attendance").document(docPath).collection(str).document();
//        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
////TODO READ DATA FROM FIRESTORE
////                if(task.isSuccessful())
////                {
////                    Log.e("onComplete: ","isSuccessful" );
////                    Map<String,Object>result=new HashMap<>();
////                    if(task.getResult().get(docPath)!=null){
////                        result.putAll((Map<? extends String, ?>) task.getResult().get(docPath));
////                    }
////                    todayStr=todayStr.replace("\\/"," ");
////                    Date date = new Date();
////                    SimpleDateFormat formats=new SimpleDateFormat("dd MM yyyy");
////                    result.put(formats.format(date),true);
////                    docRef.update(result).addOnCompleteListener(new OnCompleteListener<Void>() {
////                        @Override
////                        public void onComplete(@NonNull Task<Void> task) {
////                            if(task.isSuccessful()){
////                                new AlertDialog.Builder(ScannerActivity.this)
////                                        .setTitle("Scan Result")
////                                        .setMessage("Marked Attendance!")
////                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
////                                            @Override
////                                            public void onClick(DialogInterface dialog, int which) {
////                                                Chen.getInstance().setValue("dheere_dheere","");
////                                                dialog.dismiss();
////                                            }
////                                        })
////                                        .create()
////                                        .show();
////                            }
////                        }
////                    });
////                }
//                if (task.isSuccessful()) {
//
//                }
//            }
//        });


    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {
            Log.d("QRCodeScanner", text);
            String encryptedText=text;
            try {
                final String decrypt=aesUtils.decrypt(encryptedText);
                Log.e("decrypt: ",decrypt);
                findViewById(R.id.imageButton).setVisibility(View.VISIBLE);
                findViewById(R.id.imageButton).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        putAttendance(decrypt);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
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
                startActivity(new Intent(ScannerActivity.this,AboutDeveloperActivity.class));
                return true;
            case R.id.logoff:
                mAuth.signOut();
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
