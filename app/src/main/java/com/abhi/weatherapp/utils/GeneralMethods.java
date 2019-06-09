package com.abhi.weatherapp.utils;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import com.abhi.weatherapp.view.adapter.WeatherHistoryAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class GeneralMethods
{
    public static void addtodb(String city, String temperature, FirebaseFirestore dbdata)
    {
        Map<String, Object> user = new HashMap<>();
        user.put("city", city);
        user.put("temperature", temperature);

        dbdata.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("addtodb", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("addtodb", "Error adding document", e);
                    }
                });
    }

    public static void update2db(FirebaseFirestore db)
    {
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("update2db", document.getId() + " => " + document.getData().get("city"));
                            }
                        } else {
                            Log.w("update2db", "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}
