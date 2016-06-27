package id.dekz.code.realmexample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.dekz.code.realmexample.activity.LoginActivity;
import id.dekz.code.realmexample.adapter.ListUserAdapter;
import id.dekz.code.realmexample.model.User;
import id.dekz.code.realmexample.util.SessionManager;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.listView)ListView listView;

    private List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        SessionManager sessionManager = new SessionManager(MainActivity.this);
        if(!sessionManager.isLoggedIn()){
            Intent login = new Intent(MainActivity.this, LoginActivity.class);
            login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(login);
            MainActivity.this.finish();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        getData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_logout) {
            SessionManager sessionManager = new SessionManager(MainActivity.this);
            sessionManager.logout();
            Intent login = new Intent(MainActivity.this, LoginActivity.class);
            login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(login);
            MainActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getData(){
        userList = new ArrayList<>();
        userList.clear();
        Realm realm = Realm.getDefaultInstance();
        //RealmQuery<User> listUser = realm.where(User.class);
        RealmResults<User> resultUser = realm.where(User.class).findAll();
        for(int i=0;i<resultUser.size();i++){
            userList.add(resultUser.get(i));
        }

        ListUserAdapter listUserAdapter = new ListUserAdapter(MainActivity.this,userList);
        listView.setAdapter(listUserAdapter);
    }
}
