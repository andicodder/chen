package com.lilliemountain.chen;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.AttendanceHolder> {

    List<String> monthYearList=new ArrayList<>();
    Map<String,Boolean> attendanceMap=new HashMap<>();
    List<Double> totalLectures=new ArrayList<>();
     ;
    public AttendanceAdapter(List<String> monthYearList, Map<String, Boolean> attendanceMap,List<Double> totalLectures) {
        this.monthYearList = monthYearList;
        this.attendanceMap = attendanceMap;
        this.totalLectures = totalLectures;
    }

    @NonNull
    @Override
    public AttendanceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attendance,parent,false);
        return new AttendanceHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceHolder holder, int position) {
            holder.month.setText(monthYearList.get(position).replace("_20"," "));
            List<String> keylist=new ArrayList<>();
            Set<String> keySet=attendanceMap.keySet();
            int countattend=0;
            for (String s :
                    keySet) {
                keylist.add(s);
            }
            for (String s :
                    keylist) {
                try {
                    Date datefromkey=new SimpleDateFormat("dd MM yyyy").parse(s);
                    String monthStringKey  = (String) DateFormat.format("MMM",  datefromkey);
                    Date datefromMY=new SimpleDateFormat("MMMM_yyyy").parse(monthYearList.get(position));
                    String monthStringMY  = (String) DateFormat.format("MMM",  datefromMY);
                    if(monthStringKey.equals(monthStringMY))
                    {
                        countattend++;
                    }
                }
                catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            holder.attended.setText(countattend+"");
            holder.conducted.setText(totalLectures.get(position).intValue()+"");
            holder.percentage.setText(((countattend/totalLectures.get(position) )*100)+"");
    }

    @Override
    public int getItemCount() {
        return monthYearList.size();
    }

    public class AttendanceHolder extends RecyclerView.ViewHolder {
        TextView month,conducted,attended,percentage;
        public AttendanceHolder(@NonNull View itemView) {
            super(itemView);
            month=itemView.findViewById(R.id.month);
            conducted=itemView.findViewById(R.id.conducted);
            attended=itemView.findViewById(R.id.attended);
            percentage=itemView.findViewById(R.id.percentage);
        }
    }
}
