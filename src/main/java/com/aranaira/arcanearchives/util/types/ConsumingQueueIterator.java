package com.aranaira.arcanearchives.util.types;

import com.google.common.collect.AbstractIterator;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.List;
import java.util.Queue;

import static com.google.common.base.Preconditions.checkNotNull;

public class ConsumingQueueIterator<T> extends AbstractIterator<T> {
  private final Queue<T> queue;

  public ConsumingQueueIterator(List<T> elements) {
    this.queue = new ArrayDeque<>(elements.size());
    this.queue.addAll(elements);
  }

  @Override
  public T computeNext() {
    return queue.isEmpty() ? endOfData() : queue.remove();
  }
}
