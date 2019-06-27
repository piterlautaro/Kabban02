package cl.inacap.kabban_02.Fragments.ChatFragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import cl.inacap.kabban_02.Class.Adapter.ChatUserAdapter;
import cl.inacap.kabban_02.Class.Models.Users;
import cl.inacap.kabban_02.R;

public class SubUsersFragment extends Fragment {

    private RecyclerView recyclerView;
    private ChatUserAdapter chatUserAdapter;
    private List<Users> userList;

    public SubUsersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sub_users, container, false);
        try {
            recyclerView = view.findViewById(R.id.rv_chat_users);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            userList = new ArrayList<>();

            readUsers();
            return view;
        }catch(Exception e){
            Toast.makeText(getContext(),"onCreateView(SubUsersFragment): "+e.getMessage(),Toast.LENGTH_LONG).show();
            return null;
        }

    }

    /**
     * Se utiliza para rescatar a los usuarios de la base de datos y lugos mostrarlos en la lista de usuarios del chat
     */
    private void readUsers() {
        try {
            final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");


            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        userList.clear();
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            Users user = snapshot.getValue(Users.class);

                            if(!user.getId().equals(firebaseUser.getUid())){
                                userList.add(user);
                            }
                        }
                        chatUserAdapter = new ChatUserAdapter(getContext(),userList);
                        recyclerView.setAdapter(chatUserAdapter);
                    }catch(Exception e){
                        Toast.makeText(getContext(),"onDataChange: "+e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getContext(),"onCancelled: "+databaseError.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }catch(Exception e){
            Toast.makeText(getContext(),"readUsers: "+e.getMessage(),Toast.LENGTH_LONG).show();
        }

    }

}
