package com.infusiblecoders.simplenotesapp;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chinalwb.are.AREditText;
import com.chinalwb.are.render.AreTextView;

/**
 * Created by SUDA on 08-09-2017.
 */

public class NoteViewHolder extends RecyclerView.ViewHolder {

    View mView;

    TextView textTitle, textTime;
    AreTextView content;
    CardView noteCard;

    public NoteViewHolder(View itemView) {
        super(itemView);

        mView = itemView;

        textTitle = mView.findViewById(R.id.note_title);
        textTime = mView.findViewById(R.id.note_time);
        noteCard = mView.findViewById(R.id.note_card);
        content = mView.findViewById(R.id.content_text_custom);

    }

    public void setNoteTitle(String title) {
        textTitle.setText(title);
    }

    public void setNoteTime(long time) {
        textTime.setText(Long.toString(time));
    }
    public void setContent(String scontent){
        content.fromHtml(scontent);
    }

}
