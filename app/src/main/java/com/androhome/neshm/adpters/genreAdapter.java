package com.androhome.neshm.adpters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.androhome.neshm.R;

import java.util.ArrayList;

public class genreAdapter extends RecyclerView.Adapter<genreHolder> {

    Context context;
    ArrayList<String> genreList;
    ArrayList<Integer> colors;

    public genreAdapter(Context context, ArrayList<String> genreList) {
        this.context = context;
        this.colors = colors;
        this.genreList = genreList;
    }

    @NonNull
    @Override
    public genreHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.genre_item, parent, false);
        return new genreHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull genreHolder holder, int position) {
        holder.setDetails(genreList.get(position), context);
    }

    @Override
    public int getItemCount() {
        return genreList.size();
    }
}

class genreHolder extends RecyclerView.ViewHolder {
    private TextView genreText;

    public genreHolder(@NonNull View itemView) {
        super(itemView);
        genreText = itemView.findViewById(R.id.genre_text);
    }

    public void setDetails(String genreName, Context context) {
        genreText.setText(genreName);
    }
}