package com.defty.movie.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

public class CopyUtil {
    // Lấy danh sách thuộc tính null hoặc có kiểu dữ liệu khác nhau giữa source và target
    public static String[] getIgnoredPropertyNames(Object source, Object target) {
        final BeanWrapper srcWrapper = new BeanWrapperImpl(source);
        final BeanWrapper targetWrapper = new BeanWrapperImpl(target);

        PropertyDescriptor[] srcPds = srcWrapper.getPropertyDescriptors();
        Set<String> ignoredNames = new HashSet<>();

        for (PropertyDescriptor srcPd : srcPds) {
            String propName = srcPd.getName();
            Object srcValue = srcWrapper.getPropertyValue(propName);

            // Kiểm tra nếu giá trị null -> bỏ qua
            if (srcValue == null) {
                ignoredNames.add(propName);
                continue;
            }

            // Kiểm tra target có thuộc tính này không
            if (targetWrapper.isWritableProperty(propName)) {
                Class<?> srcType = srcPd.getPropertyType();
                Class<?> targetType = targetWrapper.getPropertyType(propName);

                // Nếu kiểu dữ liệu khác nhau -> bỏ qua
                if (!srcType.equals(targetType)) {
                    ignoredNames.add(propName);
                }
            } else {
                // Nếu target không có thuộc tính này -> bỏ qua
                ignoredNames.add(propName);
            }
        }

        return ignoredNames.toArray(new String[0]);
    }

    // Copy properties từ source vào target, bỏ qua thuộc tính null hoặc khác kiểu dữ liệu
    public static void copyPropertiesIgnoreNull(Object src, Object target) {
        BeanUtils.copyProperties(src, target, getIgnoredPropertyNames(src, target));
    }
}
