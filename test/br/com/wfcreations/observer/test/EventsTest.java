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
package br.com.wfcreations.observer.test;

import org.junit.Test;

import br.com.wfcreations.observer.dispatcher.Event;
import br.com.wfcreations.observer.dispatcher.EventDispatcher;
import br.com.wfcreations.observer.dispatcher.IEventDispatcher;
import br.com.wfcreations.observer.dispatcher.IEventListener;

public class EventsTest {

	EventDispatcher dispatcher2;

	@Test
	public void test() throws NoSuchFieldException, SecurityException {
		App app = new App(null);
		Button button1 = new Button(app);

		app.addEventListener("CLICK", new AppEventListener());
		app.addEventListener("CLICK", new AppEventListener2());

		button1.addEventListener("CLICK", new ButtonEventListener());
		Event e = new Event("CLICK");
		button1.dispatchEvent(e);
	}

	private static class App extends EventDispatcher {
		public App(IEventDispatcher target) {
			super(target);
		}
	}

	private static class Button extends EventDispatcher {
		public Button(IEventDispatcher target) {
			super(target);
		}
	}

	private static class AppEventListener implements IEventListener {
		@Override
		public void listen(Event event) {
			System.out.println("APP EVENT 1 " + event.getType() + " " + event.getEventPhase());
			event.stopPropagationImmediate();
		}
	}

	private static class ButtonEventListener implements IEventListener {
		@Override
		public void listen(Event event) {
			System.out.println("BUTTON EVENT 1 " + event.getType() + " " + event.getEventPhase());
		}
	}

	private static class AppEventListener2 implements IEventListener {
		@Override
		public void listen(Event event) {
			System.out.println("APP EVENT 2 " + event.getType() + " " + event.getEventPhase());
		}
	}
}