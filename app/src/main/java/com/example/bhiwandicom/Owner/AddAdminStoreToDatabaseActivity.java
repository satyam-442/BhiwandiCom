package com.example.bhiwandicom.Owner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.bhiwandicom.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddAdminStoreToDatabaseActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference storeRef,adminRef;
    String currentUserId, gender;
    ProgressDialog loadingBar;
    TextInputLayout registerOwnerName, registerOwnerPhone, registerShopName, registerShopAddress, registerPassword;
    EditText selectFromTime, selectToTime;
    TextView tapFromDailog,tapToDailog;
    int fromHour, fromMinute, toHour, toMinute;

    ImageView setupProfileImage;
    Uri imageUri;
    StorageReference storagePicRef;
    String myUrl = "";
    String checker = "";
    StorageTask uploadTask;

    Button setupSelectImage, setupUploadImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_admin_store_to_database);

        storeRef = FirebaseDatabase.getInstance().getReference().child("Store");
        adminRef = FirebaseDatabase.getInstance().getReference().child("Admins");

        storagePicRef = FirebaseStorage.getInstance().getReference().child("Store Logos");
        loadingBar = new ProgressDialog(this);

        registerOwnerName = findViewById(R.id.registerOwnerName);
        registerOwnerPhone = findViewById(R.id.registerOwnerPhone);
        registerShopName = findViewById(R.id.registerShopName);
        registerShopAddress = findViewById(R.id.registerShopAddress);
        registerPassword = findViewById(R.id.registerPassword);

        tapFromDailog = findViewById(R.id.tapFromDailog);
        selectFromTime = findViewById(R.id.selectFromTime);
        tapFromDailog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        AddAdminStoreToDatabaseActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                fromHour = hourOfDay;
                                fromMinute = minute;
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(0,0,0,fromHour, fromMinute);
                                //selectFromTime.setText(DateFormat.format("hh:mm aa"));
                                android.text.format.DateFormat df = new android.text.format.DateFormat();
                                selectFromTime.setText(df.format("hh:mm aa",calendar));
                            }
                        },12,0,false
                );
                timePickerDialog.updateTime(fromHour,fromMinute);
                timePickerDialog.show();
            }
        });

        tapToDailog = findViewById(R.id.tapToDailog);
        selectToTime = findViewById(R.id.selectToTime);
        tapToDailog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        AddAdminStoreToDatabaseActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                toHour = hourOfDay;
                                toMinute = minute;
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(0,0,0,toHour, toMinute);
                                //selectFromTime.setText(DateFormat.format("hh:mm aa"));
                                android.text.format.DateFormat df = new android.text.format.DateFormat();
                                selectToTime.setText(df.format("hh:mm aa",calendar));
                            }
                        },12,0,false
                );
                timePickerDialog.updateTime(toHour,toMinute);
                timePickerDialog.show();
            }
        });

        setupProfileImage = findViewById(R.id.setupProfileImage);
        setupSelectImage = findViewById(R.id.setupSelectImage);
        //CODE FOR SELECTION OF IMAGE
        setupSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity(imageUri)
                        .setAspectRatio(1, 1)
                        .start(AddAdminStoreToDatabaseActivity.this);
            }
        });

        setupUploadImage = findViewById(R.id.setupUploadImage);
        setupUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveDetailsToDB();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK && data != null)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            setupProfileImage.setImageURI(imageUri);
        }
        else
        {
            Toast.makeText(this, "Error! Try again.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AddAdminStoreToDatabaseActivity.this,AddAdminStoreToDatabaseActivity.class));
            finish();
        }
    }

    private void SaveDetailsToDB() {
        final String name = registerOwnerName.getEditText().getText().toString();
        final String phone = registerOwnerPhone.getEditText().getText().toString();
        final String shopname = registerShopName.getEditText().getText().toString();
        final String shopaddress = registerShopAddress.getEditText().getText().toString();
        final String password = registerPassword.getEditText().getText().toString();
        final String fromTime = selectFromTime.getText().toString();
        final String toTime = selectToTime.getText().toString();

        if (TextUtils.isEmpty(name))
        {
            Toast.makeText(this, "Name is empty.", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "Phone is empty.", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(shopname))
        {
            Toast.makeText(this, "Shop Name is empty.", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(shopaddress))
        {
            Toast.makeText(this, "Shop Address is empty.", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Password is empty.", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(fromTime))
        {
            Toast.makeText(this, "Select from time.", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(toTime))
        {
            Toast.makeText(this, "Select to time.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setMessage("please wait.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            if (imageUri != null)
            {
                final StorageReference fileref = storagePicRef.child(shopname + ".jpg");
                uploadTask = fileref.putFile(imageUri);
                uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                    {
                        if (!task.isSuccessful())
                        {
                            throw task.getException();
                        }
                        return fileref.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task)
                    {
                        if (task.isSuccessful())
                        {
                            Uri downloadUrl = task.getResult();
                            myUrl = downloadUrl.toString();

                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Store").child(shopname);
                            HashMap<String, Object> userMapImg = new HashMap<String, Object>();
                            userMapImg.put("image",myUrl);
                            ref.updateChildren(userMapImg);
                        }
                    }
                });
            }
            else
            {
                Toast.makeText(this, "No image selected!", Toast.LENGTH_SHORT).show();
            }

            HashMap<String, Object> userMap = new HashMap<String, Object>();
            userMap.put("OwnerName",name);
            userMap.put("OwnerPhone",phone);
            userMap.put("ShopName",shopname);
            userMap.put("ShopAddress",shopaddress);
            userMap.put("fromTime",fromTime);
            userMap.put("toTime",toTime);
            userMap.put("Password",password);
            //userMap.put("uid",currentUserId);
            /*userMap.put("image",myUrl);*/
            storeRef.child(shopname).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    if (task.isSuccessful()) {
                        Toast.makeText(AddAdminStoreToDatabaseActivity.this, "Data Updated!", Toast.LENGTH_SHORT).show();
                        /*startActivity(new Intent(AddAdminStoreToDatabaseActivity.this,AddAdminStoreToDatabaseActivity.class));
                        finish();
                        loadingBar.dismiss();
                    }
                    else
                    {
                        String msg = task.getException().getMessage();
                        Toast.makeText(AddAdminStoreToDatabaseActivity.this, "Error! " + msg, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }*/
                        HashMap<String, Object> userMap = new HashMap<String, Object>();
                        userMap.put("Name",name);
                        userMap.put("Phone",phone);
                        userMap.put("Password",password);
                        //userMap.put("uid",currentUserId);
                        /*userMap.put("image",myUrl);*/
                        adminRef.child(phone).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task)
                            {
                                if (task.isSuccessful())
                                {
                                    Toast.makeText(AddAdminStoreToDatabaseActivity.this, "Data Updated!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(AddAdminStoreToDatabaseActivity.this,AddAdminStoreToDatabaseActivity.class));
                                    finish();
                                    loadingBar.dismiss();
                                }
                                else
                                {
                                    String msg = task.getException().getMessage();
                                    Toast.makeText(AddAdminStoreToDatabaseActivity.this, "Error! " + msg, Toast.LENGTH_SHORT).show();
                                    loadingBar.dismiss();
                                }
                            }
                        });
                    }
                }
            });
        }
    }
}
