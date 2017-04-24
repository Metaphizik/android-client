package com.example.metaphizik.design;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.metaphizik.design.adapter.TabsFragmentAdapter;
import com.example.metaphizik.design.auth.EmailPasswordActivity;
import com.example.metaphizik.design.chat.ChatActivity;
import com.example.metaphizik.design.dto.RemindDTO;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity{

    private static final int MAIN_LAYOUT = R.layout.activity_main;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ViewPager viewPager;
    private TabsFragmentAdapter adapter;
    private TextView mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.ThemeDefault);
        super.onCreate(savedInstanceState);
        setContentView(MAIN_LAYOUT);
        initToolbar();
        initNavigationView();
        initTabs();
}
    public void onBackPressed() {
        DrawerLayout layout = (DrawerLayout)findViewById(R.id.drawerLayout);
        if (layout.isDrawerOpen(GravityCompat.START)) {
            layout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void initToolbar() {
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });
        toolbar.inflateMenu(R.menu.menu);
    }

    private void initTabs() {
        viewPager = (ViewPager)findViewById(R.id.viewPager);
        adapter = new TabsFragmentAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        new RemindMeTask().execute();

        TabLayout tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    //идентификация навигационной шторки,
    //создание слушателя нажатий, который переходит на первую табу,
    //исоздание тогла
    private void initNavigationView() {
        drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.view_navigation_open, R.string.view_navigation_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView);

        //для установки почты в navigation_header
        View v = navigationView.getHeaderView(0);
        mail = (TextView) v.findViewById(R.id.MAIL);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawers();
                switch (item.getItemId()){
                    case R.id.actionNotificationItem:
                        ShowNotificationTab(); break;
                    case R.id.login:
                        ShowAuthForm(); break;
                    case R.id.chat:
                        ShowChatForm(); break;
                }

                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {mail.setText("Your email");}
        else {
            mail.setText(data.getStringExtra("email_tag"));
        }
    }

    private void ShowChatForm() {
        Intent intent = new Intent(MainActivity.this, ChatActivity.class);
        startActivity(intent);
    }

    private void ShowAuthForm() {
        Intent intent = new Intent(MainActivity.this, EmailPasswordActivity.class);
        startActivityForResult(intent, 1);

    }

    private void ShowNotificationTab (){
        viewPager.setCurrentItem(Constants.TAB_ONE);
    }

    private class RemindMeTask extends AsyncTask<Void, Void, RemindDTO>{

        @Override
        protected RemindDTO doInBackground(Void... params) {
            RestTemplate template = new RestTemplate();
            template.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            RemindDTO remindDTO;
            try {remindDTO = template.getForObject(Constants.URL.GET_REMINDERS, RemindDTO.class);}
            catch (Exception e){
                remindDTO = new RemindDTO("+++{");
            }
            return remindDTO;
        }

        @Override
        protected void onPostExecute(RemindDTO remindDTO) {
            if (remindDTO.getTitle() == "+++{")
                Toast.makeText(MainActivity.this,
                        "Подключение к инернету отсутствует",
                        Toast.LENGTH_LONG).show();
            else {
                List<RemindDTO> data = new ArrayList<>();
                data.add(remindDTO);
                adapter.setDate(data);}
        }

    }

}
