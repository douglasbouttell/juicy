/*
 * Copyright 2015 Douglas Bouttell
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.co.douglasbouttell.juicy.util;

import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;

/**
 * @author Douglas
 * @since 04/07/2015
 */
public abstract class SimpleTuple implements Iterable, Serializable {
    private static final long serialVersionUID = -2943812310474656702L;

    public abstract Object get(int i);
    public abstract int size();
    public abstract boolean equals(Object o);
    public abstract int hashCode();

    public static class Unit<A> extends SimpleTuple {
        private static final long serialVersionUID = -4493889904117430181L;
        private final A a;

        public Unit(A a) {
            this.a = a;
        }

        public A get1() {
            return a;
        }

        public Object get(int i) {
            switch(i) {
                case 0: return a;
                default: throw new IndexOutOfBoundsException();
            }
        }

        @Override
        public int size() {
            return 1;
        }

        public Iterator iterator() {
            return Collections.singletonList(a).iterator();
        }

        public void forEach(Consumer action) {
            Collections.singletonList(a).forEach(action);
        }

        public Spliterator spliterator() {
            return Collections.singletonList(a).spliterator();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Unit<?> unit = (Unit<?>) o;
            return Objects.equals(a, unit.a);
        }

        @Override
        public int hashCode() {
            return Objects.hash(a);
        }
    }

    public static class Pair<A,B> extends SimpleTuple {
        private static final long serialVersionUID = -233496226568920881L;
        private final A a;
        private final B b;

        public Pair(A a, B b) {
            this.a = a;
            this.b = b;
        }

        public A get1() {
            return a;
        }

        public B get2() {
            return b;
        }

        public Object get(int i) {
            switch(i) {
                case 0: return a;
                case 1: return b;
                default: throw new IndexOutOfBoundsException();
            }
        }

        @Override
        public int size() {
            return 2;
        }

        public Iterator iterator() {
            return Arrays.asList(a, b).iterator();
        }

        public void forEach(Consumer action) {
            Arrays.asList(a, b).forEach(action);
        }

        public Spliterator spliterator() {
            return Arrays.asList(a,b).spliterator();
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pair<?, ?> pair = (Pair<?, ?>) o;
            return Objects.equals(a, pair.a) &&
                    Objects.equals(b, pair.b);
        }

        @Override
        public int hashCode() {
            return Objects.hash(a, b);
        }
    }

    public static class Triplet<A,B,C> extends SimpleTuple {
        private static final long serialVersionUID = 4297338159073624308L;
        private final A a;
        private final B b;
        private final C c;

        public Triplet(A a, B b, C c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }

        public A get1() {
            return a;
        }

        public B get2() {
            return b;
        }

        public C get3() {
            return c;
        }

        public Object get(int i) {
            switch(i) {
                case 0: return a;
                case 1: return b;
                case 2: return c;
                default: throw new IndexOutOfBoundsException();
            }
        }

        @Override
        public int size() {
            return 3;
        }

        public Iterator iterator() {
            return Arrays.asList(a, b, c).iterator();
        }

        public void forEach(Consumer action) {
            Arrays.asList(a, b, c).forEach(action);
        }

        public Spliterator spliterator() {
            return Arrays.asList(a, b, c).spliterator();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Triplet<?, ?, ?> triplet = (Triplet<?, ?, ?>) o;
            return Objects.equals(a, triplet.a) &&
                    Objects.equals(b, triplet.b) &&
                    Objects.equals(c, triplet.c);
        }

        @Override
        public int hashCode() {
            return Objects.hash(a, b, c);
        }
    }

    public static class Quad<A,B,C,D> extends SimpleTuple {
        private static final long serialVersionUID = -3435185193920683148L;
        private final A a;
        private final B b;
        private final C c;
        private final D d;

        public Quad(A a, B b, C c, D d) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
        }

        public A get1() {
            return a;
        }

        public B get2() {
            return b;
        }

        public C get3() {
            return c;
        }

        public D get4() {
            return d;
        }

        public Object get(int i) {
            switch(i) {
                case 0: return a;
                case 1: return b;
                case 2: return c;
                case 3: return d;
                default: throw new IndexOutOfBoundsException();
            }
        }

        @Override
        public int size() {
            return 4;
        }

        public Iterator iterator() {
            return Arrays.asList(a, b, c, d).iterator();
        }

        public void forEach(Consumer action) {
            Arrays.asList(a, b, c, d).forEach(action);
        }

        public Spliterator spliterator() {
            return Arrays.asList(a, b, c, d).spliterator();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Quad<?, ?, ?, ?> quad = (Quad<?, ?, ?, ?>) o;
            return Objects.equals(a, quad.a) &&
                    Objects.equals(b, quad.b) &&
                    Objects.equals(c, quad.c) &&
                    Objects.equals(d, quad.d);
        }

        @Override
        public int hashCode() {
            return Objects.hash(a, b, c, d);
        }
    }
}
