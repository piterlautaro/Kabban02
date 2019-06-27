package cl.inacap.kabban_02.Class.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import cl.inacap.kabban_02.Class.Models.Chat;
import cl.inacap.kabban_02.Class.Models.Users;
import cl.inacap.kabban_02.MessageActivity;
import cl.inacap.kabban_02.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter  extends RecyclerView.Adapter<MessageAdapter.ViewHolder>{

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    private Context ctx;
    private List<Chat> listChats;
    private String imageURL;

    FirebaseUser fuser;

    public MessageAdapter(Context ctx, List<Chat> listChats,String imageURL){
        try {
            this.listChats = listChats;
            this.ctx = ctx;
            this.imageURL = imageURL;
        }catch (Exception e){
            Toast.makeText(ctx,"ChatUserAdapter: "+e.getMessage(),Toast.LENGTH_LONG).show();
        }

    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if(viewType == MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(ctx).inflate(R.layout.chat_item_right,viewGroup,false);
            return new MessageAdapter.ViewHolder(view);
        }else{
            View view = LayoutInflater.from(ctx).inflate(R.layout.chat_item_left,viewGroup,false);
            return new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {

        Chat chat = listChats.get(position);

        holder.show_message.setText(chat.getMessage());
        Glide.with(ctx).load(imageURL).into(holder.chat_item_profile_image);

    }

    @Override
    public int getItemCount() {
        return listChats.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView show_message;
        public CircleImageView chat_item_profile_image;

        public ViewHolder(View view){
            super(view);
            try {
                show_message = view.findViewById(R.id.show_message);
                chat_item_profile_image = view.findViewById(R.id.chat_item_profile_image);
            }catch (Exception e){
                Toast.makeText(ctx,"ViewHolder: "+e.getMessage(),Toast.LENGTH_LONG).show();
            }

        }
    }

    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if(listChats.get(position).getSender().equals(fuser.getUid())){
            return MSG_TYPE_RIGHT;
        }else{
            return MSG_TYPE_LEFT;
        }
    }
}
