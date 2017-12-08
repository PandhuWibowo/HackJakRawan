package com.example.root.hackjak;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.root.hackjak.FullRestAPI.LoginPojo;
import com.example.root.hackjak.activity.SignUpActivity;
import com.example.root.hackjak.session.SessionManager;

import com.example.root.hackjak.server.Server;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class MainActivity extends AppCompatActivity {
    public Button registSignUp, signIn;

    private ProgressDialog pDialog;
    //private RelativeLayout mRelativeLayout;

    SessionManager sessionManager;

    @BindView(R.id.edt_email)
    EditText tvEmail;
    @BindView(R.id.edt_password) EditText tvPassword;

    @OnClick(R.id.btn_signin) void login() {
        //membuat progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Loading ...");
        pDialog.show();

        //mengambil data dari edittext
        String email      = tvEmail.getText().toString();
        String password = tvPassword.getText().toString();

        String URL = "http://ikutevent.com/api-event/hackjak/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService api = retrofit.create(ApiService.class);
        RequestBody bodyEmail = RequestBody.create(MediaType.parse("multipart/form-data"), email);
        RequestBody bodyPassword = RequestBody.create(MediaType.parse("multipart/form-data"), password);
        Call<LoginPojo> call = api.loginuser(bodyEmail, bodyPassword);
        call.enqueue(new Callback<LoginPojo>() {
            @Override
            public void onResponse(Call<LoginPojo> call, Response<LoginPojo> response) {

                pDialog.dismiss();
                LoginPojo responLogin = response.body();

                if(responLogin != null){
                    if(responLogin.getSuccess() != 1){
//                        Snackbar snackbar = Snackbar.make(mRelativeLayout, responLogin.message, Snackbar.LENGTH_LONG);
//
//                        // Display the Snackbar
//                        snackbar.show();
                        Toast.makeText(getApplicationContext(), responLogin.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                    else{
                        sessionManager.createLoginSession(tvEmail.getText().toString());
                        Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginPojo> call, Throwable t) {
                pDialog.dismiss();
                // Initialize a new Snackbar
//                Snackbar snackbar = Snackbar.make(mRelativeLayout,"Connection Internet isn't work!", Snackbar.LENGTH_LONG);
//
//                // Display the Snackbar
//                snackbar.show();

                Toast.makeText(getApplicationContext(), "Connection Internet isn't work!",
                        Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registSignUp = findViewById(R.id.btn_signup);
        registSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });

//        signIn = findViewById(R.id.btn_signin);
//        signIn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
//                startActivity(intent);
//            }
//        });

        //session manager
        sessionManager = new SessionManager(getApplicationContext());
        sessionManager.checkLogin();

        ButterKnife.bind(this);

    }

    @Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            finishAffinity();
        }
    }
}
