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
 * Package  : iqq.im.core
 * File     : QQStore.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2013-2-24
 * License  : Apache License 2.0 
 */
package im.smack.core;

import im.smack.IMSmkException;
import im.smack.bean.*;
import im.smack.bean.IMSmkCategory;
import im.smack.bean.content.ContentItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * 存储QQ相关的数据 如好友列表，分组列表，群列表，在线好友等
 * 
 * @author solosky <solosky772@qq.com>
 * 
 */
public class IMSmkStore implements IMSmkLifeCycle {
	private Map<Long, IMSmkBuddy> buddyMap; // uin => IMSmkBuddy, 快速通过uid查找QQ好友
	private Map<Long, IMSmkStranger> strangerMap; // uid => IMSmkStranger,
													// 快速通过uin查找临时会话用户
	private Map<Long, IMSmkCategory> categoryMap; // index => IMSmkCatelog
	private Map<Long, IMSmkDiscuz> discuzMap; // did = > IMSmkDiscuz
	private Map<Long, IMSmkGroup> groupMap; // code => IMSmkGroup, 快速通过群ID查找群
	private List<ContentItem> pictureItemList; // filename -> PicItem 上传图片列表

	public IMSmkStore() {
		this.buddyMap = new HashMap<Long, IMSmkBuddy>();
		this.strangerMap = new HashMap<Long, IMSmkStranger>();
		this.categoryMap = new HashMap<Long, IMSmkCategory>();
		this.groupMap = new HashMap<Long, IMSmkGroup>();
		this.discuzMap = new HashMap<Long, IMSmkDiscuz>();
		this.pictureItemList = new ArrayList<ContentItem>();
	}

	@Override
	public void init(IMSmkContext context) throws IMSmkException {
	}

	@Override
	public void destroy() throws IMSmkException {
	}

	// add
	public void addBuddy(IMSmkBuddy buddy) {
		buddyMap.put(buddy.getUid(), buddy);
	}

	public void addStranger(IMSmkStranger stranger) {
		strangerMap.put(stranger.getUid(), stranger);
	}

	public void addCategory(IMSmkCategory category) {
        categoryMap.put(new Long(category.getIndex()), category);
	}

	public void addGroup(IMSmkGroup group) {
		groupMap.put(group.getCode(), group);
	}

	public void addPicItem(ContentItem pictureItem) {
		pictureItemList.add(pictureItem);
	}

	public void addDiscuz(IMSmkDiscuz discuz) {
		discuzMap.put(discuz.getDid(), discuz);
	}

	// delete
	public void deleteBuddy(IMSmkBuddy buddy) {
		buddyMap.remove(buddy);
	}

	public void deleteStranger(IMSmkStranger stranger) {
		strangerMap.remove(stranger);
	}

	public void deleteCategory(IMSmkCategory catelog) {
        categoryMap.remove(catelog);
	}

	public void deleteGroup(IMSmkGroup group) {
		groupMap.remove(group.getGin());
	}

	public void deletePicItem(ContentItem picItem) {
		pictureItemList.remove(picItem);
	}

	public void deleteDiscuz(IMSmkDiscuz discuz) {
		discuzMap.remove(discuz.getDid());
	}

	// get
	public IMSmkBuddy getBuddyByUid(long uid) {
		return buddyMap.get(uid);
	}

	public IMSmkStranger getStrangerByUid(long uid) {
		return strangerMap.get(uid);
	}

	public IMSmkCategory getCatelogByIndex(long index) {
		return categoryMap.get(index);
	}

	public IMSmkGroup getGroupByCode(long code) {
		return groupMap.get(code);
	}

	public IMSmkGroup getGroupById(long id) {
		for (IMSmkGroup g : getGroupList()) {
			if (g.getGid() == id) {
				return g;
			}
		}
		return null;
	}

	public IMSmkGroup getGroupByGid(long gin) {
		for (IMSmkGroup g : getGroupList()) {
			if (g.getGin() == gin) {
				return g;
			}
		}
		return null;
	}

	public IMSmkDiscuz getDiscuzByDid(long did) {
		return discuzMap.get(did);
	}

	// get list
	public List<IMSmkBuddy> getBuddyList() {
		return new ArrayList<IMSmkBuddy>(buddyMap.values());
	}

	public List<IMSmkStranger> getStrangerList() {
		return new ArrayList<IMSmkStranger>(strangerMap.values());
	}

	public List<IMSmkCategory> getCategoryList() {
		return new ArrayList<IMSmkCategory>(categoryMap.values());
	}

	public List<IMSmkGroup> getGroupList() {
		return new ArrayList<IMSmkGroup>(groupMap.values());
	}

	public List<IMSmkDiscuz> getDiscuzList() {
		return new ArrayList<IMSmkDiscuz>(discuzMap.values());
	}

	public List<IMSmkBuddy> getOnlineBuddyList() {
		List<IMSmkBuddy> onlines = new ArrayList<IMSmkBuddy>();
		for (IMSmkBuddy buddy : buddyMap.values()) {
			if (IMSmkStatus.isGeneralOnline(buddy.getStatus())) {
				onlines.add(buddy);
			}
		}
		return onlines;
	}

	public List<ContentItem> getPicItemList() {
		return pictureItemList;
	}

	// get size
	public int getPicItemListSize() {
		return pictureItemList.isEmpty() ? 0 : pictureItemList.size();
	}

	// 查找临时会话用户 QQGroup/QQDiscuz/QQStranger
	public IMSmkUser searchUserByUin(long uid) {
		IMSmkUser user = getBuddyByUid(uid);
		if (user == null) {
			user = getStrangerByUid(uid);
		}
		if (user == null) {
			for (IMSmkGroup group : getGroupList()) {
				for (IMSmkUser u : group.getMembers()) {
					if (u.getUid() == uid) {
						return u;
					}
				}
			}
			for (IMSmkDiscuz discuz : getDiscuzList()) {
				for (IMSmkUser u : discuz.getMembers()) {
					if (u.getUid() == uid) {
						return u;
					}
				}
			}
		}
		return user;
	}
}
