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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.bhiwandicom.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class FeedbackFragment extends Fragment {

    Toolbar toolbar;
    FirebaseAuth mAuth;
    DatabaseReference UserRef,FeedbackRef;
    String currentUserId, result, feedbackId;
    CircleImageView imageVHead;
    EditText feedbackName, feedbackSpinnerValue, feedbackSpinnerOtherOption;
    Spinner selectOptions;
    Button sendButton;
    String options[] = {"-Select Options-","Material Problem","User Interface","Service Problem","Others"};
    ArrayAdapter<String> adapter;
    ProgressDialog loadingBar;

    public FeedbackFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feedback, container, false);

        toolbar = getActivity().findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Feedback");

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        UserRef= FirebaseDatabase.getInstance().getReference().child("Users");
        FeedbackRef= FirebaseDatabase.getInstance().getReference().child("Feedback").child("Users");

        //FEEDBACK VALIDATIONS
        feedbackName = view.findViewById(R.id.feedbackName);
        feedbackSpinnerValue = view.findViewById(R.id.feedbackSpinnerValue);
        feedbackSpinnerOtherOption = view.findViewById(R.id.feedbackSpinnerOtherOption);
        selectOptions = view.findViewById(R.id.selectOptions);
        loadingBar = new ProgressDialog(getActivity());

        sendButton = view.findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveFeedbackToDatabase();
            }
        });

        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,options);
        selectOptions.setAdapter(adapter);
        selectOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                switch (position)
                {
                    case 0:
                        result = "-Select Options-";
                        feedbackSpinnerValue.setText(result);
                        break;
                    case 1:
                        result = "Material Problem";
                        feedbackSpinnerValue.setText(result);
                        break;
                    case 2:
                        result = "User Interface";
                        feedbackSpinnerValue.setText(result);
                        break;
                    case 3:
                        result = "Service Problem";
                        feedbackSpinnerValue.setText(result);
                        break;
                    case 4:
                        result = "Others";
                        feedbackSpinnerValue.setText(result);
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent)
            { }
        });

        return view;
    }

    private void SaveFeedbackToDatabase()
    {
        String name = feedbackName.getText().toString();
        String spinnerValue = feedbackSpinnerValue.getText().toString();
        String suggestion = feedbackSpinnerOtherOption.getText().toString();

        final String saveCurrentTime, saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime = currentTime.format(calForTime.getTime());

        feedbackId = saveCurrentDate + saveCurrentTime;

        if (TextUtils.isEmpty(name) && TextUtils.isEmpty(spinnerValue) && TextUtils.isEmpty(suggestion))
        {
            Toast.makeText(getActivity(), "Field's are empty!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setMessage("please wait...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(false);
            HashMap<String,Object> feedbackMap = new HashMap<String,Object>();
            feedbackMap.put("fullname", name);
            feedbackMap.put("feedbackType" , spinnerValue);
            feedbackMap.put("feedbackSuggestion",suggestion);
            feedbackMap.put("uid",currentUserId);
            feedbackMap.put("fbId",feedbackId);
            feedbackMap.put("time",saveCurrentTime);
            feedbackMap.put("date",saveCurrentDate);
            /*feedbackMap.put("Contact",contact);
            feedbackMap.put("UID",uid);
            feedbackMap.put("Password",password);
            feedbackMap.put("image","default");*/
            FeedbackRef.child(spinnerValue).child(currentUserId).child(feedbackId).updateChildren(feedbackMap).addOnCompleteListener(new OnCompleteListener()
            {
                @Override
                public void onComplete(@NonNull Task task)
                {
                    if(task.isSuccessful())
                    {
                        //SendUserToMainActivity();
                        Toast.makeText(getActivity(), "Feedback updated Successfully...", Toast.LENGTH_LONG).show();
                        feedbackName.setText("");
                        feedbackSpinnerValue.setText("");
                        feedbackSpinnerOtherOption.setText("");
                        loadingBar.dismiss();
                    }
                    else
                    {
                        String message = task.getException().getMessage();
                        Toast.makeText(getActivity(), "Error Occurred:" + message, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            });
        }
    }
}
