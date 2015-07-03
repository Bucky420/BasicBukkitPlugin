package ntg.codydbgt.basic.CMD;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ROOTCMD {
	int Args() default 0;
	boolean Player() default true;
	boolean Error() default false;
	String decription() default "";
	String perms() default "null";
	String Use() default "null";
}
