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
 * File     : QQNotifyHandler.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2013-3-16
 * License  : Apache License 2.0 
 */
package im.smack.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * 一个注解可以简化NotifyEvent的监听，分发和处理
 * 只需在处理事件的方法上加入@QQNotifyHandler即可
 * 如
 * @QQNotifyHandler(QQNotifyEvent.Type.BUDDY_MSG)
 *
 * @author solosky <solosky772@qq.com>
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface IMSmkNotifyHandler {
	IMSmkNotifyEvent.Type value();
}
