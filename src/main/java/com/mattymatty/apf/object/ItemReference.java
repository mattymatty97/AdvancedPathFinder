package com.mattymatty.apf.object;

import java.util.Observer;

interface ItemReference {
    boolean isPresent();

    void update();

    void free();
}
