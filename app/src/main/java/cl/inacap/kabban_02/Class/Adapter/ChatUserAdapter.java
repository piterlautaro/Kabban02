package cl.inacap.kabban_02.Class.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import cl.inacap.kabban_02.Class.Models.Users;
import cl.inacap.kabban_02.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatUserAdapter extends RecyclerView.Adapter<ChatUserAdapter.ViewHolder>{
    private Context ctx;
    private List<Users> listUsers;

    public ChatUserAdapter(Context ctx, List<Users> listUsers){
        try {
            this.listUsers = listUsers;
            this.ctx = ctx;
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
            holder.item_user_name.setText(listUsers.get(position).getUsername());
            Glide.with(ctx).load(listUsers.get(position).getImageURL()).into(holder.item_user_image);
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

        public ViewHolder(View view){
            super(view);
            try {
                item_user_name = view.findViewById(R.id.item_user_name);
                item_user_image = view.findViewById(R.id.item_user_image);
            }catch (Exception e){
                Toast.makeText(ctx,"ViewHolder: "+e.getMessage(),Toast.LENGTH_LONG).show();
            }

        }
    }
}
