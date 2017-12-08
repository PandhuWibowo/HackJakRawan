package com.example.root.hackjak.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.root.hackjak.ApiService;
import com.example.root.hackjak.FullRestAPI.RegistPojo;
import com.example.root.hackjak.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUpActivity extends AppCompatActivity {

    private Context mContext;
    private Activity mActivity;
    private Toolbar toolbar;
    public Button btnSignUp;
    private ProgressDialog pDialog;
    private LinearLayout mLinearLayout;

    //public  EditText tvNim, tvPassword, tvFName, tvLName, tvEmail;
    @BindView(R.id.edt_email)
    EditText tvEmail;
    @BindView(R.id.edt_password) EditText tvPassword;
    @BindView(R.id.edt_fullname) EditText tvFName;
    @BindView(R.id.edt_nik) EditText tvNik;

    @OnClick(R.id.btn_signup) void daftar() {
        //membuat progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Loading ...");
        pDialog.show();

        //mengambil data dari edittext
        String nik      = tvNik.getText().toString();
        String password = tvPassword.getText().toString();
        String fName    = tvFName.getText().toString();
//        String lName    = tvLName.getText().toString();
        String email    = tvEmail.getText().toString();
        String URL = "http://ikutevent.com/api-event/hackjak/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService api = retrofit.create(ApiService.class);
        Call<RegistPojo> call = api.regist(email, password, fName, nik);
        call.enqueue(new Callback<RegistPojo>() {
            @Override
            public void onResponse(Call<RegistPojo> call, Response<RegistPojo> response) {
                int value    = response.body().getValue();
                String message  = response.body().getMessage();

                pDialog.dismiss();
                if (value == 1) {
//                    // Initialize a new Snackbar
//                    Snackbar snackbar = Snackbar.make(mLinearLayout, message, Snackbar.LENGTH_LONG);
//
//                     //Display the Snackbar
//                    snackbar.show();

                    //Clear EditText
                    tvNik.setText(null);
                    tvPassword.setText(null);
                    tvFName.setText(null);

                    tvEmail.setText(null);
                } else {
                    // Initialize a new Snackbar
                    Snackbar snackbar = Snackbar.make(mLinearLayout, message, Snackbar.LENGTH_LONG);

                    // Display the Snackbar
                    snackbar.show();
                }
            }

            @Override
            public void onFailure(Call<RegistPojo> call, Throwable t) {
                pDialog.dismiss();
//                // Initialize a new Snackbar
//                Snackbar snackbar = Snackbar.make(mRelativeLayout,"Connection Internet isn't work!", Snackbar.LENGTH_LONG);
//
//                // Display the Snackbar
//                snackbar.show();
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        toolbar.setLogoDescription(getResources().getString(R.string.sign_up_title));
        toolbar.setTitleTextColor(getResources().getColor(R.color.title));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mContext = getApplicationContext();
        mActivity = SignUpActivity.this;

        // Get the widget reference from XML layout
        //mRelativeLayout = (RelativeLayout) findViewById(R.id.rl);
    }
}
