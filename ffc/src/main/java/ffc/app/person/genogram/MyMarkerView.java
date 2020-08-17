package ffc.app.person.genogram;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;

import ffc.app.R;

@SuppressLint("ViewConstructor")
public class MyMarkerView extends MarkerView {

    private final TextView tvContent;

    private ArrayList<String> lstDate;



    private String ChartType;
    public String getChartType() {
        return ChartType;
    }

    public void setChartType(String chartType) {
        ChartType = chartType;
    }

    public ArrayList<String> getLstDate() {
        return lstDate;
    }

    public void setLstDate(ArrayList<String> lstDate) {
        this.lstDate = lstDate;
    }


    public MyMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        tvContent = findViewById(R.id.tvContent);
    }

    // runs every time the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        if (e instanceof CandleEntry) {

            CandleEntry ce = (CandleEntry) e;

            tvContent.setText(Utils.formatNumber(ce.getHigh(), 0, true));
        } else {
            //tvContent.setText(Utils.formatNumber(e.getY(), 0, true));
            if(ChartType=="Line") {
                tvContent.setText((int) e.getY() + " (" + lstDate.get((int) (e.getX())) + ")");
            }
            else if(ChartType=="Bar"){
                tvContent.setText((int) e.getY() + " (" + lstDate.get((int) (e.getX())-1) + ")");
            }
        }

        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
