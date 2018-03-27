package com.github.amatanhead.pcl.utils;

import java.util.Objects;

/**
 * Some generic tuple implementations.
 */
public final class GenericTuple {
    /**
     * Protect constructor since this is a static-only class.
     */
    protected GenericTuple() {
    }

    /**
     * A common base for all tuples. Just for the sake of `x instanceof Tuple`.
     */
    public static abstract class Tuple {
    }

    /**
     * A tuple which can hold a single element.
     */
    public static class Tuple1<T1> extends Tuple {
        public T1 t1;

        public Tuple1(T1 t1) {
            this.t1 = t1;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Tuple1<?> tuple1 = (Tuple1<?>) o;
            return Objects.equals(t1, tuple1.t1);
        }

        @Override
        public int hashCode() {
            return Objects.hash(t1);
        }
    }

    /**
     * A tuple which can hold two elements.
     */
    public static class Tuple2<T1, T2> extends Tuple {
        public T1 t1;
        public T2 t2;

        public Tuple2(T1 t1, T2 t2) {
            this.t1 = t1;
            this.t2 = t2;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Tuple2<?, ?> tuple2 = (Tuple2<?, ?>) o;
            return Objects.equals(t1, tuple2.t1) &&
                    Objects.equals(t2, tuple2.t2);
        }

        @Override
        public int hashCode() {
            return Objects.hash(t1, t2);
        }
    }

    /**
     * A tuple which can hold three elements.
     */
    public static class Tuple3<T1, T2, T3> extends Tuple {
        public T1 t1;
        public T2 t2;
        public T3 t3;

        public Tuple3(T1 t1, T2 t2, T3 t3) {
            this.t1 = t1;
            this.t2 = t2;
            this.t3 = t3;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Tuple3<?, ?, ?> tuple3 = (Tuple3<?, ?, ?>) o;
            return Objects.equals(t1, tuple3.t1) &&
                    Objects.equals(t2, tuple3.t2) &&
                    Objects.equals(t3, tuple3.t3);
        }

        @Override
        public int hashCode() {
            return Objects.hash(t1, t2, t3);
        }
    }

    /**
     * A tuple which can hold four elements.
     */
    public static class Tuple4<T1, T2, T3, T4> extends Tuple {
        public T1 t1;
        public T2 t2;
        public T3 t3;
        public T4 t4;

        public Tuple4(T1 t1, T2 t2, T3 t3, T4 t4) {
            this.t1 = t1;
            this.t2 = t2;
            this.t3 = t3;
            this.t4 = t4;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Tuple4<?, ?, ?, ?> tuple4 = (Tuple4<?, ?, ?, ?>) o;
            return Objects.equals(t1, tuple4.t1) &&
                    Objects.equals(t2, tuple4.t2) &&
                    Objects.equals(t3, tuple4.t3) &&
                    Objects.equals(t4, tuple4.t4);
        }

        @Override
        public int hashCode() {
            return Objects.hash(t1, t2, t3, t4);
        }
    }

    /**
     * A tuple which can hold five elements.
     */
    public static class Tuple5<T1, T2, T3, T4, T5> extends Tuple {
        public T1 t1;
        public T2 t2;
        public T3 t3;
        public T4 t4;
        public T5 t5;

        public Tuple5(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5) {
            this.t1 = t1;
            this.t2 = t2;
            this.t3 = t3;
            this.t4 = t4;
            this.t5 = t5;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Tuple5<?, ?, ?, ?, ?> tuple5 = (Tuple5<?, ?, ?, ?, ?>) o;
            return Objects.equals(t1, tuple5.t1) &&
                    Objects.equals(t2, tuple5.t2) &&
                    Objects.equals(t3, tuple5.t3) &&
                    Objects.equals(t4, tuple5.t4) &&
                    Objects.equals(t5, tuple5.t5);
        }

        @Override
        public int hashCode() {
            return Objects.hash(t1, t2, t3, t4, t5);
        }
    }

    // Some more tuples?
}
