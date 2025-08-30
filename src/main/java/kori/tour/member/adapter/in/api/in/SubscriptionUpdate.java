package kori.tour.member.adapter.in.api.in;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import jakarta.validation.constraints.NotBlank;

public record SubscriptionUpdate(
        @NotBlank String areaCode,
        @NotBlank String sigunGuCode,
        @NotNull Boolean subscribe
) {}
