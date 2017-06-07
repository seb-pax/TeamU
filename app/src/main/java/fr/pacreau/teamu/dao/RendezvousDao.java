package fr.pacreau.teamu.dao;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import fr.pacreau.teamu.model.Rendezvous;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */

public class RendezvousDao {

	public static final String KRONOS_RENDEZVOUS_DAO = RendezvousDao.class.getName();
	private static final String RENDEZVOUS_TABLE = "rendezvous";
	private static final String RENDEZVOUS_FIELD_UID = "uid";
	private static final String RENDEZVOUS_FIELD_NAME = "name";
	private static final String RENDEZVOUS_FIELD_EMAIL = "email";
	private static RendezvousDao instance;
	private DatabaseReference firebaseRendezvous;
	public static RendezvousDao getInstance() {
		if (instance == null) {
			instance = new RendezvousDao();
		}
		return instance;
	}

	private List<RendezvousListener> rendezvousListeners = new ArrayList<RendezvousListener>();

	private RendezvousDao() {
		rendezvousListeners = new ArrayList<RendezvousListener>();
		firebaseRendezvous = FirebaseDatabase.getInstance().getReference(RENDEZVOUS_TABLE);
	}

	public static String formatLongInTime(long p_millisUntilFinished) {
		return String.format(Locale.FRANCE, "%1$02d:%2$02d",
				//TimeUnit.MILLISECONDS.toHours(p_millisUntilFinished),
				TimeUnit.MILLISECONDS.toMinutes(p_millisUntilFinished) - TimeUnit.HOURS.toMinutes(
						TimeUnit.MILLISECONDS.toHours(p_millisUntilFinished)),
				TimeUnit.MILLISECONDS.toSeconds(p_millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
						TimeUnit.MILLISECONDS.toMinutes(p_millisUntilFinished)));
	}

	public Rendezvous createEmptyRendezvous() {
		return new Rendezvous();
	}

	public void save(Rendezvous p_oRendezvous) {
		Log.d("save", "save" +p_oRendezvous);
		String sKey = p_oRendezvous.getUid();
		if (sKey == null || sKey.isEmpty()) {
			sKey = firebaseRendezvous.push().getKey();
		}
		p_oRendezvous.setUid(sKey);
		firebaseRendezvous.child(sKey).setValue(p_oRendezvous);
	}

	public void deleteRendezvous(String p_sId) {
		firebaseRendezvous.child(p_sId).removeValue();
	}

	public void addRendezvousListener(RendezvousListener pRendezvousListener) {
		rendezvousListeners.add(pRendezvousListener);
	}

	public void removeRendezvousListener(RendezvousListener pRendezvousListener) {
		rendezvousListeners.remove(pRendezvousListener);
	}

	public void  getAsyncRendezvous() {
		Query querySelectAll = firebaseRendezvous.orderByChild(RENDEZVOUS_FIELD_NAME);
		querySelectAll.addChildEventListener(new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot dataSnapshot, String s) {
				Rendezvous oRendezvous = dataSnapshot.getValue(Rendezvous.class) ;
				Log.d("onChildAdded" , ""+ oRendezvous);
				for ( RendezvousListener rendezvousListener : rendezvousListeners) {
					rendezvousListener.onDataAdded(oRendezvous);
				}
			}

			@Override
			public void onChildRemoved(DataSnapshot dataSnapshot) {
				Rendezvous oRendezvous = dataSnapshot.getValue(Rendezvous.class) ;
				Log.d("onChildRemoved" , ""+ oRendezvous);
				for ( RendezvousListener rendezvousListener : rendezvousListeners) {
					rendezvousListener.onDataRemoved(oRendezvous);
				}
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {

			}

			@Override
			public void onChildChanged(DataSnapshot dataSnapshot, String s) {
				Rendezvous oRendezvous = dataSnapshot.getValue(Rendezvous.class) ;
				Log.d("onChildChanged" , ""+ oRendezvous);
				for ( RendezvousListener rendezvousListener : rendezvousListeners) {
					rendezvousListener.onDataAdded(oRendezvous);
				}
			}

			@Override
			public void onChildMoved(DataSnapshot dataSnapshot, String s) {
				Rendezvous oRendezvous = dataSnapshot.getValue(Rendezvous.class) ;
			}
		});
	}

	public void getAsyncRendezvous(String p_sId) {
		if (p_sId != null) {
			firebaseRendezvous.child(p_sId).addListenerForSingleValueEvent(new ValueEventListener() {
				@Override
				public void onDataChange(DataSnapshot dataSnapshot) {
					for (RendezvousListener rendezvousListener : rendezvousListeners) {
						rendezvousListener.onDataAdded(dataSnapshot.getValue(Rendezvous.class));
					}
				}

				@Override
				public void onCancelled(DatabaseError databaseError) {
				}
			});
		}
	}

	public interface RendezvousListener {

		public void onDataAdded(Rendezvous p_oRendezvous);
		public void onDataRemoved(Rendezvous p_oPlayer);
	}
}
