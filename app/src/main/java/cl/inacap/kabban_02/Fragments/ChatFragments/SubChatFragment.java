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

import com.firebase.ui.auth.data.model.User;
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
import cl.inacap.kabban_02.Class.Models.Chat;
import cl.inacap.kabban_02.Class.Models.Users;
import cl.inacap.kabban_02.R;

public class SubChatFragment extends Fragment {

    private RecyclerView recyclerView;

    private ChatUserAdapter chatUserAdapter;
    private List<Users> usersList;

    FirebaseUser fuser;
    DatabaseReference reference;

    private List<String> list;

    public SubChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sub_chat, container, false);
        try {
            recyclerView = view.findViewById(R.id.rv_chat);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            fuser = FirebaseAuth.getInstance().getCurrentUser();

            list = new ArrayList<>();

            reference = FirebaseDatabase.getInstance().getReference("Chats");
        }catch (Exception e){
            Toast.makeText(view.getContext(),"Error onCreateView: "+e.getMessage(),Toast.LENGTH_LONG).show();
        }

        try {
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    list.clear();
                    try {
                        for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                            Chat chat = snapshot.getValue(Chat.class);
                            if(chat.getSender().equals(fuser.getUid())){
                                if(!list.contains(chat.getReceiver())){
                                    list.add(chat.getReceiver());
                                }
                            }
                            if(chat.getReceiver().equals(fuser.getUid())){
                                if(!list.contains(chat.getSender())){
                                    list.add(chat.getSender());
                                }
                            }
                        }
                    }catch (Exception e){
                        Toast.makeText(getContext(),"Error onDataChange1: "+e.getMessage(),Toast.LENGTH_LONG).show();
                    }

                    readChats();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }catch (Exception e){
            Toast.makeText(view.getContext(),"Error addValueEventListener1: "+e.getMessage(),Toast.LENGTH_LONG).show();
        }
        return view;
    }

    private void readChats() {
        try {
            usersList = new ArrayList<>();

            reference = FirebaseDatabase.getInstance().getReference("Users");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    usersList.clear();
                    try {
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            Users user = snapshot.getValue(Users.class);

                            for(String id : list){
                                if(user.getId().equals(id)){
                                    if(usersList.size() != 0){
                                        for(int i = 0; i<usersList.size(); i++){
                                            Users user1 = usersList.get(i);
                                            if(!user.getId().equals(user1.getId())){
                                                usersList.add(user);
                                            }
                                        }
                                    }else{
                                        usersList.add(user);
                                    }
                                }
                            }
                        }
                    }catch (Exception e){
                        Toast.makeText(getContext(),"Error onDataChange2: "+e.getMessage(),Toast.LENGTH_LONG).show();
                    }

                    chatUserAdapter = new ChatUserAdapter(getContext(),usersList);
                    recyclerView.setAdapter(chatUserAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }catch (Exception e){
            Toast.makeText(getContext(),"Error readChats: "+e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
}
