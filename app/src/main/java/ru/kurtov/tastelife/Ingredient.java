package ru.kurtov.tastelife;

import java.util.UUID;

/**
 * Created by KURT on 19.02.2015.
 */
public class Ingredient {
    private long mId;
    private String mTitle;
    private String mDescription;
    private int mPhotoLink;
	private String mShortName;

	public String getShortName() {
		return mShortName;
	}

	public void setShortName(String shortName) {
		mShortName = shortName;
	}
	//    public Ingredient(){
//
//    }

    public void setId(long id) {
        mId = id;
    }

    public long getId() {
        return mId;
    }


    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public int getPhotoLink() {
        return mPhotoLink;
    }

    public void setPhotoLink(int photoLink) {
        mPhotoLink = photoLink;
    }
}
