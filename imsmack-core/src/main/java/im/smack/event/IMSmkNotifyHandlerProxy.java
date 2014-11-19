 /*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

 /**
 * Project  : WebQQCore
 * Package  : iqq.im.event
 * File     : QQNotifyHandlerProxy.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2013-3-16
 * License  : Apache License 2.0 
 */
package im.smack.event;

import im.smack.IMSmkNotifyListener;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

 /**
 *
 * 使用这个类简化事件的注册，分发
 * 只需在被代理的类使用@IMEventHandler注解需要处理的事件类型即可
 *
 * @author solosky <solosky772@qq.com>
 *
 */
public class IMSmkNotifyHandlerProxy implements IMSmkNotifyListener{
	private static final Logger LOG = LoggerFactory.getLogger(IMSmkNotifyHandlerProxy.class);
	private Object proxyObject;
	private Map<IMSmkNotifyEvent.Type, Method> methodMap;
	public IMSmkNotifyHandlerProxy(Object proxyObject){
		this.proxyObject = proxyObject;
		this.methodMap = new HashMap<IMSmkNotifyEvent.Type, Method>();
		 for (Method m : proxyObject.getClass().getDeclaredMethods()) {
			 if(m.isAnnotationPresent(IMSmkNotifyHandler.class)){
				 IMSmkNotifyHandler handler = m.getAnnotation(IMSmkNotifyHandler.class);
				 this.methodMap.put(handler.value(), m);
				 if(!m.isAccessible()){
					 m.setAccessible(true);
				 }
			 }
		 }
	}
	
	@Override
	public void onNotifyEvent(IMSmkNotifyEvent event) {
		Method m =  methodMap.get(event.getType());
		if(m != null){
			try {
				m.invoke(proxyObject, event);
			} catch (Throwable e) {
				LOG.warn("invoke QQNotifyHandler Error!!", e);
			}
		}else{
			LOG.warn("Not found QQNotifyHandler for QQNotifyEvent = " + event);
		}
	}

}
