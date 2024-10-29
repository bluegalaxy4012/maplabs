package sem1_2.model;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractContainer implements Container {
    protected final List<Task> tasks = new ArrayList<>();



    @Override
    public void add(Task task) {
        tasks.add(task);
    }

    @Override
    public int size() {
        return tasks.size();
    }

    @Override
    public boolean isEmpty() {
        return tasks.isEmpty();
    }
}