package com.example.manageit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class SpendAdapter extends RecyclerView.Adapter<SpendAdapter.ViewHolder> {
    String spend_rs, spend_as, updated_time;
    Context context;
    ArrayList<UserSpendings>userSpendingsArrayList;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();

    public SpendAdapter(Context context, ArrayList<UserSpendings> userSpendingsArrayList) {
        this.context = context;
        this.userSpendingsArrayList = userSpendingsArrayList;
    }

    @NonNull
    @Override
    public SpendAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.display, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SpendAdapter.ViewHolder holder, int position) {
        UserSpendings userSpendings=userSpendingsArrayList.get(position);

        holder.reason.setText(userSpendings.getReason());
        holder.amount.setText(userSpendings.getAmount());
        holder.time.setText(userSpendings.getTime());
        holder.updated_time.setText(userSpendings.getUpdated_time());

        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updated_time=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime());

                spend_rs=holder.reason.getText().toString();
                spend_as=holder.amount.getText().toString();

                if(spend_as.isEmpty()) {
                    holder.amount.setError("Amount is required.");
                }
                else {
                    databaseReference.child("spendings").child(userSpendings.getTime()).child("reason").setValue(spend_rs);
                    databaseReference.child("spendings").child(userSpendings.getTime()).child("amount").setValue(spend_as);
                    databaseReference.child("spendings").child(userSpendings.getTime()).child("updated_time").setValue(updated_time);

                    Intent intent=new Intent(context, DisplayActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                    ((Activity)context).finish();
                }
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("spendings").child(userSpendings.getTime()).removeValue();

                Intent intent=new Intent(context, DisplayActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

                ((Activity)context).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return userSpendingsArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        EditText reason, amount;
        TextView time, updated_time, update, delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            reason=itemView.findViewById(R.id.reason);
            amount=itemView.findViewById(R.id.amount);
            time=itemView.findViewById(R.id.time);
            updated_time=itemView.findViewById(R.id.updated_time);
            update=itemView.findViewById(R.id.update);
            delete=itemView.findViewById(R.id.delete);
        }
    }
}