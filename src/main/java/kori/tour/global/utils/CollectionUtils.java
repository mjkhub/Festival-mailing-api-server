package kori.tour.global.utils;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class CollectionUtils {

	private CollectionUtils() {
	}

	public static <T> Map<Boolean, List<T>> separateList(List<T> list, Predicate<T> predicate) {
		return list.stream().collect(Collectors.partitioningBy(predicate));
	}

	public static <T> List<T> getFromMap(Map<Boolean, List<T>> partitionedList, Boolean flag) {
		return partitionedList.get(flag);
	}

	public static <T> List<T> filterList(List<T> list, Predicate<T> predicate) {
		return list.stream().filter(predicate).toList();
	}

	public static <T, R> List<R> mapList(List<T> list, Function<T, R> mapper) {
		return list.stream().map(mapper).toList();
	}

}
