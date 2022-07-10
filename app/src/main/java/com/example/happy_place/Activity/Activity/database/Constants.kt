package com.example.happy_place.Activity.Activity.database

object Constants {
    const val DB_NAME = "HappyPlacesDatabase"

    const val DB_VERSION = 1

    const val TABLE_NAME = "HappyPlacesTable"

    const val C_ID = "ID"
    const val C_NAME = "NAME"
    const val C_COUPLE = "COUPLE"
    const val C_IMAGE = "IMAGE"
    const val C_ADDRESS = "ADDRESS"
    const val C_DATE = "DATE"

    const val CREATE_TABLE = (
            "CREATE TABLE" + TABLE_NAME + "("
            + C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + C_NAME + " TEXT,"
            + C_COUPLE + "TEXT,"
            + C_IMAGE + "TEXT,"
            + C_ADDRESS + "TEXT,"
            + C_DATE + "TEXT"
            + ")"
            )
}