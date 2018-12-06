package com.example.user.summerproject.myTools;



public class Quicksort {
    private int[] numbers;
    private long[] longNumbers;
    private int number;
    private int[] IDs;
    private String[] tags;
    private String[] tagsID;
    private String[] tagsNames;
    private int[] intArray1;
    private int[] intArray2;
    private int[] intArray3;
    private int[][] intArray11;

    public void sort(int[] values,int[] IDs) {
        // check for empty or null array
        if (values ==null || values.length==0){
            return;
        }
        this.numbers = values;
        this.IDs=IDs;
        number = values.length;
        quicksort(0, number - 1);
    }


    public void sort(int[] values,String[] tags) {
        // check for empty or null array
        if (values ==null || values.length==0){
            return;
        }
        this.numbers = values;
        this.tags=tags;
        number = values.length;
        quicksort(0, number - 1);
    }
    public void sort(long[] values,String[] tagsID,String[] tagsNames){
        // check for empty or null array
        if (values ==null || values.length==0){
            return;
        }
        this.longNumbers = values;
        this.tagsID=tagsID;
        this.tagsNames=tagsNames;
        number = values.length;
        longQuicksort(0, number - 1);
    }
    public void sort(int[] criticalArray,String[] letters,int[] intArray1,int[] intArray2,int[] intArray3){
        if (criticalArray ==null || criticalArray.length==0){
            return;
        }
        this.numbers = criticalArray;
        this.tags=letters;
        this.intArray1=intArray1;
        this.intArray2=intArray2;
        this.intArray3=intArray3;
        number = criticalArray.length;
        quicksort(0, number - 1);
    }
    public void sort(int[] criticalArray,String[] names,int[][] intArray11){
        if (criticalArray ==null || criticalArray.length==0){
            return;
        }
        this.numbers = criticalArray;
        this.tags=names;
        this.intArray11=intArray11;
        number = criticalArray.length;
        quicksort(0, number - 1);
    }


    private void quicksort(int low, int high) {
        int i = low, j = high;
        // Get the pivot element from the middle of the list
        int pivot = numbers[low + (high-low)/2];

        // Divide into two lists
        while (i <= j) {
            // If the current value from the left list is smaller than the pivot
            // element then get the next element from the left list
            while (numbers[i] < pivot) {
                i++;
            }
            // If the current value from the right list is larger than the pivot
            // element then get the next element from the right list
            while (numbers[j] > pivot) {
                j--;
            }

            // If we have found a value in the left list which is larger than
            // the pivot element and if we have found a value in the right list
            // which is smaller than the pivot element then we exchange the
            // values.
            // As we are done we can increase i and j
            if (i <= j) {
                exchange(i, j);
                i++;
                j--;
            }
        }
        // Recursion
        if (low < j)
            quicksort(low, j);
        if (i < high)
            quicksort(i, high);
    }
    private void longQuicksort(int low, int high) {
        int i = low, j = high;
        // Get the pivot element from the middle of the list
        long pivot = longNumbers[low + (high-low)/2];

        // Divide into two lists
        while (i <= j) {
            // If the current value from the left list is smaller than the pivot
            // element then get the next element from the left list
            while (longNumbers[i] < pivot) {
                i++;
            }
            // If the current value from the right list is larger than the pivot
            // element then get the next element from the right list
            while (longNumbers[j] > pivot) {
                j--;
            }

            // If we have found a value in the left list which is larger than
            // the pivot element and if we have found a value in the right list
            // which is smaller than the pivot element then we exchange the
            // values.
            // As we are done we can increase i and j
            if (i <= j) {
                exchange(i, j);
                i++;
                j--;
            }
        }
        // Recursion
        if (low < j)
            longQuicksort(low, j);
        if (i < high)
            longQuicksort(i, high);
    }

    private void exchange(int i, int j) {
        if(numbers!=null){
            int temp = numbers[i];
            numbers[i] = numbers[j];
            numbers[j] = temp;
        }else {
            long temp = longNumbers[i];
            longNumbers[i] = longNumbers[j];
            longNumbers[j] = temp;
        }

        if(IDs!=null) {
            int temp1 = IDs[i];
            IDs[i] = IDs[j];
            IDs[j] = temp1;
        }
        if (tags!=null){
            String temp2 = tags[i];
            tags[i] = tags[j];
            tags[j] = temp2;
        }
        if (intArray1!=null){
            int temp3=intArray1[i];
            intArray1[i]=intArray1[j];
            intArray1[j]=temp3;

            int temp4=intArray2[i];
            intArray2[i]=intArray2[j];
            intArray2[j]=temp4;

            int temp5=intArray3[i];
            intArray3[i]=intArray3[j];
            intArray3[j]=temp5;
        }
        if(intArray11!=null){

            int temp6=intArray11[i][0];
            intArray11[i][0]=intArray11[j][0];
            intArray11[j][0]=temp6;

            temp6=intArray11[i][1];
            intArray11[i][1]=intArray11[j][1];
            intArray11[j][1]=temp6;

            temp6=intArray11[i][2];
            intArray11[i][2]=intArray11[j][2];
            intArray11[j][2]=temp6;

            temp6=intArray11[i][3];
            intArray11[i][3]=intArray11[j][3];
            intArray11[j][3]=temp6;
        }
        if(tagsNames!=null){
            String temp2 = tagsID[i];
            tagsID[i] = tagsID[j];
            tagsID[j] = temp2;

            String temp3 = tagsNames[i];
            tagsNames[i] = tagsNames[j];
            tagsNames[j] = temp3;

        }
    }
}
