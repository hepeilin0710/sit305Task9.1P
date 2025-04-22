package com.example.a71p;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class ItemListActivity extends AppCompatActivity {

    ListView listView;
    Button buttonRefresh;
    DatabaseHelper db;
    ArrayList<String> itemList;
    ArrayList<Integer> idList;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        listView = findViewById(R.id.listView);
        buttonRefresh = findViewById(R.id.buttonRefresh);
        db = new DatabaseHelper(this);

        itemList = new ArrayList<>();
        idList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itemList);
        listView.setAdapter(adapter);

        loadItems();

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            int itemId = idList.get(i);
            Intent intent = new Intent(ItemListActivity.this, DetailActivity.class);
            intent.putExtra("itemId", itemId);
            startActivity(intent);
        });

        buttonRefresh.setOnClickListener(v -> loadItems());
    }

    private void loadItems() {
        itemList.clear();
        idList.clear();
        Cursor cursor = db.getAllItems();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No items found", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String type = cursor.getString(cursor.getColumnIndexOrThrow("type"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                itemList.add(type + ": " + description);
                idList.add(id);
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadItems();  // refresh
    }
}
