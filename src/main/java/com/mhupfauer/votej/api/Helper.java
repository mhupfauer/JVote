package com.mhupfauer.votej.api;

import com.mhupfauer.votej.persistence.Repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class Helper {

    @Autowired
    private UserEntRepository userEntRepository;
    @Autowired
    private AnswerEntRepository answerEntRepository;
    @Autowired
    private BallotEntRepository ballotEntRepository;
    @Autowired
    private EventEntRepository eventEntRepository;
    @Autowired
    private QuestionEntRepository questionEntRepository;
    @Autowired
    private VoterTokenRecordEntRepository voterTokenRecordEntRepository;
    @Autowired
    private CastTokenRepository castTokenRepository;
    @Autowired
    private VoterRegEntRepository voterRegEntRepository;

    public Map<Class, Field> getHandleableCustomTypes() {
        Map<Class, Field> map = new HashMap<>();
        Arrays.asList(this.getClass().getDeclaredFields()).forEach((fieldz -> map.put(((Class) ((ParameterizedType) fieldz.getType().getGenericInterfaces()[0]).getActualTypeArguments()[0]), fieldz)));
        return map;
    }

    public boolean setFields(Map<String, Object> fields, Object entity) {
        final boolean[] dirty = {false};
        Class clazz = entity.getClass();
        fields.forEach((k, v) -> {
            Field field = ReflectionUtils.findField(clazz, k);
            if (field == null) {
                return;
            }

            if (this.getHandleableCustomTypes().containsKey(field.getType())) {
                String vStr = String.valueOf(v);
                if (vStr.matches("[a-zA-Z]+")) {
                    dirty[0] = true;
                    return;
                }
                Long vLong = Long.parseLong(vStr);
                Arrays.asList(clazz.getDeclaredMethods()).forEach((method) -> {
                    if (method.getName().toLowerCase().equals("set" + k) && method.getParameterCount() == 1) {
                        Class<?> paramType = method.getParameterTypes()[0];
                        Field repoField = this.getHandleableCustomTypes().get(paramType);
                        repoField.setAccessible(true);
                        try {
                            JpaRepository repo = (JpaRepository) repoField.get(this);
                            if (repo.findById(vLong).isEmpty()) {
                                method.invoke(entity, (Object) null);
                                return;
                            }
                            Object reference = repo.findById(vLong).get();
                            method.invoke(entity, reference);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                });
                return;
            }

            if(field.getType().isEnum() && v.getClass() == String.class)
            {
                for (Object enumConstant : field.getType().getEnumConstants()) {
                    if (enumConstant.toString().equals(v.toString().toUpperCase()))
                    {
                        field.setAccessible(true);
                        ReflectionUtils.setField(field, entity, enumConstant);
                    }
                }
                return;
            }

            if (!Arrays.asList(String.class, Integer.class, Long.class, Boolean.class).contains(field.getType())) {
                dirty[0] = true;
                return;
            }

            field.setAccessible(true);
            ReflectionUtils.setField(field, entity, v);
        });
        return dirty[0];
    }

    public Map<String, Object> changeField(Map<String, Object> fields, String key, Object val) {
        if (fields.containsKey(key)) {
            fields.replace(key, val);
        } else {
            fields.put(key, val);
        }
        return fields;
    }

    public Map<String, Object> getMapWithoutFields(Object obj, String... fields)
    {
        Map<String, Object> map = new HashMap<>();
        List<String> fieldsToRemove = Arrays.asList(fields);
        List<Field> fieldList = Arrays.stream(obj.getClass().getDeclaredFields()).filter
                (field -> !fieldsToRemove.contains(field.getName())).collect(Collectors.toList());

        fieldList.forEach(field -> {
            field.setAccessible(true);
            try {
                map.put(field.getName(), field.get(obj));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        return map;
    }
}
