package pl.kj.bachelors.teams.domain.constraint;

import pl.kj.bachelors.teams.domain.validator.FieldValueMatchValidator;
import pl.kj.bachelors.teams.domain.validator.FromEnumValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FromEnumValidator.class)
@Target(value = ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FromEnum {
    Class<? extends Enum<?>> enumClass();
    String message() default "Invalid value!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
