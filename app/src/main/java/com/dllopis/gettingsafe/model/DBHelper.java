package com.dllopis.gettingsafe.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    public static final String TRAYECTOS_TABLE = "TRAYECTOS_TABLE";
    public static final String COLUMN_ORIGEN = "ORIGEN";
    public static final String COLUMN_DESTINO = "DESTINO";
    public static final String COLUMN_TIEMPO = "TIEMPO";
    public static final String COLUMN_ID = "ID";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TRAYECTOS_TABLE;

    public DBHelper(@Nullable Context context) {
        super(context, "trayectos.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + TRAYECTOS_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ORIGEN + " TEXT," +
                COLUMN_DESTINO + " TEXT," +
                COLUMN_TIEMPO + " INT)";

        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public boolean addTrip(Trayecto trayecto){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_ORIGEN, trayecto.getOrigen());
        cv.put(COLUMN_DESTINO, trayecto.getDestino());
        cv.put(COLUMN_TIEMPO, trayecto.getTiempo());

        long insert = db.insert(TRAYECTOS_TABLE, null, cv);

        if (insert == -1)
            return false;
        else
            return true;
    }

    public List<Trayecto> getLastsTrips(){
        List<Trayecto> lastsTrips = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.query(TRAYECTOS_TABLE,
                null,
                null,
                null,
                null,
                null,
                null);

        while(c.moveToNext()) {
            int id = c.getInt(c.getColumnIndexOrThrow(COLUMN_ID));
            String origen = c.getString(c.getColumnIndexOrThrow(COLUMN_ORIGEN));
            String destino = c.getString(c.getColumnIndexOrThrow(COLUMN_DESTINO));
            int tiempo = c.getInt(c.getColumnIndexOrThrow(COLUMN_TIEMPO));

            Trayecto t = new Trayecto(id,origen,destino,tiempo);

            lastsTrips.add(t);
        }
        c.close();

        return lastsTrips;
    }

    public boolean deleteTrip(Trayecto trayecto){
        SQLiteDatabase db = this.getWritableDatabase();

        String[] whereArgs = {(String.valueOf(trayecto.getId()))};

        int result = db.delete(TRAYECTOS_TABLE,
                COLUMN_ID,
                whereArgs);

        if(result!=0)
            return true;
        else
            return false;
    }
}
