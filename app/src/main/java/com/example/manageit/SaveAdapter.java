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

public class SaveAdapter extends RecyclerView.Adapter<SaveAdapter.ViewHolder> {
    Context context;
    ArrayList<UserSavings>userSavingsArrayList;
    String save_rs, save_as, updated_time;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();

    public SaveAdapter(Context context, ArrayList<UserSavings> userSavingsArrayList) {
        this.context = context;
        this.userSavingsArrayList = userSavingsArrayList;
    }

    @NonNull
    @Override
    public SaveAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.display, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SaveAdapter.ViewHolder holder, int position) {
        UserSavings userSavings=userSavingsArrayList.get(position);
        holder.reason.setText(userSavings.getReason());
        holder.amount.setText(userSavings.getAmount());
        holder.time.setText(userSavings.getTime());
        holder.updated_time.setText(userSavings.getUpdated_time());

        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updated_time=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime());

                save_rs=holder.reason.getText().toString();
                save_as=holder.amount.getText().toString();

                if(save_as.isEmpty()) {
                    holder.amount.setError("Amount is required.");
                }
                else {
                    databaseReference.child("savings").child(userSavings.getTime()).child("reason").setValue(save_rs);
                    databaseReference.child("savings").child(userSavings.getTime()).child("amount").setValue(save_as);
                    databaseReference.child("savings").child(userSavings.getTime()).child("updated_time").setValue(updated_time);

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
                databaseReference.child("savings").child(userSavings.getTime()).removeValue();

                Intent intent=new Intent(context, DisplayActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

                ((Activity)context).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return userSavingsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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