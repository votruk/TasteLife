package ru.kurtov.tastelife;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ru.kurtov.tastelife.support.IngredientLab;

/**
 * Created by KURT on 22.04.2015.
 */
public class IngredientsGridViewFragment extends Fragment{
	private static final String TAG = "IngredientsGVFragment";

	GridView mGridView;
	ArrayList<Ingredient> mIngredients;



	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_list_grid_view, container, false);
		mGridView = (GridView) v.findViewById(R.id.gridView);

		mIngredients = IngredientLab.get(getActivity()).getIngredients();

		setupAdapter();
		return v;
	}

	void setupAdapter() {
		if (getActivity() == null || mGridView == null) {
			return;
		}

		if (mIngredients != null) {
			mGridView.setAdapter(new IngredientAdapter(mIngredients));
		} else {
			mGridView.setAdapter(null);
		}
	}

	private class IngredientAdapter extends ArrayAdapter<Ingredient> {
		public IngredientAdapter(ArrayList<Ingredient> ingredients) {
			super(getActivity(), 0, ingredients);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(R.layout.grid_item, null);
			}

			Ingredient i = getItem(position);
			TextView titleTextView = (TextView)convertView.findViewById(R.id.gridTitleTextView);
			titleTextView.setText(i.getTitle());

			ImageView image = (ImageView) convertView.findViewById(R.id.gridImageView);
			image.setImageResource(i.getPhotoLink());

			return convertView;

		}


	}
}
