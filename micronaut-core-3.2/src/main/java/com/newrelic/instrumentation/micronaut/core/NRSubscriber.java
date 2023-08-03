package com.newrelic.instrumentation.micronaut.core;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import com.newrelic.agent.bridge.AgentBridge;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Token;
import com.newrelic.api.agent.Trace;

public class NRSubscriber<T> implements Subscriber<T> {
	
	private Subscriber<T> delegate = null;
	private Token token = null;
	private static boolean isTransformed = false;

	public NRSubscriber(Subscriber<T> d) {
		delegate = d;
		if(!isTransformed) {
			isTransformed = true;
			AgentBridge.instrumentation.retransformUninstrumentedClass(getClass());
		}
	}

	@Override
	public void onSubscribe(Subscription s) {
		delegate.onSubscribe(s);
		Token t = NewRelic.getAgent().getTransaction().getToken();
		if(t != null && t.isActive()) {
			token = t;
		} else if(t != null) {
			t.expire();
			t = null;
		}
	}

	@Override
	@Trace(async = true)
	public void onNext(T t) {
		if(token != null) {
			token.link();
		}
		delegate.onNext(t);
	}

	@Override
	@Trace(async = true)
	public void onError(Throwable t) {
		NewRelic.noticeError(t);
		if(token != null) {
			token.linkAndExpire();
			token = null;
		}
		delegate.onError(t);
	}

	@Override
	@Trace(async = true)
	public void onComplete() {
		if(token != null) {
			token.linkAndExpire();
			token = null;
		}
		delegate.onComplete();
	}

}
