package com.example.a71p;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    TextView textDetail;
    Button buttonRemove;
    DatabaseHelper db;
    int itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        textDetail = findViewById(R.id.textDetail);
        buttonRemove = findViewById(R.id.buttonRemove);
        db = new DatabaseHelper(this);

        Intent intent = getIntent();
        itemId = intent.getIntExtra("itemId", -1);

        if (itemId != -1) {
            loadItemDetails(itemId);
        }

        buttonRemove.setOnClickListener(v -> {
            boolean deleted = db.deleteItem(itemId);
            if (deleted) {
                Toast.makeText(this, "Item Removed", Toast.LENGTH_SHORT).show();
                finish(); //
            } else {
                Toast.makeText(this, "Failed to Remove", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadItemDetails(int id) {
        Cursor cursor = db.getAllItems();
        while (cursor.moveToNext()) {
            if (cursor.getInt(cursor.getColumnIndexOrThrow("id")) == id) {
                String type = cursor.getString(cursor.getColumnIndexOrThrow("type"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
                String desc = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                String loc = cursor.getString(cursor.getColumnIndexOrThrow("location"));

                String content = type + " item\n\n"
                        + desc + "\n\n"
                        + "By: " + name + "\n"
                        + "Phone: " + phone + "\n"
                        + "On: " + date + "\n"
                        + "At: " + loc;

                textDetail.setText(content);
                break;
            }
        }
    }
}
