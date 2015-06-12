package ru.kurtov.tastelife;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import ru.kurtov.tastelife.support.DatabaseHelper;

/**
 * Created by KURT on 10.04.2015.
 */
public class MainMenuFragment extends Fragment {
	private Button mMixButton;
	private Button mListViewButton;
	private Button mGridViewButton;

	private TextView mCreateDBTextView;
	private ProgressBar mCreateDBProgressBar;

	private static final String DB_NAME = "ingredients.sqlite";
	private static final String DB_PATH = "data/data/ru.kurtov.tastelife/databases/";

	SQLiteDatabase mDatabase;

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_main_menu, container, false);

		mMixButton = (Button) v.findViewById(R.id.mixButton);
		mMixButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(), CircleChooseActivity.class);
				startActivity(i);
			}
		});
		mCreateDBTextView = (TextView) v.findViewById(R.id.createDBTextView);
		mCreateDBProgressBar = (ProgressBar) v.findViewById(R.id.createDBProgressBar);
		mCreateDBProgressBar.setVisibility(View.INVISIBLE);

		mListViewButton = (Button) v.findViewById(R.id.getListViewButton);
		mListViewButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(), IngredientListActivity.class);
				startActivity(i);
			}
		});

		mGridViewButton = (Button) v.findViewById(R.id.getGridViewButton);
		mGridViewButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(), IngredientsGridViewActivity.class);
				startActivity(i);
			}
		});

		try {
			mDatabase = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, 0);
			mDatabase.close();
		} catch (SQLiteException e) {
			createDBTask task = new createDBTask();
			task.execute();
		}

		return v;
	}

	private class createDBTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mCreateDBTextView.setText("Creating DataBase with tastes.");
			mCreateDBProgressBar.setVisibility(View.VISIBLE);
			mMixButton.setEnabled(false);
			mMixButton.setFocusable(false);
		}

		@Override
		protected Void doInBackground(Void... params) {
			DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
			databaseHelper.fillDatabaseWithXml();
			return null;
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			super.onPostExecute(aVoid);
			mCreateDBProgressBar.setVisibility(View.INVISIBLE);
			mMixButton.setEnabled(true);
			mMixButton.setFocusable(true);
			new CountDownTimer(3000, 2000){
				@Override
				public void onTick(long millisUntilFinished) {
					mCreateDBTextView.setText("DataBase have been successfully created.");
				}

				@Override
				public void onFinish() {
					mCreateDBTextView.setText("");
					mCreateDBTextView.setVisibility(View.INVISIBLE);
					mCreateDBTextView.setEnabled(false);
					mCreateDBTextView.setFocusable(false);
				}
			}.start();
		}
	}

}
