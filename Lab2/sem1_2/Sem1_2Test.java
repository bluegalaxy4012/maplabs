package sem1_2;

import sem1_2.decorator.DelayTaskRunner;
import sem1_2.decorator.PrinterTaskRunner;
import sem1_2.decorator.StrategyTaskRunner;
import sem1_2.decorator.TaskRunner;
import sem1_2.factory.Strategy;
import sem1_2.model.*;

import java.time.LocalDateTime;

public class Sem1_2Test {
    public static MessageTask[] createMessages() {
        MessageTask msg1 = new MessageTask("1", "feedback lab 2", "Te-ai descurcat bine1", "teacher", "student", LocalDateTime.now());
        MessageTask msg2 = new MessageTask("2", "feedback lab 3", "Te-ai descurcat bine2", "teacher", "student", LocalDateTime.now());
        MessageTask msg3 = new MessageTask("3", "feedback lab 4", "Te-ai descurcat bine3", "teacher", "student", LocalDateTime.now());
        MessageTask msg4 = new MessageTask("4", "feedback lab 5", "Te-ai descurcat bine4", "teacher", "student", LocalDateTime.now());
        MessageTask msg5 = new MessageTask("5", "feedback lab 6", "Te-ai descurcat bine5", "teacher", "student", LocalDateTime.now());

        return new MessageTask[]{msg1, msg2, msg3, msg4, msg5};
    }


    public static void main(String[] args) {
        MessageTask[] messageTasks = createMessages();
        for (MessageTask messageTask: messageTasks) {
            System.out.println(messageTask);
        }

        TaskRunner strategyTaskRunner = new StrategyTaskRunner(Strategy.valueOf(args[0]));
        for (MessageTask m : messageTasks) {
            strategyTaskRunner.addTask(m);
        }
        strategyTaskRunner.executeAll();

        //testare sortingtask si delaytaskrunner
//        Integer[] v = new Integer[]{1,3,2,10,7,6,10,3};
//        AbstractSorter mergeSort = new MergeSort();
//        AbstractSorter bubbleSort = new BubbleSort();
//        Task t1 = new SortingTask("10", "nimic", v, mergeSort);
//        Task t2 = new SortingTask("11", "nimic", v, bubbleSort);
//        strategyTaskRunner.addTask(t1);
//        strategyTaskRunner.addTask(t2);
//        TaskRunner delayTaskRunner = new DelayTaskRunner(strategyTaskRunner);
//        delayTaskRunner.executeAll();

        TaskRunner delayTaskRunner = new DelayTaskRunner(strategyTaskRunner);
        for (MessageTask m : messageTasks) {
            strategyTaskRunner.addTask(m);
        }
        delayTaskRunner.executeAll();



        TaskRunner printerTaskRunner = new PrinterTaskRunner(strategyTaskRunner);
        for (MessageTask m : messageTasks) {
            strategyTaskRunner.addTask(m);
        }
        printerTaskRunner.executeAll();

    }
}
