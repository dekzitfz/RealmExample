package id.dekz.code.realmexample;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;


public class RealmExample extends Application {

    @Override
    public void onCreate(){
        super.onCreate();
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
