package id.dekz.code.realmexample;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
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
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        realm = Realm.getDefaultInstance();

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

        final RealmResults<User> resultUser = realm.where(User.class).findAll();
        for(int i=0;i<resultUser.size();i++){
            userList.add(resultUser.get(i));
        }

        ListUserAdapter listUserAdapter = new ListUserAdapter(MainActivity.this,userList);
        listView.setAdapter(listUserAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updateDialog(userList.get(position));
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                deleteConfirm(position, resultUser);
                return true;
            }
        });
    }

    private void deleteConfirm(final int pos, final RealmResults<User> result){
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle("Delete Data")
                .setMessage("Are You Sure Want to delete this data?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        realm.beginTransaction();
                        result.deleteFromRealm(pos);
                        realm.commitTransaction();
                        onResume();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(true)
                .show();
    }

    private void updateDialog(final User user){
        LayoutInflater li = LayoutInflater.from(MainActivity.this);
        View promptsView = li.inflate(R.layout.dialog_update, null, false);
        AlertDialog.Builder updateBuilder = new AlertDialog.Builder(MainActivity.this);
        updateBuilder.setView(promptsView);

        final EditText fullname = (EditText) promptsView.findViewById(R.id.tvUpdateFullName);
        fullname.setText(user.getFullName());
        final EditText username = (EditText) promptsView.findViewById(R.id.tvUpdateUsername);
        username.setText(user.getUserName());

        updateBuilder.setCancelable(true)
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(fullname.getText().length()==0 ||
                                username.getText().length()==0){
                            Toast.makeText(MainActivity.this, "please fill all fields!", Toast.LENGTH_SHORT).show();
                        }else{
                            if(isUsernameExist(username.getText().toString())){
                                Toast.makeText(MainActivity.this, "User Name alredy Used!", Toast.LENGTH_SHORT).show();
                            }else{
                                updateData(fullname.getText().toString(),username.getText().toString(),user.getId());
                            }
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog updateDialog = updateBuilder.create();
        updateDialog.show();
    }

    private boolean isUsernameExist(String username){
        RealmResults listUsername = realm
                .where(User.class)
                .equalTo("userName",username).findAll();
        if(listUsername.size()>1){
            return true;
        }else{
            return false;
        }
    }

    private void updateData(String fullname, String username, int id){
        User user = realm.where(User.class)
                .equalTo("id", id)
                .findFirst();
        realm.beginTransaction();
        user.setUserName(username);
        user.setFullName(fullname);
        realm.commitTransaction();
        onResume();
    }
}
