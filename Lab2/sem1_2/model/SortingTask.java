package sem1_2.model;

public class SortingTask extends Task {
    private Integer[] v;
    private AbstractSorter sorter;

    public SortingTask(String id, String description, Integer[] v, AbstractSorter sorter) {
        super(id, description);
        this.v = v;
        this.sorter = sorter;
    }

    @Override
    public void execute() {
        sorter.sort(v);
        for(var x:v)
            System.out.println(x);
    }
}
