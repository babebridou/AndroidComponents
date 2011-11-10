package com.fairyteller.db.framework;

import java.util.Map;


public abstract class BeanInserter<T> {
	public abstract void updateMapWithBeanValue(T bean, Map<String, Object> values, String prefix);
	public abstract void updateBeanWithSequenceValue(T bean, long sequenceId);
	public abstract String getSequenceName();
	public abstract String getSequenceProperty(String prefix);
}
