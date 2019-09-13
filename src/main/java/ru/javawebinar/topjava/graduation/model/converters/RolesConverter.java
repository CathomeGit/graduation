package ru.javawebinar.topjava.graduation.model.converters;

import ru.javawebinar.topjava.graduation.model.Role;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.EnumSet;

@Converter
public class RolesConverter implements AttributeConverter<EnumSet<Role>, Integer> {
    @Override
    public Integer convertToDatabaseColumn(EnumSet<Role> attribute) {
        return Role.rolesToBitmask(attribute);
    }

    @Override
    public EnumSet<Role> convertToEntityAttribute(Integer dbData) {
        return Role.parseRoles(dbData);
    }
}
