package com.bashellwang.refactdemo.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bashellwang.common.log.ALog;
import com.bashellwang.refactdemo.App;
import com.bashellwang.refactdemo.R;

import timber.log.Timber;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mGoNoteBtn, mGoUserBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGoNoteBtn = findViewById(R.id.go_note_btn);
        mGoUserBtn = findViewById(R.id.go_user_btn);

        mGoNoteBtn.setOnClickListener(this);
        mGoUserBtn.setOnClickListener(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.go_note_btn:

                Intent noteIntent = new Intent(this, NoteActivity.class);
                startActivity(noteIntent);

                ALog.i("test: go to note btn");

                break;
            case R.id.go_user_btn:
                Intent userIntent = new Intent(this, UserActivity.class);
                startActivity(userIntent);
//                Timber.e("test: go to user btn");
                ALog.e("test............");
                break;
            default:
                break;
        }
    }
}
