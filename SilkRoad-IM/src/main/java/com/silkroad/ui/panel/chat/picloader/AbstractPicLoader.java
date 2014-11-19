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

 /**
 * Project  : IQQ_V2.1
 * Package  : iqq.app.ui.content.chat.picloader
 * File     : AbstractPicLoader.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2013-4-20
 * License  : Apache License 2.0 
 */
package com.silkroad.ui.panel.chat.picloader;

 import com.silkroad.ui.bean.content.UIContentItem;
 import com.silkroad.ui.context.UIContext;
 import com.silkroad.ui.event.UIEvent;
 import com.silkroad.ui.service.ResourceService;
 import com.silkroad.ui.service.TaskService;
 import com.silkroad.ui.service.impl.ResourceServiceImpl;
 import com.silkroad.ui.service.impl.TaskServiceImpl;
 import org.apache.log4j.Logger;

 import javax.imageio.ImageIO;
 import java.awt.*;
 import java.awt.image.BufferedImage;
 import java.io.File;
 import java.io.IOException;
 import java.lang.ref.WeakReference;
 import java.util.ArrayList;
 import java.util.Iterator;
 import java.util.List;
 import java.util.UUID;

 /**
  *
  * 抽象的图片加载器，主要实现以下逻辑
  * 判断本地图片是否存在，如果存在，直接加载本地图片，
  * 如果本地图片不存在，就从网络加载
  *
  * @author solosky <solosky772@qq.com>
  *
  */
 public abstract class AbstractPicLoader implements PicLoader{
     private static final long serialVersionUID = 6204208580780118359L;
     private static final Logger LOG = Logger.getLogger(AbstractPicLoader.class);
     /**上下文*/
     protected transient UIContext context;
     /**加载进度回调,使用弱引用保证内存可以被释放*/
     protected transient List<WeakReference<PicLoadListener>> loadListeners;
     /**读取成功在内存中的图片*/
     protected transient BufferedImage picImage;
     /**加载状态*/
     protected PicLoadState loadState;
     /**如果成功下载到本地，保存本地图片文件名，通常在用户文件夹下，在user/{uin}/pic文件夹*/
     protected String cachedFile;
     /**本地文件路径，可能是非用户文件夹下*/
     protected String localFile;
     /**和Webqqcore对应的ContentItem，如果有的话*/
     protected UIContentItem picItem;

     public AbstractPicLoader(){
         loadState = PicLoadState.PENDING;
     }

     @Override
     public void loadPic() {
         loadState = PicLoadState.LOADING;
         if( cachedFile != null ){
             ResourceService resources = UIContext.me().getBean(ResourceServiceImpl.class);
             loadFromLocal(resources.getUserFile("pic/" + cachedFile));
         }else if(localFile != null) {
             loadFromLocal(new File(localFile));
         }else{
             loadFromServer();
         }
     }

     private void loadFromServer(){
         try {
             loadState = PicLoadState.LOADING;
             //UIEventHandlerProxy.register(this, context);
             doLoadFromServer();
         } catch (Exception e) {
             notifyError(e);
             LOG.warn("PicLoader loadFromServer failed!!!", e);
         }
     }
     @Override
     public void sendToServer() {
         loadState = PicLoadState.LOADING;
         //UIEventHandlerProxy.register(this, context);
         try {
             doSendToServer();
         } catch (Exception e) {
             notifyError(e);
             LOG.warn("PicLoader sendToSever failed!!!", e);
         }
     }

     protected void broadcastUIEvent(UIEvent event){
         //UIEventService eventHub = context.getSerivce(IMService.Type.EVENT);
         //eventHub.broadcast(event);
     }

     protected abstract void doLoadFromServer() throws Exception;
     protected abstract void doSendToServer() throws Exception;
     protected abstract boolean isMyEvent(UIEvent event);

     protected void processGetPicProgress(UIEvent event){
         if(isMyEvent(event)){
             long current = event.getLong("current");
             long total = event.getLong("total");
             notifyProgress(current, total);
         }
     }

     protected void processGetPicError(UIEvent event){
         if(isMyEvent(event)){
             notifyError((Throwable)event.getTarget());
             loadState = PicLoadState.ERROR;
             //UIEventHandlerProxy.unregister(this);
         }
     }

     protected void processGetPicSuccess(UIEvent event){
         if(isMyEvent(event)){
             //下载文件时首先存在本地
             //UIEventHandlerProxy.unregister(this);
             //从文件中加载
             cachedFile = event.getRelatedEvent().getString("cachedFile");
             ResourceService resources = UIContext.me().getBean(ResourceServiceImpl.class);
             loadFromLocal(resources.getUserFile("pic/" + cachedFile));
             loadState = PicLoadState.SUCCESS;
         }
     }

     protected void processUploadPicProgress(UIEvent event){
         if(isMyEvent(event)){
             long current = event.getLong("current");
             long total = event.getLong("total");
             notifyProgress(current, total);
         }
     }

     protected void processUploadPicError(UIEvent event){
         if(isMyEvent(event)){
             //UIEventHandlerProxy.unregister(this);
             loadState = PicLoadState.ERROR;
         }
     }

     protected void processUploadPicSuccess(UIEvent event){
         if(isMyEvent(event)){
             //UIEventHandlerProxy.unregister(this);
             loadState = PicLoadState.SUCCESS;
         }
     }

     protected String genLocalFileName(){
         return UUID.randomUUID().toString() + ".jpg";
     }

     //从本地文件加载
     private void loadFromLocal(final File file){
         LOG.debug("loadFromLocal: " + file);
         TaskService tasks = UIContext.me().getBean(TaskServiceImpl.class);
         tasks.submit(new Runnable(){
             public void run(){
                 try {
                     picImage = ImageIO.read(file);
                     Image small = picImage;
                     if(picImage.getWidth() > 400){
                         //应该根据父窗口对象缩放，这里没有父窗口引用，不方便获取大小，直接按固定值缩放即可
                         small = picImage.getScaledInstance(400, -1, 100);
                     }
                     notifySucesss(small);
                 } catch (IOException e) {
                     notifyError(e);
                     LOG.warn("PicLoader loadFromLocal failed!!!", e);
                 }
             }
         });
     }

     @Override
     public void addListener(PicLoadListener listener) {
         if(loadListeners == null){
             loadListeners = new ArrayList<WeakReference<PicLoadListener>>();
         }
         loadListeners.add(new WeakReference<PicLoadListener>(listener));
     }

     @Override
     public void removeListener(PicLoadListener listener) {
         if(loadListeners != null && loadListeners.size() > 0){
             Iterator<WeakReference<PicLoadListener>> it = loadListeners.iterator();
             while(it.hasNext()){
                 WeakReference<PicLoadListener> ref = it.next();
                 PicLoadListener tmp = ref.get();
                 if(tmp == null || tmp == listener){
                     it.remove();
                 }
             }
             loadListeners.remove(listener);
         }
     }

     @Override
     public UIContentItem getContentItem() {
         return picItem;
     }

     @Override
     public PicLoadState getState() {
         return loadState;
     }

     @Override
     public Image getPic() {
         return picImage;
     }

     private List<WeakReference<PicLoadListener>> getMutableListeners(){
         if(loadListeners != null && loadListeners.size() > 0){
             return new ArrayList<WeakReference<PicLoadListener>>(loadListeners);
         }else{
             return new ArrayList<WeakReference<PicLoadListener>>();
         }
     }

     private void notifySucesss(final Image image){
         EventQueue.invokeLater(new Runnable(){
             public void run(){
                 loadState = PicLoadState.SUCCESS;
                 for(WeakReference<PicLoadListener> listenerRef: getMutableListeners()){
                     PicLoadListener listener = listenerRef.get();
                     if(listener != null){
                         listener.onSuccess(image);
                     }
                 }
             }
         });

     }

     private void notifyError(final Throwable t){
         EventQueue.invokeLater(new Runnable(){
             public void run(){
                 loadState = PicLoadState.ERROR;
                 for(WeakReference<PicLoadListener> listenerRef: getMutableListeners()){
                     PicLoadListener listener = listenerRef.get();
                     if(listener != null){
                         listener.onError(t);
                     }
                 }
             }
         });
     }

     private void notifyProgress(final long current,final long total){
         EventQueue.invokeLater(new Runnable(){
             public void run(){
                 for(WeakReference<PicLoadListener> listenerRef: getMutableListeners()){
                     PicLoadListener listener = listenerRef.get();
                     if(listener != null){
                         listener.onProgress(current, total);
                     }
                 }
             }
         });
     }
 }
