package gr.liakos.spearo.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import gr.liakos.spearo.R;
import gr.liakos.spearo.SpearoApplication;
import gr.liakos.spearo.def.SpearoDataChangeListener;
import gr.liakos.spearo.enums.FishingSessionsState;
import gr.liakos.spearo.enums.SpearGunBrand;
import gr.liakos.spearo.enums.SpeargunType;
import gr.liakos.spearo.model.adapter.SpearGunTypesSpinnerAdapter;
import gr.liakos.spearo.model.adapter.SpeargunLengthsAdapter;
import gr.liakos.spearo.model.adapter.SpeargunListViewAdapter;
import gr.liakos.spearo.model.adapter.SpeargunBrandsSpinnerAdapter;
import gr.liakos.spearo.model.object.FishingSession;
import gr.liakos.spearo.model.object.Speargun;
import gr.liakos.spearo.util.Constants;

/**
 * Fragment for displaying user's personal statistics and {@link FishingSession}s.
 */
public class FrgSpeargunStats
extends Fragment implements SpearoDataChangeListener {

	/**
	 * Flipper position when no {@link Speargun}s exist yet.
	 */
	static final Integer POSITION_NO_GUNS = 0;

	/**
	 * Flipper position when {@link Speargun}s exist.
	 */
	static final Integer POSITION_SHOW_GUN_STATS = 1;

	/**
	 * Flipper for changing between views.
	 */
	ViewFlipper gunsFlipper;

	Speargun newSpeargun = null;

	List<Speargun> allSpearguns = new ArrayList<>();

	SpeargunListViewAdapter speargunsAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		((SpearoApplication) requireActivity().getApplication()).getListeners().add(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.frg_speargun_stats, container, false);
		setupGunListView(v);
		setupAddGunButton(v);
		initFlipper(v);
		return v;
	}

	void initFlipper(View v) {
		gunsFlipper = v.findViewById(R.id.gun_stats_flipper);
		setFlipperChild();
	}

	void setFlipperChild() {
		if (allSpearguns.isEmpty()){
			gunsFlipper.setDisplayedChild(POSITION_NO_GUNS);
		}else{
			gunsFlipper.setDisplayedChild(POSITION_SHOW_GUN_STATS);
		}
	}

	void setupGunListView(View v) {
		ListView speargunsListView = v.findViewById(R.id.listview_speargun_stats);
		allSpearguns = ((SpearoApplication) getActivity().getApplication()).getSpearGuns();
		SharedPreferences app_preferences = getActivity().getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
		boolean isMetric = !app_preferences.getBoolean(Constants.IMPERIAL, false);
		speargunsAdapter = new SpeargunListViewAdapter(getActivity(), R.layout.speargun_stat_row, allSpearguns, isMetric);
		speargunsListView.setAdapter(speargunsAdapter);
	}

	void setupAddGunButton(View v) {

		View.OnClickListener addListener = new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				alertNewSpeargun();
			}
		};

		FloatingActionButton fabAdd = v.findViewById(R.id.fab_add_gun);
			fabAdd.setOnClickListener(addListener);
		FloatingActionButton fabAdd2 = v.findViewById(R.id.fab_add_gun2);
		fabAdd2.setOnClickListener(addListener);
	}

	/**
	 * User inserts new speargun
	 */
	void alertNewSpeargun() {
		newSpeargun = new Speargun();

		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
		View dialogView = getLayoutInflater().inflate(R.layout.custom_newgun_alert_dialog, null);
		dialogBuilder.setView(dialogView);
		AlertDialog alertDialog = dialogBuilder.create();

		spinnerGunBrands(dialogView);
		spinnerGunTypes(dialogView);
		spinnerGunLengths(dialogView);
		setupPositiveNegativeAlertButton(dialogView, alertDialog);

		dialogBuilder.setCancelable(false);
		alertDialog.show();
	}

	void setupPositiveNegativeAlertButton(View dialogView, AlertDialog alertDialog) {
		EditText modelEditText = dialogView.findViewById(R.id.editTextGunModel);
		TextView validationMsgTextView = dialogView.findViewById(R.id.alert_new_gun_error_msg);

		Button positiveButton = dialogView.findViewById(R.id.alertConfirmButton);
		positiveButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				String model = modelEditText.getText().toString();
				String textValidationMsg = validate(newSpeargun, model);
				if (textValidationMsg != null){
					validationMsgTextView.setText(textValidationMsg);
					return;
				}

				newSpeargun.setModel(model);
				alertDialog.cancel();
				validationMsgTextView.setText(null);
				saveNewSpeargunActions();

			}

			private String validate(Speargun newSpeargun, String modelText) {
				if (null == newSpeargun.getBrand() || SpearGunBrand.NO_GUN.equals(newSpeargun.getBrand())){
					return getResources().getString(R.string.no_brand_selected);
				}

				if (null == newSpeargun.getType() || SpeargunType.NO_TYPE.equals(newSpeargun.getType())){
					return getResources().getString(R.string.no_type_selected);
				}

				if (null == newSpeargun.getLength() || newSpeargun.getLength() == 0){
					return getResources().getString(R.string.no_length_selected);
				}

				if (null != modelText && modelText.length() > 30){
					return getResources().getString(R.string.max_model_length_text);
				}

				return null;
			}
		});

		Button negativeButton = dialogView.findViewById(R.id.alertCloseButton);
		negativeButton.setText(getResources().getString(R.string.close));
		negativeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				newSpeargun = null;
				alertDialog.cancel();
			}
		});
	}

	void saveNewSpeargunActions() {
		newSpeargun.setCaughtFish(new ArrayList<>());
		((SpearoApplication) getActivity().getApplication()).addSpeargun(newSpeargun);
		speargunsAdapter.notifyDataSetChanged();
	}

	void spinnerGunTypes(View dialogView) {
		Spinner spinnerGunTypes = dialogView.findViewById(R.id.spinner_gun_types);
		SpeargunType[] speargunTypes = SpeargunType.values();
		SpearGunTypesSpinnerAdapter adapter = new SpearGunTypesSpinnerAdapter(getActivity(), android.R.layout.simple_spinner_item, Arrays.asList(speargunTypes));
		spinnerGunTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
				newSpeargun.setType(SpeargunType.ofPosition(position));
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});

		spinnerGunTypes.setAdapter(adapter);
	}

	void spinnerGunLengths(View dialogView) {
		Spinner spinnerGunLengths = dialogView.findViewById(R.id.spinner_gun_length);

		List<String> gunLengths = new ArrayList<>();
		gunLengths.add(getResources().getString(R.string.select_gun_length));
		int counter = 1;
		for (int i = 20; i <= 150; i=i+5){
			gunLengths.add(counter, String.valueOf(i));
			counter++;
		}

		SpeargunLengthsAdapter adapter = new SpeargunLengthsAdapter(getActivity(), android.R.layout.simple_spinner_item, gunLengths);

		spinnerGunLengths.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {

				if (position > 0) {
					Integer length = Integer.parseInt(gunLengths.get(position));
					newSpeargun.setLength(length);
				}else{
					newSpeargun.setLength(null);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});

		spinnerGunLengths.setAdapter(adapter);
	}

	void spinnerGunBrands(View dialogView) {
		Spinner spinnerGunBrands = dialogView.findViewById(R.id.spinner_gun_brands);
		List<SpearGunBrand> brands = Arrays.asList(SpearGunBrand.values());
		SpeargunBrandsSpinnerAdapter adapter = new SpeargunBrandsSpinnerAdapter(getActivity(), android.R.layout.simple_spinner_item, brands);
		spinnerGunBrands.setAdapter(adapter);

		spinnerGunBrands.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
				newSpeargun.setBrand(SpearGunBrand.ofPosition(position));
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});

	}
	
	public static FrgSpeargunStats init(int val) {
		FrgSpeargunStats truitonList = new FrgSpeargunStats();
        // Supply val input as an argument.
        Bundle args = new Bundle();
        args.putInt("val", val);
        truitonList.setArguments(args);
        return truitonList;
    }

    @Override
	public void notifyChanges(FishingSessionsState state) {
		if (FishingSessionsState.GUN_STATS_CHANGED.equals(state)
		|| FishingSessionsState.ADDED_GUN.equals(state)
		|| FishingSessionsState.REMOVED_GUN.equals(state)){
			speargunsAdapter.notifyDataSetChanged();
			setFlipperChild();
		}
	}

}
