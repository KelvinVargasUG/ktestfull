package com.sasf.ktestfull.Constant;

import java.util.List;

public class StatusConst {
    public static final String ACTIVE = "ACTIVE";
    public static final String INACTIVE = "INACTIVE";
    public static final String REMOVED = "REMOVED";
    public static final String PENDING = "PENDING";
    public static final String IN_PROGRESS = "IN_PROGRESS";
    public static final String COMPLETE = "COMPLETE";

    public static final List<String> TASK_STATUS = List.of(PENDING, IN_PROGRESS, COMPLETE);
    public static final List<String> GENERIC_STATUS = List.of(ACTIVE, INACTIVE);
}
