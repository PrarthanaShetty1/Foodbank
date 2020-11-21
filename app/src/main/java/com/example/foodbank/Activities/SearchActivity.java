package com.example.foodbank.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.androidnetworking.common.Method;
import com.example.foodbank.R;
import com.example.foodbank.Utils.Endpoints;
import com.example.foodbank.Utils.VolleySingleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        final EditText et_preference, et_city;
        et_preference = findViewById(R.id.et_preference);
        et_city = findViewById(R.id.et_city);
        Button submit_button = findViewById(R.id.submit_button);
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String preference = et_preference.getText().toString();
                String city = et_city.getText().toString();
                if(isValid(preference, city)){
                    get_search_results(preference, city);
                }
            }
        });
    }


    private void get_search_results(final String preference, final String city) {
        StringRequest stringRequest = new StringRequest(
                Method.POST, Endpoints.search_donors, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //json response
                Intent intent = new Intent(SearchActivity.this, SearchResults.class);
                intent.putExtra("city", city);
                intent.putExtra("preference", preference);
                intent.putExtra("json", response);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SearchActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                Log.d("VOLLEY", Objects.requireNonNull(error.getMessage()));
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("city", city);
                params.put("preference", preference);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }


    private boolean isValid(String preference, String city){
        List<String> valid_preference = new ArrayList<>();
        valid_preference.add("donor");
        valid_preference.add("receiver");
        if(!valid_preference.contains(preference)){
            showMsg("Preference invalid choose from " + valid_preference);
            return false;
        }else if(city.isEmpty()){
            showMsg("Enter city");
            return false;
        }
        return true;
    }


    private void showMsg(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}