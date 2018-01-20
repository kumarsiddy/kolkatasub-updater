package freakydevelopers.contorloverad.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import freakydevelopers.contorloverad.Customview.CircularTextView;
import freakydevelopers.contorloverad.Pojo.Train;
import freakydevelopers.contorloverad.Pojo.TrainDay;
import freakydevelopers.contorloverad.R;

/**
 * Created by PURUSHOTAM on 1/18/2018.
 */

public class TrainListAdapter extends RecyclerView.Adapter<TrainListAdapter.MyViewHolder> {
    private List<Train> trains;
    private Context context;

    public TrainListAdapter(Context context, List<Train> trains) {
        this.context = context;
        this.trains = trains;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_trainlist, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position) {
        Train train = trains.get(position);
        viewHolder.trainNo.setText(train.getTrainNo());
        viewHolder.trainName.setText(train.getTrainName());
//        viewHolder.srctime.setText(train.getArrivalTime());
//        viewHolder.rechtime.setText(train.getDepartTime());

        setDays(viewHolder, train.getTrainDay());
    }

    private void setDays(MyViewHolder viewHolder, TrainDay train) {
        if (train.isSun()) {
            viewHolder.sun.setStrokeColor("#8ecf93");
        } else {
            viewHolder.sun.setStrokeColor("#ea6160");
        }

        if (train.isMon()) {
            viewHolder.mon.setStrokeColor("#8ecf93");
        } else {
            viewHolder.mon.setStrokeColor("#ea6160");
        }

        if (train.isTue()) {
            viewHolder.tue.setStrokeColor("#8ecf93");
        } else {
            viewHolder.tue.setStrokeColor("#ea6160");
        }

        if (train.isWed()) {
            viewHolder.wed.setStrokeColor("#8ecf93");
        } else {
            viewHolder.wed.setStrokeColor("#ea6160");
        }

        if (train.isThu()) {
            viewHolder.thu.setStrokeColor("#8ecf93");
        } else {
            viewHolder.thu.setStrokeColor("#ea6160");
        }

        if (train.isFri()) {
            viewHolder.fri.setStrokeColor("#8ecf93");
        } else {
            viewHolder.fri.setStrokeColor("#ea6160");
        }

        if (train.isSat()) {
            viewHolder.sat.setStrokeColor("#8ecf93");
        } else {
            viewHolder.sat.setStrokeColor("#ea6160");
        }
    }

    @Override
    public int getItemCount() {
        return trains.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView trainNo;
        public TextView trainName;
        public TextView srctime;
        public TextView rechtime;
        public CircularTextView sun, mon, tue, wed, thu, fri, sat;

        public MyViewHolder(View view) {
            super(view);

            trainNo = view.findViewById(R.id.s1);
            trainName = view.findViewById(R.id.s2);
            srctime = view.findViewById(R.id.s3);
            rechtime = view.findViewById(R.id.s4);
            sun = view.findViewById(R.id.sun);
            mon = view.findViewById(R.id.mon);
            tue = view.findViewById(R.id.tue);
            wed = view.findViewById(R.id.wed);
            thu = view.findViewById(R.id.thu);
            fri = view.findViewById(R.id.fri);
            sat = view.findViewById(R.id.sat);
            sun.setStrokeWidth(1);
            sun.setSolidColor("#ffffff");
            mon.setStrokeWidth(1);
            mon.setSolidColor("#ffffff");
            tue.setStrokeWidth(1);
            tue.setSolidColor("#ffffff");
            wed.setStrokeWidth(1);
            wed.setSolidColor("#ffffff");
            thu.setStrokeWidth(1);
            thu.setSolidColor("#ffffff");
            fri.setStrokeWidth(1);
            fri.setSolidColor("#ffffff");
            sat.setStrokeWidth(1);
            sat.setSolidColor("#ffffff");

        }
    }

    class MyClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

          /*  int itemPosition = recyclerView.getChildLayoutPosition(v);
            Train train = trains.get(itemPosition);
            new SearchTrainRoute(context, train.getTrainNo(), train.getTrainName(), fromTo).execute();*/
        }
    }
}
