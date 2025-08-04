package kori.tour.common.utils;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class CollectionUtils {

	/**
	 * Prevents instantiation of the {@code CollectionUtils} utility class.
	 */
	private CollectionUtils() {
	}

	/**
	 * Partitions a list into two groups based on a predicate, returning a map with Boolean keys.
	 *
	 * @param tours the list to partition
	 * @param predicate the condition used to separate elements
	 * @return a map where {@code true} maps to elements matching the predicate and {@code false} to those that do not
	 */
	public static <T> Map<Boolean, List<T>> separateList(List<T> tours, Predicate<T> predicate) {
		return tours.stream().collect(Collectors.partitioningBy(predicate));
	}

	/**
	 * Retrieves the list of elements corresponding to the specified Boolean key from a partitioned map.
	 *
	 * @param partitionedTours a map partitioning elements into lists keyed by Boolean values
	 * @param flag the Boolean key indicating which partition to retrieve
	 * @return the list of elements associated with the given key, or null if the key is not present
	 */
	public static <T> List<T> getFromMap(Map<Boolean, List<T>> partitionedTours, Boolean flag) {
		return partitionedTours.get(flag);
	}

	/**
	 * Returns a new list containing elements from the input list that satisfy the given predicate.
	 *
	 * @param tours the list to filter
	 * @param predicate the condition to apply to each element
	 * @return a list of elements that match the predicate
	 */
	public static <T> List<T> filterList(List<T> tours, Predicate<T> predicate) {
		return tours.stream().filter(predicate).toList();
	}

	/**
	 * Returns a new list consisting of the results of applying the given mapping function to each element of the input list.
	 *
	 * @param list the list whose elements are to be mapped
	 * @param mapper the function to apply to each element
	 * @return a list containing the mapped elements
	 */
	public static <T, R> List<R> mapList(List<T> list, Function<T, R> mapper) {
		return list.stream().map(mapper).toList();
	}

}
