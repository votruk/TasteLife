package ru.kurtov.tastelife;

import android.content.Context;

import java.util.ArrayList;

import ru.kurtov.tastelife.support.ChosenIngredients;
import ru.kurtov.tastelife.support.DatabaseHelper;

/**
 * Created by KURT on 15.04.2015.
 */
public class CalculateResult {
	private Context mAppContext;
	private static CalculateResult sCalculateResult;
	private DatabaseHelper mHelper;

	private float mMaxResult;
	private float mResult;
	private int mNumberOfIngredients;

	private CalculateResult(Context context) {
		mAppContext = context;
		mHelper = new DatabaseHelper(mAppContext);
	}

	public static CalculateResult get(Context context) {
		if (sCalculateResult == null) {
			sCalculateResult = new CalculateResult(context.getApplicationContext());
		}
		return sCalculateResult;
	}

	public float getResultOfMatching() {
		Ingredient majorIngredient = ChosenIngredients.get(mAppContext).getMajorIngredient();
		ArrayList<Ingredient> minorIngredients = ChosenIngredients.get(mAppContext).getMinorIngredients();
		mNumberOfIngredients = minorIngredients.size();
		mMaxResult = 0;
		mResult = 0;

		if (majorIngredient != null) {
			mMaxResult += mNumberOfIngredients * 2;
			mResult += matchIngredients(majorIngredient, minorIngredients) * 2;
		}
		ArrayList<Ingredient> otherIngredients =  new ArrayList<>(minorIngredients);
		for (int i = 0; i < mNumberOfIngredients - 1; i++) {
			mMaxResult += i + 1;
			otherIngredients.remove(0);
			mResult += matchIngredients(minorIngredients.get(i), otherIngredients);
		}
		return mResult/mMaxResult * 100;
	}

	public float matchIngredients(Ingredient majorIngredient, ArrayList<Ingredient> minorIngredients) {
		mHelper = new DatabaseHelper(mAppContext);
		float[] preResult = mHelper.getIngredientsMatch(majorIngredient, minorIngredients);
		float result = 0;
		for (float f : preResult) {
			result += f;
		}
		return result;
	}
}
