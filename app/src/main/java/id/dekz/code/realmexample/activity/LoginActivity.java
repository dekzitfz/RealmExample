package id.dekz.code.realmexample.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.dekz.code.realmexample.MainActivity;
import id.dekz.code.realmexample.R;
import id.dekz.code.realmexample.model.User;
import id.dekz.code.realmexample.util.SessionManager;
import io.realm.Realm;
import io.realm.RealmResults;


public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.etLoginUsername)EditText username;
    @BindView(R.id.etLoginPassword)EditText password;
    @BindView(R.id.btnLogin)Button login;
    @BindView(R.id.tvLoginRegister)TextView toRegister;

    @Override
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reg = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(reg);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.getText().length()==0 ||
                        password.getText().length()==0){
                    Toast.makeText(LoginActivity.this, "fill all fields!", Toast.LENGTH_SHORT).show();
                }else{
                    validatelogin(username.getText().toString(),password.getText().toString());
                }
            }
        });
    }

    private void validatelogin(String user,String pass){
        Realm realm = Realm.getDefaultInstance();
        //check username
        RealmResults listUsername = realm
                .where(User.class)
                .equalTo("userName",user).findAll();
        if(listUsername.size()>0){
            //check password
            RealmResults listPass = realm
                    .where(User.class)
                    .equalTo("userName",user)
                    .equalTo("password",pass)
                    .findAll();
            if(listPass.size()>0){
                SessionManager sessionManager = new SessionManager(LoginActivity.this);
                sessionManager.saveLoginCredenetials(user);
                Intent main = new Intent(LoginActivity.this, MainActivity.class);
                main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(main);
                LoginActivity.this.finish();
            }else{
                Toast.makeText(LoginActivity.this, "username & password does not maatch!", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(LoginActivity.this, "username not found", Toast.LENGTH_SHORT).show();
        }
    }
}
