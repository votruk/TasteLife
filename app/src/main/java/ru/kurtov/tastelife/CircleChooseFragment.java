package ru.kurtov.tastelife;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import ru.kurtov.tastelife.support.ChosenIngredients;
import ru.kurtov.tastelife.support.Dimensions;
import ru.kurtov.tastelife.support.IngredientLab;

/**
 * Created by KURT on 12.05.2015.
 */
public class CircleChooseFragment extends Fragment {

	private static final int REQUEST_INGREDIENT = 0;

	private IngredientLab mLab;

	private FloatingActionButton mAddButton;
	private ImageButton mMainButton;
	private ImageButton mDeleteButton;
	private ImageButton mInfoButton;
	private ImageButton mOKButton;

	private Ingredient mSelectedIngredient;
	private ImageView mSelectedImageView;
	private TextView mSelectedTextView;


	private float mScale;

	private TableLayout mIngredientsTableLayout;

	private int mOneCellWidth;


	private ArrayList<Ingredient> mMinorIngredients;
	private Ingredient mMajorIngredient;


	private ImageView mCuttingBoardImageView;
	private int mCuttingBoardHeight;
	private int mTableHeight;

	private final int MARGINS_STANDARD = 16;
	private final int MARGINS_SMALL = 8;


	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		mLab = IngredientLab.get(getActivity());
		Dimensions dimensions = Dimensions.get(getActivity());
		mOneCellWidth = dimensions.getOneCellPX();
		mCuttingBoardHeight = dimensions.getCuttingTableHeightPXPX();
		mScale = dimensions.getScale();
		mTableHeight = dimensions.getTableHeight();
	}


	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_circle_choose, container, false);

		try {
			mMinorIngredients.size();
		} catch (Exception e) {
			mMinorIngredients = new ArrayList<Ingredient>();
		}

		mMajorIngredient = null;

		mAddButton = (FloatingActionButton) v.findViewById(R.id.addImageButton);
		mAddButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), IngredientListActivity.class);
				startActivityForResult(intent, REQUEST_INGREDIENT);
			}
		});

		mMainButton = (ImageButton) v.findViewById(R.id.mainButton);
		mMainButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mSelectedIngredient != null) {
					Ingredient oldMainIngredient = ChosenIngredients.get(getActivity()).getMajorIngredient();
					if (oldMainIngredient == null) {
						ChosenIngredients.get(getActivity()).transferMinorToMajor(mSelectedIngredient);
						mMainButton.setActivated(true);
					} else if (oldMainIngredient == mSelectedIngredient) {
						ChosenIngredients.get(getActivity()).transferMajorToMinor();
						mMainButton.setActivated(false);
					} else {
						Dialog dialog = new Dialog(getActivity());
						// TODO: СДелать всплывающий диалог: Вы хотите заменить главный ингредиент?
					}

					updateIngredientsTable();
				}
			}
		});

		mDeleteButton = (ImageButton) v.findViewById(R.id.deleteButton);

		mInfoButton = (ImageButton) v.findViewById(R.id.infoButton);


		mIngredientsTableLayout = (TableLayout) v.findViewById(R.id.ingredientsTableLayout);

		mSelectedImageView = (ImageView) v.findViewById(R.id.selectedImageView);
		mSelectedTextView = (TextView) v.findViewById(R.id.selectedTextView);


		mCuttingBoardImageView = (ImageView) v.findViewById(R.id.cuttingBoardImageView);

		mCuttingBoardImageView.getLayoutParams().height = mCuttingBoardHeight;

		mOKButton = (ImageButton) v.findViewById(R.id.okImageButton);
		mOKButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(), CalculationActivity.class);
				startActivity(i);
			}
		});

		return v;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_INGREDIENT) {
			if (resultCode == Activity.RESULT_OK) {
				long ingredientId = (long) data.getSerializableExtra(IngredientListFragment.EXTRA_CHOSEN_INGREDIENT);
				Ingredient chosenIngredient = mLab.getIngredient(ingredientId);
				ChosenIngredients.get(getActivity()).addMinorIngredient(chosenIngredient);
				setSelectedIngredient(chosenIngredient);
				mMainButton.setActivated(false);
				boolean update = updateIngredientsTable();
			}
		}
	}

	private boolean updateIngredientsTable() {
		mIngredientsTableLayout.removeAllViews();
		int ingredientCount = ChosenIngredients.get(getActivity()).getIngredientsCount();
		if (ingredientCount == 0) {
			return false;
		}
		boolean isMajorExists = ChosenIngredients.get(getActivity()).isMajorExists();
		if (isMajorExists && ingredientCount == 1) {
			mMajorIngredient = ChosenIngredients.get(getActivity()).getMajorIngredient();
		} else {
			if (isMajorExists) {
				mMajorIngredient = ChosenIngredients.get(getActivity()).getMajorIngredient();
			}
			mMinorIngredients = ChosenIngredients.get(getActivity()).getMinorIngredients();
		}

		int maxColumns = 4;

		int rowCount;
		if (ingredientCount > maxColumns) {
			rowCount = 2;
		} else {
			rowCount = 1;
		}


		int standardMarginPX = (int) (MARGINS_STANDARD * mScale);
		int smallMarginPX = (int) (MARGINS_SMALL * mScale);


		TableRow.LayoutParams rowParams = new TableRow.LayoutParams(
				TableRow.LayoutParams.WRAP_CONTENT,
				TableRow.LayoutParams.WRAP_CONTENT);


		TableRow.LayoutParams itemParams = new TableRow.LayoutParams(
				TableRow.LayoutParams.WRAP_CONTENT,
				TableRow.LayoutParams.WRAP_CONTENT);
		itemParams.height = mOneCellWidth;
		itemParams.width = mOneCellWidth;

		int count = 0;
		for (int i = 0; i < rowCount; i++) {
			TableRow tableRow = new TableRow(getActivity());
			tableRow.setLayoutParams(rowParams);

			int begin;
			int end;
			if (i == 0) {
				begin = 0;
				end = maxColumns > ingredientCount ? ingredientCount : maxColumns;
			} else {
				begin = maxColumns;
				end = ingredientCount;
			}

			for (int j = begin; j < end; j++) {

				ImageView imageView = new ImageView(getActivity());
				int imageSrc;
				if (j == begin) {
					if (i == 0 && isMajorExists) {
						imageSrc = mMajorIngredient.getPhotoLink();
						imageView.setBackgroundResource(R.drawable.stroke_for_images_transparent_red);
						count += 1;
					} else {
						imageSrc = mMinorIngredients.get(j - count).getPhotoLink();
						imageView.setBackgroundResource(R.drawable.stroke_for_images_transparent);
					}
				} else {
					imageSrc = mMinorIngredients.get(j - count).getPhotoLink();
					imageView.setBackgroundResource(R.drawable.stroke_for_images_transparent);

				}
				itemParams.setMargins(0, 0, smallMarginPX, smallMarginPX);
				imageView.setImageResource(imageSrc);
				imageView.setAdjustViewBounds(true);
				imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
				imageView.setLayoutParams(itemParams);
				tableRow.addView(imageView);
			}
			mIngredientsTableLayout.requestLayout();
			mIngredientsTableLayout.addView(tableRow, rowParams);
		}

		return true;
	}


	private void setSelectedIngredient(Ingredient ingredient) {
		mSelectedIngredient = ingredient;
		mSelectedImageView.setImageResource(ingredient.getPhotoLink());
		mSelectedTextView.setText(ingredient.getTitle());
	}

	private int dpToPixels(int i) {
		return (int) (i * mScale + 0.5f);
	}
}
