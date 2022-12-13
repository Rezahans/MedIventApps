package tech.farhand.mediventapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewListActivity extends AppCompatActivity {
    List<Model> mdlList = new ArrayList<>();
    RecyclerView mRcycV;
    RecyclerView.LayoutManager layoutManager;

    FirebaseFirestore db;

    CustomAdapter adapter;

    ProgressDialog pd;

    Button mAddBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("RF Pharmacy Inventory");
        db = FirebaseFirestore.getInstance();
        mRcycV = findViewById(R.id.recycler_view);
        mRcycV.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        mRcycV.setLayoutManager(layoutManager);
        mAddBtn = findViewById(R.id.addBtn);
        pd = new ProgressDialog(this);

        showData();

        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewListActivity.this, MainActivity.class));
                finish();
            }
        });


    }

    private void showData() {
        pd.setTitle("Getting Medicine List...");
        pd.show();

        db.collection("Medicine").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                pd.dismiss();

                for (DocumentSnapshot doc: task.getResult()){
                    Model model = new Model(doc.getString("id"),
                            doc.getString("batchNo"),
                            doc.getString("medName"),
                            doc.getString("medType"),
                            doc.getString("medQty"),
                            doc.getString("medPrice"),
                            doc.getString("medExpDate"),
                            doc.getString("medDesc"));
                    mdlList.add(model);
                }
                adapter = new CustomAdapter(ViewListActivity.this, mdlList);

                mRcycV.setAdapter(adapter);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(ViewListActivity.this, "Error "+e+" While Retrieving Data!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void deleteData(int index){
        String dMedName = db.collection("Medicine").document(mdlList.get(index).getMedName()).toString();
        pd.setTitle("Deleting "+dMedName);
        pd.show();
        db.collection("Medicine").document(mdlList.get(index).getId()).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        pd.dismiss();
                        Toast.makeText(ViewListActivity.this, "Deleted "+dMedName, Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(ViewListActivity.this, "Error "+e+" While Deleting "+dMedName, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}