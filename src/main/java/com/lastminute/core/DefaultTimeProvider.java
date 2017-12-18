package com.lastminute.core;

import java.time.LocalDateTime;

public class DefaultTimeProvider implements TimeProvider
{
    @Override
    public LocalDateTime getCurrentDateTime()
    {
        return LocalDateTime.now();
    }
}
