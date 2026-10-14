package com.mycompany.Algorithms;

import java.util.ArrayList;

public class BinarySearch {
    public static int binarySearchInteger(int[] arr, int min, int max, int key) {
        if (min > max) {
            return -1; 
        }

        // Find midpoint
        int mid = (min + max) / 2;

        // Return the index, if equal
        if (arr[mid] == key) {
            return mid; 
        } 

        // If the middle element is less than the key, search in the right half
        else if (arr[mid] < key) {
            return binarySearchInteger(arr, mid + 1, max, key);
        } 

        // Else, search in the left half
        else {
            return binarySearchInteger(arr, min, mid - 1, key); 
        }
    }

    public static int binarySearchObject(ArrayList<?> arr, int min, int max, String key) {
        if (min > max) {
            return -1;
        }

        // Find midpoint
        int mid = (min + max) / 2;

        // Compare the key with the middle element (converted to String)
        int comparison = key.compareTo(arr.get(mid).toString());

        // Return the index, if equal
        if (comparison == 0) {
            return mid;
        }

        // If the middle element is less than the key, search the right half
        else if (comparison > 0) {
            return binarySearchObject(arr, mid + 1, max, key); 
        }

        // Else, search the left half
        else {
            return binarySearchObject(arr, min, mid - 1, key); 
        }
    }
}