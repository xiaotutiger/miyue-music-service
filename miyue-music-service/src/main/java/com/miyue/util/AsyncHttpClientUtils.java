package com.miyue.util;

import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;

public class AsyncHttpClientUtils {
	public static void main(String[] args) throws Exception{
		 AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
		 asyncHttpClient.prepareGet("http://www.ning.com/").execute(new AsyncCompletionHandler<Response>(){

		        @Override
		        public Response onCompleted(Response response) throws Exception{
		            // Do something with the Response
		            // ...
		            return response;
		        }

		        @Override
		        public void onThrowable(Throwable t){
		            // Something wrong happened.
		        }
		    });
	}
}
