package com.savochkin.twitter.domain;

import java.util.*;

// feed of items, the size of feed is capped by maxItems
// items are maintained in order defined by comparator
public class Feed<T> {
    int maxItems;
    Comparator<T> comparator;
    LinkedList<T> feed = new LinkedList<>();

    public Feed(int maxItems, Comparator<T> comparator) {
        this.maxItems = maxItems;
        this.comparator = comparator;
    }

    public void addFirst(T item) {
        feed.addFirst(item);
        if (feed.size() > maxItems) feed.removeLast();
    }

    public void addAll(List<T> items) {
        feed.addAll(items);
        feed.sort(comparator);
        feed = new LinkedList(feed.stream().limit(maxItems).toList());
    }

    public List<T> getAll() {
        return feed;
    }

    public void removeAll(Feed<T> f) {
        Set<T> set = new HashSet<>(f.getAll());
        this.feed = new LinkedList(
                feed.stream()
                .filter(t -> !set.contains(t))
                .toList()
        );
    }
}
