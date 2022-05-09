package org.jfree.chart.renderer;


import javax.swing.event.EventListenerList;
import org.jfree.chart.event.RendererChangeListener;
import org.jfree.chart.internal.Args;
import java.util.EventListener;
import java.util.List;
import java.util.Arrays;
import org.jfree.chart.event.RendererChangeEvent;
import java.io.Serializable;

public class AbstractRendererProduct4 implements Serializable, Cloneable {
	private transient EventListenerList listenerList;

	public void setListenerList(EventListenerList listenerList) {
		this.listenerList = listenerList;
	}

	/**
	* Registers an object to receive notification of changes to the renderer.
	* @param listener   the listener ( {@code  null}  not permitted).
	* @see #removeChangeListener(RendererChangeListener)
	*/
	public void addChangeListener(RendererChangeListener listener) {
		Args.nullNotPermitted(listener, "listener");
		this.listenerList.add(RendererChangeListener.class, listener);
	}

	/**
	* Deregisters an object so that it no longer receives notification of changes to the renderer.
	* @param listener   the object ( {@code  null}  not permitted).
	* @see #addChangeListener(RendererChangeListener)
	*/
	public void removeChangeListener(RendererChangeListener listener) {
		Args.nullNotPermitted(listener, "listener");
		this.listenerList.remove(RendererChangeListener.class, listener);
	}

	/**
	* Returns  {@code  true}  if the specified object is registered with the dataset as a listener.  Most applications won't need to call this method, it exists mainly for use by unit testing code.
	* @param listener   the listener.
	* @return  A boolean.
	*/
	public boolean hasListener(EventListener listener) {
		List<Object> list = Arrays.asList(this.listenerList.getListenerList());
		return list.contains(listener);
	}

	/**
	* Notifies all registered listeners that the renderer has been modified.
	* @param event   information about the change event.
	*/
	public void notifyListeners(RendererChangeEvent event) {
		Object[] ls = this.listenerList.getListenerList();
		for (int i = ls.length - 2; i >= 0; i -= 2) {
			if (ls[i] == RendererChangeListener.class) {
				((RendererChangeListener) ls[i + 1]).rendererChanged(event);
			}
		}
	}

	public Object clone() throws CloneNotSupportedException {
		return (AbstractRendererProduct4) super.clone();
	}
}