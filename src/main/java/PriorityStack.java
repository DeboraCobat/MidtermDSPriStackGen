import java.util.NoSuchElementException;

public class PriorityStack<T> {

    private class Container<T> {
        T value;
        boolean hasPriority;
        Container<T> nextBelow;

        Container(T value, boolean hasPriority, Container<T> nextBelow) {
            this.value = value;
            this.hasPriority = hasPriority;
            this.nextBelow = nextBelow;
        }
    }

    private Container<T> top;
    private int size;

    public PriorityStack() {
        top = null;
        size = 0;
    }

    public void push(T value) {
        push(value, false);
    }

    public void push(T value, boolean hasPriority) {
        Container<T> newContainer = new Container<>(value, hasPriority, top);
        top = newContainer;
        size++;
    }

    public T pop() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        T value = top.value;
        top = top.nextBelow;
        size--;
        return value;
    }

    public T popPriority() {
        if (size == 0) {
            throw new NoSuchElementException();
        }

        T value;
        if (top.hasPriority) {
            value = top.value;
            top = top.nextBelow;
        } else {
            Container<T> current = top;
            while (current.nextBelow != null && !current.nextBelow.hasPriority) {
                current = current.nextBelow;
            }
            if (current.nextBelow != null) {
                value = current.nextBelow.value;
                current.nextBelow = current.nextBelow.nextBelow;
            } else {
                value = top.value;
                top = top.nextBelow;
            }
        }

        size--;
        return value;
    }

    public int hasValue(T value) {
        int index = 0;
        Container<T> current = top;
        while (current != null) {
            if (value.equals(current.value)) {
                return index;
            }
            current = current.nextBelow;
            index++;
        }
        return -1;
    }

    public T removeValue(T value) {
        if (size == 0) {
            throw new NoSuchElementException();
        }

        if (value.equals(top.value)) {
            T removedValue = top.value;
            top = top.nextBelow;
            size--;
            return removedValue;
        }

        Container<T> current = top;
        while (current.nextBelow != null) {
            if (value.equals(current.nextBelow.value)) {
                T removedValue = current.nextBelow.value;
                current.nextBelow = current.nextBelow.nextBelow;
                size--;
                return removedValue;
            }
            current = current.nextBelow;
        }

        throw new NoSuchElementException();
    }

    public int getSize() {
        return size;
    }

    public void reorderByPriority() {
        if (size <= 1) {
            return;
        }

        Container<T> priorityHead = null;
        Container<T> priorityTail = null;
        Container<T> nonPriorityHead = null;
        Container<T> nonPriorityTail = null;

        Container<T> current = top;
        while (current != null) {
            Container<T> next = current.nextBelow;
            if (current.hasPriority) {
                if (priorityHead == null) {
                    priorityHead = current;
                    priorityTail = current;
                    current.nextBelow = null;
                } else {
                    priorityTail.nextBelow = current;
                    current.nextBelow = null;
                    priorityTail = current;
                }
            } else {
                if (nonPriorityHead == null) {
                    nonPriorityHead = current;
                    nonPriorityTail = current;
                    current.nextBelow = null;
                } else {
                    nonPriorityTail.nextBelow = current;
                    current.nextBelow = null;
                    nonPriorityTail = current;
                }
            }
            current = next;
        }

        if (priorityTail != null) {
            priorityTail.nextBelow = nonPriorityHead;
            top = priorityHead;
        }
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Container<T> current = top;
        while (current != null) {
            sb.append(current.value.toString());
            sb.append(current.hasPriority ? ":P" : ":N");
            if (current.nextBelow != null) {
                sb.append(",");
            }
            current = current.nextBelow;
        }
        sb.append("]");
        return sb.toString();
    }

    private void toArrayReversedRecursive(Container<T> current, T[] array, int index) {
        if (current == null) {
            return;
        }
        toArrayReversedRecursive(current.nextBelow, array, index + 1);
        array[index] = current.value;
    }

    public T[] toArrayReversed() {
        if (size == 0) {
            return (T[]) new Object[0];
        }

        T[] array = (T[]) new Object[size];
        toArrayReversedRecursive(top, array, 0);
        return array;
    }

    public T[] toArray() {
        if (size == 0) {
            return (T[]) new Object[0];
        }

        T[] array = (T[]) new Object[size];
        Container<T> current = top;
        int index = 0;
        while (current != null) {
            array[index++] = current.value;
            current = current.nextBelow;
        }
        return array;
    }
}
