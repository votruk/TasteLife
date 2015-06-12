package ru.kurtov.tastelife.support;

import android.content.Context;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import ru.kurtov.tastelife.R;

/**
 * Created by KURT on 11.03.2015.
 */
public class XMLPullParserFirstList {

    private ArrayList<String> mIngredients;
    private String mText;
    private Context mAppContext;

	private static final String XML_INGREDIENT = "ShortName";

    private int mIngredientsInfo = R.raw.ingredients_last_small;

    public XMLPullParserFirstList(Context c) {
        mAppContext = c;
    }

    public ArrayList<String> getIngredientsList(){
        InputStream is = mAppContext.getResources().openRawResource(mIngredientsInfo);

        mIngredients = new ArrayList<String>();
		XmlPullParserFactory factory = null;
		XmlPullParser parser = null;
		try {
			factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			parser = factory.newPullParser();

			parser.setInput(is, null);

			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				String tagname = parser.getName();
				switch (eventType) {
					case XmlPullParser.TEXT:
						mText = parser.getText();
						break;
					case XmlPullParser.END_TAG:
						if (tagname.equalsIgnoreCase(XML_INGREDIENT)) {
							mIngredients.add(mText);
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
		return mIngredients;
	}
}
