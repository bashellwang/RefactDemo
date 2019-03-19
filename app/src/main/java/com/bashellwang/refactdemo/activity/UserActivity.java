package com.bashellwang.refactdemo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.bashellwang.refactdemo.R;
import com.bashellwang.refactdemo.business.User;
import com.bashellwang.refactdemo.business.UsersAdapter;
import com.bashellwang.refactdemo.storage.dbmodule.DBManager;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;


public class UserActivity extends AppCompatActivity {

    private EditText editText;
    private View addUserButton;

    private UsersAdapter usersAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        setUpViews();

        updateUsers();
    }

    private void updateUsers() {
        List<User> users = DBManager.getInstance().getmDataBase().queryAll(User.class);
        usersAdapter.setUsers(users);
    }

    protected void setUpViews() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewUsers);
        //noinspection ConstantConditions
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        usersAdapter = new UsersAdapter(userClickListener);
        recyclerView.setAdapter(usersAdapter);

        addUserButton = findViewById(R.id.buttonAdd);
        //noinspection ConstantConditions
        addUserButton.setEnabled(false);

        editText = (EditText) findViewById(R.id.editTextUser);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    addUser();
                    return true;
                }
                return false;
            }
        });
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean enable = s.length() != 0;
                addUserButton.setEnabled(enable);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public void onAddButtonClick(View view) {
        addUser();
    }

    private void addUser() {
        String userText = editText.getText().toString();
        editText.setText("");

        final DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
        String comment = "Added on " + df.format(new Date());

        User user = new User();
        user.setName(userText);
        user.setAge(new Random().nextInt(30));
        user.setDesc(comment);
        DBManager.getInstance().getmDataBase().insert(user);

        Log.d("DaoExample", "Inserted new user, ID: " + user.getId());

        updateUsers();
    }

    UsersAdapter.UserClickListener userClickListener = new UsersAdapter.UserClickListener() {
        @Override
        public void onUserClick(int position) {
            User user = usersAdapter.getUser(position);
            Long userId = user.getId();

            DBManager.getInstance().getmDataBase().delete(user);
            Log.d("DaoExample", "Deleted user, ID: " + userId);

            updateUsers();
        }
    };
}
