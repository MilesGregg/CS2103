abstract class A {
    abstract void q();
}

interface Y {
    int p(int j);
    A me();
}

interface X {
    void m(Object o);
    X n();
}

class B extends A {
    @Override
    void q() {
        System.out.println("Hi");
    }
}

public class Testing implements X, Y{
    @Override
    public int p(int j) {
        return 0;
    }

    @Override
    public A me() {
        // can't return a abstract class
        return new B();
    }

    @Override
    public void m(Object o) {

    }

    @Override
    public X n() {
        return this;
    }
}
