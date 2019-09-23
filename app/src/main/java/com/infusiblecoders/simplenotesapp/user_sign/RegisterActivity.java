package com.infusiblecoders.simplenotesapp.user_sign;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.infusiblecoders.simplenotesapp.MainActivity;
import com.infusiblecoders.simplenotesapp.R;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private Button btnReg;
    private TextInputLayout inName, inEmail, inPass;

    private FirebaseAuth fAuth;
    private DatabaseReference fUsersDatabase;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnReg = (Button) findViewById(R.id.btn_reg);
        inName = (TextInputLayout) findViewById(R.id.input_reg_name);
        inEmail = (TextInputLayout) findViewById(R.id.input_reg_email);
        inPass = (TextInputLayout) findViewById(R.id.input_reg_pass);

        fAuth = FirebaseAuth.getInstance();
        fUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uname = inName.getEditText().getText().toString().trim();
                String uemail = inEmail.getEditText().getText().toString().trim();
                String upass = inPass.getEditText().getText().toString().trim();

                registerUser(uname, uemail, upass);

            }
        });

    }

    private void registerUser(final String name, String email, String password){

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Processing your request, please wait...");

        progressDialog.show();
//
//        HashMap<String , Object > userMap = new HashMap<>();
//        userMap.put("u")
        fAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    fUsersDatabase.child(fAuth.getCurrentUser().getUid())
                            .child("basic").child("name").setValue(name)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){

                                progressDialog.dismiss();

                                Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(mainIntent);
                                finish();
                                Toast.makeText(RegisterActivity.this, "User created!", Toast.LENGTH_SHORT).show();

                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(RegisterActivity.this, "ERROR : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                Log.i("dxd" , task.getException().getMessage());
                            }

                        }
                    });

                } else {

                    progressDialog.dismiss();

                    Toast.makeText(RegisterActivity.this, "ERROR: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }

        return true;
    }

}
