package com.zaki.ecommerce_white_label_template2.Fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.zaki.ecommerce_white_label_template2.Activities.AddressShowingInputActivity;
import com.zaki.ecommerce_white_label_template2.Activities.LoginActivity;
import com.zaki.ecommerce_white_label_template2.Activities.MainActivity;
import com.zaki.ecommerce_white_label_template2.Activities.MyOrdersActivity;
import com.zaki.ecommerce_white_label_template2.R;
import com.zaki.ecommerce_white_label_template2.Utils.SessionManager;

public class ProfileFragment extends Fragment {
    ImageView editNameBtn;
    LinearLayout myOrdersLL,addressLL, logoutLL;
    SessionManager sessionManager;
    String authToken;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        sessionManager = new SessionManager(getContext());
        authToken = sessionManager.getUserData().get("authToken");

        editNameBtn = view.findViewById(R.id.editNameBtn);

        editNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEditDialog();
            }
        });

        myOrdersLL = view.findViewById(R.id.myOrderLL);
        addressLL = view.findViewById(R.id.addressLL);
        logoutLL = view.findViewById(R.id.logoutLL);

        myOrdersLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), MyOrdersActivity.class));
            }
        });
        addressLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AddressShowingInputActivity.class));
            }
        });
        logoutLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.logout();
                startActivity(new Intent(getContext(), MainActivity.class));
            }
        });

        return view;
    }
    Dialog drawerDialog;
    ImageView crossBtn;
    Button saveBtn;
    TextInputLayout fullNameLayout, emailNameLayout, phoneLayout;
    EditText fullNameEditText, emailEditText, phoneEditText;
    @SuppressLint("ResourceAsColor")
    public void openEditDialog() {
        drawerDialog = new Dialog(ProfileFragment.this.getContext());
        drawerDialog.setContentView(R.layout.profile_edit_dialog);
        drawerDialog.setCancelable(true);

        crossBtn = drawerDialog.findViewById(R.id.crossBtn);

        saveBtn = drawerDialog.findViewById(R.id.saveBtn);
        fullNameLayout = drawerDialog.findViewById(R.id.fullNameLayout);
        emailNameLayout = drawerDialog.findViewById(R.id.emailNameLayout);
        phoneLayout = drawerDialog.findViewById(R.id.phoneLayout);

        fullNameEditText = drawerDialog.findViewById(R.id.fullNameEditText);
        emailEditText = drawerDialog.findViewById(R.id.emailEditText);
        phoneEditText = drawerDialog.findViewById(R.id.phoneEditText);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();
            }
        });

        crossBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerDialog.dismiss();
            }
        });

        fullNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.equals("")){
                    fullNameLayout.setError(null);
                }else {
                    fullNameLayout.setError("First Name Required");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.equals("")){
                    emailNameLayout.setError(null);
                }else {
                    emailNameLayout.setError("Email is Required");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        phoneEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.equals("")){
                    phoneLayout.setError(null);
                }else {
                    phoneLayout.setError("Phone is Required");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        drawerDialog.show();
        drawerDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        drawerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        drawerDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationDisplayRight;
        drawerDialog.getWindow().setGravity(Gravity.TOP);
        drawerDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            drawerDialog.getWindow().setStatusBarColor(R.color.white);
        }
    }

    private void checkValidation() {
        if (fullNameEditText.getText().toString().isEmpty()) {
            fullNameLayout.setError("Please enter your first name");
        }
        if (emailEditText.getText().toString().isEmpty()) {
            emailNameLayout.setError("Please enter your email");
        }
        if (phoneEditText.getText().toString().isEmpty()) {
            phoneLayout.setError("Please enter your phone number");
        } else {
            drawerDialog.dismiss();
        }
    }
}
