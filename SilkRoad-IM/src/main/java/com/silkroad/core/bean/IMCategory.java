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
 * Package  : iqq.im.vo
 * File     : QQCategory.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2012-7-31
 * License  : Apache License 2.0 
 */
package com.silkroad.core.bean;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 * QQ分组
 * 
 * @author solosky <solosky772@qq.com>
 * 
 */
public class IMCategory implements Serializable {
	private static final long serialVersionUID = 1L;
	private int index;
	private int sort;
	private String name;

	private List<IMBuddy> buddyList = new LinkedList<IMBuddy>();

	public IMBuddy getIMBuddyByUid(int uid) {
		if (!buddyList.isEmpty() && uid != 0) {
			for (IMBuddy b : buddyList) {
				if (b.getUid() == uid) {
					return b;
				}
			}
		}
		return null;
	}

	public List<IMBuddy> getBuddyList() {
		return buddyList;
	}

	public void setBuddyList(List<IMBuddy> buddyList) {
		this.buddyList = buddyList;
	}

	public int getOnlineCount() {
		int count = 0;
		for(IMBuddy buddy: buddyList){
			IMStatus stat = buddy.getStatus();
			if(IMStatus.isGeneralOnline(stat)){
				count++;
			}
		}
		return count;
	}
	
	public int getBuddyCount(){
		return buddyList!=null ? buddyList.size() : 0;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}
}
