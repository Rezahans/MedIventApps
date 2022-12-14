package tech.farhand.mediventapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

    Button mAddBtn, mReturnBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("Home RF-Med");
        db = FirebaseFirestore.getInstance();
        mRcycV = findViewById(R.id.recycler_view);
        mRcycV.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        mRcycV.setLayoutManager(layoutManager);
        mAddBtn = findViewById(R.id.addBtn);
        mReturnBtn = findViewById(R.id.returnBtn);
        mReturnBtn.setVisibility(View.GONE);
        pd = new ProgressDialog(this);

        showData();

        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewListActivity.this, MainActivity.class));
                finish();
            }
        });

        mReturnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showData();
                mReturnBtn.setVisibility(View.GONE);
            }
        });


    }

    private void showData() {
        pd.setTitle("Getting Medicine List...");
        pd.show();

        db.collection("Medicine").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                mdlList.clear();
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
                Toast.makeText(ViewListActivity.this, "Error "+e.getMessage()+" While Retrieving Data!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void deleteData(int index){
        String dMedName = mdlList.get(index).getMedName();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] options = {"Sure", "No"};
        builder.setTitle("Delete "+dMedName+" ?");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0){
                    pd.setTitle("Deleting "+dMedName);
                    pd.show();
                    db.collection("Medicine").document(mdlList.get(index).getId()).delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    pd.dismiss();
                                    Toast.makeText(ViewListActivity.this, "Deleted "+dMedName, Toast.LENGTH_SHORT).show();
                                    showData();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    pd.dismiss();
                                    Toast.makeText(ViewListActivity.this, "Error "+e.getMessage()+" While Deleting "+dMedName, Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                if (which == 1) {
                    dialog.dismiss();
                }
            }
        }).create().show();

    }

    private void searchData(String query) {
        pd.setTitle("Searching "+ query);
        pd.show();
        db.collection("Medicine").whereEqualTo("query", query.toLowerCase()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        mdlList.clear();
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
                        if(!mdlList.isEmpty()){
                            adapter = new CustomAdapter(ViewListActivity.this, mdlList);
                            mRcycV.setAdapter(adapter);
                            mReturnBtn.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(ViewListActivity.this, "No Matching Medicine Mame With "+query, Toast.LENGTH_SHORT).show();
                            showData();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(ViewListActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchData(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_about){
            Toast.makeText(this, "Developer: Farhan Dwi O - CCIT 3SE2\n" +
                    "Support: Reza Hans L - CCIT 3SE2", Toast.LENGTH_SHORT).show();
        }
        if (item.getItemId() == R.id.action_refresh){
            showData();
            Toast.makeText(this, "Refreshed", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}