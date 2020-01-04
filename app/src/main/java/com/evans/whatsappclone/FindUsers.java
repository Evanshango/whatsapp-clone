package com.evans.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;

import com.evans.whatsappclone.adapter.UserAdapter;
import com.evans.whatsappclone.model.User;

import java.util.ArrayList;
import java.util.List;

public class FindUsers extends AppCompatActivity {

    private static final String TAG = "FindUsers";
    private RecyclerView mUserRv;
    private UserAdapter mUserAdapter;
    private List<User> mUsers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_users);

        initViews();

        initLayout();

        getContacts();

    }

    private void initLayout() {
        mUserAdapter = new UserAdapter(mUsers);
        mUserRv.setHasFixedSize(true);
        mUserRv.setNestedScrollingEnabled(false);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mUserRv.setLayoutManager(manager);

        mUserRv.setAdapter(mUserAdapter);
    }

    private void getContacts(){
        Cursor phones = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null, null);
        if (phones != null){
            while (phones.moveToNext()){
                String name = phones.getString(phones.getColumnIndex(
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phone = phones.getString(phones.getColumnIndex(
                        ContactsContract.CommonDataKinds.Phone.NUMBER));
                User user = new User(name, phone);
                mUsers.add(user);
                mUserAdapter.notifyDataSetChanged();
            }
        } else {
            Log.d(TAG, "getContacts: Looks like you don't have any contact list");
        }
    }

    private void initViews() {
        mUserRv = findViewById(R.id.userList);
    }
}
