package com.bashellwang.refactdemo.business;

import org.greenrobot.greendao.converter.PropertyConverter;

/**
 * Created by liang.wang on 2019/3/5.
 */
public class NoteTypeConverter implements PropertyConverter<NoteType, String> {
    @Override
    public NoteType convertToEntityProperty(String databaseValue) {
        return NoteType.valueOf(databaseValue);
    }

    @Override
    public String convertToDatabaseValue(NoteType entityProperty) {
        return entityProperty.name();
    }
}
