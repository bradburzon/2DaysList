package com.bradburzon.a2dayslist.settings;

import com.bradburzon.a2dayslist.tasks.Task;
import org.junit.Test;

import java.util.Comparator;

import static org.junit.Assert.assertTrue;

public class SortStrategyFactoryTest {

    @Test
    public void create_WhenSortStrategyTypeIsByName_ReturnsByNameSortStrategy() {
        Comparator<Task> comparator = SortStrategyFactory.create(SortStrategyType.BY_NAME);
        assertTrue(comparator instanceof ByNameSortStrategy);
    }

    @Test
    public void create_WhenSortStrategyTypeIsByStatus_ReturnsByStatusSortStrategy() {
        Comparator<Task> comparator = SortStrategyFactory.create(SortStrategyType.BY_STATUS);
        assertTrue( comparator instanceof ByStatusSortStrategy);
    }

    @Test
    public void create_WhenSortStrategyTypeIsIndex_ReturnsByIndexSortStrategy_() {
        Comparator<Task> comparator = SortStrategyFactory.create(SortStrategyType.BY_INDEX);
        assertTrue( comparator instanceof ByIndexSortStrategy);
    }
}
