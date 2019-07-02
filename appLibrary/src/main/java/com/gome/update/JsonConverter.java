package com.gome.update;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public interface JsonConverter<T> {

	T convert(String str);
	
	
	public abstract class SimpleConverer<T> implements JsonConverter<T>{

		private final static Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

		protected abstract TypeToken<T> getTypeToken();
		
		@Override
		public T convert(String str) {
			return gson.fromJson(str, getTypeToken().getType());
		}
//
//		@Override
//		public List<T> convertArray(String str) {			
//			return gson.fromJson(str, getTypeToken().getType());			
//		}
		
	}

}
