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
 * Package  : iqq.app.ui.content.chat.conversation
 * File     : UIMsgPane.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2013-4-9
 * License  : Apache License 2.0 
 */
package com.silkroad.ui.panel.chat.msg;

import com.alee.extended.image.WebDecoratedImage;
import com.alee.extended.layout.WrapFlowLayout;
import com.alee.extended.panel.FlowPanel;
import com.alee.extended.panel.WrapPanel;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.silkroad.ui.bean.UIMsg;
import com.silkroad.ui.context.UIContext;
import com.silkroad.ui.panel.UIPanel;
import com.silkroad.ui.panel.chat.rich.RichTextPane;
import com.silkroad.ui.service.I18nService;
import com.silkroad.ui.service.SkinService;
import com.silkroad.ui.service.impl.I18nServiceImpl;
import com.silkroad.ui.service.impl.SkinServiceImpl;
import com.silkroad.util.UIUtils;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;

 /**
  *
  * 消息框，用于在MsgListPane里面显示一条消息
  *
  * @author solosky <solosky772@qq.com>
  *
  */
 public class MsgPane extends UIPanel {
     private static final long serialVersionUID = 1587863328325375740L;
     private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
     private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM-dd");
     private WebDecoratedImage avatar;
     private WebLabel nameLabl;
     private WebLabel infoLabl;
     private WebPanel msgBg;
     private UIMsg msg;

     public MsgPane(UIMsg msg){
         setOpaque(false);
         this.msg = msg;
         if(msg.getSender().getAvatar() != null){
             this.avatar = new WebDecoratedImage(new ImageIcon(msg.getSender().getAvatar()));
         }else{
             this.avatar = new WebDecoratedImage(UIUtils.Bean.getDefaultAvatar());
         }
         initComponent();
         addComponent();
         invalidate();
     }

     private void addComponent() {
         WrapPanel msgWrap = new WrapPanel(msgBg);
         msgWrap.setMargin(10, 5, 5, 50);
         WrapFlowLayout wf = new WrapFlowLayout();
         wf.setHalign(SwingConstants.LEFT);
         msgWrap.setLayout(wf);
         add(msgWrap, BorderLayout.CENTER);

         FlowPanel faceFp = new FlowPanel(SwingConstants.TOP, avatar);
         faceFp.setOpaque(false);
         faceFp.setMargin(10, 10, 5, 5);
         add(faceFp, BorderLayout.LINE_START);
     }

     private void initComponent() {
         msgBg = new WebPanel();
         msgBg.setMargin(8, 18, 8, 8);

         Insets margin = new Insets(5, 10, 5, 10);
         nameLabl = new WebLabel(msg.getSender().getNick());
         Font font = WebLookAndFeel.globalTitleFont;
         font = font.deriveFont(Font.BOLD);
         nameLabl.setFont(font);
         nameLabl.setForeground(Color.darkGray);
         nameLabl.setMargin(margin);

         Font infoFont = WebLookAndFeel.globalTitleFont.deriveFont(12f);
         infoLabl = new WebLabel("..");
         infoLabl.setFont(infoFont);
         infoLabl.setForeground(Color.darkGray);
         infoLabl.setMargin(margin);

         // 根据输入框内容，新建一个富文本框放到消息气泡中
         RichTextPane msgTP = new RichTextPane();
         msgTP.setRichItems(UIUtils.Bean.toRichItem(msg.getContents()));
         msgTP.setEditable(false);
         msgTP.setOpaque(false);
         msgTP.setMargin(margin);
         msgBg.add(nameLabl, BorderLayout.PAGE_START);
         msgBg.add(infoLabl, BorderLayout.PAGE_END);
         msgBg.add(msgTP, BorderLayout.CENTER);

         avatar.setShadeWidth(1);
         avatar.setRound(3);
         avatar.setBorderColor(Color.WHITE);
         /*if(msg.getSender().getUin() == account.getUin()) {
             changeSkin(SkinUtils.getPainter(Type.NPICON, "chat/balloon/left/blue"));
         } else {
             changeSkin(SkinUtils.getPainter(Type.NPICON, "chat/balloon/left/white"));
         }*/

         SkinService skinService = UIContext.me().getBean(SkinServiceImpl.class);
         msgBg.setPainter(skinService.getPainterByKey("chat/balloon/left/white"));
     }

     @Override
     public void invalidate() {
         if(msg.getSender().getAvatar() != null){
             this.avatar.setIcon(new ImageIcon(msg.getSender().getAvatar()));
         }else{
             this.avatar.setIcon(UIUtils.Bean.getDefaultAvatar());
         }
         //小于一天就显示时间，否则就显示日期
         String time = DATE_FORMAT.format(msg.getDate());
         if(System.currentTimeMillis() - msg.getDate().getTime() < 24*3600*1000){
             time = TIME_FORMAT.format(msg.getDate());
         }
         I18nService i18nService = UIContext.me().getBean(I18nServiceImpl.class);
         String stat = i18nService.getMessage("chat.msg." + msg.getState().name().toLowerCase());
         infoLabl.setText("[ " + time + ", " + stat + " ]");

         super.invalidate();
     }

     /**
      * @return the msg
      */
     public UIMsg getMsg() {
         return msg;
     }
 }
