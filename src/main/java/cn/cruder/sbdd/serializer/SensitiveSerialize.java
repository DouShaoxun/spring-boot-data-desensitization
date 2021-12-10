package cn.cruder.sbdd.serializer;


import cn.cruder.sbdd.annotation.Sensitive;
import cn.cruder.sbdd.senum.SensitiveTypeEnum;
import cn.cruder.sbdd.util.DataDesensitizationUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.Objects;

/**
 * SpringBoot 默认json序列号工具 jackson
 *
 * @Author: cruder
 * @Date: 2021/12/10/11:04
 */
@NoArgsConstructor
@AllArgsConstructor
public class SensitiveSerialize extends JsonSerializer<String> implements ContextualSerializer {

    /**
     * 脱敏类型
     */
    private SensitiveTypeEnum sensitiveTypeEnum;

    /**
     * 前几位不脱敏
     */
    private Integer prefixNoMaskLen;

    /**
     * 最后几位不脱敏
     */
    private Integer suffixNoMaskLen;

    /**
     * 用什么打码
     */
    private String symbol;

    /**
     * 可以调用该方法以要求实现序列化此序列化程序处理的类型的值。
     *
     * @param origin             要序列化的值； 不能为空
     * @param jsonGenerator      用于输出结果 Json 内容的生成器
     * @param serializerProvider 可用于获取序列化器以序列化对象值包含的提供程序（如果有）。
     * @throws IOException {@link  IOException}
     */
    @Override
    public void serialize(final String origin,
                          final JsonGenerator jsonGenerator,
                          final SerializerProvider serializerProvider) throws IOException {
        switch (sensitiveTypeEnum) {
            case CUSTOMER:
                jsonGenerator.writeString(DataDesensitizationUtil.desValue(origin, prefixNoMaskLen, suffixNoMaskLen, symbol));
                break;
            case NAME:
                jsonGenerator.writeString(DataDesensitizationUtil.chineseName(origin));
                break;
            case ID_NUM:
                jsonGenerator.writeString(DataDesensitizationUtil.idCardNum(origin));
                break;
            case PHONE_NUM:
                jsonGenerator.writeString(DataDesensitizationUtil.mobilePhone(origin));
                break;
            default:
                throw new IllegalArgumentException("unknown sensitive type enum " + sensitiveTypeEnum);
        }
    }

    /**
     * 调用的方法以查看是否需要不同（或不同配置）的序列化程序来序列化指定属性的值
     *
     * @param serializerProvider 用于访问配置、其他序列化程序的序列化程序提供程序
     * @param beanProperty       表示属性的方法或字段（用于访问要序列化的值）
     * @return {@link  JsonSerializer}
     * @throws JsonMappingException {@link  JsonMappingException}
     */
    @Override
    public JsonSerializer<?> createContextual(final SerializerProvider serializerProvider,
                                              final BeanProperty beanProperty) throws JsonMappingException {
        if (beanProperty != null) {
            if (Objects.equals(beanProperty.getType().getRawClass(), String.class)) {
                Sensitive sensitive = beanProperty.getAnnotation(Sensitive.class);
                if (sensitive == null) {
                    sensitive = beanProperty.getContextAnnotation(Sensitive.class);
                }
                if (sensitive != null) {
                    // 如果字段上加了自定义注解(Sensitive)
                    return new SensitiveSerialize(sensitive.type(), sensitive.prefixNoMaskLen(), sensitive.suffixNoMaskLen(), sensitive.symbol());
                }
            }
            return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
        }
        return serializerProvider.findNullValueSerializer(null);
    }

}

