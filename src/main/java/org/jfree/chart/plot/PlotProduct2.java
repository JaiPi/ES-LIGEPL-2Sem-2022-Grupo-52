package org.jfree.chart.plot;


import javax.swing.event.EventListenerList;
import org.jfree.chart.event.PlotChangeListener;
import org.jfree.chart.event.PlotChangeEvent;
import java.io.Serializable;
import org.jfree.chart.api.PublicCloneable;

public class PlotProduct2 implements Serializable, PublicCloneable {
	private transient EventListenerList listenerList;

	public void setListenerList(EventListenerList listenerList) {
		this.listenerList = listenerList;
	}

	/**
	* Registers an object for notification of changes to the plot.
	* @param listener   the object to be registered.
	* @see #removeChangeListener(PlotChangeListener)
	*/
	public void addChangeListener(PlotChangeListener listener) {
		this.listenerList.add(PlotChangeListener.class, listener);
	}

	/**
	* Unregisters an object for notification of changes to the plot.
	* @param listener   the object to be unregistered.
	* @see #addChangeListener(PlotChangeListener)
	*/
	public void removeChangeListener(PlotChangeListener listener) {
		this.listenerList.remove(PlotChangeListener.class, listener);
	}

	/**
	* Sets a flag that controls whether or not listeners receive {@link PlotChangeEvent}  notifications.
	* @param notify   a boolean.
	* @see #isNotify()
	*/
	public void setNotify(boolean notify, Plot plot) {
		plot.setNotify2(notify);
		if (notify) {
			notifyListeners(new PlotChangeEvent(plot), plot);
		}
	}

	/**
	* Notifies all registered listeners that the plot has been modified.
	* @param event   information about the change event.
	*/
	public void notifyListeners(PlotChangeEvent event, Plot plot) {
		if (!plot.isNotify()) {
			return;
		}
		Object[] listeners = this.listenerList.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == PlotChangeListener.class) {
				((PlotChangeListener) listeners[i + 1]).plotChanged(event);
			}
		}
	}

	public Object clone() throws CloneNotSupportedException {
		return (PlotProduct2) super.clone();
	}
}