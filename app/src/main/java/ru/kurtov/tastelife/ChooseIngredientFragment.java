package ru.kurtov.tastelife;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ru.kurtov.tastelife.support.ChosenIngredients;
import ru.kurtov.tastelife.support.IngredientLab;

/**
* Created by KURT on 19.02.2015.
*/
public class ChooseIngredientFragment extends Fragment {
    private static final int REQUEST_MAJOR_INGREDIENT = 0;
    private static final int REQUEST_MINOR_INGREDIENT = 1;
    private static final int REQUEST_CALCULATION = 2;

    private static final String TAG = "ChooseIngredient";

    private static final int MAX_MINOR_INGREDIENTS = 4;

    private TextView mMajorTextView;
    private TextView mMinorTextView;

//	private IngredientManager mManager;
	private IngredientLab mLab;

    private Button mMajorButton;
    private Button mMinorButton;
    private Button mDeleteButton;

    private Button mCalculateButton;

    private ArrayList<Ingredient> mMinorIngredients;
    private Ingredient mMajorIngredient;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

//		mManager = IngredientManager.get(getActivity());
		mLab = IngredientLab.get(getActivity());
	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_choose_ingredient, parent, false);

        try {
            mMinorIngredients.size();
        } catch (Exception e) {
            mMinorIngredients = new ArrayList<Ingredient>();
        }

        mMajorIngredient = null;

        mMajorButton = (Button)v.findViewById(R.id.majorIngredientButton);
        mMajorButton.setOnClickListener(chooseIngredient(REQUEST_MAJOR_INGREDIENT));
        // TODO Нужно сохранить, какая кнопка была нажата

        mMinorButton = (Button)v.findViewById(R.id.minorIngredientButton);
        mMinorButton.setOnClickListener(chooseIngredient(REQUEST_MINOR_INGREDIENT));
        // TODO Нужно сохранить, какая кнопка была нажата

        mMinorTextView = (TextView) v.findViewById(R.id.minorIngredientTextView);
        updateMinorText();

        mMajorTextView = (TextView) v.findViewById(R.id.majorIngredientTextView);
        updateMajorText();

        mDeleteButton = (Button) v.findViewById(R.id.deleteIngredientButton);
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Должен запускаться фрагмент со списком ингредиентов, чтобы можно было выбрать ингредиент и удалить его
                deleteAllIngredients();
            }
        });

        mCalculateButton = (Button) v.findViewById(R.id.calculateButton);
        mCalculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), CalculationActivity.class);
                startActivityForResult(i, REQUEST_CALCULATION);
            }
        });

        return v;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CALCULATION) {
            if (resultCode == Activity.RESULT_OK) {
                updateMinorText();
                updateMajorText();
                return;
            }
        }

        if (resultCode == Activity.RESULT_OK) {
            long ingredientId = (long) data.getSerializableExtra(IngredientListFragment.EXTRA_CHOSEN_INGREDIENT);
//            Ingredient chosenIngredient = IngredientLab.get(getActivity()).getIngredient(ingredientId);
			Ingredient chosenIngredient = mLab.getIngredient(ingredientId);
//			Ingredient chosenIngredient = mManager.getIngredient(ingredientId);

            Log.d(TAG, "Chosen ingredient is " + chosenIngredient.getTitle());

            if (requestCode == REQUEST_MINOR_INGREDIENT) {
                ChosenIngredients.get(getActivity()).addMinorIngredient(chosenIngredient);
                updateMinorText();
            } else if (requestCode == REQUEST_MAJOR_INGREDIENT) {
                ChosenIngredients.get(getActivity()).addMajorIngredient(chosenIngredient);
                updateMajorText();
            }
        }

    }

    private void updateMinorText(){
        try{
            String s = "";
            mMinorIngredients = ChosenIngredients.get(getActivity()).getMinorIngredients();
            for (Ingredient i : mMinorIngredients) {
                s += i.getTitle() + "\n";
            }
            mMinorTextView.setText(s);
        } catch (Exception e) {
            Log.d(TAG, "List of minor ingredients is empty");
        }
    }

    private void updateMajorText(){
        mMajorIngredient = ChosenIngredients.get(getActivity()).getMajorIngredient();
        String s = "";
        if (mMajorIngredient != null) {
            s = mMajorIngredient.getTitle();
        }
        mMajorTextView.setText(s);
    }

    private View.OnClickListener chooseIngredient(final int requestCode) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (requestCode == REQUEST_MAJOR_INGREDIENT) {
                    mMajorIngredient = ChosenIngredients.get(getActivity()).getMajorIngredient();
                    if (mMajorIngredient != null) {
                        Toast.makeText(getActivity(), "Major ingredient has been already chosen", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else if (requestCode == REQUEST_MINOR_INGREDIENT) {
                    mMinorIngredients = ChosenIngredients.get(getActivity()).getMinorIngredients();
                    if (mMinorIngredients.size() == MAX_MINOR_INGREDIENTS) {
                        Toast.makeText(getActivity(), "Max number of minor ingredients (" +
                                MAX_MINOR_INGREDIENTS + ") has been chosen", Toast.LENGTH_SHORT)
                                .show();
                        return;
                    }
                }
                Intent intent = new Intent(getActivity(), IngredientListActivity.class);
                startActivityForResult(intent, requestCode);
            }
        };
    }

    private void deleteAllIngredients() {
        ChosenIngredients.get(getActivity()).deleteAllIngredients();
        updateMajorText();
        updateMinorText();
    }

}
