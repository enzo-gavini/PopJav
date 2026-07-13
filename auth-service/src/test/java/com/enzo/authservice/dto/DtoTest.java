package com.enzo.authservice.dto;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Exercises the Lombok-generated code of every DTO (getters, setters,
 * equals/hashCode/toString) so regressions in their contracts are caught.
 */
class DtoTest {

    private static final List<Class<?>> DTOS = List.of(
            AuthResponse.class, LoginRequest.class, RegisterRequest.class,
            UserCreateRequest.class, UserDTO.class, UserFullDTO.class);

    @Test
    void allDtos_respectEqualsHashCodeAndAccessorContracts() throws Exception {
        for (Class<?> type : DTOS) {
            exercise(type);
        }
    }

    private static void exercise(Class<?> type) throws Exception {
        Object a = type.getDeclaredConstructor().newInstance();
        Object b = type.getDeclaredConstructor().newInstance();

        for (Method m : type.getMethods()) {
            if (m.getName().startsWith("set") && m.getParameterCount() == 1) {
                Object value = sample(m.getParameterTypes()[0]);
                m.invoke(a, value);
                m.invoke(b, value);
            }
        }
        for (Method m : type.getMethods()) {
            if (m.getParameterCount() == 0 && m.getDeclaringClass() != Object.class
                    && (m.getName().startsWith("get") || m.getName().startsWith("is"))) {
                m.invoke(a);
            }
        }

        assertThat(a).as(type.getSimpleName()).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
        assertThat(a.toString()).contains(type.getSimpleName());
        assertThat(a).isEqualTo(a);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo(new Object());
    }

    private static Object sample(Class<?> t) {
        if (t == String.class) return "value";
        if (t == Long.class || t == long.class) return 7L;
        if (t == Integer.class || t == int.class) return 7;
        if (t == Boolean.class || t == boolean.class) return true;
        if (t == List.class) return new ArrayList<>();
        if (t == Map.class) return new HashMap<>();
        if (t.isEnum()) return t.getEnumConstants()[0];
        return null;
    }
}
