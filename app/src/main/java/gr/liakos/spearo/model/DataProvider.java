
package gr.liakos.spearo.model;

import gr.liakos.spearo.model.ContentDescriptor.FishAverageStatistic;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class DataProvider extends ContentProvider {
    private Database database;

    //private static final String sOrderAsc = "%s ASC";
    private static final String sWhere = "%s = ?";
    private static final String sWhereLike = "%s LIKE '%%%s%%'";
    //private static final String sJoin = "%s JOIN %s ON %s=%s";

    @Override
    public boolean onCreate() {
        Context ctx = getContext();
        database = new Database(ctx);
        return true;
    }

    @Override
    public String getType(Uri uri) {
        //Log.v(TAG, String.format("GetType for uri [%s]", uri));
        final int match = ContentDescriptor.URI_MATCHER.match(uri);
        switch (match) {


            case ContentDescriptor.FishingSession.PATH_TOKEN:
            case ContentDescriptor.FishingSession.PATH_START_LETTERS_TOKEN:
                return ContentDescriptor.FishingSession.CONTENT_TYPE_DIR;


            case ContentDescriptor.FishingSession.PATH_FOR_ID_TOKEN:
                return ContentDescriptor.FishingSession.CONTENT_ITEM_TYPE;

                
            //********

            case ContentDescriptor.FishCatch.PATH_TOKEN:
            case ContentDescriptor.FishCatch.PATH_START_LETTERS_TOKEN:
                return ContentDescriptor.FishCatch.CONTENT_TYPE_DIR;


            case ContentDescriptor.FishCatch.PATH_FOR_ID_TOKEN:
                return ContentDescriptor.FishCatch.CONTENT_ITEM_TYPE;

            //********
                

            case ContentDescriptor.Fish.PATH_TOKEN:
            case ContentDescriptor.Fish.PATH_START_LETTERS_TOKEN:
                return ContentDescriptor.Fish.CONTENT_TYPE_DIR;


            case ContentDescriptor.Fish.PATH_FOR_ID_TOKEN:
                return ContentDescriptor.Fish.CONTENT_ITEM_TYPE;
                
           //********
                
            case ContentDescriptor.FishAverageStatistic.PATH_TOKEN:
            case ContentDescriptor.FishAverageStatistic.PATH_START_LETTERS_TOKEN:
                return ContentDescriptor.FishAverageStatistic.CONTENT_TYPE_DIR;


            case ContentDescriptor.FishAverageStatistic.PATH_FOR_ID_TOKEN:
                return ContentDescriptor.FishAverageStatistic.CONTENT_ITEM_TYPE;     


            default:
                throw new UnsupportedOperationException("URI " + uri + " is not supported.");
        }
    }

    @Override
    public Cursor query(final Uri uri, final String[] projection, final String selection,
                        final String[] selectionArgs, final String sortOrder) {
        //Log.v(TAG, String.format("Query for uri [%s]", uri));
//        if (Log.isLoggable(TAG, Log.VERBOSE)) {
//            if (projection != null) {
//                String proj = "projection: ";
//                for (int i = 0; i < projection.length; i++)
//                    proj += String.format(" [%s] ", projection[i]);
//                Log.v(TAG, proj);
//            } else{
//                Log.v(TAG, "to projection einai null");
//            }
//        }
        SQLiteDatabase db = database.getReadableDatabase();
        Cursor toRet = null;
        final int match = ContentDescriptor.URI_MATCHER.match(uri);
        switch (match) {
            //START FishingSession
            case ContentDescriptor.FishingSession.PATH_TOKEN: {
               // String searchFor = uri.getQueryParameter(ContentDescriptor.PARAM_SEARCH);
                SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
                builder.setTables(ContentDescriptor.FishingSession.TABLE_NAME);

//                if (!TextUtils.isEmpty(searchFor)) {
//                    String where = String.format(sWhereLike, ContentDescriptor.FishingSession.Cols.DESCRIPTION, searchFor);
//                    where += " OR " + (String.format(sWhereLike, ContentDescriptor.FishingSession.Cols.DESCRIPTION, searchFor));
//                    //Log.v(TAG, String.format("where [%s]", where));
//                    builder.appendWhere(where);
//                }

                toRet = builder.query(db, projection, selection, selectionArgs, null, null,
                        sortOrder);
            }
            break;
            case ContentDescriptor.FishingSession.PATH_START_LETTERS_TOKEN: {
                SQLiteDatabase rdb = database.getReadableDatabase();
                toRet = rdb
                        .rawQuery(
                                "select * from running order by fishingdate desc",
                                null);
            }
            break;
            case ContentDescriptor.FishingSession.PATH_FOR_ID_TOKEN: {
                String id = uri.getLastPathSegment();
                //Log.v(TAG, String.format("querying for [%s]", id));
                SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
                builder.setTables(ContentDescriptor.FishingSession.TABLE_NAME);
                toRet = builder.query(db, projection,
                        String.format(sWhere, ContentDescriptor.FishingSession.Cols.FISHINGSESSIONID), new String[]{
                                id
                        },
                        null, null, null, sortOrder);
            }
            break;
            // END FishingSession


            //START FishCatch
            case ContentDescriptor.FishCatch.PATH_TOKEN: {
                //String searchFor = uri.getQueryParameter(ContentDescriptor.PARAM_SEARCH);
                SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
                builder.setTables(ContentDescriptor.FishCatch.TABLE_NAME);

//                if (!TextUtils.isEmpty(searchFor)) {
//                    String where = String.format(sWhereLike, ContentDescriptor.FishCatch.Cols.MILLISECONDS, searchFor);
//                    where += " OR " + (String.format(sWhereLike, ContentDescriptor.FishCatch.Cols.MILLISECONDS, searchFor));
//                    //Log.v(TAG, String.format("where [%s]", where));
//                    builder.appendWhere(where);
//                }

                toRet = builder.query(db, projection, selection, selectionArgs, null, null,
                        sortOrder);
            }
            break;
//            case ContentDescriptor.FishCatch.PATH_START_LETTERS_TOKEN: {
//                SQLiteDatabase rdb = database.getReadableDatabase();
//                toRet = rdb
//                        .rawQuery(
//                                "select distinct substr(milliseconds, 1, 1) from interval order by 1 asc",
//                                null);
//            }
//            break;
            case ContentDescriptor.FishCatch.PATH_FOR_ID_TOKEN: {
                String id = uri.getLastPathSegment();
                //Log.v(TAG, String.format("querying for [%s]", id));
                SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
                builder.setTables(ContentDescriptor.FishCatch.TABLE_NAME);
                toRet = builder.query(db, projection,
                        String.format(sWhere, ContentDescriptor.FishCatch.Cols.FISHCATCHID), new String[]{
                                id
                        },
                        null, null, null, sortOrder);
            }
            break;
            // END FishCatch


            //START Fish
            case ContentDescriptor.Fish.PATH_TOKEN: {
                String searchFor = uri.getQueryParameter(ContentDescriptor.PARAM_SEARCH);
                SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
                builder.setTables(ContentDescriptor.Fish.TABLE_NAME);

                if (!TextUtils.isEmpty(searchFor)) {
                    String where = String.format(sWhereLike, ContentDescriptor.Fish.Cols.LATINNAME, searchFor);
                    //Log.v(TAG, String.format("where [%s]", where));
                    builder.appendWhere(where);
                }

                toRet = builder.query(db, projection, selection, selectionArgs, null, null,
                        sortOrder);
            }
            break;
            case ContentDescriptor.Fish.PATH_START_LETTERS_TOKEN: {
                SQLiteDatabase rdb = database.getReadableDatabase();
                toRet = rdb
                        .rawQuery(
                                "select distinct substr(commonname, 1, 1) from fish order by 1 asc",
                                null);
            }
            break;
            case ContentDescriptor.Fish.PATH_FOR_ID_TOKEN: {
                String id = uri.getLastPathSegment();
                //Log.v(TAG, String.format("querying for [%s]", id));
                SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
                builder.setTables(ContentDescriptor.Fish.TABLE_NAME);
                toRet = builder.query(db, projection,
                        String.format(sWhere, ContentDescriptor.Fish.Cols.FISHID), new String[]{
                                id
                        },
                        null, null, null, sortOrder);
            }
            break;
            // END Fish
            
            
          //START FishAverageStat
            case ContentDescriptor.FishAverageStatistic.PATH_TOKEN: {
                String searchFor = uri.getQueryParameter(ContentDescriptor.PARAM_SEARCH);
                SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
                builder.setTables(ContentDescriptor.FishAverageStatistic.TABLE_NAME);

                if (!TextUtils.isEmpty(searchFor)) {
                    String where = String.format(sWhereLike, ContentDescriptor.FishAverageStatistic.Cols.FISHAVGID, searchFor);
                    //Log.v(TAG, String.format("where [%s]", where));
                    builder.appendWhere(where);
                }

                toRet = builder.query(db, projection, selection, selectionArgs, null, null,
                        sortOrder);
            }
            break;
            case ContentDescriptor.FishAverageStatistic.PATH_START_LETTERS_TOKEN: {
                SQLiteDatabase rdb = database.getReadableDatabase();
                toRet = rdb
                        .rawQuery(
                                "select distinct substr("+FishAverageStatistic.Cols.FISHAVGID +" , 1, 1) from " +FishAverageStatistic.TABLE_NAME+ " order by 1 asc",
                                null);
            }
            break;
            case ContentDescriptor.FishAverageStatistic.PATH_FOR_ID_TOKEN: {
                String id = uri.getLastPathSegment();
                //Log.v(TAG, String.format("querying for [%s]", id));
                SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
                builder.setTables(ContentDescriptor.FishAverageStatistic.TABLE_NAME);
                toRet = builder.query(db, projection,
                        String.format(sWhere, ContentDescriptor.FishAverageStatistic.Cols.FISHAVGID), new String[]{
                                id
                        },
                        null, null, null, sortOrder);
            }
            break;
            // END FishAverageStat
            
            

            default:
//                Log.d(TAG, String.format("Could not handle matcher [%d]", match));
        }
        if (toRet != null) {
            toRet.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return toRet;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        //Log.v(TAG, String.format("Insert for uri [%s]", uri));
        SQLiteDatabase db = database.getWritableDatabase();
        int token = ContentDescriptor.URI_MATCHER.match(uri);
        long id = Database.INVALID_ID;
        switch (token) {
            //FishingSession
            case ContentDescriptor.FishingSession.PATH_TOKEN: {
                id = db.insert(ContentDescriptor.FishingSession.TABLE_NAME, null,
                        adjustIdField(values, ContentDescriptor.FishingSession.Cols.FISHINGSESSIONID));
            }
            break;
            //End FishingSession

            //Fish
            case ContentDescriptor.Fish.PATH_TOKEN: {
                id = db.insert(ContentDescriptor.Fish.TABLE_NAME, null,
                        adjustIdField(values, ContentDescriptor.Fish.Cols.FISHID));
            }
            break;
            //End Fish

            //FishCatch
            case ContentDescriptor.FishCatch.PATH_TOKEN: {
                id = db.insert(ContentDescriptor.FishCatch.TABLE_NAME, null,
                        adjustIdField(values, ContentDescriptor.FishCatch.Cols.FISHCATCHID));
            }
            break;
            //End FishCatch
            
            //FishAvgStat
            case ContentDescriptor.FishAverageStatistic.PATH_TOKEN: {
                id = db.insert(ContentDescriptor.FishAverageStatistic.TABLE_NAME, null,
                        adjustIdField(values, ContentDescriptor.FishAverageStatistic.Cols.FISHAVGID));
            }
            break;
            //End FishAvgStat


            default: {
                throw new UnsupportedOperationException("URI: " + uri + " not supported.");
            }
        }
        Uri toRet = ContentUris.withAppendedId(uri, id);
        //Log.v(TAG, String.format("new id [%d] notify via [%s]", id, toRet));
        getContext().getContentResolver().notifyChange(toRet, null);
        return toRet;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        //Log.v(TAG, String.format("update for uri [%s]", uri));
        int toRet = 0;
        SQLiteDatabase db = database.getWritableDatabase();
        int token = ContentDescriptor.URI_MATCHER.match(uri);
        switch (token) {
            //FishingSession
            case ContentDescriptor.FishingSession.PATH_TOKEN: {
                toRet = db.update(ContentDescriptor.FishingSession.TABLE_NAME, values, selection,
                        selectionArgs);
            }
            break;
            //End FishingSession

            //FishCatch
            case ContentDescriptor.FishCatch.PATH_TOKEN: {
                toRet = db.update(ContentDescriptor.FishCatch.TABLE_NAME, values, selection,
                        selectionArgs);
            }
            break;
            //End FishCatch

            //Fish
            case ContentDescriptor.Fish.PATH_TOKEN: {
                toRet = db.update(ContentDescriptor.Fish.TABLE_NAME, values, selection,
                        selectionArgs);
            }
            break;
            //End Fish
            
            //FishAvgStat
            case ContentDescriptor.FishAverageStatistic.PATH_TOKEN: {
                toRet = db.update(ContentDescriptor.FishAverageStatistic.TABLE_NAME, values, selection,
                        selectionArgs);
            }
            break;
            //End FishAvgStat


            default: {
                throw new UnsupportedOperationException("URI: " + uri + " not supported.");
            }
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return toRet;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        //Log.v(TAG, String.format("Delete for uri [%s]", uri));
        int toRet = 0;
        SQLiteDatabase db = database.getWritableDatabase();
        int token = ContentDescriptor.URI_MATCHER.match(uri);
        switch (token) {
            //running
            case ContentDescriptor.FishingSession.PATH_TOKEN: {
                toRet = db.delete(ContentDescriptor.FishingSession.TABLE_NAME, selection, selectionArgs);
            }
            break;
            //running

            //Fish
            case ContentDescriptor.Fish.PATH_TOKEN: {
                toRet = db.delete(ContentDescriptor.Fish.TABLE_NAME, selection, selectionArgs);
            }
            break;
            //Fish

            //FishCatch
            case ContentDescriptor.FishCatch.PATH_TOKEN: {
                toRet = db.delete(ContentDescriptor.FishCatch.TABLE_NAME, selection, selectionArgs);
            }
            break;
            //FishCatch
            
            
          //FishAvgStat
            case ContentDescriptor.FishAverageStatistic.PATH_TOKEN: {
                toRet = db.delete(ContentDescriptor.FishAverageStatistic.TABLE_NAME, selection, selectionArgs);
            }
            break;
            //FishAvgStat


            default: {
                throw new UnsupportedOperationException("URI: " + uri + " not supported.");
            }
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return toRet;
    }

    /**
     * checks if we have an invalid mId in values. If so, it removes it and let
     * autoincrement do the job
     *
     * @param values
     * @param idcol
     * @return
     */
    static ContentValues adjustIdField(ContentValues values, String idcol) {
        synchronized (values) {
            if (values.getAsInteger(idcol) .equals( Database.INVALID_ID)) {
                values.remove(idcol);
            }
            return values;
        }
    }
}
