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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import fr.pacreau.teamu.model.Player;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */

public class PlayerDao {

	public static final String KRONOS_PLAYER_DAO = PlayerDao.class.getName();
	private static final String PLAYER_TABLE = "players";
	private static final String PLAYER_FIELD_UID = "uid";
	private static final String PLAYER_FIELD_NAME = "name";
	private static final String PLAYER_FIELD_EMAIL = "email";
	private static PlayerDao instance;
	private DatabaseReference firebasePlayers;
	public static PlayerDao getInstance() {
		if (instance == null) {
			instance = new PlayerDao();
		}
		return instance;
	}

	private List<PlayerListener> playerListeners = new ArrayList<PlayerListener>();

	private PlayerDao() {
		playerListeners = new ArrayList<PlayerListener>();
		firebasePlayers = FirebaseDatabase.getInstance().getReference(PLAYER_TABLE);
	}

	public static String formatLongInTime(long p_millisUntilFinished) {
		return String.format(Locale.FRANCE, "%1$02d:%2$02d",
				//TimeUnit.MILLISECONDS.toHours(p_millisUntilFinished),
				TimeUnit.MILLISECONDS.toMinutes(p_millisUntilFinished) - TimeUnit.HOURS.toMinutes(
						TimeUnit.MILLISECONDS.toHours(p_millisUntilFinished)),
				TimeUnit.MILLISECONDS.toSeconds(p_millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
						TimeUnit.MILLISECONDS.toMinutes(p_millisUntilFinished)));
	}

	public Player createEmptyPlayer() {
		return new Player();
	}

	public void save(Player p_oPlayer) {
		Log.d("save", "save" +p_oPlayer);
		String sKey = p_oPlayer.getUid();
		if (sKey == null || sKey.isEmpty()) {
			sKey = firebasePlayers.push().getKey();
		}
		p_oPlayer.setUid(sKey);
		firebasePlayers.child(sKey).setValue(p_oPlayer);
	}

	public void deletePlayer(String p_sId) {
		firebasePlayers.child(p_sId).removeValue();
	}

	public void addPlayerListener(PlayerListener pPlayerListener) {
		playerListeners.add(pPlayerListener);
	}

	public void removePlayerListener(PlayerListener pPlayerListener) {
		playerListeners.remove(pPlayerListener);
	}

	public void  getAsyncPlayers() {
		Query querySelectAll = firebasePlayers.orderByChild(PLAYER_FIELD_NAME);
		querySelectAll.addChildEventListener(new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot dataSnapshot, String s) {
				Player oPlayer = dataSnapshot.getValue(Player.class) ;
				Log.d("onChildAdded" , ""+ oPlayer);
				for ( PlayerListener playerListener : playerListeners) {
					playerListener.onDataAdded(oPlayer);
				}
			}

			@Override
			public void onChildRemoved(DataSnapshot dataSnapshot) {
				Player oPlayer = dataSnapshot.getValue(Player.class) ;
				Log.d("onChildRemoved" , ""+ oPlayer);
				for ( PlayerListener playerListener : playerListeners) {
					playerListener.onDataRemoved(oPlayer);
				}
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {

			}

			@Override
			public void onChildChanged(DataSnapshot dataSnapshot, String s) {
				Player oPlayer = dataSnapshot.getValue(Player.class) ;
				Log.d("onChildChanged" , ""+ oPlayer);
				for ( PlayerListener playerListener : playerListeners) {
					playerListener.onDataAdded(oPlayer);
				}
			}

			@Override
			public void onChildMoved(DataSnapshot dataSnapshot, String s) {
				Player oPlayer = dataSnapshot.getValue(Player.class) ;
			}
		});
	}

	public void getAsyncPlayer(String p_sId) {
		if (p_sId != null) {
			firebasePlayers.child(p_sId).addListenerForSingleValueEvent(new ValueEventListener() {
				@Override
				public void onDataChange(DataSnapshot dataSnapshot) {
					for (PlayerListener playerListener : playerListeners) {
						playerListener.onDataAdded(dataSnapshot.getValue(Player.class));
					}
				}

				@Override
				public void onCancelled(DatabaseError databaseError) {
				}
			});
		}
	}

	public interface PlayerListener {

		public void onDataAdded(Player p_oPlayer);
		public void onDataRemoved(Player p_oPlayer);
	}
}
