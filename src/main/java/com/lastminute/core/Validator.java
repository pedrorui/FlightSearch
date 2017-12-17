package com.lastminute.core;

public interface Validator<T>
{
    void validate(T target);
}
