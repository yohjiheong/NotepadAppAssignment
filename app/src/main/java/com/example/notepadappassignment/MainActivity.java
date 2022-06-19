package com.example.notepadappassignment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Layout;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.notepadappassignment.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    NavigationView navigationView;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    FloatingActionButton floating_action_button;
    GridView gridView;

    SharedPreferences sharedPreferences;
    static ArrayList<String> arrayListTitle = new ArrayList<String>();
    static ArrayList<String> arrayListContent = new ArrayList<String>();
    static ArrayAdapter arrayAdapter;
    ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Navigation Drawer
//        toolbar = findViewById(R.id.searchBar);
        setSupportActionBar(binding.mainLayout.toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        drawerLayout = binding.drawerLayout;
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView = binding.navDrawer;
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);



        // floating action button to add
        floating_action_button = binding.mainLayout.fab;
        floating_action_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
                startActivity(intent);
            }
        });

        // shared preferences
        sharedPreferences = getApplicationContext().getSharedPreferences(
                "com.example.notepadappassignment",
                MODE_PRIVATE
        );

        HashSet<String> hashSetTitle = (HashSet<String>) sharedPreferences.getStringSet("title", null);
        HashSet<String> hashSetContent = (HashSet<String>) sharedPreferences.getStringSet("content", null);
        if (hashSetTitle != null && hashSetContent != null) {
            arrayListTitle = new ArrayList<>(hashSetTitle);
            arrayListContent = new ArrayList<>(hashSetContent);
        }

        gridView = binding.mainLayout.gridView;
        arrayAdapter = new ArrayAdapter(this, R.layout.grid_layout, R.id.text1, arrayListTitle) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = view.findViewById(R.id.text1);
                TextView text2 = view.findViewById(R.id.text2);

                text1.setText(arrayListTitle.get(position).trim());
                text2.setText(arrayListContent.get(position).trim());
                return view;
            }
        };
        gridView.setAdapter(arrayAdapter);

        // tap short to edit existing note
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String s = (String) adapterView.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
                intent.putExtra("data", position);
                startActivity(intent);
            }
        });


        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete Note")
                        .setMessage("Are you sure?")
                        .setPositiveButton(
                                "Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        arrayListTitle.remove(position);
                                        arrayListContent.remove(position);
                                        arrayAdapter.notifyDataSetChanged();

                                        sharedPreferences = getApplicationContext().getSharedPreferences("com.example.notepadappassignment", Context.MODE_PRIVATE);
                                        HashSet<String> hashSetTitle = new HashSet<String>(MainActivity.arrayListTitle);
                                        HashSet<String> hashSetContent = new HashSet<String>(MainActivity.arrayListContent);

                                        SharedPreferences.Editor sharedPreferenceEditor = sharedPreferences.edit();
                                        sharedPreferenceEditor.putStringSet("title", hashSetTitle);
                                        sharedPreferenceEditor.putStringSet("content", hashSetContent);
                                        sharedPreferenceEditor.apply();
                                        Toast.makeText(MainActivity.this, "Deleted successfully!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                        ).setNegativeButton("No", null).show();

                return true;
            }
        });

    }

    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.allNotes) {
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}