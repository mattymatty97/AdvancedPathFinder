package com.mattymatty.apf.object;

import java.util.Observable;

interface PoolItem {

    boolean isValid();

    boolean isUsed();

    void addReference(ItemReference reference);

    void notifyAndClear();

    void clear();
}
