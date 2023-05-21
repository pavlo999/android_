package com.example.sim.account;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.sim.BaseActivity;
import com.example.sim.MainActivity;
import com.example.sim.R;
import com.example.sim.application.HomeApplication;
import com.example.sim.dto.account.LoginDTO;
import com.example.sim.dto.account.LoginResponse;
import com.example.sim.dto.account.ValidationRegisterDTO;
import com.example.sim.security.JwtSecurityService;
import com.example.sim.service.ApplicationNetwork;
import com.example.sim.utils.CommonUtils;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {

    private Button btnLogin;
    private TextInputLayout tlEmail;
    private TextInputLayout tlPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (HomeApplication.getInstance().isAuth())
        {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }



        tlEmail = findViewById(R.id.tfEmail);
        tlPassword = findViewById(R.id.tfPassword);
        btnLogin = findViewById(R.id.btnLogin);

        //Validation fields
        setupError();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!validation())
                    return;
                LoginDTO model = new LoginDTO();
                model.setEmail(tlEmail.getEditText().getText().toString());
                model.setPassword(tlPassword.getEditText().getText().toString());

                CommonUtils.showLoading();
                ApplicationNetwork.getInstance()
                        .getAccountJsonApi()
                        .login(model)
                        .enqueue(new Callback<LoginResponse>() {
                            @Override
                            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                                if(response.isSuccessful())
                                {
                                    String token = response.body().getToken();
                                    JwtSecurityService jwt = HomeApplication.getInstance();
                                    jwt.saveJwtToken(token);
//                                    Toast.makeText(LoginActivity.this,"Вхід вірний:"+token , Toast.LENGTH_LONG).show();
//
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else{
                                    try {
                                        String resp = response.errorBody().string();
                                        showErrorsServer(resp);
                                    }
                                    catch (IOException e)
                                    {
                                        Toast.makeText(LoginActivity.this,"Щось пішло не так" , Toast.LENGTH_LONG).show();
                                    }
                                }
                                CommonUtils.hideLoading();
                            }

                            @Override
                            public void onFailure(Call<LoginResponse> call, Throwable t) {
                                CommonUtils.hideLoading();

                            }
                        });
            }
        });
    }
    private void showErrorsServer(String json){
        Gson gson = new Gson();
        ValidationRegisterDTO result = gson.fromJson(json,ValidationRegisterDTO.class);
        String str ="";
        try {
            if (result.getErrors().getEmail()!=null)
            {
                for (String item:result.getErrors().getEmail())
                    str+=item;
                tlEmail.setError(str);
            }

            if (result.getErrors().getPassword()!=null)
            {
                str ="";
                for (String item:result.getErrors().getPassword())
                    str+=item;
                tlPassword.setError(str);
            }
        }
        catch (Exception er)
        {
                    Toast.makeText(this,"Данні вказано не вірно",Toast.LENGTH_SHORT).show();
        }
    }
    private void setupError() {
        tlEmail.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence text, int i, int i1, int i2) {
                if (text.length() <= 4 && text.toString().contains("@") ) {
                    tlEmail.setError(getString(R.string.email_required));
                } else {
                    tlEmail.setError("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        tlPassword.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence text, int i, int i1, int i2) {
                if (text.length() < 6  ) {
                    tlPassword.setError(getString(R.string.password_required));
                } else {
                    tlPassword.setError("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }
    private boolean validation() {
        boolean isValid=true;
        String email = tlEmail.getEditText().getText().toString();
        String password = tlPassword.getEditText().getText().toString();

        if(email.isEmpty() || password.isEmpty() )
            isValid = false;

        if(email.length() <= 4 || email.contains("@")==false) {
            tlEmail.setError(getString(R.string.email_required));
            isValid=false;
        } else if (password.length() < 6  ) {
            tlPassword.setError(getString(R.string.password_required));
            isValid=false;
        }

        return isValid;
    }
}