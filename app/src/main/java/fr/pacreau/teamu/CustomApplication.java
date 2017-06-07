package fr.pacreau.teamu;

import android.app.Application;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;

import fr.pacreau.teamu.module.AppModule;
import fr.pacreau.teamu.module.NetComponent;
import fr.pacreau.teamu.module.DaggerNetComponent;

/**
 * TeamU
 * fr.pacreau.teamu
 *
 * @author spacreau
 * @since 07/03/2017
 */
public class CustomApplication extends Application {

	private FirebaseUser user = null;
	NetComponent deps;

	@Override
	public void onCreate() {
		super.onCreate();
		deps = DaggerNetComponent.builder().appModule(new AppModule(this)).build();

		FirebaseDatabase.getInstance().setPersistenceEnabled(true);
		FirebaseDatabase.getInstance().setLogLevel(Logger.Level.DEBUG);
	}

	public FirebaseUser getUser() {
		return user;
	}

	public void setUser(FirebaseUser p_user) {
		this.user = p_user;
	}

	public NetComponent getDeps() {
		return deps;
	}
}
