package br.com.wfcreations.observer.test;

import static org.junit.Assert.*;

import org.junit.Test;

import br.com.wfcreations.observer.dispatcher.Event;
import br.com.wfcreations.observer.dispatcher.EventDispatcher;
import br.com.wfcreations.observer.dispatcher.EventPhase;
import br.com.wfcreations.observer.dispatcher.IEventListener;

public class EventTest {

	public static String CLICK = "CLICK";

	@Test
	public void test() {
		EventDispatcher app = new EventDispatcher();
		EventDispatcher button = new EventDispatcher(app);

		IEventListener appListener = new AppHandler();
		IEventListener buttonListener = new ButtonHandler();

		app.addEventListener(CLICK, appListener, true);
		app.addEventListener(CLICK, appListener, false);
		button.addEventListener(CLICK, buttonListener);

		button.dispatchEvent(new MyEvent("CLICK"));
	}

	private static class ButtonHandler implements IEventListener {

		@Override
		public void listen(Event event) {
			System.out.println("Button");
			System.out.println(event.eventPhase());
			System.out.println(event.currentTarget());
			System.out.println(event.type);
			if (event instanceof MyEvent) {
				MyEvent e = (MyEvent) event;
				e.setMsg("Minha MSG");
			}
		}
	}

	private static class AppHandler implements IEventListener {

		@Override
		public void listen(Event event) {
			System.out.println("App");
			System.out.println(event.eventPhase());
			System.out.println(event.currentTarget());
			System.out.println(event.type);
			if (event.eventPhase() == EventPhase.BUBBLING_PHASE) {
				if (event instanceof MyEvent) {
					MyEvent e = (MyEvent) event;
					if (e.getMsg() != null)
						System.out.println(e.getMsg());
				}
			}
		}
	}

	private static class MyEvent extends Event {
		private String msg;

		public MyEvent(String type) {
			super(type, true, true);
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}
	}
}