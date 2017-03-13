package com.wifibyteschallenge.android.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wifibyteschallenge.android.R;

import org.w3c.dom.Text;

/**
 * Created by juanj on 13/03/2017.
 */

public class RecyclerPostAdapter extends RecyclerView.Adapter<RecyclerPostAdapter.PostHolder> {

    @Override
    public RecyclerPostAdapter.PostHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_item_list, parent, false);
        return new PostHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(RecyclerPostAdapter.PostHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
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
