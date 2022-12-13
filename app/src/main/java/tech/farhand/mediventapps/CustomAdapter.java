package tech.farhand.mediventapps;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<ViewHolder> {
    ViewListActivity la;
    List<Model> mdlList;
    Context context;

    public CustomAdapter(ViewListActivity la, List<Model> mdlList) {
        this.la = la;
        this.mdlList = mdlList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.model_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(la);
                String[] options = {"Update", "Delete", "Cancel"};
                builder.setTitle(mdlList.get(position).getMedName());
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0){
                            String id = mdlList.get(position).getId();
                            String medBatchNo = mdlList.get(position).getMedBatchNo();
                            String medName = mdlList.get(position).getMedName();
                            String medType = mdlList.get(position).getMedType();
                            String medQty = mdlList.get(position).getMedQty();
                            String medPrice = mdlList.get(position).getMedPrice();
                            String medExpDate = mdlList.get(position).getMedExpDate();
                            String medDesc = mdlList.get(position).getMedDesc();

                            Intent intent = new Intent(la, MainActivity.class);
                            intent.putExtra("pId", id);
                            intent.putExtra("pMedBatchNo", medBatchNo);
                            intent.putExtra("pMedName", medName);
                            intent.putExtra("pMedType", medType);
                            intent.putExtra("pMedQty", medQty);
                            intent.putExtra("pMedPrice", medPrice);
                            intent.putExtra("pMedExpDate", medExpDate);
                            intent.putExtra("pMedDesc", medDesc);

                            la.startActivity(intent);

                        }
                        if(which == 1) {
                            Toast.makeText(la, "On Progress", Toast.LENGTH_SHORT).show();
                        }
                        if(which == 2){
                            dialog.dismiss();
                        }

                    }
                }).create().show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                String medName = mdlList.get(position).getMedName(), medBatch = mdlList.get(position).getMedBatchNo();
                Toast.makeText(la, medBatch+"\n"+medName, Toast.LENGTH_SHORT).show();
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mMedBatchFd.setText("BatchNo."+mdlList.get(position).getMedBatchNo());
        holder.mMedNameFd.setText(mdlList.get(position).getMedName());
        holder.mMedTypeFd.setText("Type: "+mdlList.get(position).getMedType());
        holder.mMedQtyFd.setText("Qty: "+mdlList.get(position).getMedQty());
        holder.mMedPriceFd.setText("Price: Rp."+mdlList.get(position).getMedPrice());
        holder.mMedExpDateFd.setText("Expire Date: "+mdlList.get(position).getMedExpDate());
        holder.mMedDescFd.setText("Description:\n"+mdlList.get(position).getMedDesc());

    }

    @Override
    public int getItemCount() {
        return mdlList.size();
    }
}
