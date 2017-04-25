package com.example.metaphizik.design.chat;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.metaphizik.design.MainActivity;
import com.example.metaphizik.design.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import static android.app.Activity.RESULT_OK;

public class ChatActivity extends Fragment{

    private static int SIGN_IN_REQUEST_CODE = 1;
    private FirebaseListAdapter<Message> adapter;
    private RelativeLayout activity_chat;
    private Button send_message;
    View vi;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        vi = inflater.inflate(R.layout.activity_chat, null);
        activity_chat = (RelativeLayout)vi.findViewById(R.id.chat_relative_layout);

        send_message = (Button)vi.findViewById(R.id.send_message);
        send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText input = (EditText)vi.findViewById(R.id.input_field);
                if (!"".equals(input.getText().toString())){
                    FirebaseDatabase.getInstance().getReference().push()
                            .setValue(new Message(FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                                    input.getText().toString()));
                    input.setText("");}
            }
        });
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            displayChat();
        } else {
            Toast.makeText(getActivity(), "Вход не выполнен",
                    Toast.LENGTH_SHORT).show();

        }
        //регистрация из чата
        /*if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivityForResult(AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .build(), SIGN_IN_REQUEST_CODE);*/
        return vi;
    }

    private void displayChat() {
        ListView listMessages = (ListView)vi.findViewById(R.id.listView);
        Query recentMessages = FirebaseDatabase.getInstance().getReference().limitToLast(5);
        adapter = new FirebaseListAdapter<Message>(getActivity(), Message.class, R.layout.chat_item, recentMessages) {
            //Делаем самые новые соббщение сверху
            @Override
            public Message getItem(int position) {

                return super.getItem(getCount() - position - 1);
            }

            @Override
            protected void populateView(View v, Message model, int position) {

                TextView textMessage, author, timeMessage;
                textMessage = (TextView)v.findViewById(R.id.iMessage);
                author = (TextView)v.findViewById(R.id.iUser);
                timeMessage = (TextView)v.findViewById(R.id.iTime);

                textMessage.setText(model.getTextMessage());
                author.setText(model.getAuthor());
                timeMessage.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getTimeMessage()));
            }
        };
        listMessages.setAdapter(adapter);

    }
    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN_REQUEST_CODE)
        {
            if (resultCode == RESULT_OK)
            {
                Snackbar.make(activity_chat, "Вход выполнен", Snackbar.LENGTH_SHORT).show();
                displayChat();
            } else {
                Snackbar.make(activity_chat, "Вход не выполнен", Snackbar.LENGTH_SHORT).show();
                getActivity().finish();
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.chat_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_signout)
        {
            AuthUI.getInstance().signOut(getActivity())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                                Snackbar.make(activity_chat, "Выход выполнен", Snackbar.LENGTH_SHORT).show();
                                getActivity().finish();

                            }
                        });
                return true;

        }
        return true;
    }*/
}

