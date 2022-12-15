package tech.farhand.mediventapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    final Calendar myCalendar= Calendar.getInstance();
    EditText mExpDateEt, mBatchNoEt, mMedNameEt, mMedTypeEt, mMedQtyEt, mMedPriceEt, mMedDescEt;
    Button mSaveBtn, mBackBtn;
    ProgressDialog pd;
    FirebaseFirestore db;
    String pId, pMedBatchNo, pMedName, pMedType, pMedQty, pMedPrice, pMedExpDate, pMedDesc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar ab = getSupportActionBar();
        mExpDateEt=(EditText) findViewById(R.id.medExpDateEt);
        mBatchNoEt=(EditText) findViewById(R.id.medBatchNoEt);
        mMedNameEt=(EditText) findViewById(R.id.medNameEt);
        mMedTypeEt=(EditText) findViewById(R.id.medTypeEt);
        mMedQtyEt=(EditText) findViewById(R.id.medQtyEt);
        mMedPriceEt=(EditText) findViewById(R.id.medPriceEt);
        mMedDescEt=(EditText) findViewById(R.id.medDescEt);
        mSaveBtn=(Button) findViewById(R.id.saveBtn);
        mBackBtn=(Button) findViewById(R.id.backBtn);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            pId = bundle.getString("pId");
            pMedBatchNo = bundle.getString("pMedBatchNo");
            pMedName = bundle.getString("pMedName");
            ab.setTitle("Update Medicine "+pMedName);
            pMedType = bundle.getString("pMedType");
            pMedQty = bundle.getString("pMedQty");
            pMedPrice = bundle.getString("pMedPrice");
            pMedExpDate = bundle.getString("pMedExpDate");
            pMedDesc = bundle.getString("pMedDesc");
            mBatchNoEt.setText(pMedBatchNo);
            mMedNameEt.setText(pMedName);
            mMedTypeEt.setText(pMedType);
            mMedQtyEt.setText(pMedQty);
            mMedPriceEt.setText(pMedPrice);
            mExpDateEt.setText(pMedExpDate);
            mMedDescEt.setText(pMedDesc);


            mSaveBtn.setText("Update");

        }else {
            ab.setTitle("Add Medicine");
            mSaveBtn.setText("Save");
        }

        pd = new ProgressDialog(this);
        db = FirebaseFirestore.getInstance();
        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };
        mExpDateEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(MainActivity.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = getIntent().getExtras();
                if(bundle != null){
                    String id=pId, BatchNo = mBatchNoEt.getText().toString().trim(), MedName = mMedNameEt.getText().toString().trim(), MedType = mMedTypeEt.getText().toString().trim(), MedQty = mMedQtyEt.getText().toString().trim(), MedPrice = mMedPriceEt.getText().toString().trim(), MedDesc = mMedDescEt.getText().toString().trim(), MedExpDate = mExpDateEt.getText().toString().trim();
                    if(!mBatchNoEt.getText().toString().trim().isEmpty() && !mMedNameEt.getText().toString().trim().isEmpty() && !mMedTypeEt.getText().toString().trim().isEmpty() && !mMedQtyEt.getText().toString().trim().isEmpty() && !mMedPriceEt.getText().toString().trim().isEmpty() && !mMedDescEt.getText().toString().trim().isEmpty() && !mExpDateEt.getText().toString().trim().isEmpty()){
                        updateData(id, BatchNo, MedName, MedType, MedQty, MedPrice, MedExpDate, MedDesc);
                    } else {
                        Toast.makeText(MainActivity.this, "Please Fill Required Data !!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    String  BatchNo = mBatchNoEt.getText().toString().trim(), MedName = mMedNameEt.getText().toString().trim(), MedType = mMedTypeEt.getText().toString().trim(), MedQty = mMedQtyEt.getText().toString().trim(), MedPrice = mMedPriceEt.getText().toString().trim(), MedDesc = mMedDescEt.getText().toString().trim(), MedExpDate = mExpDateEt.getText().toString().trim();

                    if(!mBatchNoEt.getText().toString().trim().isEmpty() && !mMedNameEt.getText().toString().trim().isEmpty() && !mMedTypeEt.getText().toString().trim().isEmpty() && !mMedQtyEt.getText().toString().trim().isEmpty() && !mMedPriceEt.getText().toString().trim().isEmpty() && !mMedDescEt.getText().toString().trim().isEmpty() && !mExpDateEt.getText().toString().trim().isEmpty()){
                        uploadData(BatchNo,MedName,MedType,MedQty,MedPrice,MedExpDate, MedDesc);
                    } else {
                        Toast.makeText(MainActivity.this, "Please Fill Required Data !!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ViewListActivity.class));
                finish();
            }
        });


    }

    private void updateData(String id, String batchNo, String medName, String medType, String medQty, String medPrice, String medExpDate, String medDesc) {
        pd.setTitle("Updating Medicine "+medName+" To Database");
        pd.show();
        Map<String, Object> medIventUpdt = new HashMap<>();
        medIventUpdt.put("id", id);
        medIventUpdt.put("batchNo", batchNo);
        medIventUpdt.put("medName", medName);
        medIventUpdt.put("medType", medType);
        medIventUpdt.put("medQty", medQty);
        medIventUpdt.put("medPrice", medPrice);
        medIventUpdt.put("medExpDate", medExpDate);
        medIventUpdt.put("medDesc", medDesc);
        medIventUpdt.put("query", medName.toLowerCase());

        db.collection("Medicine").document(id).update(medIventUpdt).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                pd.dismiss();
                Toast.makeText(MainActivity.this, "Success Updating Medicine "+medName, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, ViewListActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(MainActivity.this, "Failed Add Data: "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void uploadData(String batchNo, String medName, String medType, String medQty, String medPrice, String medExpDate, String medDesc) {
        pd.setTitle("Adding New Medicine To Database");
        pd.show();
        String id = UUID.randomUUID().toString();

        Map<String, Object> medIvent = new HashMap<>();
        medIvent.put("id", id);
        medIvent.put("batchNo", batchNo);
        medIvent.put("medName", medName);
        medIvent.put("medType", medType);
        medIvent.put("medQty", medQty);
        medIvent.put("medPrice", medPrice);
        medIvent.put("medExpDate", medExpDate);
        medIvent.put("medDesc", medDesc);
        medIvent.put("query", medName.toLowerCase());

        db.collection("Medicine").document(id).set(medIvent).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                pd.dismiss();
                Toast.makeText(MainActivity.this, "Data Successfully Added", Toast.LENGTH_SHORT).show();
                mExpDateEt.getText().clear();
                mBatchNoEt.getText().clear();
                mMedNameEt.getText().clear();
                mMedPriceEt.getText().clear();
                mMedQtyEt.getText().clear();
                mMedTypeEt.getText().clear();
                mMedDescEt.getText().clear();
                startActivity(new Intent(MainActivity.this, ViewListActivity.class));
                finish();
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(MainActivity.this, "Failed Add Data: "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateLabel(){
        String myFormat="dd/MM/yy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        mExpDateEt.setText(dateFormat.format(myCalendar.getTime()));
    }
}