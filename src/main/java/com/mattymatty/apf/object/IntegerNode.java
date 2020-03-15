package com.mattymatty.apf.object;

import com.mattymatty.apf.exceptions.ReleasedObjectException;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.SoftReferenceObjectPool;

import java.lang.ref.WeakReference;

public class IntegerNode implements ItemReference, Position, GraphNode{

    InnerNode reference;

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
        if(reference!=null){
        try {
            pool.returnObject(reference);
            update();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        }
    }

    @Override
    public boolean isPresent() {
        return reference!=null;
    }

    @Override
    public Position[] getHistory() {
        if(reference==null)
            throw new ReleasedObjectException("this object no longer exists");
        return reference.getHistory();
    }

    @Override
    public double getWeight() {
        if(reference==null)
            throw new ReleasedObjectException("this object no longer exists");
        return reference.getWeight();
    }

    private static class InnerNode implements PoolItem, Position, GraphNode{

        public InnerNode() {
        }

        int x=0;

        int y=0;

        int z=0;

        boolean inUse = false;

        Position[] history = new Position[0];

        double weight = 0;

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
        public Position[] getHistory() {
            return history;
        }

        @Override
        public double getWeight() {
            return weight;
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
            history = new InnerNode[0];
            weight = 0;
        }

        @Override
        public boolean equals(Object obj) {
            if(!(obj instanceof InnerNode))
                return false;
            InnerNode cmp = (InnerNode)obj;
            if(inUse && cmp.inUse)
                return ((x==cmp.getX())&&(y==cmp.getY())&&(z==cmp.getZ()));
            return false;
        }

    }

    private static class ObjectFactory extends BasePooledObjectFactory<InnerNode> {

        @Override
        public InnerNode create() throws Exception {
            return new InnerNode();
        }

        @Override
        public PooledObject<InnerNode> wrap(InnerNode obj) {
            return new DefaultPooledObject<>(obj);
        }

        @Override
        public boolean validateObject(PooledObject<InnerNode> p) {
            return p.getObject().isValid();
        }

        @Override
        public void activateObject(PooledObject<InnerNode> p) throws Exception {
            p.getObject().clear();
            p.getObject().inUse = true;
        }

        @Override
        public void passivateObject(PooledObject<InnerNode> p) throws Exception {
            p.getObject().notifyAndClear();
            p.getObject().inUse = false;
        }
    }

    private static ObjectPool<InnerNode> pool = new SoftReferenceObjectPool<>(new ObjectFactory());

    public static int itemCount(){
        return pool.getNumActive();
    }

    public IntegerNode(Position pos) {
        try {
            reference = pool.borrowObject();
            reference.x = (int)Math.floor(pos.getX());
            reference.y = (int)Math.floor(pos.getY());
            reference.z = (int)Math.floor(pos.getZ());
            reference.history = new Position[]{pos};
            reference.weight = 0;
            reference.addReference(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public IntegerNode(int x, int y, int z, Position[] history, double weight){
        try {
            reference = pool.borrowObject();
            reference.x = x;
            reference.y = y;
            reference.z = z;
            reference.history = history;
            reference.weight = weight;
            reference.addReference(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public IntegerNode(int x, int y, int z, double weight){
        try {
            reference = pool.borrowObject();
            reference.x = x;
            reference.y = y;
            reference.z = z;
            reference.history = new Position[0];
            reference.weight = weight;
            reference.addReference(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof IntegerNode))
            return false;
        if(reference==null || ((IntegerNode)obj).reference==null)
            return false;
        return reference.equals(((IntegerNode)obj).reference);
    }
}
