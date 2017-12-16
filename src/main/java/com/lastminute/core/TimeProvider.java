package com.lastminute.core;

import java.time.LocalDateTime;

public interface TimeProvider
{
    LocalDateTime getCurrentDateTime();
}
