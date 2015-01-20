package com.leadtone.where.service;

import com.leadtone.where.protocol.beans.Content;
import com.leadtone.where.protocol.beans.WhereMessage;

public interface IProtocolService {

	public Content dispatch(WhereMessage message);
	
	@Deprecated
	public boolean boardcast(Integer id ,String from,String to,String message);
	
	public boolean route(String from,String to,String message);
	
	
	
}
