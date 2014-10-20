package com.dangchienhsgs.giffus.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.dangchienhsgs.giffus.utils.SelectionBuilder;

/**
 * This class handle all data which presented in SQLite
 * We connect and change/update data via DataProvider
 * In this class, we must re-define all actions because
 * data can be SQL, XML, JSON or anything you want
 */
public class DataProvider extends ContentProvider {
    private String TAG = "DataProvider";

    public static final String CONTENT_AUTHORITY = "com.dangchienhsgs.giffus.provider";

    // Define CODE DETERMINE OF TABLE
    public static final int TABLE_FRIEND_ALL_ROWS = 0;
    public static final int TABLE_FRIEND_SINGLE_ROW = 1;
    public static final int TABLE_GIFT_SENT_ALL_ROWS = 2;
    public static final int TABLE_GIFT_SENT_SINGLE_ROW = 3;
    public static final int TABLE_GIFT_RECEIVER_ALL_ROWS = 4;
    public static final int TABLE_GIFT_RECEIVER_SINGLE_ROW = 5;
    public static final int TABLE_NOTIFICATION_ALL_ROW = 6;
    public static final int TABLE_NOTIFICATION_SINGLE_ROW = 7;

    public static final UriMatcher uriMatcher;

    /**
     * Structure of a content provider:
     * <standard_prefix>:// <authority> / <data_path> / <id></id>
     * standard_prefix is always "content://"
     * authority: name of the content provider
     * data path: define the data we need to get. Example: content://contact/people
     * id define the index of the record we want to receive
     */
    static {
        /**
         * As we know,all the uris end with # is the uri we need to query to get a
         * single row. The # means a number which is the index of record
         */
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(CONTENT_AUTHORITY, FriendContract.TABLE_NAME, TABLE_FRIEND_ALL_ROWS);
        uriMatcher.addURI(CONTENT_AUTHORITY, FriendContract.TABLE_NAME + "/#", TABLE_FRIEND_SINGLE_ROW);

        uriMatcher.addURI(CONTENT_AUTHORITY, GiftSentContract.TABLE_NAME, TABLE_GIFT_SENT_ALL_ROWS);
        uriMatcher.addURI(CONTENT_AUTHORITY, GiftSentContract.TABLE_NAME + "/#", TABLE_GIFT_SENT_SINGLE_ROW);

        uriMatcher.addURI(CONTENT_AUTHORITY, GiftReceivedContract.TABLE_NAME, TABLE_GIFT_RECEIVER_ALL_ROWS);
        uriMatcher.addURI(CONTENT_AUTHORITY, GiftReceivedContract.TABLE_NAME + "/#", TABLE_GIFT_RECEIVER_SINGLE_ROW);

        uriMatcher.addURI(CONTENT_AUTHORITY, NotificationContract.TABLE_NAME, TABLE_NOTIFICATION_ALL_ROW);
        uriMatcher.addURI(CONTENT_AUTHORITY, NotificationContract.TABLE_NAME + "/#", TABLE_NOTIFICATION_SINGLE_ROW);
    }

    private DBHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());

        SQLiteDatabase database = dbHelper.getWritableDatabase();
        if (database != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param uri           The TABLE we want to query
     * @param projection    The COLUMNS LIST we want to query Eg projections=String[]{age, name}
     * @param selection     WHERE STATEMENT, Eg:
     *                      selection=String[]{"name LIKE '%Lee'"}
     * @param selectionArgs The second method of WHERE STATEMENT, instead of
     *                      use the above method, you can define selection=" name LIKE ? "
     *                      selectionArgs=new String[]{"%Chien", "%Tom Hank"}
     *                      to search row which has name like "Chien" or "Tom Hank"
     * @param sortOrder     "Order by" such as SQL
     *                      Example: We can order the name ascending such as:
     *                      sortOrder="name ASC" >< sortOrder="name DESC"
     * @return
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        switch (uriMatcher.match(uri)) {
            case TABLE_FRIEND_ALL_ROWS:
                queryBuilder.setTables(FriendContract.TABLE_NAME);
                break;
            case TABLE_FRIEND_SINGLE_ROW:
                queryBuilder.setTables(FriendContract.TABLE_NAME);
                queryBuilder.appendWhere("_id = " + uri.getLastPathSegment());
                break;
            case TABLE_GIFT_SENT_ALL_ROWS:
                queryBuilder.setTables(GiftSentContract.TABLE_NAME);
                break;
            case TABLE_GIFT_SENT_SINGLE_ROW:
                queryBuilder.setTables(GiftSentContract.TABLE_NAME);
                queryBuilder.appendWhere("_id = " + uri.getLastPathSegment());
                break;
            case TABLE_GIFT_RECEIVER_ALL_ROWS:
                queryBuilder.setTables(GiftReceivedContract.TABLE_NAME);
                break;
            case TABLE_GIFT_RECEIVER_SINGLE_ROW:
                queryBuilder.setTables(GiftReceivedContract.TABLE_NAME);
                queryBuilder.appendWhere("_id = " + uri.getLastPathSegment());
                break;
            case TABLE_NOTIFICATION_ALL_ROW:
                queryBuilder.setTables(NotificationContract.TABLE_NAME);
                break;
            case TABLE_NOTIFICATION_SINGLE_ROW:
                queryBuilder.appendWhere("_id = " + uri.getLastPathSegment());
                break;

            default:
                throw new IllegalArgumentException("Unsupport URI: " + uri);
        }

        Cursor cursor = queryBuilder.query(database, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    /**
     * Insert row to database
     *
     * @param uri:          The uri of which table we want to add
     * @param contentValues the values (row) we need to add to table
     * @return
     */
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        long id;
        switch (uriMatcher.match(uri)) {
            case TABLE_FRIEND_ALL_ROWS:
                // id is the index of the row we insert
                id = database.insertOrThrow(FriendContract.TABLE_NAME, null, contentValues);
                break;

            case TABLE_GIFT_SENT_ALL_ROWS:
                id = database.insertOrThrow(GiftSentContract.TABLE_NAME, null, contentValues);
                break;

            case TABLE_GIFT_RECEIVER_ALL_ROWS:
                id = database.insertOrThrow(GiftReceivedContract.TABLE_NAME, null, contentValues);
                break;
            case TABLE_NOTIFICATION_ALL_ROW:
                id = database.insertOrThrow(NotificationContract.TABLE_NAME, null, contentValues);
                break;

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        Uri insertUri = ContentUris.withAppendedId(uri, id);
        getContext().getContentResolver().notifyChange(insertUri, null, false);
        return insertUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selections) {
        SelectionBuilder selectionBuilder = new SelectionBuilder();
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        int count;

        switch (match) {
            case TABLE_FRIEND_ALL_ROWS:
                count = selectionBuilder.table(FriendContract.TABLE_NAME)
                        .where(selection, selections)
                        .delete(db);
                break;
            case TABLE_FRIEND_SINGLE_ROW:
                count = selectionBuilder.table(FriendContract.TABLE_NAME)
                        .where(FriendContract.Entry._ID + "=?", uri.getLastPathSegment())
                        .where(selection, selections)
                        .delete(db);
                break;
            case TABLE_GIFT_RECEIVER_ALL_ROWS:
                count = selectionBuilder.table(GiftReceivedContract.TABLE_NAME)
                        .where(selection, selections)
                        .delete(db);
                break;
            case TABLE_GIFT_RECEIVER_SINGLE_ROW:
                count = selectionBuilder.table(GiftReceivedContract.TABLE_NAME)
                        .where(FriendContract.Entry._ID + "=?", uri.getLastPathSegment())
                        .where(selection, selections)
                        .delete(db);
                break;
            case TABLE_GIFT_SENT_ALL_ROWS:
                count = selectionBuilder.table(GiftSentContract.TABLE_NAME)
                        .where(selection, selections)
                        .delete(db);
                break;
            case TABLE_GIFT_SENT_SINGLE_ROW:
                count = selectionBuilder.table(GiftSentContract.TABLE_NAME)
                        .where(FriendContract.Entry._ID + "=?", uri.getLastPathSegment())
                        .where(selection, selections)
                        .delete(db);
                break;
            case TABLE_NOTIFICATION_ALL_ROW:
                count = selectionBuilder.table(NotificationContract.TABLE_NAME)
                        .where(selection, selections)
                        .delete(db);
                break;
            case TABLE_NOTIFICATION_SINGLE_ROW:
                count = selectionBuilder.table(NotificationContract.TABLE_NAME)
                        .where(NotificationContract.Entry._ID + "=?", uri.getLastPathSegment())
                        .where(selection, selections)
                        .delete(db);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        Context context = getContext();
        context.getContentResolver().notifyChange(uri, null, false);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selections) {
        final SQLiteDatabase database = dbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        SelectionBuilder builder = new SelectionBuilder();

        int count;
        switch (match) {
            case TABLE_FRIEND_ALL_ROWS:
                count = builder.table(FriendContract.TABLE_NAME)
                        .where(selection, selections)
                        .update(database, contentValues);
                break;
            case TABLE_FRIEND_SINGLE_ROW:
                count = builder.table(FriendContract.TABLE_NAME)
                        .where(FriendContract.Entry._ID + "=?", uri.getLastPathSegment())
                        .where(selection, selections)
                        .update(database, contentValues);
                break;
            case TABLE_GIFT_SENT_ALL_ROWS:
                count = builder.table(GiftSentContract.TABLE_NAME)
                        .where(selection, selections)
                        .update(database, contentValues);
                break;
            case TABLE_GIFT_SENT_SINGLE_ROW:
                count = builder.table(GiftSentContract.TABLE_NAME)
                        .where(GiftSentContract.Entry._ID + "=?", uri.getLastPathSegment())
                        .where(selection, selections)
                        .update(database, contentValues);
                break;
            case TABLE_GIFT_RECEIVER_ALL_ROWS:
                count = builder.table(GiftReceivedContract.TABLE_NAME)
                        .where(selection, selections)
                        .update(database, contentValues);
                break;
            case TABLE_GIFT_RECEIVER_SINGLE_ROW:
                count = builder.table(GiftReceivedContract.TABLE_NAME)
                        .where(GiftReceivedContract.Entry._ID + "=?", uri.getLastPathSegment())
                        .where(selection, selections)
                        .update(database, contentValues);
                break;
            case TABLE_NOTIFICATION_ALL_ROW:
                count = builder.table(NotificationContract.TABLE_NAME)
                        .where(selection, selections)
                        .update(database, contentValues);
                break;
            case TABLE_NOTIFICATION_SINGLE_ROW:
                count = builder.table(NotificationContract.TABLE_NAME)
                        .where(NotificationContract.Entry._ID + "=?", uri.getLastPathSegment())
                        .where(selection, selections)
                        .update(database, contentValues);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        Context context = getContext();
        context.getContentResolver().notifyChange(uri, null, false);
        return count;
    }

    private class DBHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "giffus.db";
        private static final int DATABASE_VERSION = 1;
        private static final String TYPE_TEXT = " TEXT";
        private static final String TYPE_INT = " INTEGER";
        private static final String COMMA_SEP = ",";

        @Override
        public void onCreate(SQLiteDatabase db) {
            // create table friends
            db.execSQL("create table " + FriendContract.TABLE_NAME + " ("
                    + FriendContract.Entry._ID + TYPE_INT + " primary key autoincrement, "
                    + FriendContract.Entry.USERNAME + TYPE_TEXT + COMMA_SEP
                    + FriendContract.Entry.USER_ID + TYPE_TEXT + COMMA_SEP
                    + FriendContract.Entry.EMAIL + TYPE_TEXT + COMMA_SEP
                    + FriendContract.Entry.FULL_NAME + TYPE_TEXT + COMMA_SEP
                    + FriendContract.Entry.RELATIONSHIP + TYPE_TEXT + COMMA_SEP
                    + FriendContract.Entry.BIRTHDAY + TYPE_TEXT + COMMA_SEP
                    + FriendContract.Entry.GENRE + TYPE_TEXT + COMMA_SEP
                    + FriendContract.Entry.MOBILE_PHONE + TYPE_TEXT + COMMA_SEP
                    + FriendContract.Entry.AVATAR_ID + TYPE_TEXT
                    + ");");

            // create table gift receiver
            db.execSQL("create table " + GiftSentContract.TABLE_NAME + " ("
                    + GiftSentContract.Entry._ID + TYPE_INT + " primary key autoincrement, "
                    + GiftSentContract.Entry.GIFT_ID + TYPE_TEXT + COMMA_SEP
                    + GiftSentContract.Entry.MESSAGE + TYPE_TEXT + COMMA_SEP
                    + GiftSentContract.Entry.RECEIVER_ID + TYPE_TEXT + COMMA_SEP
                    + GiftSentContract.Entry.DATETIME + TYPE_TEXT + COMMA_SEP
                    + GiftSentContract.Entry.LOCATION + TYPE_TEXT + COMMA_SEP
                    + GiftSentContract.Entry.IS_FINISHED + TYPE_INT
                    + ");");
            // create table gift sent
            db.execSQL("create table " + GiftReceivedContract.TABLE_NAME + " ("
                    + GiftReceivedContract.Entry._ID + TYPE_INT + " primary key autoincrement, "
                    + GiftReceivedContract.Entry.GIFT_ID + TYPE_TEXT + COMMA_SEP
                    + GiftReceivedContract.Entry.SENDER_ID + TYPE_TEXT + COMMA_SEP
                    + GiftReceivedContract.Entry.DATETIME + TYPE_TEXT + COMMA_SEP
                    + GiftReceivedContract.Entry.MESSAGE + TYPE_TEXT + COMMA_SEP
                    + GiftReceivedContract.Entry.LOCATION + TYPE_TEXT + COMMA_SEP
                    + GiftReceivedContract.Entry.IS_FINISHED + TYPE_INT +
                    ");");
            db.execSQL("create table " + NotificationContract.TABLE_NAME + " ("
                    + NotificationContract.Entry._ID + TYPE_INT + " primary key autoincrement, "
                    + NotificationContract.Entry.FRIEND_ID + TYPE_TEXT + COMMA_SEP
                    + NotificationContract.Entry.ENABLE + TYPE_INT + COMMA_SEP
                    + NotificationContract.Entry.MESSAGE + TYPE_TEXT + COMMA_SEP
                    + NotificationContract.Entry.TYPE + TYPE_TEXT
                    + ");");
        }

        private DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override

        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

        }
    }
}
