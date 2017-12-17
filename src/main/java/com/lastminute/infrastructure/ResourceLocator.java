package com.lastminute.infrastructure;

public class ResourceLocator
{
    public String fullPathTo(String fileName)
    {
        return getClass().getClassLoader().getResource(fileName).getPath();
    }
}
