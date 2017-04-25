package com.example.metaphizik.design;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.metaphizik.design.adapter.TabsFragmentAdapter;
import com.example.metaphizik.design.auth.EmailPasswordActivity;
import com.example.metaphizik.design.chat.ChatActivity;
import com.example.metaphizik.design.dto.RemindDTO;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
    private TabLayout tabLayout;
    private FirebaseAuth mAuth;
    private NavigationView navigationView;
    FragmentManager myFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.ThemeDefault);
        super.onCreate(savedInstanceState);
        setContentView(MAIN_LAYOUT);
        initToolbar();
        initNavigationView();
        initTabs();
        myFragmentManager = getSupportFragmentManager();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            ShowAuthForm();
        }
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

        tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }



   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_navigation, menu);
        return super.onCreateOptionsMenu(menu);
    }

     @Override
     public boolean onPrepareOptionsMenu(Menu menu) {
         MenuItem login = (MenuItem) findViewById(R.id.login);
         if (mail.getText().equals("Your email")){
             login.setTitle("Вход");
         }
         else {
             login.setTitle("Выйти из учетной записи");
         }
         return super.onPrepareOptionsMenu(menu);
     }*/
    //идентификация навигационной шторки,
    //создание слушателя нажатий, который переходит на первую табу,
    //исоздание тогла
    private void initNavigationView() {
        drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.view_navigation_open, R.string.view_navigation_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.navigationView);

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
                    case R.id.logout:
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
        if (data == null) {
            mail.setText("Your email");
            navigationView.getMenu().findItem(R.id.login).setVisible(true);
            navigationView.getMenu().findItem(R.id.logout).setVisible(false);}
        else {
            mail.setText(data.getStringExtra("email_tag"));
            navigationView.getMenu().findItem(R.id.login).setVisible(false);
            navigationView.getMenu().findItem(R.id.logout).setVisible(true);
        }
    }

    private void ShowChatForm() {
        //TODO поправить приверкую не брать getcurrentUser.
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            tabLayout.setVisibility(View.GONE);
            viewPager.setVisibility(View.GONE);
            Fragment chat = new ChatActivity();
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, chat);
            ft.commit();
        } else {
            ShowAuthForm();
            Toast.makeText(this, "Выполнинте вход",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void ShowAuthForm() {
        Intent intent = new Intent(MainActivity.this, EmailPasswordActivity.class);
        if(mail.getText().equals("Your email")) {
            navigationView.getMenu().findItem(R.id.login).setVisible(true);
            navigationView.getMenu().findItem(R.id.logout).setVisible(false);

        } else {
            navigationView.getMenu().findItem(R.id.login).setVisible(false);
            navigationView.getMenu().findItem(R.id.logout).setVisible(true);
        }
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
