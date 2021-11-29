package com.mhupfauer.votej.api;

import com.mhupfauer.votej.persistence.Entity.*;
import com.mhupfauer.votej.persistence.Repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

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

    public Map<Class, Field> getHandleableCustomTypes() {
        Map<Class, Field> map = new HashMap<>();
        Arrays.asList(this.getClass().getDeclaredFields()).forEach((fieldz -> {
            map.put(
                    ((Class) ((ParameterizedType) fieldz.getType().getGenericInterfaces()[0]).getActualTypeArguments()[0]),
                    fieldz
            );
        }));
        return map;
    }

    public boolean setFields(Map<String, Object> fields, Object entity)
    {
        final boolean[] dirty = {false};
        Class clazz = entity.getClass();
        fields.forEach((k, v) -> {
            Field field = ReflectionUtils.findField(clazz, k);
            if(field == null)
                return;

            if(this.getHandleableCustomTypes().containsKey(field.getType()))
            {
                String vStr = String.valueOf(v);
                if(vStr.matches("[a-zA-Z]+"))
                {
                    dirty[0] = true;
                    return;
                }
                Long vLong = Long.parseLong(vStr);
                Arrays.asList(clazz.getDeclaredMethods()).forEach((method) -> {
                    if(method.getName().toLowerCase().equals("set"+k) && method.getParameterCount() == 1)
                    {
                        Class paramType = method.getParameterTypes()[0];
                        Field repoField = this.getHandleableCustomTypes().get(paramType);
                        repoField.setAccessible(true);
                        try {
                            JpaRepository<Object, Object> repo = (JpaRepository<Object, Object>) repoField.get(this);
                            if(repo.findById(vLong).isEmpty()) {
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

            field.setAccessible(true);
            ReflectionUtils.setField(field,entity,v);
        });
        return dirty[0];
    }
}
