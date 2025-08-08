package kori.tour.global.annotation;

import java.lang.annotation.*;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Repository;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repository
public @interface PersistenceAdapter {

	@AliasFor(annotation = Repository.class)
	String value() default "";

}
