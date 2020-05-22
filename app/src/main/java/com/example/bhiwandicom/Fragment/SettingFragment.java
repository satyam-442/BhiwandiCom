package com.example.bhiwandicom.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bhiwandicom.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingFragment extends Fragment {


    FirebaseAuth mAuth;
    DatabaseReference userRef;
    String currentUserId;
    ProgressDialog loadingBar;
    TextInputLayout settingName, settingEmail, settingPhone, settingGender, settingAge;
    Button updateAccountCredentials;
    CircleImageView profileImage;
    TextView profileName, profileEmail;
    Toolbar toolbar;

    public SettingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        toolbar = getActivity().findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Setting");

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);
        loadingBar = new ProgressDialog(getActivity());

        settingName = view.findViewById(R.id.settingName);
        settingEmail = view.findViewById(R.id.settingEmail);
        settingPhone = view.findViewById(R.id.settingPhone);
        settingGender = view.findViewById(R.id.settingGender);
        settingAge = view.findViewById(R.id.settingAge);

        profileImage = view.findViewById(R.id.profileImage);
        profileName = view.findViewById(R.id.profileName);
        profileEmail = view.findViewById(R.id.profileEmail);

        updateAccountCredentials = view.findViewById(R.id.updateAccountCredentials);
        updateAccountCredentials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateAccountInfo();
            }
        });

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String userNAME = dataSnapshot.child("name").getValue().toString();
                    String userAGE = dataSnapshot.child("age").getValue().toString();
                    String userGENDER = dataSnapshot.child("gender").getValue().toString();
                    String userCONTACT = dataSnapshot.child("phone").getValue().toString();
                    String userEMAIL = dataSnapshot.child("email").getValue().toString();

                    settingName.getEditText().setText(userNAME);
                    settingPhone.getEditText().setText(userCONTACT);
                    settingAge.getEditText().setText(userAGE);
                    settingGender.getEditText().setText(userGENDER);
                    settingEmail.getEditText().setText(userEMAIL);

                    profileEmail.setText(userEMAIL);
                    profileName.setText(userNAME);

                    final String image = dataSnapshot.child("image").getValue().toString();
                    if (!image.equals("default")) {
                        Picasso.with(getActivity()).load(image).placeholder(R.drawable.default_avatar).into(profileImage);
                        Picasso.with(getActivity()).load(image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.default_avatar).into(profileImage, new Callback() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onError() {
                                Picasso.with(getActivity()).load(image).placeholder(R.drawable.default_avatar).into(profileImage);
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        return view;
    }

    private void ValidateAccountInfo() {
        String username = settingName.getEditText().getText().toString();
        String useremail = settingEmail.getEditText().getText().toString();
        String usercontact = settingPhone.getEditText().getText().toString();
        String usergender = settingGender.getEditText().getText().toString();
        String userage = settingAge.getEditText().getText().toString();

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(getActivity(), "Enter The Full Name", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(useremail)) {
            Toast.makeText(getActivity(), "Enter The E-mail", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(usercontact)) {
            Toast.makeText(getActivity(), "Enter The Contact no.", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(usergender)) {
            Toast.makeText(getActivity(), "Enter The Gender", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(userage)) {
            Toast.makeText(getActivity(), "Enter The DOB", Toast.LENGTH_SHORT).show();
        } else {
            UpdateAccountInfo(username, useremail, usercontact, usergender, userage);
        }
    }

    private void UpdateAccountInfo(String username, String useremail, String usercontact, String usergender, String userage) {
        HashMap<String, Object> studentMap = new HashMap<String, Object>();
        studentMap.put("name", username);
        studentMap.put("phone", usercontact);
        studentMap.put("age", userage);
        studentMap.put("gender", usergender);
        studentMap.put("email", useremail);
        userRef.updateChildren(studentMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    /*SendUserToMainActivity();*/
                    Toast.makeText(getActivity(), "Updated Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Error Occurred: Try again...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
