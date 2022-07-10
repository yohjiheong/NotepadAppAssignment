package com.example.notepadappassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.notepadappassignment.databinding.ActivityAddNoteBinding;


public class AddNoteActivity extends AppCompatActivity {

    ActivityAddNoteBinding binding;

    ImageButton imageButton;
    EditText title;
    EditText content;

    public static final String EXTRA_ID =
            "com.bdtask.architectureexample.EXTRA_ID";
    public static final String EXTRA_TITLE =
            "com.bdtask.architectureexample.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION =
            "com.bdtask.architectureexample.EXTRA_DESCRIPTION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_add_note);

        binding = ActivityAddNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        title = binding.noteTitle;
        content = binding.noteContent;
        imageButton = binding.back;

        Intent intent = getIntent();
        if(intent.hasExtra(EXTRA_ID)) {
            title.setText(intent.getStringExtra(EXTRA_TITLE));
            content.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
        }

        imageButton.setOnClickListener(view -> {
            String EditTextTitle = title.getText().toString();
            String EditTextDescription = content.getText().toString();

            if (EditTextTitle.trim().isEmpty() || EditTextDescription.trim().isEmpty()) {
                Toast.makeText(this, "Please insert a tile and description", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent data = new Intent();
            data.putExtra(EXTRA_TITLE, title.getText().toString());
            data.putExtra(EXTRA_DESCRIPTION, content.getText().toString());

            int id = getIntent().getIntExtra(EXTRA_ID,-1);
            if (id != -1){
                data.putExtra(EXTRA_ID,id);
            }

            setResult(RESULT_OK, data);
            finish();

        });

    }



}