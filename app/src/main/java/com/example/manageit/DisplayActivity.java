package com.example.manageit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DisplayActivity extends AppCompatActivity {
    TextView save, spend, remain;
    RecyclerView saveRecyclerView, spendRecyclerView;
    SaveAdapter saveAdapter;
    SpendAdapter spendAdapter;
    ArrayList<UserSavings>userSavingsArrayList;
    ArrayList<UserSpendings>userSpendingsArrayList;
    float totalSavings=0, totalSpendins=0, remaining=0;
    int saveCount=0, spendCount=0;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        save=findViewById(R.id.save);
        spend=findViewById(R.id.spend);
        remain=findViewById(R.id.remain);

        saveRecyclerView=findViewById(R.id.saveView);
        saveRecyclerView.setHasFixedSize(true);
        saveRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        spendRecyclerView=findViewById(R.id.spendView);
        spendRecyclerView.setHasFixedSize(true);
        spendRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        userSavingsArrayList=new ArrayList<>();
        saveAdapter=new SaveAdapter(this, userSavingsArrayList);
        saveRecyclerView.setAdapter(saveAdapter);

        userSpendingsArrayList=new ArrayList<>();
        spendAdapter=new SpendAdapter(this, userSpendingsArrayList);
        spendRecyclerView.setAdapter(spendAdapter);

        databaseReference.child("savings").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()) {
                    UserSavings userSavings=dataSnapshot.getValue(UserSavings.class);
                    userSavingsArrayList.add(userSavings);

                    totalSavings+=Float.valueOf(userSavings.getAmount());
                    save.setText("Total: "+String.valueOf(totalSavings)+"TK");

                    remain.setText(String.valueOf(totalSavings));

                    saveCount= (int) snapshot.getChildrenCount();
                }
                saveAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference.child("spendings").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()) {
                    UserSpendings userSpendings=dataSnapshot.getValue(UserSpendings.class);
                    userSpendingsArrayList.add(userSpendings);

                    totalSpendins+=Float.valueOf(userSpendings.getAmount());
                    spend.setText("Total: "+String.valueOf(totalSpendins)+"TK");

                    remaining= Float.parseFloat(remain.getText().toString());
                    remain.setText(String.valueOf(remaining-Float.valueOf(userSpendings.getAmount())));

                    spendCount= (int) snapshot.getChildrenCount();
                }
                spendAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if(saveCount==0) save.setText("No Savings");
        if(spendCount==0) spend.setText("No Spendings");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.delete_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.d_save) {
            databaseReference.child("savings").removeValue();
            startActivity(new Intent(this, DisplayActivity.class));
            finish();
        }

        if(item.getItemId()==R.id.d_spend) {
            databaseReference.child("spendings").removeValue();
            startActivity(new Intent(this, DisplayActivity.class));
            finish();
        }

        if(item.getItemId()==R.id.d_all) {
            databaseReference.removeValue();
            startActivity(new Intent(this, DisplayActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}