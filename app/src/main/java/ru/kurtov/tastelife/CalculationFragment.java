package ru.kurtov.tastelife;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import ru.kurtov.tastelife.support.ChosenIngredients;

/**
 * Created by KURT on 10.03.2015.
 */
public class CalculationFragment extends Fragment {

    private TextView mRatingTextView;
    private int mRating;

    private Button mCreateNewButton;
	private ProgressBar mCalculateResultProgressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_calculation, container, false);

        mRatingTextView = (TextView) v.findViewById(R.id.calculation_ratingTextView);



        mCreateNewButton = (Button) v.findViewById(R.id.create_new_dishButton);
        mCreateNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChosenIngredients.get(getActivity()).deleteAllIngredients();
                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
//                Intent i = new Intent(getActivity(), ChooseIngredientActivity.class);
//                startActivity(i);
            }
        });

		mCalculateResultProgressBar = (ProgressBar) v.findViewById(R.id.calculateResultProgressBar);
		mCalculateResultProgressBar.setVisibility(View.INVISIBLE);

		CalculateResultTask task = new CalculateResultTask();
		task.execute();

        return v;
    }

	private class CalculateResultTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mCalculateResultProgressBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {
			float result = CalculateResult.get(getActivity()).getResultOfMatching();
			mRating = ((int) result);
			return null;

		}

		@Override
		protected void onPostExecute(Void aVoid) {
			super.onPostExecute(aVoid);
			mRatingTextView.setText("" + mRating);
			mCalculateResultProgressBar.setVisibility(View.INVISIBLE);
		}
	}
}
