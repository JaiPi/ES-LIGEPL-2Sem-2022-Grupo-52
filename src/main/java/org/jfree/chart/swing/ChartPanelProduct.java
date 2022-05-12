package org.jfree.chart.swing;


import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JMenu;
import java.awt.Dimension;
import java.awt.Toolkit;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.Zoomable;
import java.io.Serializable;
import org.jfree.chart.JFreeChart;

public class ChartPanelProduct implements Serializable {
	private JMenuItem zoomInBothMenuItem;
	private JMenuItem zoomInDomainMenuItem;
	private JMenuItem zoomInRangeMenuItem;
	private JMenuItem zoomOutBothMenuItem;
	private JMenuItem zoomOutDomainMenuItem;
	private JMenuItem zoomOutRangeMenuItem;
	private JMenuItem zoomResetBothMenuItem;
	private JMenuItem zoomResetDomainMenuItem;
	private JMenuItem zoomResetRangeMenuItem;

	/**
	* Creates a popup menu for the panel.  This method includes code that auto-detects JFreeSVG and OrsonPDF (via reflection) and, if they are present (and the  {@code  save}  argument is  {@code  true} , adds a menu item for each.
	* @param properties   include a menu item for the chart property editor.
	* @param copy  include a menu item for copying to the clipboard.
	* @param save   include one or more menu items for saving the chart to supported image formats.
	* @param print   include a menu item for printing the chart.
	* @param zoom   include menu items for zooming.
	* @return  The popup menu.
	*/
	public JPopupMenu createPopupMenu(boolean properties, boolean copy, boolean save, boolean print, boolean zoom,
			ChartPanel chartPanel) {
		JPopupMenu result = new JPopupMenu(ChartPanel.localizationResources.getString("Chart") + ":");
		boolean separator = false;
		if (properties) {
			JMenuItem propertiesItem = new JMenuItem(ChartPanel.localizationResources.getString("Properties..."));
			propertiesItem.setActionCommand(ChartPanel.PROPERTIES_COMMAND);
			propertiesItem.addActionListener(chartPanel);
			result.add(propertiesItem);
			separator = true;
		}
		if (copy) {
			if (separator) {
				result.addSeparator();
			}
			JMenuItem copyItem = new JMenuItem(ChartPanel.localizationResources.getString("Copy"));
			copyItem.setActionCommand(ChartPanel.COPY_COMMAND);
			copyItem.addActionListener(chartPanel);
			result.add(copyItem);
			separator = !save;
		}
		if (save) {
			if (separator) {
				result.addSeparator();
			}
			JMenu saveSubMenu = new JMenu(ChartPanel.localizationResources.getString("Save_as"));
			{
				JMenuItem pngItem = new JMenuItem(ChartPanel.localizationResources.getString("PNG..."));
				pngItem.setActionCommand(ChartPanel.SAVE_AS_PNG_COMMAND);
				pngItem.addActionListener(chartPanel);
				saveSubMenu.add(pngItem);
			}
			{
				final Dimension ss = Toolkit.getDefaultToolkit().getScreenSize();
				final String pngName = "PNG (" + ss.width + "x" + ss.height + ") ...";
				JMenuItem pngItem = new JMenuItem(pngName);
				pngItem.setActionCommand(ChartPanel.SAVE_AS_PNG_SIZE_COMMAND);
				pngItem.addActionListener(chartPanel);
				saveSubMenu.add(pngItem);
			}
			if (ChartUtils.isJFreeSVGAvailable()) {
				JMenuItem svgItem = new JMenuItem(ChartPanel.localizationResources.getString("SVG..."));
				svgItem.setActionCommand(ChartPanel.SAVE_AS_SVG_COMMAND);
				svgItem.addActionListener(chartPanel);
				saveSubMenu.add(svgItem);
			}
			if (ChartUtils.isOrsonPDFAvailable()) {
				JMenuItem pdfItem = new JMenuItem(ChartPanel.localizationResources.getString("PDF..."));
				pdfItem.setActionCommand(ChartPanel.SAVE_AS_PDF_COMMAND);
				pdfItem.addActionListener(chartPanel);
				saveSubMenu.add(pdfItem);
			}
			result.add(saveSubMenu);
			separator = true;
		}
		if (print) {
			if (separator) {
				result.addSeparator();
			}
			JMenuItem printItem = new JMenuItem(ChartPanel.localizationResources.getString("Print..."));
			printItem.setActionCommand(ChartPanel.PRINT_COMMAND);
			printItem.addActionListener(chartPanel);
			result.add(printItem);
			separator = true;
		}
		if (zoom) {
			if (separator) {
				result.addSeparator();
			}
			JMenu zoomInMenu = new JMenu(ChartPanel.localizationResources.getString("Zoom_In"));
			this.zoomInBothMenuItem = new JMenuItem(ChartPanel.localizationResources.getString("All_Axes"));
			this.zoomInBothMenuItem.setActionCommand(ChartPanel.ZOOM_IN_BOTH_COMMAND);
			this.zoomInBothMenuItem.addActionListener(chartPanel);
			zoomInMenu.add(this.zoomInBothMenuItem);
			zoomInMenu.addSeparator();
			this.zoomInDomainMenuItem = new JMenuItem(ChartPanel.localizationResources.getString("Domain_Axis"));
			this.zoomInDomainMenuItem.setActionCommand(ChartPanel.ZOOM_IN_DOMAIN_COMMAND);
			this.zoomInDomainMenuItem.addActionListener(chartPanel);
			zoomInMenu.add(this.zoomInDomainMenuItem);
			this.zoomInRangeMenuItem = new JMenuItem(ChartPanel.localizationResources.getString("Range_Axis"));
			this.zoomInRangeMenuItem.setActionCommand(ChartPanel.ZOOM_IN_RANGE_COMMAND);
			this.zoomInRangeMenuItem.addActionListener(chartPanel);
			zoomInMenu.add(this.zoomInRangeMenuItem);
			result.add(zoomInMenu);
			JMenu zoomOutMenu = new JMenu(ChartPanel.localizationResources.getString("Zoom_Out"));
			this.zoomOutBothMenuItem = new JMenuItem(ChartPanel.localizationResources.getString("All_Axes"));
			this.zoomOutBothMenuItem.setActionCommand(ChartPanel.ZOOM_OUT_BOTH_COMMAND);
			this.zoomOutBothMenuItem.addActionListener(chartPanel);
			zoomOutMenu.add(this.zoomOutBothMenuItem);
			zoomOutMenu.addSeparator();
			this.zoomOutDomainMenuItem = new JMenuItem(ChartPanel.localizationResources.getString("Domain_Axis"));
			this.zoomOutDomainMenuItem.setActionCommand(ChartPanel.ZOOM_OUT_DOMAIN_COMMAND);
			this.zoomOutDomainMenuItem.addActionListener(chartPanel);
			zoomOutMenu.add(this.zoomOutDomainMenuItem);
			this.zoomOutRangeMenuItem = new JMenuItem(ChartPanel.localizationResources.getString("Range_Axis"));
			this.zoomOutRangeMenuItem.setActionCommand(ChartPanel.ZOOM_OUT_RANGE_COMMAND);
			this.zoomOutRangeMenuItem.addActionListener(chartPanel);
			zoomOutMenu.add(this.zoomOutRangeMenuItem);
			result.add(zoomOutMenu);
			JMenu autoRangeMenu = new JMenu(ChartPanel.localizationResources.getString("Auto_Range"));
			this.zoomResetBothMenuItem = new JMenuItem(ChartPanel.localizationResources.getString("All_Axes"));
			this.zoomResetBothMenuItem.setActionCommand(ChartPanel.ZOOM_RESET_BOTH_COMMAND);
			this.zoomResetBothMenuItem.addActionListener(chartPanel);
			autoRangeMenu.add(this.zoomResetBothMenuItem);
			autoRangeMenu.addSeparator();
			this.zoomResetDomainMenuItem = new JMenuItem(ChartPanel.localizationResources.getString("Domain_Axis"));
			this.zoomResetDomainMenuItem.setActionCommand(ChartPanel.ZOOM_RESET_DOMAIN_COMMAND);
			this.zoomResetDomainMenuItem.addActionListener(chartPanel);
			autoRangeMenu.add(this.zoomResetDomainMenuItem);
			this.zoomResetRangeMenuItem = new JMenuItem(ChartPanel.localizationResources.getString("Range_Axis"));
			this.zoomResetRangeMenuItem.setActionCommand(ChartPanel.ZOOM_RESET_RANGE_COMMAND);
			this.zoomResetRangeMenuItem.addActionListener(chartPanel);
			autoRangeMenu.add(this.zoomResetRangeMenuItem);
			result.addSeparator();
			result.add(autoRangeMenu);
		}
		return result;
	}

	/**
	* The idea is to modify the zooming options depending on the type of chart being displayed by the panel.
	* @param x   horizontal position of the popup.
	* @param y   vertical position of the popup.
	*/
	public void displayPopupMenu(int x, int y, JPopupMenu thisPopup, JFreeChart thisChart, ChartPanel chartPanel) {
		if (thisPopup == null) {
			return;
		}
		boolean isDomainZoomable = false;
		boolean isRangeZoomable = false;
		Plot plot = (thisChart != null ? thisChart.getPlot() : null);
		if (plot instanceof Zoomable) {
			Zoomable z = (Zoomable) plot;
			isDomainZoomable = z.isDomainZoomable();
			isRangeZoomable = z.isRangeZoomable();
		}
		if (this.zoomInDomainMenuItem != null) {
			this.zoomInDomainMenuItem.setEnabled(isDomainZoomable);
		}
		if (this.zoomOutDomainMenuItem != null) {
			this.zoomOutDomainMenuItem.setEnabled(isDomainZoomable);
		}
		if (this.zoomResetDomainMenuItem != null) {
			this.zoomResetDomainMenuItem.setEnabled(isDomainZoomable);
		}
		if (this.zoomInRangeMenuItem != null) {
			this.zoomInRangeMenuItem.setEnabled(isRangeZoomable);
		}
		if (this.zoomOutRangeMenuItem != null) {
			this.zoomOutRangeMenuItem.setEnabled(isRangeZoomable);
		}
		if (this.zoomResetRangeMenuItem != null) {
			this.zoomResetRangeMenuItem.setEnabled(isRangeZoomable);
		}
		if (this.zoomInBothMenuItem != null) {
			this.zoomInBothMenuItem.setEnabled(isDomainZoomable && isRangeZoomable);
		}
		if (this.zoomOutBothMenuItem != null) {
			this.zoomOutBothMenuItem.setEnabled(isDomainZoomable && isRangeZoomable);
		}
		if (this.zoomResetBothMenuItem != null) {
			this.zoomResetBothMenuItem.setEnabled(isDomainZoomable && isRangeZoomable);
		}
		thisPopup.show(chartPanel, x, y);
	}
}