package com.silkroad.core.event.base;
 /*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE com.alee.extended.breadcrumb.file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this com.alee.extended.breadcrumb.file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this com.alee.extended.breadcrumb.file except in compliance with
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

import java.io.Serializable;

/**
 * Project  : iqq
 * Author   : solosky < solosky772@qq.com >
 * Created  : 6/14/14
 * License  : Apache License 2.0
 */
public class CoreEvent extends Intent implements Serializable {
    protected int eventId;
    protected Object target;
    protected CoreEvent relatedEvent;

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public CoreEvent getRelatedEvent() {
        return relatedEvent;
    }

    public void setRelatedEvent(CoreEvent relatedEvent) {
        this.relatedEvent = relatedEvent;
    }
}
