package com.example.sim.account;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sim.BaseActivity;
import com.example.sim.ChangeImageActivity;
import com.example.sim.MainActivity;
import com.example.sim.R;
import com.example.sim.dto.account.RegisterDTO;
import com.example.sim.dto.account.ValidationRegisterDTO;
import com.example.sim.service.ApplicationNetwork;
import com.example.sim.utils.CommonUtils;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends BaseActivity {

    private static int SELECT_IMAGE_RESULT = 300;
    private Uri uri = null;
    private ImageView IVPreviewImage;
    private TextInputLayout tfLastName;
    private TextInputLayout tfFirstName;
    private TextInputLayout tfEmail;
    private TextInputLayout tfPassword;
    private TextInputLayout tfConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        IVPreviewImage = findViewById(R.id.IVPreviewImage);

        tfLastName = findViewById(R.id.tfLastName);
        tfFirstName = findViewById(R.id.tfFirstName);
        tfEmail = findViewById(R.id.tfEmail);
        tfPassword = findViewById(R.id.tfPassword);
        tfConfirmPassword = findViewById(R.id.tfConfirmPassword);
    }

    public void onClickRegister(View view) {
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setLastName(tfLastName.getEditText().getText().toString());
        registerDTO.setFirstName(tfFirstName.getEditText().getText().toString());
        registerDTO.setEmail(tfEmail.getEditText().getText().toString());
        registerDTO.setPassword(tfPassword.getEditText().getText().toString());
        registerDTO.setConfirmPassword(tfConfirmPassword.getEditText().getText().toString());
        registerDTO.setImageBase64(uriGetBase64(uri));
        CommonUtils.showLoading();
        ApplicationNetwork.getInstance()
                .getAccountJsonApi()
                .register(registerDTO)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.isSuccessful())
                        {
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            try {
                                String resp = response.errorBody().string();
                                showErrorsServer(resp);
                            }catch(Exception ex) {
                                System.out.println("Error try");;
                            }
//                            Toast.makeText(RegisterActivity.this,"Не вірно вказані данні" , Toast.LENGTH_LONG).show();
                        }
                        CommonUtils.hideLoading();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        CommonUtils.hideLoading();
                    }
                });
    }

    private void showErrorsServer(String json){
        Gson gson = new Gson();
        ValidationRegisterDTO result = gson.fromJson(json,ValidationRegisterDTO.class);
        String str ="";
        if (result.getErrors().getEmail()!=null)
        {
            for (String item:result.getErrors().getEmail())
                str+=item;
            tfEmail.setError(str);
        }
        if (result.getErrors().getFirstName()!=null)
        {
            str ="";
            for (String item:result.getErrors().getFirstName())
                str+=item;
            tfFirstName.setError(str);
        }
        if (result.getErrors().getLastName()!=null)
        {
            str ="";
            for (String item:result.getErrors().getLastName())
                str+=item;
            tfLastName.setError(str);
        }

        if (result.getErrors().getPassword()!=null)
        {
            str ="";
            for (String item:result.getErrors().getPassword())
                str+=item;
            tfPassword.setError(str);
        }
        if (result.getErrors().getConfirmPassword()!=null)
        {
            str ="";
            for (String item:result.getErrors().getConfirmPassword())
                str+=item;
            tfConfirmPassword.setError(str);
        }
//        Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
    }
    private String uriGetBase64(Uri uri) {
        try {
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            byte[] byteArr = bytes.toByteArray();
            return Base64.encodeToString(byteArr, Base64.DEFAULT);

        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE_RESULT) {
            uri = (Uri) data.getParcelableExtra("croppedUri");
            IVPreviewImage.setImageURI(uri);
        }
    }
    //Вибір фото і її обрізання
    public void onClickSelectImage(View view) {
        Intent intent = new Intent(this, ChangeImageActivity.class);
        startActivityForResult(intent, SELECT_IMAGE_RESULT);
    }
}