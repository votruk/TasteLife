package ru.kurtov.tastelife.support;

import android.content.Context;
import android.content.res.Resources;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import ru.kurtov.tastelife.Ingredient;
import ru.kurtov.tastelife.R;

/**
 * Created by KURT on 11.03.2015.
 */
public class XMLPullParserHandler {

    ArrayList<Ingredient> mIngredients;
    private Ingredient mIngredient;
    private String mText;
    private Context mAppContext;

    private int mIngredientsInfo = R.raw.ingredients_last_small;

    public XMLPullParserHandler(Context c) {
        mAppContext = c;
    }

    public ArrayList<Ingredient> loadIngredients() {
        InputStream is = mAppContext.getResources().openRawResource(mIngredientsInfo);

        mIngredients = new ArrayList<Ingredient>();
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
                    case XmlPullParser.START_TAG:
                        if (tagname.equalsIgnoreCase("row")) {
                            // create a new instance of employee
                            mIngredient = new Ingredient();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        mText = parser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase("row")) {
                            // add employee object to list
                            mIngredients.add(mIngredient);
                        } else if (tagname.equalsIgnoreCase("ingredient")) {
                            mIngredient.setTitle(mText);
                        } else if (tagname.equalsIgnoreCase("shortname")) {
                            mIngredient.setShortName(mText);
                        } else if (tagname.equalsIgnoreCase("description")) {
                            mIngredient.setDescription(mText);
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
