package kori.tour.tour.domain;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Keywords {

    @ElementCollection
    @CollectionTable(
            name = "tour_keywords",
            joinColumns = @JoinColumn(name = "tour_id")
    )
    @Column(name = "keyword")
    private Set<String> keywordSet = new HashSet<>();

    public void addKeyword(String keyword) {
        this.keywordSet.add(keyword);
    }

    public Set<String> getKeywordSet() {
        return Collections.unmodifiableSet(keywordSet);
    }

}
