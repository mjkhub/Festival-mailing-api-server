package kori.tour.common.annotation;

import java.lang.annotation.*;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Service;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Service
public @interface UseCase {

  @AliasFor(annotation = Service.class)
  String value() default "";
}
