package ru.kurtov.tastelife;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import ru.kurtov.tastelife.support.ChosenIngredients;
import ru.kurtov.tastelife.support.DatabaseHelper.IngredientCursor;
import ru.kurtov.tastelife.support.IngredientLab;

/**
 * Created by KURT on 19.02.2015.
 */
public class IngredientListFragment extends ListFragment{
    private ArrayList<Ingredient> mIngredients;

	private IngredientCursor mCursor;

    private static final int REQUEST_CHOSEN_INGREDIENT = 0;
    public static final String EXTRA_CHOSEN_INGREDIENT = "ru.kurtov.tastelife.chose_ingredient";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Ingredients");
		mIngredients = IngredientLab.get(getActivity()).getIngredients();
        IngredientAdapter adapter = new IngredientAdapter(mIngredients);
        setListAdapter(adapter);
//		mCursor = IngredientManager.get(getActivity()).getAllIngredients();
//		IngredientCursorAdapter adapter = new IngredientCursorAdapter(getActivity(), mCursor);
//		setListAdapter(adapter);
	}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CHOSEN_INGREDIENT) {
            if (resultCode == Activity.RESULT_OK) {
                long ingredientId = (long) data.getSerializableExtra(IngredientFragment.EXTRA_CHOSEN_INGREDIENT);
                Intent intent = new Intent();
                intent.putExtra(EXTRA_CHOSEN_INGREDIENT, ingredientId);
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
//		((IngredientCursorAdapter)getListAdapter()).notifyDataSetChanged();
        ((IngredientAdapter)getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
//		Intent i = new Intent(getActivity(), IngredientPagerActivity.class);
//		i.putExtra(IngredientFragment.EXTRA_INGREDIENT_ID, id);
//		startActivityForResult(i, REQUEST_CHOSEN_INGREDIENT);

        Ingredient ingredient = ((IngredientAdapter)getListAdapter()).getItem(position);
        Intent intent = new Intent(getActivity(), IngredientPagerActivity.class);
        intent.putExtra(IngredientFragment.EXTRA_INGREDIENT_ID, ingredient.getId());
//		Запускаем интент и ожидаемаем результата, как ой ингредиент выбран
        startActivityForResult(intent, REQUEST_CHOSEN_INGREDIENT);

	}

    private class IngredientAdapter extends ArrayAdapter<Ingredient>{
        public IngredientAdapter(ArrayList<Ingredient> ingredients) {
            super(getActivity(), 0, ingredients);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_ingredient, null);
            }

            Ingredient i = getItem(position);
            TextView titleTextView = (TextView)convertView.findViewById(R.id.ingredient_list_itemTextView);
            titleTextView.setText(i.getTitle());

            ImageButton b = (ImageButton) convertView.findViewById(R.id.ingredient_list_itemButton);
            b.setFocusable(false);
            if (ChosenIngredients.get(getActivity()).isIngredientChosen(i)) {
                b.setEnabled(false);
            } else {
                b.setEnabled(true);
            }

            return convertView;

        }
    }

//	private static class IngredientCursorAdapter extends CursorAdapter {
//
//		private IngredientCursor mIngredientCursor;
//
//		public IngredientCursorAdapter(Context context, IngredientCursor ingredientCursor) {
//			super(context, ingredientCursor, 0);
//			mIngredientCursor = ingredientCursor;
//		}
//
//		@Override
//		public View newView(Context context, Cursor cursor, ViewGroup parent) {
//			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//			return inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
//		}
//
//		@Override
//		public void bindView(View view, Context context, Cursor cursor) {
//			Ingredient ingredient = mIngredientCursor.getIngredient();
//
//			TextView ingredientTextView = (TextView) view;
//			String ingredientName = ingredient.getTitle();
//			ingredientTextView.setText(ingredientName);
//		}
//	}

}
