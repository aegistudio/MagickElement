package net.aegistudio.magick;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

import org.bukkit.configuration.ConfigurationSection;

/**
 * Intended for configuration serialization.
 * 
 * @author aegistudio
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)

public @interface Configurable {
	public Type value();
	public String name() default "";
	
	public enum Type {
		STRING {
			@Override
			public void load(String fieldName, Field field, Object instance, MagickElement element,
					ConfigurationSection section) throws Exception {
				if(section.contains(fieldName)) {
					String locale = section.getString(fieldName);
					field.set(instance, locale);
				}
			}

			@Override
			public void save(String fieldName, Field field, Object instance, MagickElement element,
					ConfigurationSection section) throws Exception{
				String locale = (String) field.get(instance);
				section.set(fieldName, locale);
			}
		},
		LOCALE {
			@Override
			public void load(String fieldName, Field field, Object instance, MagickElement element,
					ConfigurationSection section) throws Exception {
				String locale = section.contains(fieldName)? section.getString(fieldName) : null;
				field.set(instance, locale);
			}

			@Override
			public void save(String fieldName, Field field, Object instance, MagickElement element,
					ConfigurationSection section) throws Exception{
				STRING.save(fieldName, field, instance, element, section);
			}
		}, /** must be string type **/
		ALGEBRA {
			
			@Override
			public void load(String fieldName, Field field, Object instance, MagickElement element,
					ConfigurationSection section) throws Exception{
				
			}

			@Override
			public void save(String fieldName, Field field, Object instance, MagickElement element,
					ConfigurationSection section) throws Exception{
				
			}
			
		},	/** must be expressoin type **/
		CONSTANT {

			@Override
			public void load(String fieldName, Field field, Object instance, MagickElement element,
					ConfigurationSection section) throws Exception {
				if(!section.contains(fieldName)) return;
				
				Class<?> clazz = field.getType(); Object value = null;
				
				if(clazz == boolean.class) value = section.getBoolean(fieldName);
				if(clazz == short.class) value = (short)section.getInt(fieldName);
				if(clazz == int.class) value = section.getInt(fieldName);
				if(clazz == long.class) value = section.getLong(fieldName);
				if(clazz == float.class) value = (float)section.getDouble(fieldName);
				if(clazz == double.class) value = section.getDouble(fieldName);
				if(value == null) value = section.get(fieldName);
				
				field.set(instance, value);
			}

			@Override
			public void save(String fieldName, Field field, Object instance, MagickElement element,
					ConfigurationSection section) throws Exception{
				Class<?> clazz = field.getType(); Object value = field.get(instance);
				if(clazz == short.class) value = (int)(short) value;
				if(clazz == float.class) value = (double)(float) value;
				section.set(fieldName, value);
			}
		}	/** must be constant (int, float, etc) type **/;
		
		public abstract void load(String fieldName, Field field, Object instance, MagickElement element, 
				ConfigurationSection section) throws Exception;
		
		public abstract void save(String fieldName, Field field, Object instance, MagickElement element, 
				ConfigurationSection section) throws Exception;
	}
}
