package com.refat.chowdhury.myreceipt;


import android.app.backup.BackupAgentHelper;
import android.app.backup.FileBackupHelper;
import android.util.Log;

public class TheBackupAgent extends BackupAgentHelper{

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		//super.onCreate();
		Log.d("ConnectBot.BackupAgent", "onCreate called");

       /* SharedPreferencesBackupHelper prefs = new SharedPreferencesBackupHelper(this, getPackageName() + "_preferences");
        addHelper(PreferenceConstants.BACKUP_PREF_KEY, prefs);*/

        FileBackupHelper hosts = new FileBackupHelper(this, "../databases/" + dataAdapter.DATABASE_NAME);
        addHelper(dataAdapter.DATABASE_NAME, hosts);
        
        /*FileBackupHelper pubkeys = new FileBackupHelper(this, "../databases/" + PubkeyDatabase.DB_NAME);
        addHelper(PubkeyDatabase.DB_NAME, pubkeys);*/


	}

	

}
