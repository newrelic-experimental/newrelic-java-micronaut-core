package io.micronaut.inject;

import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;

@Weave(type = MatchType.Interface)
public abstract class ExecutionHandle<T, R> {
	
	public abstract Class getDeclaringType();

	@Trace
	public R invoke(Object... arguments) {
		return Weaver.callOriginal();
	}
}
