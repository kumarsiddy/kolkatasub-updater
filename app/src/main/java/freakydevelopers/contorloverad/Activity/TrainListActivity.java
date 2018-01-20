package freakydevelopers.contorloverad.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import freakydevelopers.contorloverad.Adapter.TrainListAdapter;
import freakydevelopers.contorloverad.Pojo.Train;
import freakydevelopers.contorloverad.R;

public class TrainListActivity extends AppCompatActivity {

    private List<Train> trains;
    private Context context;
    private RecyclerView recyclerView;
    private TrainListAdapter trainListAdapter;
    LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_list);
        initView();
        trains = (List<Train>) getIntent().getSerializableExtra("list");
        mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        trainListAdapter = new TrainListAdapter(context, trains);
        recyclerView.setAdapter(trainListAdapter);
        trainListAdapter.notifyDataSetChanged();
    }

    private void initView() {
        recyclerView = findViewById(R.id.recycler_view);
    }
}
