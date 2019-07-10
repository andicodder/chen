package com.lilliemountain.chen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lilliemountain.chen.pojo.Students;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileActivity extends AppCompatActivity {
    String emailAddress;
    FirebaseAuth mAuth;
    TextView email,name,rollno,grade_class,address,contact,guardianemail;
    EditText password;
    CircleImageView picture;
    String key;
    Students s;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
        picture=findViewById(R.id.picture);
        email=findViewById(R.id.email);
        rollno=findViewById(R.id.rollno);
        name=findViewById(R.id.fullname);
        grade_class=findViewById(R.id.gradeclass);
        address=findViewById(R.id.address);
        contact=findViewById(R.id.textView6);
        guardianemail=findViewById(R.id.guardianemail);
        password=findViewById(R.id.editText);
        mAuth=FirebaseAuth.getInstance();
        emailAddress=mAuth.getCurrentUser().getEmail();
        FirebaseDatabase.getInstance().getReference("lilliemountain-college").child("students").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds :
                        dataSnapshot.getChildren()) {
                     s=ds.getValue(Students.class);
                    if(s.getEmail().toLowerCase().equals(emailAddress.toLowerCase()))
                    {
                        key=ds.getKey();
                        email.setText(s.getEmail());
                        rollno.setText(s.getRollno());
                        name.setText(s.getName());
                        grade_class.setText(s.getGrade()+" "+s.get_class());
                        address.setText(s.getAddress());
                        contact.setText(s.getContact());
                        guardianemail.setText(s.getGuardianemail());
                        password.setText(s.getPassword());
                        Picasso.get().load(s.getStorage()).into(picture);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(password.getText().length()>5)
                {
                    s.setPassword(password.getText().toString());
                    mAuth.getCurrentUser().updatePassword(password.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            FirebaseDatabase.getInstance().getReference("lilliemountain-college").child("students").child(key).setValue(s).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(ProfileActivity.this, "Password Updated", Toast.LENGTH_SHORT).show();
                                    onBackPressed();
                                }
                            });
                        }
                    });
                }
                else
                    Snackbar.make(v,"Password length should be > 6.",Snackbar.LENGTH_LONG).show();

            }
        });
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { onBackPressed(); }});
    }
}
