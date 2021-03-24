package com.fgonzalez.categorycalendar.util;

import java.util.Calendar;

import lombok.Getter;

public class ChangeTimeVerificatorUtil {
    @Getter
    private Long lastChangeTime;

    public ChangeTimeVerificatorUtil() {
        updateLastChangeTime();
    }

    public Boolean verifyHaveChanges(Long timeInMillis) {
        if (timeInMillis == null) {
            return true;
        }
        return timeInMillis != lastChangeTime;
    }

    public void updateLastChangeTime() {
        lastChangeTime = Calendar.getInstance().getTimeInMillis();
    }
}
