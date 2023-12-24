package com.example.weatherapptutorial;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "user.db";
    private static final int DATABASE_VERSION = 2; // 데이터베이스 버전을 2로 증가

    // 회원가입 시 사용자 정보와 도시 정보를 데이터베이스에 저장합니다.
    public boolean addUser(String username, String password, String city) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        values.put("city", city);
        long result = db.insert("users", null, values);
        return result != -1;
    }

    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"_id"};
        String selection = "username=? and password=?";
        String[] selectionArgs = {username, password};
        Cursor cursor = db.query("users", columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }

    // 테이블 생성 쿼리
    private static final String CREATE_TABLE_USERS =
            "CREATE TABLE users (_id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, city TEXT, password TEXT);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 테이블 생성
        db.execSQL(CREATE_TABLE_USERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 기존 테이블 삭제 후 새로운 테이블 생성
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }

    // 사용자 이름을 기반으로 도시 정보를 가져오는 메서드
    public String getCityByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"city"};
        String selection = "username=?";
        String[] selectionArgs = {username};
        Cursor cursor = db.query("users", columns, selection, selectionArgs, null, null, null);

        String city = null;
        if (cursor.moveToFirst()) {
            city = cursor.getString(cursor.getColumnIndex("city"));
        }

        cursor.close();
        return city;
    }

    // 사용자 이름을 기반으로 해당 사용자가 존재하는지 확인하는 메서드
    public boolean checkIfUserExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"username"};
        String selection = "username=?";
        String[] selectionArgs = {username};
        Cursor cursor = db.query("users", columns, selection, selectionArgs, null, null, null);

        int count = cursor.getCount();
        cursor.close();

        return count > 0;  // 사용자가 존재하면 true, 그렇지 않으면 false 반환
    }

}
