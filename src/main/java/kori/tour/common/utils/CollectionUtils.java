package kori.tour.common.utils;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class CollectionUtils {

	private CollectionUtils() {
	}

	public static <T> Map<Boolean, List<T>> separateList(List<T> tours, Predicate<T> predicate) {
		return tours.stream().collect(Collectors.partitioningBy(predicate));
	}

	public static <T> List<T> getFromMap(Map<Boolean, List<T>> partitionedTours, Boolean flag) {
		return partitionedTours.get(flag);
	}

	public static <T> List<T> filterList(List<T> tours, Predicate<T> predicate) {
		return tours.stream().filter(predicate).toList();
	}

	public static <T, R> List<R> mapList(List<T> list, Function<T, R> mapper) {
		return list.stream().map(mapper).toList();
	}

}
