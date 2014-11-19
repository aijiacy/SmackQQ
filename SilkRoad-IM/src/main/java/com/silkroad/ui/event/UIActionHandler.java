package com.silkroad.ui.event;

import java.lang.annotation.*;

/**
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface UIActionHandler {
	String name() default "";

	String enabledProperty() default "";

	String selectedProperty() default "";

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.PARAMETER)
	@interface Parameter {
		String value() default "";
	}
}
