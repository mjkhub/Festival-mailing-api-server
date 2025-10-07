package kori.tour.config.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.lang.Nullable;

import java.util.concurrent.Callable;

@RequiredArgsConstructor
public class TwoLevelCache implements Cache {

    private final String name;
    private final Cache l1; // Caffeine (Spring Cache)
    private final Cache l2; // Redis (Spring Cache)

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getNativeCache() {
        // 참고: 필요하면 더 풍부한 정보로 바꿔도 됨
        return java.util.Map.of(
                "l1", l1.getNativeCache(),
                "l2", l2.getNativeCache()
        );
    }

    @Override
    public @Nullable ValueWrapper get(Object key) {
        // 1) L1 조회
        ValueWrapper v1 = l1.get(key);
        if (v1 != null && v1.get() != null) {
            return v1;
        }
        // 2) L2 조회
        ValueWrapper v2 = l2.get(key);
        if (v2 != null && v2.get() != null) {
            // L2 hit 시 L1 승격
            l1.put(key, v2.get());
            return new SimpleValueWrapper(v2.get());
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public @Nullable <T> T get(Object key, @Nullable Class<T> type) {
        // 1) L1 조회
        T v1 = (type == null) ? (T) getFrom(l1, key) : l1.get(key, type);
        if (v1 != null) return v1;

        // 2) L2 조회
        T v2 = (type == null) ? (T) getFrom(l2, key) : l2.get(key, type);
        if (v2 != null) {
            // L2 hit 시 L1 승격
            l1.put(key, v2);
            return v2;
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(Object key, Callable<T> valueLoader) {
        Object cached = getFrom(l1, key);
        if (cached != null) return (T) cached;

        cached = getFrom(l2, key);
        if (cached != null) {
            // L2 hit → L1 승격
            l1.put(key, cached);
            return (T) cached;
        }

        try {
            // 미스 → 로더 실행 후 write-through
            T loaded = valueLoader.call();
            if (loaded != null) {
                l1.put(key, loaded);
                l2.put(key, loaded);
            }
            return loaded;
        } catch (Exception ex) {
            throw new ValueRetrievalException(key, valueLoader, ex);
        }
    }

    @Override
    public void put(Object key, @Nullable Object value) {
        // write-through
        l1.put(key, value);
        l2.put(key, value);
    }

    @Override
    public void evict(Object key) {
        // 양쪽 동시 무효화
        l1.evict(key);
        l2.evict(key);
    }

    @Override
    public void clear() {
        // 캐시 전체 비우기
        l1.clear();
        l2.clear();
    }

    // ───────────────────────────────────────────────────────────
    private @Nullable Object getFrom(Cache cache, Object key) {
        ValueWrapper vw = cache.get(key);
        return (vw != null) ? vw.get() : null;
    }
}