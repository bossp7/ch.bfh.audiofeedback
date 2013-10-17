package ch.bfh.audiofeedback;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

public class LineChart {
    /** The main dataset that includes all the series that go into a chart. */
    private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
    /** The main renderer that includes all the renderers customizing a chart. */
    private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
    /** The most recently added series. */
    private XYSeries mCurrentSeries = new XYSeries("Spectrum");
    /** The most recently created renderer, customizing the current series. */
    private XYSeriesRenderer renderer = new XYSeriesRenderer();
    
    private static String TAG = "LineChart";


	public LineChart(){
		// put single dataset to the multiple dataset
		mDataset.addSeries(mCurrentSeries);
		
		// configure the main renderer
		renderer.setColor(Color.WHITE);
		renderer.setPointStyle(PointStyle.POINT);
		renderer.setFillPoints(true);
		
		// set up the multiple renderer
		mRenderer.setZoomButtonsVisible(true);
		mRenderer.setXTitle("Frequency [Hz]");
		mRenderer.setYTitle("Magnitude [dB]");
		
		// add single renderer to multiple renderer
		mRenderer.addSeriesRenderer(renderer);	
		
	}
	
	public void setNewPoints(Point p){	
//		Log.d(TAG, "Adding point at " + Double.toString(p.getX()) + " / " + Double.toString(p.getY()));
		mCurrentSeries.add(p.getX(), p.getY());		
			
	}
	
	public GraphicalView getView(Context context){
		return ChartFactory.getLineChartView(context, mDataset, mRenderer);	
	}
	
	public void resetPoints(){
		mCurrentSeries.clear();
	}
	
	public void removePoints(int index){
		mCurrentSeries.remove(index);
	}
}
