package cl.inacap.kabban_02.Class.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cl.inacap.kabban_02.Class.Models.News;
import cl.inacap.kabban_02.R;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private Context ctx;
    private List<News> list;
    public static final int USER_PUBLISH = 1;
    public static final int GROUP_PUBLISH = 2;
    public static final int PROJECT_PUBLISH = 3;

    public NewsAdapter(Context ctx, List<News> list) {
        this.ctx = ctx;
        this.list = list;
    }


    @NonNull
    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if(viewType == 1){
            View view = LayoutInflater.from(ctx).inflate(R.layout.rv_publish_user_item,viewGroup,false);
            return new NewsAdapter.ViewHolder(view);
        }else if(viewType == 2){
            View view = LayoutInflater.from(ctx).inflate(R.layout.rv_publish_user_item,viewGroup,false);
            return new NewsAdapter.ViewHolder(view);
        }else{
            View view = LayoutInflater.from(ctx).inflate(R.layout.rv_publish_user_item,viewGroup,false);
            return new NewsAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.ViewHolder holder, int position) {
        News news = list.get(position);

        holder.item_user_name_publish.setText(news.getOwner_name());
        Glide.with(ctx).load(news.getImageURL()).into(holder.item_user_image_publish);
        holder.item_user_publish.setText(news.getPublish());
        holder.item_publish_date.setText(news.getDate());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView item_user_name_publish;
        public ImageView item_user_image_publish;
        public TextView item_user_publish;
        public TextView item_publish_date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_user_name_publish = itemView.findViewById(R.id.item_user_name_publish);
            item_user_image_publish = itemView.findViewById(R.id.item_user_image_publish);
            item_user_publish = itemView.findViewById(R.id.item_user_publish);
            item_publish_date = itemView.findViewById(R.id.item_publish_date);
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (list.get(position).getType()){
            case 1:
                return USER_PUBLISH;
            case 2:
                return GROUP_PUBLISH;
            case 3:
                return PROJECT_PUBLISH;
            default:
                return 0;
        }
    }
}
