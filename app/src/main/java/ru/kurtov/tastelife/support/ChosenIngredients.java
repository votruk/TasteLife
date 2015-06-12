package ru.kurtov.tastelife.support;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import ru.kurtov.tastelife.Ingredient;

/**
 * Created by KURT on 10.03.2015.
 */
public class ChosenIngredients {
	private static final String TAG = "ChosenIngredients";

	private ArrayList<Ingredient> mMinorIngredients;
	private Ingredient mMajorIngredient;

	private static ChosenIngredients sChosenIngredients;

	private Context mAppContext;

	private ChosenIngredients(Context appContext) {
		mAppContext = appContext;

		try {
			mMinorIngredients.size();
		} catch (Exception e) {
			mMinorIngredients = new ArrayList<Ingredient>();
		}

		try {
			mMajorIngredient.getTitle();
		} catch (Exception e) {
			mMajorIngredient = null;
		}
	}

	public static ChosenIngredients get(Context c) {
		if (sChosenIngredients == null) {
			sChosenIngredients = new ChosenIngredients(c.getApplicationContext());
		}
		return sChosenIngredients;
	}

	public boolean isIngredientChosen(Ingredient ingredient) {
		if (mMajorIngredient != null && mMajorIngredient.getTitle().equals(ingredient.getTitle())) {
			Log.d(TAG, mMajorIngredient.getTitle() + " equals " + ingredient.getTitle());
			return true;
		} else {
			for (Ingredient i : mMinorIngredients) {
				if (i.getTitle().equals(ingredient.getTitle())) {
					Log.d(TAG, i.getTitle() + " equals " + ingredient.getTitle());
					return true;
				}
			}
		}
		return false;
	}

	public void addMinorIngredient(Ingredient ingredient) {
		mMinorIngredients.add(ingredient);
	}

	public void addMajorIngredient(Ingredient ingredient) {
		mMajorIngredient = ingredient;
	}

	public ArrayList<Ingredient> getMinorIngredients() {

		return mMinorIngredients;
	}

	public Ingredient getMajorIngredient() {
		return mMajorIngredient;
	}

	public void deleteAllIngredients() {
		mMajorIngredient = null;
		mMinorIngredients = new ArrayList<Ingredient>();
	}

	public void removeMinorIngredient(Ingredient ingredient) {
		for (Ingredient i : mMinorIngredients) {
			if (i == ingredient) {
				mMinorIngredients.remove(i);
				Log.d(TAG, ingredient.getShortName() + " was removed from MinorList");
				return;
			}
		}
		Log.d(TAG, ingredient.getShortName() + "was not found in MinorList");
	}

	public boolean transferMinorToMajor(Ingredient ingredient) {
		if (isMajorExists()) {
			Log.d(TAG, "Major Ingredient " + mMajorIngredient.getShortName() + " already exists");
			return false;
		} else {
			mMajorIngredient = ingredient;
			removeMinorIngredient(ingredient);
			return true;
		}
	}

	public void transferMajorToMinor() {
		mMinorIngredients.add(mMajorIngredient);
		mMajorIngredient = null;
	}

	public boolean isMajorExists() {
		if (mMajorIngredient != null) {
			return true;
		} else {
			return false;
		}

	}


	public int getIngredientsCount() {
		int ingredientsCount = 0;
		try {
			ingredientsCount = mMinorIngredients.size();
		} catch (Exception e) {

		}
		if (mMajorIngredient != null) {
			ingredientsCount += 1;
		}

		return ingredientsCount;
	}

}
