package com.tweebaa.ex_seat.activity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.tweebaa.ex_seat.R;
import com.tweebaa.ex_seat.model.DataUtil;
import com.tweebaa.ex_seat.model.DatabaseHelper;

import java.text.SimpleDateFormat;

/**
 * Created by Zhuang on 2016-03-03.
 */
public class HistoryRecord extends Fragment {

    private Activity mAct;
    private DatabaseHelper database;
    private SQLiteDatabase db;
    private Cursor db_cursor;
    private SimpleDateFormat sDateFormat;
    private SimpleCursorAdapter simple_adapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mAct = getActivity();
        //mActivity.setHandler(mHandler);
        database = new DatabaseHelper(context, DataUtil.DB_NAME,null, DataUtil.version);
        db = database.getReadableDatabase();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Bundle bundle = getArguments();
        //bundle.getString("EVENT");
        //setListAdapter(adapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.layout_record, container, false);
        ListView m_listview = (ListView) view.findViewById(R.id.listView_data);

        db_cursor = db.query("data", new String[]{"_id","max", "date", "duration", "avg"}, null, null, null, null, null);
        simple_adapter = new SimpleCursorAdapter(view.getContext(), R.layout.data_item, db_cursor,
                new String[]{"max","date", "duration", "avg"}, new int[]{R.id.max,R.id.date, R.id.duration, R.id.avg},
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        m_listview.setAdapter(simple_adapter);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


}
