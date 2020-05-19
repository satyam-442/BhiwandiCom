package com.example.bhiwandicom;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class RegisterTwoActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    ProgressDialog loadingBar;
    ImageView backButton;
    Button next, login;
    TextView titleText;
    String gender, age, fullname, email, phone, password;
    EditText genderText, ageText;
    DatePicker picker;
    RadioGroup radioGroup;
    RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_two);

        mAuth = FirebaseAuth.getInstance();
        backButton = findViewById(R.id.signup_back_button);
        next = findViewById(R.id.signup_next_button);
        login = findViewById(R.id.signup_login_button);
        titleText = findViewById(R.id.signup_title_text);

        radioGroup = findViewById(R.id.genderGroup);
        fullname = getIntent().getStringExtra("fullname");
        email = getIntent().getStringExtra("email");
        phone = getIntent().getStringExtra("phone");
        password = getIntent().getStringExtra("password");

        genderText = findViewById(R.id.genderText);
        ageText = findViewById(R.id.ageText);

        picker = findViewById(R.id.ageDatePicker);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ageText.setText(picker.getDayOfMonth()+"/"+(picker.getMonth() + 1) + "/" + picker.getYear());
                Intent intent = new Intent(RegisterTwoActivity.this, RegisterThreeActivity.class);
                intent.putExtra("fullname",fullname);
                intent.putExtra("email",email);
                intent.putExtra("phone",phone);
                intent.putExtra("password",password);
                intent.putExtra("gender",genderText.getText().toString());
                intent.putExtra("age",ageText.getText().toString());
                Pair[] pairs = new Pair[4];
                pairs[0] = new Pair<View,String>(backButton,"transition_back_btn");
                pairs[1] = new Pair<View,String>(titleText,"transition_title_text");
                pairs[2] = new Pair<View,String>(next,"transition_next_btn");
                pairs[3] = new Pair<View,String>(login,"transition_login_btn");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RegisterTwoActivity.this,pairs);
                startActivity(intent,options.toBundle());
            }
        });
    }

    public void checkButtom(View view)
    {
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);

        //toast = new Toast(this);
        /*Toast.makeText(this, radioButton.getText(), Toast.LENGTH_SHORT).show();*/
        genderText.setText(radioButton.getText());
    }

    public void backToWelcome(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    public void CallNextRegisterLastScreen(View view) {
        Intent intent = new Intent(this, RegisterThreeActivity.class);

        Pair[] pairs = new Pair[4];
        pairs[0] = new Pair<View,String>(backButton,"transition_back_btn");
        pairs[1] = new Pair<View,String>(titleText,"transition_title_text");
        pairs[2] = new Pair<View,String>(next,"transition_next_btn");
        pairs[3] = new Pair<View,String>(login,"transition_login_btn");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RegisterTwoActivity.this,pairs);
        startActivity(intent,options.toBundle());
    }
}
