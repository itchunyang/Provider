package com.itchunyang.provider;

import android.content.UriMatcher;


/**
 * Created by lcy on 2016/11/11.
 */

public class ContentData {

    public static final String AUTHORITY = "com.itchunyang.girlProvider";
    public static final String DATABASE_NAME = "girl.db";
    public static final int GIRL = 1;
    public static final int GIRLS = 2;

    public static final class Table {
        public static final String TABLE_NAME = "girl";

        public static final String NAME = "name";
        public static final String AGE = "age";
        public static final String SEX = "sex";
        public static final String ADDRESS = "address";
    }

    public static final UriMatcher MATCHER;
    static{
        MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        MATCHER.addURI(AUTHORITY,"girl",GIRLS);
        MATCHER.addURI(AUTHORITY,"girl/#",GIRL);
    }
}
