package Chart;

import org.apache.commons.math3.linear.RealMatrix;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

import javax.swing.*;
import java.awt.*;


/**
 * A simple demonstration application showing how to create a line chart using data from an
 * {@link XYDataset}.
 */
public class Chart extends JFrame {

    /**
     * Creates a new demo.
     *
     * @param title the frame title.
     */
    public Chart(final String title) {

        super(title);

    }

    public void printChart(RealMatrix matrix, String[] variableMapping, double samplingInterval, double missionTime) {
        final XYDataset dataset = createDataset(matrix, variableMapping, samplingInterval);
        final JFreeChart chart = createChart(dataset);
        chart.getXYPlot().getDomainAxis().setRange(0, missionTime + 0.5);

        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(3000, 1570));
        setContentPane(chartPanel);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        this.pack();
        RefineryUtilities.centerFrameOnScreen(this);
        this.setVisible(true);

    }

    /**
     * Creates a sample dataset.
     *
     * @param matrix
     * @param variableMapping
     * @return a sample dataset.
     */
    private XYDataset createDataset(RealMatrix matrix, String[] variableMapping, double samplingInterval) {
        XYSeries series;
        double tempSamplingInterval;
        final XYSeriesCollection dataset = new XYSeriesCollection();
        for (int i = 0; i < matrix.getColumnDimension(); i++) {
            series = new XYSeries(variableMapping[i]);
            tempSamplingInterval = 0;
            for (int j = 0; j < matrix.getRowDimension(); j++) {
                series.add(tempSamplingInterval, matrix.getEntry(j, i));
                tempSamplingInterval += samplingInterval;
            }
            dataset.addSeries(series);
        }

        return dataset;
    }

    /**
     * Creates a chart.
     *
     * @param dataset the data for the chart.
     * @return a chart.
     */
    private JFreeChart createChart(final XYDataset dataset) {

        // create the chart...
        final JFreeChart chart = ChartFactory.createXYLineChart(
                "Probabilities",      // chart title
                "Mission time",                      // x axis label
                "Probability",                      // y axis label
                dataset,                  // data
                PlotOrientation.VERTICAL,
                true,                     // include legend
                true,                     // tooltips
                false                     // urls
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
        chart.setBackgroundPaint(Color.white);

        // get a reference to the plot for further customisation...
        final XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.lightGray);
        //    plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0, 5.0, 5.0, 5.0));
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesLinesVisible(0, false);
        renderer.setSeriesShapesVisible(1, false);
        plot.setRenderer(renderer);

        // change the auto tick unit selection to integer units only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        ((NumberAxis) plot.getRangeAxis()).setTickUnit(new NumberTickUnit(0.05));
        plot.getRangeAxis().setRange(0, 1);


        return chart;
    }

}