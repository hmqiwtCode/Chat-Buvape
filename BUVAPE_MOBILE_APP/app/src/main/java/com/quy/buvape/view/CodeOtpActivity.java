package com.quy.buvape.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import  com.quy.buvape.R;

import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;

public class CodeOtpActivity extends AppCompatActivity {
    private EditText code1;
    private EditText code2;
    private EditText code3;
    private EditText code4;
    private EditText code5;
    private EditText code6;
    private Button btn_Confirm;
    private TextView txtPhone_P;

    private String mobile;

    private FirebaseAuth mAuth;
    private String mVerificationId = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_otp);
        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        mobile = intent.getStringExtra("mobile");
        sendVerificationCode(mobile);


        code1 = findViewById(R.id.code1);
        code2 = findViewById(R.id.code2);
        code3 = findViewById(R.id.code3);
        code4 = findViewById(R.id.code4);
        code5 = findViewById(R.id.code5);
        code6 = findViewById(R.id.code6);
        btn_Confirm = findViewById(R.id.btn_Confirm);
        txtPhone_P = findViewById(R.id.txtPhone_P);
        txtPhone_P.setText("+" + mobile);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(this.getResources().getColor(R.color.white));


        code1.setFocusable(true);

        code1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                code1.setFocusable(false);
                code2.setFocusable(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        code2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                code2.setFocusable(false);
                code3.setFocusable(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        code3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                code3.setFocusable(false);
                code4.setFocusable(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        code4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                code4.setFocusable(false);
                code5.setFocusable(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        code5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                code5.setFocusable(false);
                code6.setFocusable(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        code6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                code6.setFocusable(false);
                code1.setFocusable(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btn_Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (code1.getText().toString().trim().equals("") ||code2.getText().toString().trim().equals("") ||
                code3.getText().toString().trim().equals("") || code4.getText().toString().trim().equals("")
            || code5.getText().toString().trim().equals("") || code6.getText().toString().trim().equals("")){
                    Toasty.error(CodeOtpActivity.this, "Code error", Toast.LENGTH_SHORT, true).show();
                    return;
                }
                String code = code1.getText().toString() + code2.getText().toString() + code3.getText().toString()
                        + code4.getText().toString() + code5.getText().toString() + code6.getText().toString();
                verifyVerificationCode(code);
            }
        });
    }

    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+" + mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
//            if (code != null) {
//                editTextCode.setText(code);
//                //verifying the code
//                verifyVerificationCode(code);
//            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(CodeOtpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("LOG",e.getMessage());
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            //storing the verification id that is sent to the user
            Log.d("TAG", "onCodeSent:" + s);
            Log.d("TAG", "onCodeSent:" + forceResendingToken+"");
            mVerificationId = s;
        }
    };


    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = null;
        if (mVerificationId != null) {
            credential = PhoneAuthProvider.getCredential(mVerificationId, code);
            signInWithPhoneAuthCredential(credential);
        }
        //signing the user

    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(CodeOtpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //verification successful we will start the profile activity
                            Intent intent = new Intent(CodeOtpActivity.this, AddInfoUserActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.putExtra("mobile",mobile);
                            startActivity(intent);

                        } else {

                            //verification unsuccessful.. display an error message

                            String message = "Code wrong";
                            Toast.makeText(CodeOtpActivity.this, message, Toast.LENGTH_SHORT).show();


                        }
                    }
                });
    }
}