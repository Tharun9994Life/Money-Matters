package com.mycompany.Algorithms;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;

import com.mycompany.Records.EconomicNewsRecord;
import com.mycompany.Records.Purchase;

public class MergeSort {

    public static <T> void mergeSort(ArrayList<T> arr, Comparator<T> comparator) {
        if (arr.size() < 2) {
            return;
        }

        int mid = arr.size() / 2;

        // Split array into left and right halves
        ArrayList<T> left = new ArrayList<>(arr.subList(0, mid));
        ArrayList<T> right = new ArrayList<>(arr.subList(mid, arr.size()));

        // Recursively sort the halves
        mergeSort(left, comparator);
        mergeSort(right, comparator);

        // Merge the sorted halves
        merge(arr, left, right, comparator);
    }

    private static <T> void merge(ArrayList<T> result, ArrayList<T> left, ArrayList<T> right, Comparator<T> comparator) {
        int i = 0, j = 0;
        result.clear(); // Clear the result list before merging

        // Merge elements in order based on the comparator
        while (i < left.size() && j < right.size()) {
            if (comparator.compare(left.get(i), right.get(j)) <= 0) {
                result.add(left.get(i++));
            } 
            
            else {
                result.add(right.get(j++));
            }
        }

        // Add any remaining elements from left
        while (i < left.size()) {
            result.add(left.get(i++));
        } 
        
        // Add any remaining elements from right
        while (j < right.size()) {
            result.add(right.get(j++));
        }
    }

    // Sorts EconomicNewsRecord objects alphabetically by their title.
    public static void mergeSortEconomicNewsByTitle(ArrayList<EconomicNewsRecord> arr) {
        mergeSort(arr, Comparator.comparing(EconomicNewsRecord::title));
    }

    // Sorts EconomicNewsRecord by their publication date (oldest to newest)
    public static void mergeSortEconomicNewsByDate(ArrayList<EconomicNewsRecord> arr) {
        mergeSort(arr, Comparator.comparing(MergeSort::parseNewsDate));
    }

    private static LocalDate parseNewsDate(EconomicNewsRecord record) {
        return LocalDate.parse(record.pubDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    // Sorts Purchase by purchase date (oldest to newest)
    public static void mergeSortPurchasesByDate(ArrayList<Purchase> arr) {
        mergeSort(arr, Comparator.comparing(MergeSort::parsePurchaseDate));
    }

    private static LocalDate parsePurchaseDate(Purchase purchase) {
        return LocalDate.parse(purchase.getPurchaseDate());
    }

    // Sorts Purchase by price (lowest to highest).
    public static void mergeSortPurchasesByPrice(ArrayList<Purchase> arr) {
        mergeSort(arr, Comparator.comparingDouble(Purchase::getGoodPrice));
    }

    // Sorts Purchase by quantity purchased (lowest to highest).
    public static void mergeSortPurchasesByQuantity(ArrayList<Purchase> arr) {
        mergeSort(arr, Comparator.comparingDouble(Purchase::getGoodQuantity));
    }
}