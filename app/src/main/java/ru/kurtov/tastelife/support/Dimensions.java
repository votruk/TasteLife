package ru.kurtov.tastelife.support;

import android.app.Activity;
import android.graphics.Point;
import android.view.Display;


public class Dimensions {

	private Activity mActivity;
	private float mScale;
	private int mScreenWidth;
	private int mScreenHeight;
	private int mPaddingLayout;

	private final int PADDING_STANDART = 8;
	private final int COLUMNS_COUNT = 4;
	private final int MARGIN_STANDARD = 16;
	private final int ROWS_COUNT = 2;

	private int mOneCellPX;

	private int mCuttingTableHeightPX;



	private int mTableHeight;


	private static Dimensions sDimensions;

	private Dimensions(Activity activity) {
		mActivity = activity;
		mScale = mActivity.getResources().getDisplayMetrics().density;


		Display display = mActivity.getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		mScreenWidth = size.x;
		mScreenHeight = size.y;

		mPaddingLayout = (int) (MARGIN_STANDARD * 2 * mScale);

		mOneCellPX = (mScreenWidth
				- mPaddingLayout
				- dpToPixels(MARGIN_STANDARD * 2)
				- dpToPixels((COLUMNS_COUNT - 1) * PADDING_STANDART)) / COLUMNS_COUNT;

		mCuttingTableHeightPX = (mOneCellPX + dpToPixels(MARGIN_STANDARD)) * ROWS_COUNT
				+ dpToPixels(PADDING_STANDART);

		mTableHeight = mOneCellPX * 2 + dpToPixels(MARGIN_STANDARD);

	}

	public static Dimensions get(Activity activity) {
		if (sDimensions == null) {
			sDimensions = new Dimensions(activity);
		}
		return sDimensions;
	}

	private int dpToPixels(int i) {
		return (int) (i * mScale + 0.5f);
	}

	public int getOneCellPX() {
		return mOneCellPX;
	}


	public float getScale() {
		return mScale;
	}

	public int getCuttingTableHeightPXPX() {
		return mCuttingTableHeightPX;
	}
	public int getTableHeight() {
		return mTableHeight;
	}

}
