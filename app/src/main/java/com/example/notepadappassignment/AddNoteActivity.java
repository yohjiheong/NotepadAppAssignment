package com.example.notepadappassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.notepadappassignment.databinding.ActivityAddNoteBinding;

import java.util.Collections;
import java.util.HashSet;

public class AddNoteActivity extends AppCompatActivity {

    int index;
    ActivityAddNoteBinding binding;
    SharedPreferences sharedPreferences;

    ImageButton imageButton;
    EditText title;
    EditText content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_add_note);

        binding = ActivityAddNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        title = binding.noteTitle;
        content = binding.noteContent;
        imageButton = binding.back;

        index = getIntent().getIntExtra("data", -1);
        if(index == -1){
            MainActivity.arrayListTitle.add("");
            MainActivity.arrayListContent.add("");
            index = MainActivity.arrayListTitle.size()-1;
            MainActivity.arrayAdapter.notifyDataSetChanged();
        } else {
            title.setText(MainActivity.arrayListTitle.get(index));
            content.setText(MainActivity.arrayListContent.get(index));
        }

        imageButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                MainActivity.arrayListTitle.set(index, title.getText().toString());
                MainActivity.arrayListContent.set(index, content.getText().toString());
                MainActivity.arrayAdapter.notifyDataSetChanged();

                sharedPreferences = getApplicationContext().getSharedPreferences(
                        "com.example.notepadappassignment",
                        MODE_PRIVATE
                );

                HashSet<String> hashSetTitle = new HashSet<String>(MainActivity.arrayListTitle);
                HashSet<String> hashSetContent = new HashSet<String>(MainActivity.arrayListContent);
                SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
                sharedPreferencesEditor.putStringSet("title", hashSetTitle);
                sharedPreferencesEditor.putStringSet("description", hashSetContent);
                sharedPreferencesEditor.apply();

                Intent intent = new Intent(AddNoteActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}