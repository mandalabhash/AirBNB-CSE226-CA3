package com.aimmore.airbnv_ca3

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class PropertyDB(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "Airbnb.db"
        private const val DATABASE_VERSION = 1

        // Table and Column Names
        private const val TABLE_NAME = "Properties"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_LOCATION = "location"
        private const val COLUMN_PRICE = "price"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_CONTACT = "contact"
        private const val COLUMN_IS_FAVORITE = "isFavorite"
        private const val COLUMN_IMAGE = "image"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT NOT NULL,
                $COLUMN_LOCATION TEXT NOT NULL,
                $COLUMN_PRICE REAL NOT NULL,
                $COLUMN_DESCRIPTION TEXT,
                $COLUMN_CONTACT TEXT NOT NULL,
                $COLUMN_IS_FAVORITE INTEGER DEFAULT 0,
                $COLUMN_IMAGE BLOB
            )
        """.trimIndent()
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun InsertProperty(property: PropsModel): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, property.name)
            put(COLUMN_LOCATION, property.location)
            put(COLUMN_PRICE, property.price)
            put(COLUMN_DESCRIPTION, property.description)
            put(COLUMN_CONTACT, property.contact)
            put(COLUMN_IS_FAVORITE, property.isFavorite)
            put(COLUMN_IMAGE, property.image)
        }
        val id = db.insert(TABLE_NAME, null, values)
        db.close()
        return id
    }

    fun DeleteProperty(id:Long) {
        val db = writableDatabase
        db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
    }

    fun GetAllProperty(): List<PropsModel> {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null
        )
        val properties = mutableListOf<PropsModel>()
        while (cursor.moveToNext()) {
            val property = PropsModel(
                id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID)),
                name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                location = cursor.getString(cursor.getColumnIndex(COLUMN_LOCATION)),
                price = cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE)),
                description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)),
                contact = cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT)),
                isFavorite = cursor.getInt(cursor.getColumnIndex(COLUMN_IS_FAVORITE)) == 1,
                image = cursor.getBlob(cursor.getColumnIndex("image")) // Fetching the image as BLOB
            )
            properties.add(property)
        }
        cursor.close()
        db.close()
        return properties
    }


    fun GetPropertyBYId(id: Long): PropsModel? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_NAME,
            null,
            "$COLUMN_ID = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )
        val property = if (cursor.moveToNext()) {
            PropsModel(
                id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID)),
                name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                location = cursor.getString(cursor.getColumnIndex(COLUMN_LOCATION)),
                price = cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE)),
                description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)),
                contact = cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT)),
                isFavorite = cursor.getInt(cursor.getColumnIndex(COLUMN_IS_FAVORITE)) == 1,
                image = cursor.getBlob(cursor.getColumnIndex("image")) // Fetching the image as BLOB
            )
        } else {
            null
        }
        cursor.close()
        db.close()
        return property
    }

    fun GetFavouriteProperty(): List<PropsModel> {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_NAME,
            null,
            "$COLUMN_IS_FAVORITE = ?",
            arrayOf("1"),
            null,
            null,
            null
        )
        val properties = mutableListOf<PropsModel>()
        while (cursor.moveToNext()) {
            val property = PropsModel(
                id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID)),
                name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                location = cursor.getString(cursor.getColumnIndex(COLUMN_LOCATION)),
                price = cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE)),
                description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)),
                contact = cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT)),
                isFavorite = cursor.getInt(cursor.getColumnIndex(COLUMN_IS_FAVORITE)) == 1,
                image = cursor.getBlob(cursor.getColumnIndex("image")) // Fetching the image as BLOB
            )
            properties.add(property)
        }
        cursor.close()
        db.close()
        return properties
    }

    fun UpdateFavProperty(id: Long, isFav: Boolean) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_IS_FAVORITE, if (isFav) 1 else 0)
        }
        db.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
    }

}

