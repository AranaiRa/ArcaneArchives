/*package com.aranaira.arcanearchives.types.iterators;

import java.util.*;
import java.util.stream.Collectors;

// This remains as a testament to my inability to function properly
// Yes, addAll is a thing.
public class ListConcatIterable<T> implements Iterable<T> {
  private List<Iterator<T>> iterators;

  public ListConcatIterable(Collection<List<T>> lists) {
    this.iterators = lists.stream().map(List::iterator).collect(Collectors.toList());
  }

  @SuppressWarnings("unchecked")
  @Override
  public Iterator<T> iterator() {
    if (iterators.isEmpty()) {
      return (Iterator<T>) Collections.emptyList().iterator();
    }

    return new ListConcatIterator();
  }

  public class ListConcatIterator implements Iterator<T> {
    private int index = 0;

    private Iterator<T> getCurrentIterator () {
      if (index < iterators.size()) {
       return iterators.get(index);
      }



      return null;
    }

    private Iterator<T> nextIterator () {
      index++;
      return getCurrentIterator();
    }

    private boolean hasNextIterator () {
      return (index + 1 < iterators.size());
    }

    @Override
    public boolean hasNext() {
      Iterator<T> iter = getCurrentIterator();
      if (iter == null) {
        return false;
      }
      return iter.hasNext();
    }

    @Override
    public T next() {
      Iterator<T> iter = getCurrentIterator();
      if (iter == null) {

      }
      if (!getCurrentIterator().hasNext()) {

      }
      if (iterator == null || !iterator.hasNext()) {
        nextIterator();
      }

      if (iterator == null || !iterator.hasNext()) {
        throw new NoSuchElementException();
      }

      return iterator.next();
    }
  }
}*/
