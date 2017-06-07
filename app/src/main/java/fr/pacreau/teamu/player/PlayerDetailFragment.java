package fr.pacreau.teamu.player;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnEditorAction;
import butterknife.Unbinder;
import fr.pacreau.teamu.R;
import fr.pacreau.teamu.dao.PlayerDao;
import fr.pacreau.teamu.model.Player;

/**
 * A fragment representing a single Player detail screen.
 * This fragment is either contained in a {@link PlayerListActivity}
 * in two-pane mode (on tablets) or a {@link PlayerDetailActivity}
 * on handsets.
 */
public class PlayerDetailFragment extends Fragment implements PlayerDao.PlayerListener {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private Player mItem = null;

    // UI references.
    @BindView(R.id.player_email)
    EditText mEmailView;
    @BindView(R.id.player_fullname)
    EditText mFullnameView;
    @BindView(R.id.player_name)
    EditText mNameView;

    private Unbinder unbinder;

    CollapsingToolbarLayout appBarLayout;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PlayerDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            PlayerDao.getInstance().addPlayerListener(this);
            PlayerDao.getInstance().getAsyncPlayer(getArguments().getString(ARG_ITEM_ID));
        }
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab_player_save);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });
    }

    @OnEditorAction(value = R.id.player_fullname)
    protected boolean mobileNumberFieldShim(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            submit();
            return true;
        }
        return false;
    }

    private void submit() {
        Snackbar.make(this.getView(),"Save data",Snackbar.LENGTH_LONG).setAction("Action",null).show();
        saveData();
        NavUtils.navigateUpTo( getActivity(), new Intent(getActivity(),PlayerListActivity.class));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.player_detail, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        Activity activity = this.getActivity();
        appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
        return rootView;
    }

    @Override
    public void onDataAdded(Player p_oPlayer) {
        mItem = p_oPlayer;
        updateView();
    }

    @Override
    public void onDataRemoved(Player p_oPlayer) {
        updateView();
    }

    private void updateView() {
        if (mItem != null) {
            appBarLayout.setTitle(mItem.getUid());
            mFullnameView.setText(mItem.getFullname());
            mEmailView.setText(mItem.getEmail());
            mNameView.setText(mItem.getName());
        }
    }

    private boolean validateData() {
        boolean rIsValid = false;
        if (mEmailView.getText().length() > 0 && mNameView.getText().length() > 0 && mNameView.getText().length() > 0) {
            rIsValid = true;
        }
        return rIsValid;
    }

    private void saveData() {
        if (mItem == null) {
            mItem = new Player(
                    mNameView.getText().toString(),
                    mFullnameView.getText().toString(),
                    mEmailView.getText().toString());
        } else {
            mItem.setName(mNameView.getText().toString());
            mItem.setEmail(mEmailView.getText().toString());
            mItem.setFullname(mFullnameView.getText().toString());
        }
        PlayerDao.getInstance().save(mItem);
    }

    @Override
    public void onDestroy() {
        PlayerDao.getInstance().removePlayerListener(this);
        super.onDestroy();
        unbinder.unbind();
    }
}
