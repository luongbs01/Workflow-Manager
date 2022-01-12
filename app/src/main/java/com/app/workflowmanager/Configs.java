package com.app.workflowmanager;

public class Configs {
    public static class SortByType {
        public static final int SORT_MODE_FROM_A_TO_Z = 0;
        public static final int SORT_MODE_FROM_Z_TO_A = 1;
        public static final int SORT_MODE_FROM_LATEST = 2;
        public static final int SORT_MODE_FROM_OLDEST = 3;
    }

    public static class SortType {
        public static final int DATE = 0;
        public static final int NAME = 1;

        public static final boolean ASCENDING = true;
        public static final boolean DESCENDING = false;
    }
}
