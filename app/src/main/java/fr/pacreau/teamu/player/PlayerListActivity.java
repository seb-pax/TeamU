package fr.pacreau.teamu.player;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fr.pacreau.teamu.R;

import fr.pacreau.teamu.dao.PlayerDao;
import fr.pacreau.teamu.model.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a list of Players. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link PlayerDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class PlayerListActivity extends AppCompatActivity implements PlayerDao.PlayerListener {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private PlayerItemRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Add Player", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Context context = view.getContext();
                Intent intent = new Intent(context, PlayerDetailActivity.class);
                context.startActivity(intent);

            }
        });

        View recyclerView = findViewById(R.id.player_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    /*
        if (findViewById(R.id.player_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }*/
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        PlayerDao.getInstance().addPlayerListener(this);
        PlayerDao.getInstance().getAsyncPlayers();
        adapter = new PlayerItemRecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDataAdded(Player p_oPlayer) {
        //View recyclerView = findViewById(R.id.player_list);
        adapter.addItem(p_oPlayer);
    }

    @Override
    public void onDataRemoved(Player p_oPlayer) {
        adapter.removeItem(p_oPlayer);
    }

    @Override
    protected void onDestroy() {
        PlayerDao.getInstance().removePlayerListener(this);
        super.onDestroy();
    }
}
