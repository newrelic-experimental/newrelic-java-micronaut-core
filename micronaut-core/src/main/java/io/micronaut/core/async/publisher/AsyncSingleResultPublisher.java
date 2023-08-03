package io.micronaut.core.async.publisher;

import org.reactivestreams.Subscriber;

import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.micronaut.core.NRSubscriber;

import io.micronaut.core.async.subscriber.SingleThreadedBufferingSubscriber;

@Weave
public abstract class AsyncSingleResultPublisher<T> {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void subscribe(Subscriber<? super T> subscriber) {
		if(!(subscriber instanceof NRSubscriber) && !(subscriber instanceof CompletableFuturePublisher) && !(subscriber instanceof SingleThreadedBufferingSubscriber)) {
			NRSubscriber<? super T> wrapper = new NRSubscriber(subscriber);
			subscriber = wrapper;
		}
		Weaver.callOriginal();
	}
}
