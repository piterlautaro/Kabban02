package cl.inacap.kabban_02;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import cl.inacap.kabban_02.Class.Models.Users;
import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

    private FirebaseUser fuser;
    private DatabaseReference reference;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        try {
            Toolbar toolbar = findViewById(R.id.chat_toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }catch (Exception e){
            Toast.makeText(MessageActivity.this,"Chat toolbar: "+e.getMessage(),Toast.LENGTH_LONG).show();
        }

        final CircleImageView chat_user_image = findViewById(R.id.chat_profile_image);
        final TextView chat_user_name = findViewById(R.id.chat_user_name);

        intent = getIntent();
        final String userid = intent.getStringExtra("userid");
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

        try {
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        Users user = dataSnapshot.getValue(Users.class);
                        chat_user_name.setText(user.getUsername());
                        Glide.with(MessageActivity.this).load(user.getImageURL()).into(chat_user_image);
                    }catch (Exception e){
                        Toast.makeText(MessageActivity.this,"onDataChange: "+e.getMessage(),Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(MessageActivity.this,"onCancelled: "+databaseError.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }catch (Exception e){
            Toast.makeText(MessageActivity.this,"Database(evt): "+e.getMessage(),Toast.LENGTH_LONG).show();
        }


    }
}
