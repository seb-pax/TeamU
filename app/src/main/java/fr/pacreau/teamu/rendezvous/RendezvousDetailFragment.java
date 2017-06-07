package fr.pacreau.teamu.rendezvous;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.widget.TimePicker;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnEditorAction;
import butterknife.OnTouch;
import butterknife.Unbinder;
import fr.pacreau.teamu.R;
import fr.pacreau.teamu.dao.RendezvousDao;
import fr.pacreau.teamu.model.Rendezvous;
import fr.pacreau.teamu.util.DateUtil;

/**
 * A fragment representing a single Rendezvous detail screen.
 * This fragment is either contained in a {@link RendezvousListActivity}
 * in two-pane mode (on tablets) or a {@link RendezvousDetailActivity}
 * on handsets.
 */
public class RendezvousDetailFragment extends Fragment  implements RendezvousDao.RendezvousListener {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";

	/**
	 * The dummy content this fragment is presenting.
	 */
	private Rendezvous mItem = null;

	// UI references.
	@BindView(R.id.rendezvous_datetime)
	TextView mDateView;
	@BindView(R.id.rendezvous_address)
	EditText mAddressView;

	private Unbinder unbinder;

	CollapsingToolbarLayout appBarLayout;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public RendezvousDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(ARG_ITEM_ID)) {
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			RendezvousDao.getInstance().addRendezvousListener(this);
			RendezvousDao.getInstance().getAsyncRendezvous(getArguments().getString(ARG_ITEM_ID));
		}
		FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab_rendezvous_save);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				submit();
			}
		});
	}

	@OnEditorAction(value = R.id.rendezvous_address)
	protected boolean mobileNumberFieldShim(int actionId) {
		if (actionId == EditorInfo.IME_ACTION_DONE) {
			submit();
			return true;
		}
		return false;
	}

	@OnTouch(R.id.rendezvous_datetime)
	public boolean openDialog(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			final View dialogView = View.inflate(getActivity(), R.layout.date_time_picker, null);
			final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();

			dialogView.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {

					DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);
					TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.time_picker);

					Calendar calendar = new GregorianCalendar(datePicker.getYear(),
							datePicker.getMonth(),
							datePicker.getDayOfMonth(),
							timePicker.getCurrentHour(),
							timePicker.getCurrentMinute());

					mDateView.setText(DateUtil.formatDateTime(calendar.getTime()));
					alertDialog.dismiss();
				}});
			alertDialog.setView(dialogView);
			alertDialog.show();
		}
		return true;
	}


	private void submit() {
		Snackbar.make(this.getView(),"Save data",Snackbar.LENGTH_LONG).setAction("Action",null).show();
		saveData();
		NavUtils.navigateUpTo( getActivity(), new Intent(getActivity(),RendezvousListActivity.class));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.rendezvous_detail, container, false);
		unbinder = ButterKnife.bind(this, rootView);

		Activity activity = this.getActivity();
		appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
		return rootView;
	}

	@Override
	public void onDataAdded(Rendezvous p_oRendezvous) {
		mItem = p_oRendezvous;
		updateView();
	}

	@Override
	public void onDataRemoved(Rendezvous p_oRendezvous) {
		updateView();
	}

	private void updateView() {
		if (mItem != null) {
			appBarLayout.setTitle(mItem.getUid());
			mDateView.setText(DateUtil.formatDateTime( mItem.getDate()));
			mAddressView.setText(mItem.getAddress());
		}
	}

	private boolean validateData() {
		boolean rIsValid = false;
		if (mDateView.getText().length() > 0 && mAddressView.getText().length() > 0) {
			rIsValid = true;
		}
		return rIsValid;
	}

	private void saveData() {
		if (mItem == null) {
			mItem = new Rendezvous(
					getChoosenDate(),
					mAddressView.getText().toString()
					//mDepartureTimeView.getText().toString(),
					);
		} else {
			mItem.setAddress(mAddressView.getText().toString());
			mItem.setDate(getChoosenDate());
			//mItem.setDepartureTime(mDepartureTimeView.getText().toString());
		}
		RendezvousDao.getInstance().save(mItem);
	}

	private Date getChoosenDate() {
		return DateUtil.convertDateTimeString(mDateView.getText().toString());
	}
	@Override
	public void onDestroy() {
		RendezvousDao.getInstance().removeRendezvousListener(this);
		super.onDestroy();
		unbinder.unbind();
	}
}
