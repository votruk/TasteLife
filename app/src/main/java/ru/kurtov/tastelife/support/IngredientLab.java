package ru.kurtov.tastelife.support;

import android.content.Context;

import java.util.ArrayList;

import ru.kurtov.tastelife.support.DatabaseHelper.IngredientCursor;
import ru.kurtov.tastelife.Ingredient;

/**
* Created by KURT on 19.02.2015.
*/
public class IngredientLab {

    private static final String TAG = "IngredientLab";
    private ArrayList<Ingredient> mIngredients;

    private static IngredientLab sIngredientLab;
    private Context mAppContext;

	private IngredientCursor mCursor;


	private DatabaseHelper mHelper;

    private IngredientLab(Context appContext) {
        mAppContext = appContext;


		mHelper = new DatabaseHelper(mAppContext);
		mCursor = mHelper.getAllIngredients();


		ArrayList<Ingredient> mArrayList = new ArrayList<Ingredient>();
		for(mCursor.moveToFirst(); !mCursor.isAfterLast(); mCursor.moveToNext()) {
			// The Cursor is now set to the right position
			mArrayList.add(mCursor.getIngredient());

			// FOR TESTING PURPOSE
//			if (mArrayList.size() == 24) {
//				break;
//			}
		}
		mIngredients = mArrayList;
	}


    public static IngredientLab get(Context c) {
        if (sIngredientLab == null){
            sIngredientLab = new IngredientLab(c.getApplicationContext());
        }
        return sIngredientLab;
    }

    public ArrayList<Ingredient> getIngredients() {
        return mIngredients;
    }

    public Ingredient getIngredient(long id) {
        for (Ingredient i : mIngredients) {
            if (i.getId() == id)
                return i;
        }
        return null;
    }
}
