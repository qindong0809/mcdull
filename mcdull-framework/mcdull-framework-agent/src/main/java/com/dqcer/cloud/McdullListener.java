package com.dqcer.cloud;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.utility.JavaModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 监听器
 *
 * @author dqcer
 * @date 2022/11/10
 */
public class McdullListener implements AgentBuilder.Listener {

    private static final Logger log = LoggerFactory.getLogger(McdullListener.class);

    /**
     * Invoked upon a type being supplied to a transformer.
     *
     * @param typeName    The binary name of the instrumented type.
     * @param classLoader The class loader which is loading this type or {@code null} if loaded by the boots loader.
     * @param module      The instrumented type's module or {@code null} if the current VM does not support modules.
     * @param loaded      {@code true} if the type is already loaded.
     */
    @Override
    public void onDiscovery(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {

    }

    /**
     * Invoked prior to a successful transformation being applied.
     *
     * @param typeDescription The type that is being transformed.
     * @param classLoader     The class loader which is loading this type or {@code null} if loaded by the boots loader.
     * @param module          The transformed type's module or {@code null} if the current VM does not support modules.
     * @param loaded          {@code true} if the type is already loaded.
     * @param dynamicType     The dynamic type that was created.
     */
    @Override
    public void onTransformation(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, boolean loaded, DynamicType dynamicType) {

    }

    /**
     * Invoked when a type is not transformed but ignored.
     *
     * @param typeDescription The type being ignored for transformation.
     * @param classLoader     The class loader which is loading this type or {@code null} if loaded by the boots loader.
     * @param module          The ignored type's module or {@code null} if the current VM does not support modules.
     * @param loaded          {@code true} if the type is already loaded.
     */
    @Override
    public void onIgnored(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, boolean loaded) {

    }

    /**
     * Invoked when an error has occurred during transformation.
     *
     * @param typeName    The binary name of the instrumented type.
     * @param classLoader The class loader which is loading this type or {@code null} if loaded by the boots loader.
     * @param module      The instrumented type's module or {@code null} if the current VM does not support modules.
     * @param loaded      {@code true} if the type is already loaded.
     * @param throwable   The occurred error.
     */
    @Override
    public void onError(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded, Throwable throwable) {

    }

    /**
     * Invoked after a class was attempted to be loaded, independently of its treatment.
     *
     * @param typeName    The binary name of the instrumented type.
     * @param classLoader The class loader which is loading this type or {@code null} if loaded by the boots loader.
     * @param module      The instrumented type's module or {@code null} if the current VM does not support modules.
     * @param loaded      {@code true} if the type is already loaded.
     */
    @Override
    public void onComplete(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {

    }
}
