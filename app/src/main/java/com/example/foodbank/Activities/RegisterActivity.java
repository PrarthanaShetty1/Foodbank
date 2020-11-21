package com.example.foodbank.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.foodbank.R;
import com.example.foodbank.Utils.Endpoints;
import com.example.foodbank.Utils.VolleySingleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private EditText nameEt, cityEt, preferenceEt, passwordEt, numberEt;
    private Button submitButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        nameEt = findViewById(R.id.name);
        cityEt = findViewById(R.id.city);
        preferenceEt = findViewById(R.id.preference);
        passwordEt = findViewById(R.id.password);
        numberEt = findViewById(R.id.number);
        submitButton = findViewById(R.id.submit_button);
        ((View) submitButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name, city, preference, password, number;
                name = nameEt.getText().toString();
                city = cityEt.getText().toString();
                preference = preferenceEt.getText().toString();
                password = passwordEt.getText().toString();
                number = numberEt.getText().toString();
                if(isValid(name, city, preference, password, number)) {
                    register(name, city, preference, password, number);
                }

            }
        });
     }

    private void register(final String name, final String city, final String preference, final String password,
                          final String mobile) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoints.register_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals("Invalid Credentials")) {
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("city", city).apply();
                    Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    RegisterActivity.this.finish();
                } else {
                    Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterActivity.this, "Something went wrong :(", Toast.LENGTH_SHORT).show();
                Log.d("VOLLEY",error.getMessage());

            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("city", city);
                params.put("preference", preference);
                params.put("password", password);
                params.put("number", mobile);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }



    private boolean isValid(String name, String city, String preference, String password, String mobile){

        List<String> valid_preference = new ArrayList<>();
        valid_preference.add("donor");
        valid_preference.add("receiver");

        if(name.isEmpty()){
            showMessage("Name is Empty");
            return false;
        }else if(city.isEmpty()){
            showMessage("City is required");
            return false;
        }else if(!valid_preference.contains(preference)){
            showMessage("Preference invalid choose from"+valid_preference);
            return false;
        }else if(mobile.length() != 10){
            showMessage("Invalid mobile number, number should be of 10 digits");
            return false;
        }

        return true;
    }

    private void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


}