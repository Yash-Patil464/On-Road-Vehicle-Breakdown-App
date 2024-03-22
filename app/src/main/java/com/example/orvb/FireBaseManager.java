package com.example.orvb;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FireBaseManager {

    private static final String DATABASE_URL = "https://orvb-sem-proj-default-rtdb.firebaseio.com/";
    private static volatile FireBaseManager instance;
    private DatabaseReference databaseReference;

    private FireBaseManager() {

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl(DATABASE_URL);
    }

    public static synchronized FireBaseManager getInstance() {
        if (instance == null) {
            synchronized (FireBaseManager.class) {
                if (instance == null) {
                    instance = new FireBaseManager();
                }
            }
        }
        return instance;
    }

    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }
}
