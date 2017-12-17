package com.lastminute.core;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

public abstract class BaseValidator<T> implements Validator<T>
{
    private final List<Rule> rules = new LinkedList<>();

    protected void addRule(Predicate<T> condition, Supplier<RuntimeException> exception)
    {
        rules.add(new Rule(condition, exception));
    }

    @Override
    public void validate(T target)
    {
        rules.forEach(rule -> {
            if (rule.condition.test(target))
            {
                throw rule.exception.get();
            }
        });
    }

    private class Rule
    {
        final Predicate<T> condition;
        final Supplier<RuntimeException> exception;

        Rule(Predicate<T> condition, Supplier<RuntimeException> exception)
        {
            this.condition = condition;
            this.exception = exception;
        }
    }
}
