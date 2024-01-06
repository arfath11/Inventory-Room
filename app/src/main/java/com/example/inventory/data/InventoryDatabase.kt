package com.example.inventory.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/*
Tip: You can use this code as a template for your future projects. The way you create the RoomDatabase instance is similar to the process in the previous steps.
You might have to replace the entities and DAOs specific to your app.
 */




/*
Specify the Item as the only class with the list of entities.
Set the version as 1. Whenever you change the schema of the database table, you have to increase the version number.
Set exportSchema to false so as not to keep schema version history backups.
 */
@Database(entities = [Item::class], version = 1, exportSchema = false)
abstract class InventoryDatabase : RoomDatabase() {


    abstract fun itemDao(): ItemDao

    companion object {
        /*
        The value of a volatile variable is never cached, and all reads and writes are to and from the main memory. These features help ensure the value of Instance is always up to date and is the same for all execution threads.
        It means that changes made by one thread to Instance are immediately visible to all other threads
         */

        @Volatile
        private var Instance: InventoryDatabase? = null

        /*
        Multiple threads can potentially ask for a database instance at the same time, which results in two databases instead of one. This issue is known as a race condition. Wrapping the code to get the database inside a synchronized block means that only one thread of execution at a time can enter this block of code,
        which makes sure the database only gets initialized once.
         */


        fun getDatabase(context: Context): InventoryDatabase {

            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, InventoryDatabase::class.java, "item_database")
                    .fallbackToDestructiveMigration()
/*
Note: Normally, you would provide a migration object with
 a migration strategy for when the schema changes. A migration object is an object that defines how you take all rows with the old schema and convert them to rows in the new schema, so that no data is lost. Migration is beyond the scope of this codelab, but the term refers to when the schema is changed and you need to move your date without losing the data. Since this is a sample app, a simple alternative is to destroy and rebuild the database, which means that the inventory data is lost. For example, if you change something in the entity class, like adding a new parameter,
 you can allow the app to delete and re-initialize the database.
 */
                    .build()
                    .also { Instance = it }            }

        }


    }

}


