package com.example.susanne.myapp;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.ResourceCursorAdapter;
import android.view.View;

/**
 * Created by Susanne on 14-12-2017.
 */

public class scoreArray extends ResourceCursorAdapter {

    public scoreArray(Context context, Cursor c) {
        super(context, R.layout.score_row, c, 0);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

    }
}
