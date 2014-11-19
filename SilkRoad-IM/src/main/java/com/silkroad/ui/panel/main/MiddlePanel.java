package com.silkroad.ui.panel.main;

import com.alee.laf.menu.WebMenuItem;
import com.alee.laf.menu.WebMenuItemStyle;
import com.alee.laf.menu.WebMenuItemUI;
import com.alee.laf.menu.WebPopupMenu;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.tabbedpane.TabStretchType;
import com.alee.laf.tabbedpane.TabbedPaneStyle;
import com.alee.laf.tabbedpane.WebTabbedPane;
import com.alee.utils.ImageUtils;
import com.silkroad.core.bean.base.IMEntity;
import com.silkroad.ui.bean.*;
import com.silkroad.ui.event.UIEvent;
import com.silkroad.ui.event.UIEventType;
import com.silkroad.ui.frame.UITree;
import com.silkroad.ui.frame.main.MainFrame;
import com.silkroad.ui.manager.ChatManager;
import com.silkroad.ui.panel.UIPanel;
import com.silkroad.ui.renderer.BoddyTreeCellRenderer;
import com.silkroad.ui.renderer.RecentTreeCellRenderer;
import com.silkroad.ui.renderer.RoomTreeCellRenderer;
import com.silkroad.ui.renderer.node.BuddyNode;
import com.silkroad.ui.renderer.node.CategoryNode;
import com.silkroad.ui.renderer.node.EntityNode;
import com.silkroad.ui.renderer.node.RoomNode;
import com.silkroad.ui.service.SkinService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * 主界面，主要是包含了一个Tab控件
 * 显示：好友列表/群/最近列表
 * <p/>
 * Project  : iqq-projects
 * Author   : 承∮诺 < 6208317@qq.com >
 * Created  : 14-5-8
 * License  : Apache License 2.0
 */
public class MiddlePanel extends UIPanel {
    private static final Logger LOG = LoggerFactory.getLogger(MiddlePanel.class);
    private MainFrame frame;
    private WebTabbedPane mainTab;
    /**
     * 三部分面板，用于添加到Tab中
     */
    private UIPanel buddyPanel = new UIPanel();
    private UIPanel groupPanel = new UIPanel();
    private UIPanel recentPanel = new UIPanel();

    /**
     * 三个树控件，可以使用model无状态更新数据
     */
    private UITree contactsTree = new UITree();
    private UITree groupsTree = new UITree();
    private UITree recentTree = new UITree();


    /**
     * 树组件的鼠标事件，点击展开，双击打开聊天窗口
     */
    private TreeMouseListener treeMouse;

    public MiddlePanel(MainFrame frame) {
        super();
        this.frame = frame;
        treeMouse = new TreeMouseListener(frame);

        initTab();
        initBuddy();
        initRoom();
        initRecent();
    }

    /**
     * 最近列表
     */
    private void initRecent() {
        recentTree.addMouseListener(treeMouse);
        // 使用自定义的渲染器
        recentTree.setCellRenderer(new RecentTreeCellRenderer());
        WebScrollPane treeScroll = new WebScrollPane(recentTree, false, false);
        // 背景色
        treeScroll.getViewport().setBackground(new Color(250, 250, 250));
        // 滚动速度
        treeScroll.getVerticalScrollBar().setUnitIncrement(30);
        treeScroll.setHorizontalScrollBarPolicy(WebScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        recentPanel.add(treeScroll);

        // 测试数据

         DefaultMutableTreeNode root = new DefaultMutableTreeNode();
         UIBuddy[] buddys = new UIBuddy[10];
         int j = 0;
         for(UIBuddy buddy : buddys) {
         buddy = new UIBuddy();
         buddy.setNick("buddy-" + j);
         buddy.setSign("sing..." + j++);
         try {
         File file = frame.getResourceService().getFile("icons/login/avatar2.png");
         buddy.setAvatar(ImageIO.read(file));
         } catch (FileNotFoundException e) {
         e.printStackTrace();
         } catch (IOException e) {
         e.printStackTrace();
         }
         root.add(new BuddyNode(buddy));
         }
         UIRoom[] rooms = new UIRoom[10];
         int k = 0;
         for(UIRoom room : rooms) {
         room = new UIRoom();
         room.setNick("Room-" + k++);
         try {
         File file = frame.getResourceService().getFile("icons/login/group.png");
         room.setAvatar(ImageIO.read(file));
         } catch (FileNotFoundException e) {
         e.printStackTrace();
         } catch (IOException e) {
         e.printStackTrace();
         }
         root.add(new RoomNode(room));
         }

         DefaultTreeModel groupModel = new DefaultTreeModel(root);
         recentTree.setModel(groupModel);

    }

    /**
     * 聊天室列表
     */
    private void initRoom() {
        groupsTree.addMouseListener(treeMouse);
        // 使用自定义的渲染器
        groupsTree.setCellRenderer(new RoomTreeCellRenderer());
        WebScrollPane treeScroll = new WebScrollPane(groupsTree, false, false);
        // 背景色
        treeScroll.getViewport().setBackground(new Color(250, 250, 250));
        // 滚动速度
        treeScroll.getVerticalScrollBar().setUnitIncrement(30);
        treeScroll.setHorizontalScrollBarPolicy(WebScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        groupPanel.add(treeScroll);

        // 测试数据
        DefaultMutableTreeNode root = new DefaultMutableTreeNode();
        UIRoomCategory[] cates = new UIRoomCategory[5];
        int i = 0;
        for (UIRoomCategory cate : cates) {
            cate = new UIRoomCategory();
            cate.setName("Room Category-" + i++);
            CategoryNode cateNode = new CategoryNode(cate);
            root.add(cateNode);

            UIRoom[] rooms = new UIRoom[10];
            int j = 0;
            for (UIRoom room : rooms) {
                room = new UIRoom();
                room.setNick("Room-" + j++);
                try {
                    File file = frame.getResourceService().getFile("icons/login/group.png");
                    room.setAvatar(ImageIO.read(file));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                cateNode.add(new RoomNode(room));
            }
        }


        DefaultTreeModel groupModel = new DefaultTreeModel(root);
        groupsTree.setModel(groupModel);
    }

    /**
     * 好友列表
     */
    private void initBuddy() {
        contactsTree.addMouseListener(treeMouse);
        // 使用自定义的渲染器
        contactsTree.setCellRenderer(new BoddyTreeCellRenderer());
        WebScrollPane treeScroll = new WebScrollPane(contactsTree, false, false);
        // 背景色
        treeScroll.getViewport().setBackground(new Color(250, 250, 250));
        // 滚动速度
        treeScroll.getVerticalScrollBar().setUnitIncrement(30);
        treeScroll.setHorizontalScrollBarPolicy(WebScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        buddyPanel.add(treeScroll);

        // 测试数据
        /**
         DefaultMutableTreeNode root = new DefaultMutableTreeNode();
         UIBuddyCategory[] cates = new UIBuddyCategory[10];
         int i = 0;
         for(UIBuddyCategory cate : cates) {
         cate = new UIBuddyCategory();
         cate.setName("Category-" + i++);
         CategoryNode cateNode = new CategoryNode(cate);

         UIBuddy[] buddys = new UIBuddy[30];
         int j = 0;
         for(UIBuddy buddy : buddys) {
         buddy = new UIBuddy();
         buddy.setNick("buddy-" + j);
         buddy.setSign("sing..." + j++);
         try {
         File file = frame.getResourceService().getFile("icons/login/avatar2.png");
         buddy.setAvatar(ImageIO.read(file));
         } catch (FileNotFoundException e) {
         e.printStackTrace();
         } catch (IOException e) {
         e.printStackTrace();
         }
         cateNode.add(new BuddyNode(buddy));
         }
         root.add(cateNode);
         }

         DefaultTreeModel buddyModel = new DefaultTreeModel(root);
         contactsTree.setModel(buddyModel);
         **/
    }

    /**
     * 初始化Tab
     */
    private void initTab() {
        mainTab = new WebTabbedPane();
        mainTab.setTabbedPaneStyle(TabbedPaneStyle.attached);
        mainTab.setTabStretchType(TabStretchType.always);
        mainTab.setOpaque(false);
        mainTab.setTopBg(new Color(240, 240, 240, 60));
        mainTab.setBottomBg(new Color(255, 255, 255, 160));
        mainTab.setSelectedTopBg(new Color(240, 240, 255, 50));
        mainTab.setSelectedBottomBg(new Color(240, 240, 255, 50));
        mainTab.setBackground(new Color(255, 255, 255, 200));

        // 添加这几个的panel
        mainTab.addTab("", buddyPanel);
        mainTab.addTab("", groupPanel);
        mainTab.addTab("", recentPanel);


        add(mainTab);

    }

    /**
     * 安装皮肤
     *
     * @param skinService
     */
    @Override
    public void installSkin(SkinService skinService) {
        super.installSkin(skinService);

        mainTab.setIconAt(0, skinService.getIconByKey("main/tabBoddyIcon", 25, 25));
        mainTab.setIconAt(1, skinService.getIconByKey("main/tabGroupIcon", 25, 25));
        mainTab.setIconAt(2, skinService.getIconByKey("main/tabRecentIcon", 25, 25));

        //buddyPanel.setPainter(skinService.getPainterByKey("skin/background"));
        //groupPanel.setPainter(skinService.getPainterByKey("skin/background"));
        //recentPanel.setPainter(skinService.getPainterByKey("skin/background"));

    }

    public void updateBuddyList(List<UIBuddyCategory> uiCategories) {
        frame.getCacheService().setUICategories(uiCategories);
        DefaultMutableTreeNode root = new DefaultMutableTreeNode();
        BufferedImage defaultAvatar = getDefaultAvatar();
        for (UIBuddyCategory cate : uiCategories) {
            CategoryNode cateNode = new CategoryNode(cate);
            for (UIBuddy buddy : cate.getBuddyList()) {
                /**头像处理**/
                if (buddy.getStatus() == UIStatus.ONLINE) {
                    if (buddy.getAvatar() == null) {
                        buddy.setAvatar(defaultAvatar);
                    }
                } else {
                    if (buddy.getAvatar() == null) {
                        buddy.setAvatar(ImageUtils.createGrayscaleCopy(defaultAvatar));
                    } else {
                        buddy.setAvatar(ImageUtils.createGrayscaleCopy(buddy.getAvatar()));
                    }
                }
                cateNode.add(new BuddyNode(buddy));
            }
            root.add(cateNode);
        }

        DefaultTreeModel buddyModel = new DefaultTreeModel(root);
        contactsTree.setModel(buddyModel);
    }

    public void updateUserFace(UIUser uiUser) {
        DefaultTreeModel model = (DefaultTreeModel) contactsTree.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        BufferedImage defaultAvatar = getDefaultAvatar();
        //更新在线用户头像发亮
        for (int i = 0; i < root.getChildCount(); i++) {
            CategoryNode categoryNode = (CategoryNode) root.getChildAt(i);
            int online = categoryNode.getCategory().getOnline();
            for (int j = 0; j < categoryNode.getChildCount(); j++) {
                BuddyNode buddyNode = (BuddyNode) categoryNode.getChildAt(j);
                if (buddyNode.getBuddy().getJid().equals(uiUser.getJid())) {
                    if (uiUser.getStatus() == UIStatus.ONLINE) {
                        if (uiUser.getAvatar() == null) {
                            uiUser.setAvatar(defaultAvatar);
                        }
                        if(buddyNode.getBuddy().getStatus() != UIStatus.ONLINE) {
                            online++;
                        }
                    } else {
                        if (uiUser.getAvatar() == null) {
                            uiUser.setAvatar(ImageUtils.createGrayscaleCopy(defaultAvatar));
                        } else {
                            uiUser.setAvatar(ImageUtils.createGrayscaleCopy(uiUser.getAvatar()));
                        }
                        if(buddyNode.getBuddy().getStatus() != UIStatus.OFFLINE) {
                            online--;
                        }
                    }
                    buddyNode.getBuddy().setAvatar(uiUser.getAvatar());
                    model.reload(buddyNode);
                    //如果对话框存在更新对话框窗口亮起
                    UIEvent uiEvent = new UIEvent(UIEventType.SYNC_CHATMSG_STATE, buddyNode.getBuddy());
                    frame.getEventService().broadcast(uiEvent);
                }
            }
            categoryNode.getCategory().setOnline(online);
            model.reload(categoryNode);
        }

    }


    private BufferedImage getDefaultAvatar() {
        try {
            File file = frame.getResourceService().getFile("icons/login/avatar.png");
            return ImageIO.read(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 树组件的鼠标事件，点击展开，双击打开聊天窗口
     */
    class TreeMouseListener extends MouseAdapter {
        MainFrame frame;

        public TreeMouseListener(MainFrame frame) {
            this.frame = frame;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            // 获取选择的节点
            if (e.getSource() instanceof UITree) {
                UITree tree = (UITree) e.getSource();
                Object obj = tree.getLastSelectedPathComponent();
                DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) obj;
                final int sx = frame.getX();
                final int sy = frame.getY() + 100;
                //鼠标左键
                switch (e.getButton()) {
                    case MouseEvent.BUTTON1://左键
                        if (obj instanceof CategoryNode) {
                            // 判断是否展开
                            if (!tree.isExpanded(tree.getSelectionPath())) {
                                // 展开
                                tree.expandPath(tree.getSelectionPath());
                            } else {
                                // 合并
                                tree.collapsePath(tree.getSelectionPath());
                            }
                        } else if (e.getClickCount() == 2 && obj instanceof EntityNode) {
                            // 双击打开聊天窗口
                            EntityNode entityNode = (EntityNode) obj;
                            IMEntity imEntity = (IMEntity) entityNode.getUserObject();
                            ChatManager.addChat(imEntity);
                        }
                        break;
                    case MouseEvent.BUTTON2://中键
                        break;
                    case MouseEvent.BUTTON3://右键
                        if (obj instanceof CategoryNode) {
                            CategoryNode categoryNode = (CategoryNode) obj;
                            final String categoryName = categoryNode.getCategory().getName();
                            WebPopupMenu popupMenu = new WebPopupMenu();
                            WebMenuItem itemAdd = new WebMenuItem("添加分组");
                            itemAdd.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    frame.addCategory(categoryName, sx, sy);
                                }
                            });
                            WebMenuItem itemMod = new WebMenuItem("重命名");
                            itemMod.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    frame.updCategory(categoryName, sx, sy);
                                }
                            });
                            WebMenuItem itemDel = new WebMenuItem("删除分组");
                            itemDel.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    frame.delCategory(categoryName, sx, sy);
                                }
                            });
                            popupMenu.add(itemAdd);
                            popupMenu.add(itemMod);
                            popupMenu.add(itemDel);
                            popupMenu.show(e.getComponent(), e.getX(), e.getY());
                        }
                        break;
                }
            }
        }

    }
}
