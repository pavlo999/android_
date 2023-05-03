package com.example.sim.modals;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sim.BaseActivity;
import com.example.sim.MainActivity;
import com.example.sim.R;
import com.example.sim.application.HomeApplication;
import com.example.sim.service.CategoryNetwork;
import com.example.sim.utils.CommonUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeleteConfirmation extends BaseActivity {

    private Button ok_btn;
    private Button cancel_btn;
    private TextView textView;
    private Intent mainActivity;
    private String name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_confirmation);
        Bundle arguments = getIntent().getExtras();

        name = arguments.get("name").toString();

        ok_btn = findViewById(R.id.btn_ok);
        cancel_btn = findViewById(R.id.btn_calcel);
        textView = findViewById(R.id.message);
        textView.setText("Видалити категорію \""+name+"\" ?");

        //Додаванна Активіті на яку перейду
        mainActivity = new Intent(this, MainActivity.class);
        mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (arguments != null) {
                    int id  =Integer.parseInt(arguments.get("itemId").toString());
                    requestServerDeleteCategory(id);
                }
                else
                    Toast.makeText(HomeApplication.getAppContext(),"Ідентифікатор елемента не знайдено ",Toast.LENGTH_LONG).show();
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(mainActivity);
                finish();
            }
        });
    }

    void requestServerDeleteCategory(int id) {
        CommonUtils.showLoading();
        CategoryNetwork
                .getInstance()
                .getJsonApi()
                .delete(id)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response)  {
                        if (response.isSuccessful())
                        {
                            Toast.makeText(HomeApplication.getAppContext(),"Категорію \""+name+"\" видалено успішно ",Toast.LENGTH_LONG).show();
                            System.out.println("----Category delete successfuly");
                            startActivity(mainActivity);
                            finish();
                            CommonUtils.hideLoading();
                        }
                        else{
                            System.out.println("Server erroe");
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        System.out.println("----Category delete Error");
                        CommonUtils.hideLoading();
                    }
                });
    }
}