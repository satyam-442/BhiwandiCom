package com.example.bhiwandicom;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddNewProductActivity extends AppCompatActivity
{

    EditText productName, productDescription, productCost, productDiscount;
    String CategoryName, Descriptiion, Price, Pname, Discount, saveCurrentDate, saveCurrentTime;
    Button addProduct;
    ImageView productImage;
    static final int GalleryPick = 1;
    Uri ImageUri;
    String productRandomKey, downloadImageUrl;
    DatabaseReference productRef;
    StorageReference ProductImagesRef;

    ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);

        CategoryName = getIntent().getExtras().get("category").toString();
        ProductImagesRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        productRef = FirebaseDatabase.getInstance().getReference().child("Products");

        productName = (EditText) findViewById(R.id.product_name);
        productDescription = (EditText) findViewById(R.id.product_discription);
        productCost = (EditText) findViewById(R.id.product_cost);
        productDiscount = (EditText) findViewById(R.id.product_discount);

        productImage = (ImageView) findViewById(R.id.select_product_image);
        productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });

        addProduct = (Button) findViewById(R.id.addProductPhoneButton);
        addProduct.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ValidateProductData();
            }
        });

        loadingBar = new ProgressDialog(this);

    }

    private void OpenGallery()
    {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,GalleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GalleryPick && resultCode == RESULT_OK && data!=null)
        {
            ImageUri = data.getData();
            productImage.setImageURI(ImageUri);
        }
    }

    private void ValidateProductData()
    {
        Pname = productName.getText().toString();
        Descriptiion = productDescription.getText().toString();
        Price = productCost.getText().toString();
        //Discount = productDiscount.getText().toString();

        if (ImageUri == null)
        {
            Toast.makeText(this, "Image is not selected...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Pname) && TextUtils.isEmpty(Descriptiion) && TextUtils.isEmpty(Price))
        {
            Toast.makeText(this, "Field's are empty", Toast.LENGTH_SHORT).show();
        }
        else
        {
            StoreImageInformation();
        }

    }

    private void StoreImageInformation()
    {

        loadingBar.setMessage("please wait...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentDate + saveCurrentTime;
        final StorageReference filePath = ProductImagesRef.child(ImageUri.getLastPathSegment() + productRandomKey + ".jpg");
        final UploadTask uploadTask = filePath.putFile(ImageUri);
        uploadTask.addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                String msg = e.toString();
                Toast.makeText(AdminAddNewProductActivity.this, "Error occurred:" +msg, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
        {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                Toast.makeText(AdminAddNewProductActivity.this, "Image uploaded successfully...", Toast.LENGTH_SHORT).show();
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>()
                {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                    {
                        if (!task.isSuccessful())
                        {
                            throw task.getException();
                        }
                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task)
                    {
                        if (task.isSuccessful())
                        {
                            downloadImageUrl = task.getResult().toString();
                            Toast.makeText(AdminAddNewProductActivity.this, "Image saved to server", Toast.LENGTH_SHORT).show();
                            SaveProductInfoToDB();
                        }
                    }
                });
            }
        });
    }

    private void SaveProductInfoToDB()
    {
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid",productRandomKey);
        productMap.put("Date",saveCurrentDate);
        productMap.put("Time",saveCurrentTime);
        productMap.put("Description",Descriptiion);
        productMap.put("Category",CategoryName);
        productMap.put("image",downloadImageUrl);
        productMap.put("Price",Price);
        productMap.put("Pname",Pname);
        productRef.child(CategoryName).child(productRandomKey).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isSuccessful())
                {
                    loadingBar.dismiss();
                    Toast.makeText(AdminAddNewProductActivity.this, "Product has been uploaded to server", Toast.LENGTH_SHORT).show();
                    Intent newIn = new Intent(AdminAddNewProductActivity.this, AdminCategoryActivity.class);
                    startActivity(newIn);
                    finish();
                }
                else
                {
                    loadingBar.dismiss();
                    String msg = task.getException().getMessage();
                    Toast.makeText(AdminAddNewProductActivity.this, "Error:" + msg , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
