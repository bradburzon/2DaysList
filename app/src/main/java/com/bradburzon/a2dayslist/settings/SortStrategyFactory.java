package com.bradburzon.a2dayslist.settings;

import com.bradburzon.a2dayslist.tasks.Task;

import java.util.Comparator;

public class SortStrategyFactory {

    public static Comparator<Task> create(SortStrategyType sortStrategytype){
        switch (sortStrategytype) {
            case BY_NAME:
                return new ByNameSortStrategy();
            case BY_STATUS:
                return new ByStatusSortStrategy();
            default:
                return new ByIndexSortStrategy();
        }
    }
}
