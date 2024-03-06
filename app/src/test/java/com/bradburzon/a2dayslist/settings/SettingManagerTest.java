package com.bradburzon.a2dayslist.settings;

import com.bradburzon.a2dayslist.tasks.Task;
import org.junit.Before;
import org.junit.Test;
import java.util.Comparator;

import static org.junit.Assert.*;

public class SettingManagerTest {

    private SettingManager settingManager;

    @Before
    public void setUp() {
        settingManager = SettingManager.createSettingsManager();
    }

    @Test
    public void createSettingsManager_defaultConstructor_defaultSortStrategySet() {
        assertNotNull(settingManager);
        assertEquals(SortStrategyType.BY_INDEX, settingManager.getSortStrategyType());
    }

    @Test
    public void setSortStrategyType_changeToBY_NAME_sortStrategyUpdated() {
        settingManager.setSortStrategyType(SortStrategyType.BY_NAME);

        assertEquals(SortStrategyType.BY_NAME, settingManager.getSortStrategyType());
    }

    @Test
    public void createSortStrategyType_setBY_INDEX_correctComparatorReturned() {
        settingManager.setSortStrategyType(SortStrategyType.BY_INDEX);

        Comparator<Task> comparator = settingManager.createSortStrategyType();

        assertNotNull(comparator);
    }

    @Test
    public void createSortStrategyType_setBY_NAME_correctComparatorReturned() {
        settingManager.setSortStrategyType(SortStrategyType.BY_NAME);

        Comparator<Task> comparator = settingManager.createSortStrategyType();

        assertNotNull(comparator);
    }
}
