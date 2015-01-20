package com.leadtone.where.service;

import java.lang.reflect.Method;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import com.leadtone.where.WhereBiz;
import com.leadtone.where.protocol.beans.Content;

@Service
public class BizServiceImpl implements IBizService {

	
	@Override
	public  Content process(Content requestContent,Object classInstance) {
		String type = requestContent.getType();
		Content content =  null;
		Method[] methods = ReflectionUtils.getAllDeclaredMethods(classInstance.getClass());
		for (Method method : methods) {
			WhereBiz rb = AnnotationUtils.findAnnotation(method,WhereBiz.class);
			if (rb!=null && type.equals(rb.value())) {
				try {
					content =(Content) method.invoke(classInstance, requestContent.getData());
					content.setType(type);
					break;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return content;
	}

}
