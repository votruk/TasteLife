package ru.kurtov.tastelife.support;

import android.content.Context;

import ru.kurtov.tastelife.support.DatabaseHelper.IngredientCursor;
import ru.kurtov.tastelife.Ingredient;

/**
 * Created by KURT on 14.04.2015.
 */
public class IngredientManager {
	private static final String TAG = "IngredientManager";

	private static IngredientManager sIngredientManager;
	private Context mAppContext;
	private DatabaseHelper mHelper;


	private IngredientManager(Context appContext) {
		mAppContext = appContext;
		mHelper = new DatabaseHelper(mAppContext);
	}

	public static IngredientManager get(Context context) {
		if (sIngredientManager == null) {
			sIngredientManager = new IngredientManager(context.getApplicationContext());
		}
		return sIngredientManager;
	}

	public IngredientCursor getAllIngredients() {
		return mHelper.getAllIngredients();
	}

	public Ingredient getIngredient(long id) {
		Ingredient ingredient = null;
		IngredientCursor cursor = mHelper.getIngredient(id);
		cursor.moveToFirst();
		if (!cursor.isAfterLast()) {
			ingredient = cursor.getIngredient();
		}
		cursor.close();
		return ingredient;
	}
}
