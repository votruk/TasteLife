package ru.kurtov.tastelife;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;

import ru.kurtov.tastelife.support.IngredientLab;

/**
 * Created by KURT on 19.02.2015.
 */
public class IngredientPagerActivity extends FragmentActivity {
	private ViewPager mViewPager;
	private ArrayList<Ingredient> mIngredients;


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mViewPager = new ViewPager(this);
		mViewPager.setId(R.id.viewPager);
		setContentView(mViewPager);

		mIngredients = IngredientLab.get(this).getIngredients();

		FragmentManager fm = getSupportFragmentManager();
		mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
			@Override
			public Fragment getItem(int position) {
				Ingredient ingredient = mIngredients.get(position);
				return IngredientFragment.newInstance(ingredient.getId());
			}

			@Override
			public int getCount() {
				return mIngredients.size();
			}
		});

		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}

			@Override
			public void onPageSelected(int position) {
				Ingredient ingredient = mIngredients.get(position);
				if (ingredient.getTitle() != null) {
					setTitle(ingredient.getTitle());
				}
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});

		long ingredientId = (long)getIntent().getSerializableExtra(IngredientFragment.EXTRA_INGREDIENT_ID);
		for (int i = 0; i < mIngredients.size(); i++) {
			if (mIngredients.get(i).getId() == ingredientId) {
				mViewPager.setCurrentItem(i);
				break;
			}
		}
	}
}
