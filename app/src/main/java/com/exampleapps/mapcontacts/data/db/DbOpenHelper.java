package com.exampleapps.mapcontacts.data.db;

import android.content.Context;

import com.exampleapps.mapcontacts.data.db.model.DaoMaster;
import com.exampleapps.mapcontacts.di.ApplicationContext;
import com.exampleapps.mapcontacts.di.DatabaseInfo;

import org.greenrobot.greendao.database.Database;

import javax.inject.Inject;

import timber.log.Timber;

public class DbOpenHelper extends DaoMaster.OpenHelper{

    @Inject
    public DbOpenHelper(@ApplicationContext Context context, @DatabaseInfo String name){
        super(context, name);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
        Timber.d("DB_OLD_VERSION : " + oldVersion + ", DB_NEW_VERSION : " + newVersion);

        switch (oldVersion){
            case 1:
            case 2:
                //db.execSQL("ALTER TABLE " + UserDao.TABLENAME + " ADD COLUMN "
                // + UserDao.Properties.Name.columnName + " TEXT DEFAULT 'DEFAULT_VAL'");
        }
    }
}
