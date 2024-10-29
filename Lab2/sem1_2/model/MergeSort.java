package sem1_2.model;

public class MergeSort implements AbstractSorter{

    void merge(Integer[] v, Integer beg, Integer mid, Integer end)
    {
        Integer i, j, k;
        Integer n1 = mid - beg + 1;
        Integer n2 = end - mid;

        Integer[] l = new Integer[n1];
        Integer[] r = new Integer[n2]; //temporary arrays

        /* copy data to temp arrays */
        for (i = 0; i < n1; i++)
            l[i] = v[beg + i];
        for (j = 0; j < n2; j++)
            r[j] = v[mid + 1 + j];

        i = 0; /* initial index of first sub-array */
        j = 0; /* initial index of second sub-array */
        k = beg;  /* initial index of merged sub-array */

        while (i < n1 && j < n2)
        {
            if(l[i] <= r[j])
            {
                v[k] = l[i];
                i++;
            }
            else
            {
                v[k] = r[j];
                j++;
            }
            k++;
        }
        while (i<n1)
        {
            v[k] = l[i];
            i++;
            k++;
        }

        while (j<n2)
        {
            v[k] = r[j];
            j++;
            k++;
        }
    }

    void mergeSort(Integer[] v, Integer beg, Integer end)
    {
        if (beg < end)
        {
            Integer mid = (beg + end) / 2;
            mergeSort(v, beg, mid);
            mergeSort(v, mid + 1, end);
            merge(v, beg, mid, end);
        }
    }

    @Override
    public void sort(Integer[] v) {
        int dim = v.length;
        mergeSort(v, 0, dim-1);


    }
}
