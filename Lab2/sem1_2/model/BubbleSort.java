package sem1_2.model;

public class BubbleSort implements AbstractSorter{

    @Override
    public void sort(Integer[] v) {
        for(int i=0; i<v.length; i++)
            for(int j=1; j<v.length-i; j++)
                if(v[j-1]>v[j])
                {
                    int temp = v[j-1];
                    v[j-1] = v[j];
                    v[j] = temp;
                }
    }
}
