package ru.kurtov.tastelife;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import ru.kurtov.tastelife.support.ChosenIngredients;
import ru.kurtov.tastelife.support.IngredientLab;

/**
 * Created by KURT on 19.02.2015.
 */
public class IngredientFragment extends Fragment {
    public static final String EXTRA_INGREDIENT_ID = "ru.kurtov.tastelife.ingredient_id";
    public static final String EXTRA_CHOSEN_INGREDIENT = "ru.kurtov.tastelife.major_ingredient";


    private Button mConfirmIngredientButton;

    private Ingredient mIngredient;

//	private IngredientManager mManager;
	private IngredientLab mLab;

    private TextView mTitleTextView;
    private TextView mDescriptionTextView;
    private ImageView mIngredientImageView;

    public static IngredientFragment newInstance(long ingredientId) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_INGREDIENT_ID, ingredientId);
        IngredientFragment fragment = new IngredientFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long ingredientId = (long)getArguments().getSerializable(EXTRA_INGREDIENT_ID);
		mLab = IngredientLab.get(getActivity());
//		mManager = IngredientManager.get(getActivity());
		mIngredient = mLab.getIngredient(ingredientId);
//		mIngredient = mManager.getIngredient(ingredientId);


    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ingredient, parent, false);

        getActivity().setResult(Activity.RESULT_CANCELED);

        mTitleTextView = (TextView) v.findViewById(R.id.ingredientTitleTextView);
        mTitleTextView.setText(mIngredient.getTitle());

        mDescriptionTextView = (TextView)v.findViewById(R.id.ingredientDescriptionTextView);
        mDescriptionTextView.setText(mIngredient.getDescription());

        mConfirmIngredientButton = (Button)v.findViewById(R.id.confirmIngredientButton);
        mConfirmIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean b = ChosenIngredients.get(getActivity()).isIngredientChosen(mIngredient);
                if (b) {
                    Toast.makeText(getActivity(), "This ingredient is already have been chosen", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra(EXTRA_CHOSEN_INGREDIENT, mIngredient.getId());
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
            }
        });

        mIngredientImageView = (ImageView) v.findViewById(R.id.ingredientImageView);
		if (mIngredient.getPhotoLink() != 0) {
			mIngredientImageView.setImageResource(mIngredient.getPhotoLink());
		}

        return v;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(EXTRA_INGREDIENT_ID, mIngredient.getId());
    }

}
