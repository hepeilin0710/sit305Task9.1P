package com.example.a71p;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class CreateAdvertActivity extends AppCompatActivity {

    RadioGroup radioGroupType;
    EditText editName, editPhone, editDescription, editDate, editLocation;
    Button buttonSave;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_advert);

        radioGroupType = findViewById(R.id.radioGroupType);
        editName = findViewById(R.id.editName);
        editPhone = findViewById(R.id.editPhone);
        editDescription = findViewById(R.id.editDescription);
        editDate = findViewById(R.id.editDate);
        editLocation = findViewById(R.id.editLocation);
        buttonSave = findViewById(R.id.buttonSave);

        db = new DatabaseHelper(this);

        buttonSave.setOnClickListener(v -> {
            int selectedId = radioGroupType.getCheckedRadioButtonId();
            RadioButton selectedRadio = findViewById(selectedId);
            String type = selectedRadio.getText().toString();

            String name = editName.getText().toString();
            String phone = editPhone.getText().toString();
            String description = editDescription.getText().toString();
            String date = editDate.getText().toString();
            String location = editLocation.getText().toString();

            boolean inserted = db.insertData(type, name, phone, description, date, location);
            if (inserted) {
                Toast.makeText(this, "Saved Successfully!", Toast.LENGTH_SHORT).show();
                finish(); // return to mainpage
            } else {
                Toast.makeText(this, "Failed to Save!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
