package cl.inacap.kabban_02.Class.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import cl.inacap.kabban_02.Class.Models.Chat;
import cl.inacap.kabban_02.Class.Models.Users;
import cl.inacap.kabban_02.MessageActivity;
import cl.inacap.kabban_02.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatUserAdapter extends RecyclerView.Adapter<ChatUserAdapter.ViewHolder>{
    private Context ctx;
    private List<Users> listUsers;
    private boolean ischat;
    String last;

    public ChatUserAdapter(Context ctx, List<Users> listUsers, boolean ischat){
        try {
            this.listUsers = listUsers;
            this.ctx = ctx;
            this.ischat = ischat;
        }catch (Exception e){
            Toast.makeText(ctx,"ChatUserAdapter: "+e.getMessage(),Toast.LENGTH_LONG).show();
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        try {
            View view = LayoutInflater.from(ctx).inflate(R.layout.rv_user_item,viewGroup,false);
            return new ChatUserAdapter.ViewHolder(view);
        }catch (Exception e){
            Toast.makeText(ctx,"onCreateViewHolder: "+e.getMessage(),Toast.LENGTH_LONG).show();
            return null;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            final Users users = listUsers.get(position);
            holder.item_user_name.setText(users.getUsername());
            Glide.with(ctx).load(users.getImageURL()).into(holder.item_user_image);

            if(ischat){
                lastMessage(users.getId(),holder.last_msg);
                if(users.getStatus().equals("En línea")){
                    holder.img_on.setVisibility(View.VISIBLE);
                    holder.img_off.setVisibility(View.GONE);
                }else{
                    holder.img_off.setVisibility(View.VISIBLE);
                    holder.img_on.setVisibility(View.GONE);
                }
            }else{
                holder.last_msg.setVisibility(View.GONE);
                holder.img_off.setVisibility(View.GONE);
                holder.img_on.setVisibility(View.GONE);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Intent intent = new Intent(ctx,MessageActivity.class);
                        intent.putExtra("userid",users.getId());
                        ctx.startActivity(intent);
                    }catch (Exception e){
                        Toast.makeText(ctx,"onClick: "+e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            });
        }catch (Exception e){
            Toast.makeText(ctx,"onBindViewHolder: "+e.getMessage(),Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public int getItemCount() {
        return listUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView item_user_name;
        public CircleImageView item_user_image;
        private ImageView img_off;
        private ImageView img_on;
        private TextView last_msg;

        public ViewHolder(View view){
            super(view);
            try {
                item_user_name = view.findViewById(R.id.item_user_name);
                item_user_image = view.findViewById(R.id.item_user_image);
                img_off = view.findViewById(R.id.img_off);
                img_on = view.findViewById(R.id.img_on);
                last_msg = view.findViewById(R.id.last_msg);
            }catch (Exception e){
                Toast.makeText(ctx,"ViewHolder: "+e.getMessage(),Toast.LENGTH_LONG).show();
            }

        }
    }

    private void lastMessage(final String userid, final TextView last_msg){
        last = "default";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if(chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid) ||
                    chat.getReceiver().equals(userid) && chat.getSender().equals(firebaseUser.getUid())){
                        last = chat.getMessage();
                    }
                }
                switch (last){
                    case "default":
                        last_msg.setText("Sin mensajes nuevos");
                        break;
                    default:
                        last_msg.setText(last);
                        break;
                }

                last = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
