/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation;

import java.io.Serializable;

import org.mockito.cglib.proxy.MethodProxy;
import org.mockito.cglib.reflect.FastClass;
import org.mockito.internal.util.reflection.Whitebox;

public class SerializableMockitoMethodProxy extends AbstractMockitoMethodProxy implements Serializable {

    private static final long serialVersionUID = -8869440102413620466L;
    private final Class<?> c1;
    private final Class<?> c2;
    private final String desc;
    private final String name;
    private final String superName;
    private transient MethodProxy methodProxy;

    public SerializableMockitoMethodProxy(MethodProxy methodProxy) {
        this.methodProxy = methodProxy;
        desc = methodProxy.getSignature().getDescriptor();
        name = methodProxy.getSignature().getName();
        superName = methodProxy.getSuperName();
      
        Object createInfo = Whitebox.getInternalState(methodProxy, "createInfo");
        if (createInfo != null) {
            c1 = (Class<?>) Whitebox.getInternalState(createInfo, "c1");
            c2 = (Class<?>) Whitebox.getInternalState(createInfo, "c2");
        } else {
            Object fcInfo = Whitebox.getInternalState(methodProxy, "fastClassInfo");
            FastClass f1 = (FastClass) Whitebox.getInternalState(fcInfo, "f1");
            FastClass f2 = (FastClass) Whitebox.getInternalState(fcInfo, "f2");
            c1 = f1.getJavaClass();
            c2 = f2.getJavaClass();
        }
    }

    public MethodProxy getMethodProxy() {
        if (methodProxy == null)
            methodProxy = MethodProxy.create(c1, c2, desc, name, superName);
        return methodProxy;
    }
}