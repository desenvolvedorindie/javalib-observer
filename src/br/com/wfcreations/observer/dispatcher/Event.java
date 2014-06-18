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

public class Event {

	private final String type;

	private final boolean bubbles;

	private final boolean cancelable;

	private IEventDispatcher target;

	private EventPhase eventPhase;

	private IEventDispatcher currentTarget;

	@SuppressWarnings("unused")
	private boolean stop;

	@SuppressWarnings("unused")
	private boolean immediate;

	public Event(String type, boolean bubbles, boolean cancelable) {
		this.type = type;
		this.bubbles = bubbles;
		this.cancelable = cancelable;
	}

	public Event(String type, boolean bubbles) {
		this(type, bubbles, false);
	}

	public Event(String type) {
		this(type, false, false);
	}

	public void stopPropagation() {
		if (cancelable) {
			stop = true;
			immediate = false;
		}
	}

	public void stopPropagationImmediate() {
		if (cancelable) {
			stop = true;
			immediate = true;
		}
	}

	public String getType() {
		return type;
	}

	public boolean isCancelable() {
		return cancelable;
	}

	public IEventDispatcher getCurrentTarget() {
		return currentTarget;
	}

	public EventPhase getEventPhase() {
		return eventPhase;
	}

	public IEventDispatcher getTarget() {
		return target;
	}

	public boolean isBubbles() {
		return bubbles;
	}
}