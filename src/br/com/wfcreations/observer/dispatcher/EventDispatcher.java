/*
 * Copyright (c) 2013, Welsiton Ferreira (wfcreations@gmail.com)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice, this
 *  list of conditions and the following disclaimer.
 *
 *  Redistributions in binary form must reproduce the above copyright notice, this
 *  list of conditions and the following disclaimer in the documentation and/or
 *  other materials provided with the distribution.
 *
 *  Neither the name of the WFCreation nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package br.com.wfcreations.observer.dispatcher;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class EventDispatcher implements IEventDispatcher {

	private List<EventListenerObject> events = new LinkedList<>();

	IEventDispatcher target;

	private static Comparator<EventListenerObject> comparator = new Comparator<EventListenerObject>() {
		@Override
		public int compare(EventListenerObject a, EventListenerObject b) {
			return a.priority > b.priority ? -1 : a.priority < b.priority ? 1 : 0;
		}
	};

	public EventDispatcher(IEventDispatcher target) {
		this.target = target;
	}

	public EventDispatcher() {
	}

	public void addEventListener(String type, IEventListener listener, boolean useCapture, int priority) {
		events.add(new EventListenerObject(type, listener, useCapture, priority));
		Collections.sort(events, comparator);
	}

	public void addEventListener(String type, IEventListener listener, boolean useCapture) {
		this.addEventListener(type, listener, useCapture, 0);
	}

	public void addEventListener(String type, IEventListener listener) {
		this.addEventListener(type, listener, false);
	}

	public void dispatchEvent(Event event) {
		if (event.getTarget() == null)
			setFieldValue(event, "target", this);

		if ((boolean) getFieldValue(event, "stop"))
			return;

		if (target != null && (getFieldValue(event, "eventPhase") == null || getFieldValue(event, "eventPhase") == EventPhase.CAPTURING_PHASE)) {
			setFieldValue(event, "eventPhase", EventPhase.CAPTURING_PHASE);
			target.dispatchEvent(event);
		}

		if (event.getTarget() == this) {
			setFieldValue(event, "eventPhase", EventPhase.AT_TARGET);
		}

		if ((boolean) getFieldValue(event, "stop"))
			return;

		setFieldValue(event, "currentTarget", this);
		for (EventListenerObject eventListener : events) {
			if ((boolean) getFieldValue(event, "immediate"))
				return;
			if (eventListener.type == event.getType() && !(getFieldValue(event, "eventPhase") == EventPhase.CAPTURING_PHASE && !eventListener.useCapture))
				eventListener.listener.listen(event);
		}

		if ((boolean) getFieldValue(event, "stop"))
			return;

		if (target != null && event.isBubbles() && (getFieldValue(event, "eventPhase") == EventPhase.BUBBLING_PHASE || getFieldValue(event, "eventPhase") == EventPhase.AT_TARGET)) {
			setFieldValue(event, "eventPhase", EventPhase.BUBBLING_PHASE);
			target.dispatchEvent(event);
		}
	}

	private Object getFieldValue(Event event, String field) {
		Object object = null;
		try {
			Field currentTargetField = event.getClass().getDeclaredField(field);
			currentTargetField.setAccessible(true);
			try {
				object = currentTargetField.get(event);
			} catch (IllegalArgumentException | IllegalAccessException e1) {
				e1.printStackTrace();
			} finally {
				currentTargetField.setAccessible(false);
			}
			return object;
		} catch (NoSuchFieldException | SecurityException e2) {
			e2.printStackTrace();
		}
		return object;
	}

	private void setFieldValue(Event event, String field, Object value) {
		try {
			Field currentTargetField = event.getClass().getDeclaredField(field);
			currentTargetField.setAccessible(true);
			try {
				currentTargetField.set(event, value);
			} catch (IllegalArgumentException | IllegalAccessException e1) {
				e1.printStackTrace();
			} finally {
				currentTargetField.setAccessible(false);
			}
		} catch (NoSuchFieldException | SecurityException e2) {
			e2.printStackTrace();
		}
	}

	@Override
	public boolean hasEventListener(String type) {
		for (EventListenerObject eventListener : events)
			if (eventListener.type == type)
				return true;
		return false;
	}

	@Override
	public void removeEventListener(String type, IEventListener listener) {
		Iterator<EventListenerObject> iter = events.iterator();
		while (iter.hasNext()) {
			EventListenerObject element = iter.next();
			if (element.type == type && element.listener == listener)
				iter.remove();
		}
	}

	@Override
	public boolean willTrigger(String type) {
		if (this.hasEventListener(type) || (this.target != null && this.target.willTrigger(type)))
			return true;
		return false;
	}

	private static class EventListenerObject {

		public final String type;

		public final IEventListener listener;

		public final boolean useCapture;

		public final int priority;

		public EventListenerObject(String type, IEventListener listener, boolean useCapture, int priority) {
			this.type = type;
			this.listener = listener;
			this.useCapture = useCapture;
			this.priority = priority;
		}
	}
}