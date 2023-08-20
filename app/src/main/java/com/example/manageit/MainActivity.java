package com.example.manageit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
    EditText save_r, save_a, spend_r, spend_a;
    String save_rs, save_as, spend_rs, spend_as, time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        save_r=findViewById(R.id.save_r);
        save_a=findViewById(R.id.save_a);
        spend_r=findViewById(R.id.spend_r);
        spend_a=findViewById(R.id.spend_a);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.display) {
            startActivity(new Intent(this, DisplayActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    public void savingPage(View view) {
        time=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime());

        save_rs=save_r.getText().toString();
        save_as=save_a.getText().toString();

        if(save_as.isEmpty()) {
            save_a.setError("Amount is required.");
        }
        else {
            databaseReference.child("savings").child(time).child("reason").setValue(save_rs);
            databaseReference.child("savings").child(time).child("amount").setValue(save_as);
            databaseReference.child("savings").child(time).child("time").setValue(time);
            databaseReference.child("savings").child(time).child("updated_time").setValue(time);

            Toast.makeText(this, "Saved as Savings", Toast.LENGTH_SHORT).show();
            save_r.setText("");
            save_a.setText("");
        }
    }

    public void spendingPage(View view) {
        time=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime());

        spend_rs=spend_r.getText().toString();
        spend_as=spend_a.getText().toString();

        if(spend_as.isEmpty()) {
            spend_a.setError("Amount is required.");
        }
        else {
            databaseReference.child("spendings").child(time).child("reason").setValue(spend_rs);
            databaseReference.child("spendings").child(time).child("amount").setValue(spend_as);
            databaseReference.child("spendings").child(time).child("time").setValue(time);
            databaseReference.child("spendings").child(time).child("updated_time").setValue(time);

            Toast.makeText(this, "Saved as Spendings", Toast.LENGTH_SHORT).show();
            spend_r.setText("");
            spend_a.setText("");
        }
    }
}