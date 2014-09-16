import java.awt.geom.Point2D;

public class Mergesort {
  private ClosestPairs.Node[] numbers;
  private ClosestPairs.Node[] helper;
  private String axis;

  private int number;

  public void sort(ClosestPairs.Node[] values, String axis) {
    this.numbers = values;
    number = values.length;
    this.helper = new ClosestPairs.Node[number];
    this.axis = axis;
    mergesort(0, number - 1);
  }

    // low and high are indexes
  private void mergesort(int low, int high) {
    // check if low is smaller then high, if not then the array is sorted
    if (low < high) {
      // Get the index of the element which is in the middle
      int middle = low + (high - low) / 2;
      // Sort the left side of the array
      mergesort(low, middle);
      // Sort the right side of the array
      mergesort(middle + 1, high);
      // Combine them both
      merge(low, middle, high);
    }
  }

  private boolean comparePoints(Point2D.Double pointI, Point2D.Double pointJ) {
      if (axis.equalsIgnoreCase("x")) {
          return pointI.getX() <= pointJ.getX();
      } else {
          return pointI.getY() <= pointJ.getY();
      }
  }
  private void merge(int low, int middle, int high) {
    // Copy both parts into the helper array
    for (int i = low; i <= high; i++) {
      helper[i] = numbers[i];
    }
    int i = low;
    int j = middle + 1;
    int k = low;
    // Copy the smallest values from either the left or the right side back
    // to the original array
    while (i <= middle && j <= high) {
      if (comparePoints(helper[i].point, helper[j].point)) {
        numbers[k] = helper[i];
        i++;
      } else {
        numbers[k] = helper[j];
        j++;
      }
      k++;
    }
    // Copy the rest of the left side of the array into the target array
    while (i <= middle) {
      numbers[k] = helper[i];
      k++;
      i++;
    }
  }
} 