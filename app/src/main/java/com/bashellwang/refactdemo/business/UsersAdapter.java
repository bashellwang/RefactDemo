package com.bashellwang.refactdemo.business;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bashellwang.refactdemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liang.wang on 2019/3/5.
 */
public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    private UserClickListener clickListener;
    private List<User> dataset;

    public interface UserClickListener {
        void onUserClick(int position);
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {

        public TextView text;
        public TextView comment;

        public UserViewHolder(View itemView, final UserClickListener clickListener) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.textViewNoteText);
            comment = (TextView) itemView.findViewById(R.id.textViewNoteComment);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickListener != null) {
                        clickListener.onUserClick(getAdapterPosition());
                    }
                }
            });
        }
    }

    public UsersAdapter(UserClickListener clickListener) {
        this.clickListener = clickListener;
        this.dataset = new ArrayList<User>();
    }

    public void setUsers(@NonNull List<User> users) {
        dataset = users;
        notifyDataSetChanged();
    }

    public User getUser(int position) {
        return dataset.get(position);
    }

    @Override
    public UsersAdapter.UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_note, parent, false);
        return new UserViewHolder(view, clickListener);
    }

    @Override
    public void onBindViewHolder(UsersAdapter.UserViewHolder holder, int position) {
        User user = dataset.get(position);
        holder.text.setText(user.getName());
        holder.comment.setText(user.getDesc());
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
