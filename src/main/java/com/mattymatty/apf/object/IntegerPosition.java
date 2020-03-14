package com.mattymatty.apf.object;

import com.mattymatty.apf.exceptions.ReleasedObjectException;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.SoftReferenceObjectPool;


import java.lang.ref.WeakReference;

public class IntegerPosition implements ItemReference, Position{

    InnerPosition reference;

    @Override
    public float getX(){
        if(reference==null)
            throw new ReleasedObjectException("this object no longer exists");
        return reference.getX();
    }

    @Override
    public float getY() {
        if(reference==null)
            throw new ReleasedObjectException("this object no longer exists");
        return reference.getY();
    }

    @Override
    public float getZ() {
        if(reference==null)
            throw new ReleasedObjectException("this object no longer exists");
        return reference.getZ();
    }

    @Override
    public void update() {
        reference=null;
    }

    @Override
    public void free() {
        if(reference!=null) {
            try {
                pool.returnObject(reference);
                update();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public boolean isPresent() {
        return reference!=null;
    }

    private static class InnerPosition implements PoolItem, Position {

        public InnerPosition() {
        }

        int x=0;

        int y=0;

        int z=0;

        boolean inUse = false;

        @Override
        public float getX() {
            return x;
        }

        @Override
        public float getY() {
            return y;
        }

        @Override
        public float getZ() {
            return z;
        }

        @Override
        public boolean isValid() {
            return true;
        }

        @Override
        public boolean isUsed() {
            return inUse;
        }

        WeakReference<ItemReference> referencer = new WeakReference<>(null);

        @Override
        public void addReference(ItemReference reference) {
            referencer = new WeakReference<>(reference);
        }

        @Override
        public void notifyAndClear() {
            ItemReference reference = referencer.get();
            if(reference!=null)
                reference.update();
            referencer.clear();
            clear();
        }

        @Override
        public void clear() {
            x = 0;
            y = 0;
            z = 0;
        }

        @Override
        public boolean equals(Object obj) {
            if(!(obj instanceof InnerPosition))
                return false;
            InnerPosition cmp = (InnerPosition)obj;
            if(inUse && cmp.inUse)
                return ((x==cmp.getX())&&(y==cmp.getY())&&(z==cmp.getZ()));
            return false;
        }
    }

    private static class ObjectFactory extends BasePooledObjectFactory<InnerPosition> {

        @Override
        public InnerPosition create() throws Exception {
            return new InnerPosition();
        }

        @Override
        public PooledObject<InnerPosition> wrap(InnerPosition obj) {
            return new DefaultPooledObject<>(obj);
        }

        @Override
        public boolean validateObject(PooledObject<InnerPosition> p) {
            return p.getObject().isValid();
        }

        @Override
        public void activateObject(PooledObject<InnerPosition> p) throws Exception {
            p.getObject().clear();
            p.getObject().inUse = true;
        }

        @Override
        public void passivateObject(PooledObject<InnerPosition> p) throws Exception {
            p.getObject().notifyAndClear();
            p.getObject().inUse = false;
        }
    }

    private static ObjectPool<InnerPosition> pool = new SoftReferenceObjectPool<>(new ObjectFactory());

    public static int itemCount(){
        return pool.getNumActive();
    }

    public IntegerPosition(int x, int y, int z){
        try {
            reference = pool.borrowObject();
            reference.x = x;
            reference.y = y;
            reference.z = z;
            reference.addReference(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof IntegerPosition))
            return false;
        if(reference==null || ((IntegerPosition)obj).reference==null)
            return false;
        return reference.equals(((IntegerPosition)obj).reference);
    }
}
