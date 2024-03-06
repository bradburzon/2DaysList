package com.bradburzon.a2dayslist.settings;

import com.bradburzon.a2dayslist.tasks.Task;

import java.util.Comparator;

public class SettingManager {

   private static SortStrategyType sortStrategyType;
   // SettingStorage class

    public SettingManager(){
        this(SortStrategyType.BY_INDEX);
    }

    public SettingManager(SortStrategyType sortStrategyType){
        SettingManager.sortStrategyType = sortStrategyType;
    }

    public static  SettingManager createSettingsManager(){
        return new SettingManager();
    }

    public Comparator<Task> createSortStrategyType() {
        return SortStrategyFactory.create(sortStrategyType);
    }

    public void setSortStrategyType(SortStrategyType sortStrategyType) {
        SettingManager.sortStrategyType = sortStrategyType;
    }

    public SortStrategyType getSortStrategyType() {
        return sortStrategyType;
    }
}
