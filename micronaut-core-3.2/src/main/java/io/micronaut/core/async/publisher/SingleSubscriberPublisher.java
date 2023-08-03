package io.micronaut.core.async.publisher;

import org.reactivestreams.Subscriber;

import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.micronaut.core.NRSubscriber;

@Weave(type = MatchType.BaseClass)
public abstract class SingleSubscriberPublisher<T> {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void doSubscribe(Subscriber<? super T> subscriber) {
		if(!(subscriber instanceof NRSubscriber)) {
			NRSubscriber<? super T> wrapper = new NRSubscriber(subscriber);
			subscriber = wrapper;
		}
		Weaver.callOriginal();
	}
}
