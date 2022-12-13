package tech.farhand.mediventapps;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {
    TextView mMedBatchFd, mMedNameFd, mMedTypeFd, mMedQtyFd, mMedPriceFd, mMedExpDateFd, mMedDescFd;
    View mView;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;

        //itm click
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v, getAbsoluteAdapterPosition());
            }
        });

        //Ketika di hold
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mClickListener.onItemLongClick(v, getAbsoluteAdapterPosition());
            return true;
            }
        });

        mMedBatchFd = itemView.findViewById(R.id.rMedBatchNo);
        mMedNameFd = itemView.findViewById(R.id.rMedName);
        mMedTypeFd = itemView.findViewById(R.id.rMedType);
        mMedQtyFd = itemView.findViewById(R.id.rMedQty);
        mMedPriceFd = itemView.findViewById(R.id.rMedPrice);
        mMedExpDateFd = itemView.findViewById(R.id.rMedExpDate);
        mMedDescFd = itemView.findViewById(R.id.rMedDesc);

    }
    private  ViewHolder.ClickListener mClickListener;
    public interface ClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public void setOnClickListener(ViewHolder.ClickListener clickListener){
        mClickListener = clickListener;
    }
}
