package com.its.module.utils;

import com.its.module.model.annotation.*;
import com.its.module.model.entity.Editable;
import com.its.module.model.entity.Modifiable;
import com.its.module.model.entity.Removable;
import com.its.module.model.exception.*;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

public class ObjectUtils {
	private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	public static <T> void validate(T obj) {
		Set<ConstraintViolation<T>> set = validator.validate(obj);
		if (set.size() > 0) {
			ConstraintViolation<T> item = set.iterator().next();
			throw new BadRequestException("Giá trị đầu vào không hợp lệ", 1090,
					item.getPropertyPath().toString() + " " + item.getMessage());
		}
	}

	public static void validateObject(final Object object) {
		try {
			for (Field field : object.getClass().getDeclaredFields()) {
				field.setAccessible(true);
				String name = field.getName();
				if (field.isAnnotationPresent(NotNull.class)) {
					if (field.get(object) == null) {
						throw new NullFieldException(name);
					}
				}

				/**
				 * call recursive
				 */
				if (field.isAnnotationPresent(Nested.class) && field.get(object) != null) {
					validateObject(field.get(object));
				}

				if (field.isAnnotationPresent(Trim.class)) {
					Object value = field.get(object);
					if (value != null) {
						String newValue = ((String) value).trim();
						field.set(object, newValue);
					}
				}

				if (field.isAnnotationPresent(LowerCase.class)) {
					Object value = field.get(object);
					if (value != null) {
						String newValue = ((String) value).toLowerCase();
						field.set(object, newValue);
					}
				}

				if (field.isAnnotationPresent(MaxLength.class)) {
					Object value = field.get(object);
					int length = field.getAnnotation(MaxLength.class).value();
					if (value != null)
						if (((String) value).length() > length)
							throw new MaxLengthException(name);
				}

				if (field.isAnnotationPresent(RangedValue.class)) {
					Object value = field.get(object);
					if (value != null) {
						int min = field.getAnnotation(RangedValue.class).min();
						int max = field.getAnnotation(RangedValue.class).max();
						int intValue = (Integer) value;
						if (intValue < min || max < intValue)
							throw new RangeExceededFieldException(name);
					}
				}

				if (field.isAnnotationPresent(Phone.class)) {
					Object value = field.get(object);
					if (value != null) {
						String stringValue = (String) value;
						if (!StringUtils.isValidPhoneNumber(stringValue))
							throw new NotvalidFieldException(name);
					}
				}

				if (field.isAnnotationPresent(OrdinalEnum.class)) {
					Object value = field.get(object);
					if (value != null) {
						Class<? extends Enum> enumClass = field.getAnnotation(OrdinalEnum.class).value();
						Integer intValue = (Integer) value;
						if (intValue < 0 || enumClass.getFields().length <= intValue)
							throw new RangeExceededFieldException(name);
					}
				}

				if (field.isAnnotationPresent(ListPhone.class)) {
					Object value = field.get(object);
					if (value != null) {
						String stringValue = StringUtils.strictlyTrim(((String) value).replace(";", " "));
						stringValue = stringValue.replace(" ", ";");
						if (!"".equals(stringValue))
							for (String number : stringValue.split(";")) {
								if (!StringUtils.isValidPhoneNumber(number))
									throw new NotvalidFieldException(name);
							}
						field.set(object, stringValue);
					}
				}

				if (field.isAnnotationPresent(ListEmail.class)) {
					Object value = field.get(object);
					if (value != null) {
						String stringValue = StringUtils.strictlyTrim(((String) value).replace(";", " "));
						stringValue = stringValue.replace(" ", ";");
						if (!"".equals(stringValue))
							for (String email : stringValue.split(";")) {
								if (!StringUtils.isValidEmail(email))
									throw new NotvalidFieldException(name);
							}
						field.set(object, stringValue);
					}
				}

				if (field.isAnnotationPresent(Password.class)) {
					Object value = field.get(object);
					if (value != null) {
						String stringValue = (String) value;
						if (!StringUtils.isStrongPass(stringValue))
							throw new NotvalidFieldException(name);
						field.set(object, stringValue);
					}
				}

				if (field.isAnnotationPresent(Email.class)) {
					Object value = field.get(object);
					if (value != null) {
						String stringValue = (String) value;
						if (!StringUtils.isValidEmail(stringValue))
							throw new NotvalidFieldException(name);
						field.set(object, stringValue);
					}
				}
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public static void setCreateHistory(Editable editable, Integer authId) {
		editable.setModifiedDate(LocalDateTime.now());
		editable.setModifiedBy(authId);
		editable.setCreatedDate(LocalDateTime.now());
		editable.setCreatedBy(authId);
		editable.setIsActive(true);
	}

	public static void setModifyHistory(Modifiable modifiable, Integer authId) {
		modifiable.setModifiedBy(authId);
		modifiable.setModifiedDate(LocalDateTime.now());
	}

	public static void setCreateHistory(Removable removable, Integer authId) {
		removable.setModifiedDate(LocalDateTime.now());
		removable.setModifiedBy(authId);
		removable.setCreatedDate(LocalDateTime.now());
		removable.setCreatedBy(authId);
	}

	private static Map<String, Field> getFields(Object object) {
		Map<String, Field> map = new HashMap<>();
		for (Field field : object.getClass().getDeclaredFields()) {
			map.put(field.getName(), field);
		}
		return map;
	}

	public static void fillData(Object target, Object source) {
		try {
			Map<String, Field> targetMap = getFields(target);
			for (Field field : source.getClass().getDeclaredFields()) {
				field.setAccessible(true);
				Object value = field.get(source);
				if (value != null && targetMap.containsKey(field.getName())) {
					Field targetField = targetMap.get(field.getName());
					targetField.setAccessible(true);
					targetField.set(target, value);
				}
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
