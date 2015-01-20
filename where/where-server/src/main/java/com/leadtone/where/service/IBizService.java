package com.leadtone.where.service;

import com.leadtone.where.protocol.beans.Content;

public interface IBizService {
	
	public  Content process(Content requestContent,Object classInstance);
	
	
}
