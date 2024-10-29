package sem1_2.factory;

import sem1_2.model.Container;
import sem1_2.model.QueueContainer;
import sem1_2.model.StackContainer;

public class TaskContainerFactory implements Factory {

    private static TaskContainerFactory instanta;

    private TaskContainerFactory() {
    }

    public static TaskContainerFactory getInstance() {
        if (instanta == null)
            instanta = new TaskContainerFactory();
        return instanta;
    }

    @Override
    public Container createContainer(Strategy strategy) {
        if (strategy == Strategy.FIFO) {
            return new QueueContainer();
            //return null; //erainvers
        } else {
            return new StackContainer();
        }
    }
}
