package com.zaki.ecommerce_white_label_template2.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.zaki.ecommerce_white_label_template2.R;
import com.zaki.ecommerce_white_label_template2.Utils.Constant;
import com.zaki.ecommerce_white_label_template2.Utils.MySingleton;
import com.zaki.ecommerce_white_label_template2.Utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    String signUpURL = Constant.BASE_URL + "auth/customer";
    EditText fullNameEditTxt,emailEditTxt, passwordEditTxt;
    Button signUpBtn;
    TextView loginInTxt;
    RelativeLayout continueWithGoogleRLBTN;
    private boolean isPasswordVisible = false;
    Dialog progressBarDialog;
    SessionManager sessionManager;
    String storeId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE){
            EdgeToEdge.enable(this);
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.black));
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        sessionManager = new SessionManager(SignUpActivity.this);
        storeId = sessionManager.getStoreId();

        fullNameEditTxt = findViewById(R.id.fullNameEditTxt);
        emailEditTxt = findViewById(R.id.emailEditTxt);
        passwordEditTxt = findViewById(R.id.passwordEditTxt);
        signUpBtn = findViewById(R.id.signUpBtn);
        loginInTxt = findViewById(R.id.loginTxt);
        continueWithGoogleRLBTN = findViewById(R.id.continueWithGoogleRLBTN);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBarDialog = new Dialog(SignUpActivity.this);
                progressBarDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                progressBarDialog.setContentView(R.layout.progress_bar_dialog);
                progressBarDialog.setCancelable(false);
                progressBarDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                progressBarDialog.getWindow().setGravity(Gravity.CENTER); // Center the dialog
                progressBarDialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT); // Adjust the size
                progressBarDialog.show();
                checkValidations();            }
        });
        continueWithGoogleRLBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidations();
            }
        });
        loginInTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });
        passwordEditTxt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Check if the user clicked on the drawableEnd (eye icon)
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getX() >= passwordEditTxt.getWidth() - passwordEditTxt.getCompoundDrawables()[2].getBounds().width()) {
                        // Toggle password visibility when clicked on the eye icon
                        togglePasswordVisibility();
                        return true; // Consume the touch event
                    }
                }
                return false; // Don't consume other touch events
            }
        });
    }
    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            // Set password to hidden
            passwordEditTxt.setTransformationMethod(PasswordTransformationMethod.getInstance());
            passwordEditTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eye_close, 0); // Change icon to "eye_open"
        } else {
            // Set password to visible
            passwordEditTxt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            passwordEditTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eye_open, 0); // Change icon to "eye_closed"
        }

        // Move the cursor to the end of the EditText after changing input type
        passwordEditTxt.setSelection(passwordEditTxt.getText().length());

        // Toggle the state of visibility
        isPasswordVisible = !isPasswordVisible;
    }
    private void checkValidations() {
        if (fullNameEditTxt.getText().toString().isEmpty()) {
            fullNameEditTxt.setError("Email is required");
            progressBarDialog.dismiss();
            return;
        }
        if (emailEditTxt.getText().toString().isEmpty()) {
            emailEditTxt.setError("Email is required");
            progressBarDialog.dismiss();
            return;
        }
        if (passwordEditTxt.getText().toString().isEmpty()) {
            passwordEditTxt.setError("Password is required");
            progressBarDialog.dismiss();
            return;
        }
        signUpUser();
    }
    private void signUpUser() {
        String fullName,password,email;

        fullName = fullNameEditTxt.getText().toString().trim();
        password = passwordEditTxt.getText().toString().trim();
        email = emailEditTxt.getText().toString().trim();

        JSONObject userOBJ = new JSONObject();
        try {
            userOBJ.put("fullName",fullName);
            userOBJ.put("password",password);
            userOBJ.put("email",email);
            userOBJ.put("storeId",storeId);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, signUpURL, userOBJ,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String message = response.getString("message");
                            Toast.makeText(SignUpActivity.this, message, Toast.LENGTH_SHORT).show();
                            Toast.makeText(SignUpActivity.this, "Please Login to continue", Toast.LENGTH_SHORT).show();
                            progressBarDialog.dismiss();
                            onBackPressed();
                        } catch (JSONException e) {
                            progressBarDialog.dismiss();
                            Log.e("Exam Catch error", "Error parsing JSON: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBarDialog.dismiss();
                        String errorMessage = "Error: " + error.toString();
                        if (error.networkResponse != null) {
                            try {
                                // Parse the error response
                                String jsonError = new String(error.networkResponse.data);
                                JSONObject jsonObject = new JSONObject(jsonError);
                                String message = jsonObject.optString("message", "Unknown error");
                                // Now you can use the message
                                Toast.makeText(SignUpActivity.this, message, Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        Log.e("ExamListError", errorMessage);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }
}