package com.wifibyteschallenge.android.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wifibyteschallenge.android.R;
import com.wifibyteschallenge.android.model.Posts;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by juanj on 13/03/2017.
 */

public class RecyclerPostAdapter extends RecyclerView.Adapter<RecyclerPostAdapter.PostHolder> {
    private List<Posts> listOfItems;

    public RecyclerPostAdapter(List<Posts> posts) {
        listOfItems = posts;
    }

    @Override
    public RecyclerPostAdapter.PostHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_item_list, parent, false);
        return new PostHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(RecyclerPostAdapter.PostHolder postItem, int index) {
        postItem.textTitle.setText(listOfItems.get(index).getTitle());
        postItem.textBody.setText(listOfItems.get(index).getBody());
    }

    @Override
    public int getItemCount() {
        return listOfItems.size();
    }

    public class PostHolder extends RecyclerView.ViewHolder {
        private TextView textTitle;
        private TextView textBody;

        public PostHolder(View iView) {
            super(iView);
            textTitle = (TextView) iView.findViewById(R.id.titlePost);
            textBody = (TextView) iView.findViewById(R.id.bodyPost);
        }
    }
}
