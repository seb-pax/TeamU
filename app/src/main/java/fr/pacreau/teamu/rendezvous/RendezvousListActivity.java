package fr.pacreau.teamu.rendezvous;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import fr.pacreau.teamu.R;
import fr.pacreau.teamu.dao.RendezvousDao;
import fr.pacreau.teamu.model.Rendezvous;

/**
 * An activity representing a list of Rendezvous. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RendezvousDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RendezvousListActivity extends AppCompatActivity implements RendezvousDao.RendezvousListener {

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private RendezvousItemRecyclerViewAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rendezvous_list);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		toolbar.setTitle(getTitle());

		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Snackbar.make(view, "Add Rendezvous", Snackbar.LENGTH_LONG)
						.setAction("Action", null).show();
				Context context = view.getContext();
				Intent intent = new Intent(context, RendezvousDetailActivity.class);
				context.startActivity(intent);

			}
		});

		View recyclerView = findViewById(R.id.rendezvous_list);
		assert recyclerView != null;
		setupRecyclerView((RecyclerView) recyclerView);
	/*
		if (findViewById(R.id.rendezvous_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-w900dp).
			// If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;
		}*/
	}

	private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
		RendezvousDao.getInstance().addRendezvousListener(this);
		RendezvousDao.getInstance().getAsyncRendezvous();
		adapter = new RendezvousItemRecyclerViewAdapter(this);
		recyclerView.setAdapter(adapter);
	}

	@Override
	public void onDataAdded(Rendezvous p_oRendezvous) {
		//View recyclerView = findViewById(R.id.rendezvous_list);
		adapter.addItem(p_oRendezvous);
	}

	@Override
	public void onDataRemoved(Rendezvous p_oRendezvous) {
		adapter.removeItem(p_oRendezvous);
	}

	@Override
	protected void onDestroy() {
		RendezvousDao.getInstance().removeRendezvousListener(this);
		super.onDestroy();
	}
}