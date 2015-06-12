package ru.kurtov.tastelife.support;

/**
 * Created by KURT on 10.04.2015.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import ru.kurtov.tastelife.Ingredient;
import ru.kurtov.tastelife.R;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static final String TAG = "DatabaseHelper";

	// Declare Variables
	private static final String DB_NAME = "ingredients.sqlite";
	private static final String DB_PATH = "data/data/ru.kurtov.tastelife/databases/";

	private static final int DB_VERSION = 1;

	private static final String TABLE_INFO_NAME = "tableinfo";
	private static final String TABLE_MATCH_NAME = "tablematch";

	private static final String COLUMN_SHORT_NAME = "ShortName";
	private static final String COLUMN_DESCRIPTION = "Description";
	private static final String COLUMN_INGREDIENT = "Ingredient";
	private static final String COLUMN_PHOTO_LINK = "PhotoLink";
	private static final String COLUMN_ID = "_id";

	private String mText;
	private ContentValues mCV;

	private ArrayList<String> mIngredients;
	private Context mAppContext;

	private int mIngredientsInfo = R.raw.ingredients_last_small;
	private int mIngredientsMatches = R.raw.ingredients_table_last_small;


	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		mAppContext = context;
//		createDBIfNotExist();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		XMLPullParserFirstList parser = new XMLPullParserFirstList(mAppContext);
		mIngredients = parser.getIngredientsList();


		// Создание таблицы с информацией
		String createQuery = "CREATE TABLE " + TABLE_INFO_NAME + " (" +
				COLUMN_ID + " integer primary key autoincrement, " +
				COLUMN_INGREDIENT + " text, " +
				COLUMN_SHORT_NAME + " text, " +
				COLUMN_PHOTO_LINK + " integer, " +
				COLUMN_DESCRIPTION + " text);";
		db.execSQL(createQuery);


		// Создание таблицы с цифрами
		createQuery = "CREATE TABLE " + TABLE_MATCH_NAME + " (" +
				COLUMN_ID + " REFERENCES " + TABLE_INFO_NAME + "(" + COLUMN_ID + "), " +
				COLUMN_SHORT_NAME + " text";

		for (String shortName : mIngredients) {
			createQuery = createQuery + ", " + shortName + " real";
		}
//		createQuery = createQuery + ", Ingredient_id REFERENCES " + TABLE_INFO_NAME + "(" + COLUMN_ID + "));";
		createQuery = createQuery + ");";

		db.execSQL(createQuery);
//		db.close();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//		// Database will be wipe on version change
//		db.execSQL("DROP TABLE IF EXISTS " + TABLE_INFO_NAME);
//		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MATCH_NAME);
//		onCreate(db);
	}

	public void fillDatabaseWithXml() {


		InputStream is = mAppContext.getResources().openRawResource(mIngredientsInfo);

		XmlPullParserFactory factory = null;
		XmlPullParser parser = null;


		mCV = new ContentValues();
		try {
			factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			parser = factory.newPullParser();

			parser.setInput(is, null);

			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				String tagname = parser.getName();
				switch (eventType) {
					case XmlPullParser.START_TAG:
						if (tagname.equalsIgnoreCase("row")) {
							mCV.clear();
						}
						break;

					case XmlPullParser.TEXT:
						mText = parser.getText();
						break;

					case XmlPullParser.END_TAG:
						if (tagname.equalsIgnoreCase("row")) {
							// add employee object to list
							getWritableDatabase().insert(TABLE_INFO_NAME, null, mCV);
						} else if (tagname.equalsIgnoreCase(COLUMN_SHORT_NAME)) {
							mCV.put(tagname, mText);
							int imageResource = mAppContext.getResources().getIdentifier("unknown", "drawable", "ru.kurtov.tastelife");
							try {
								imageResource = mAppContext.getResources().getIdentifier(mText.toLowerCase(), "drawable", "ru.kurtov.tastelife");
							} catch (Exception e) {
								Log.d(TAG, "There is no image for ingredient: " + mText);
							}
							mCV.put(COLUMN_PHOTO_LINK, imageResource);
						} else {
							mCV.put(tagname, mText);
						}
						break;
					default:
						break;
				}
				eventType = parser.next();
			}

		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}


		factory = null;
		parser = null;

		is = mAppContext.getResources().openRawResource(mIngredientsMatches);

		mCV = new ContentValues();
		try {
			factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			parser = factory.newPullParser();

			parser.setInput(is, null);

			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				String tagname = parser.getName();
				switch (eventType) {
					case XmlPullParser.START_TAG:
						if (tagname.equalsIgnoreCase("row")) {
							mCV.clear();
						}
						break;

					case XmlPullParser.TEXT:
						mText = parser.getText();
						break;

					case XmlPullParser.END_TAG:
						if (tagname.equalsIgnoreCase("row")) {
							// add employee object to list
							getWritableDatabase().insert(TABLE_MATCH_NAME, null, mCV);
						} else if (tagname.equalsIgnoreCase(COLUMN_SHORT_NAME)) {
							mCV.put(tagname, mText);
							long id = getId(mText);
							mCV.put(COLUMN_ID, id);
						} else {
							mCV.put(tagname, mText);
						}
						break;
					default:
						break;
				}
				eventType = parser.next();
			}

		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		getWritableDatabase().close();

	}

	public IngredientCursor getAllIngredients() {
		Cursor wrapped = getReadableDatabase().query(
				TABLE_INFO_NAME,
				null,
				null,
				null,
				null,
				null,
				COLUMN_INGREDIENT + " asc");
		return new IngredientCursor(wrapped);
	}

	public IngredientCursor getIngredient(long id) {
		Cursor wrapped = getReadableDatabase().query(
				TABLE_INFO_NAME,
				null,
				COLUMN_ID + " = ?",
				new String[]{String.valueOf(id)},
				null,
				null,
				null,
				"1");
		return new IngredientCursor(wrapped);
	}

	public float[] getIngredientsMatch(Ingredient mainIngredient, ArrayList<Ingredient> otherIngredients) {

		int numberOfIngredients = otherIngredients.size();

		String createQuery = "SELECT ";
		for (int i = 0; i < numberOfIngredients; i++) {
			createQuery = createQuery + otherIngredients.get(i).getShortName();
			if (i != numberOfIngredients - 1) {
				createQuery = createQuery + ", ";
			}
		}
		createQuery = createQuery + " FROM " + TABLE_MATCH_NAME + " WHERE " + COLUMN_SHORT_NAME + " = ?;";

		String[] searchingIngredient = {mainIngredient.getShortName()};
		Cursor cursor = getReadableDatabase().rawQuery(createQuery, searchingIngredient);

		String answer = DatabaseUtils.dumpCursorToString(cursor);

		float[] result = new float[numberOfIngredients];
		if (cursor.moveToFirst()) {
			if (!cursor.isAfterLast()) {
				for (int i = 0; i < numberOfIngredients; i++) {
					result[i] = cursor.getFloat(cursor.getColumnIndex(otherIngredients.get(i).getShortName()));
				}
				// do what ever you want here
			}
		}
		cursor.close();
		getReadableDatabase().close();
		return result;
	}

	public static class IngredientCursor extends CursorWrapper {
		public IngredientCursor(Cursor cursor) {
			super(cursor);
		}

		public Ingredient getIngredient() {
			if (isBeforeFirst() || isAfterLast()) {
				return null;
			}
			Ingredient ingredient = new Ingredient();
			int ingredientId = getInt(getColumnIndex(COLUMN_ID));
			ingredient.setId(ingredientId);
			String shortName = getString(getColumnIndex(COLUMN_SHORT_NAME));
			ingredient.setShortName(shortName);
			String title = getString(getColumnIndex(COLUMN_INGREDIENT));
			ingredient.setTitle(title);
			String description = getString(getColumnIndex(COLUMN_DESCRIPTION));
			ingredient.setDescription(description);
			int photoLink = (int) getLong(getColumnIndex(COLUMN_PHOTO_LINK));
			ingredient.setPhotoLink(photoLink);
			return ingredient;
		}
	}


	public void createDBIfNotExist() {
		try {
			SQLiteDatabase database = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, 0);
		} catch (SQLiteException e) {
			fillDatabaseWithXml();
		}
	}

	private int getId(String shortName) {

		SQLiteDatabase database = getReadableDatabase();

		String createQuery = String.format("SELECT %s FROM %s WHERE %s = ?",
				COLUMN_ID, TABLE_INFO_NAME, COLUMN_SHORT_NAME);
		Cursor cursor = database.rawQuery(createQuery, new String[]{shortName});
		String answer2 = DatabaseUtils.dumpCursorToString(cursor);
		int id = 0;
		if (cursor.moveToFirst()) {
			if (!cursor.isAfterLast()) {
				id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
			}
		}
		cursor.close();
//		database.close();
		return id;
	}
}
