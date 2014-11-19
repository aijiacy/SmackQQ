package com.silkroad.ui.renderer.node;

import com.alee.laf.label.WebLabel;
import com.silkroad.ui.bean.UICategory;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * 好友分类、聊天室分类显示节点
 *
 * Project  : iqq-projects
 * Author   : 承∮诺 < 6208317@qq.com >
 * Created  : 14-5-10
 * License  : Apache License 2.0
 */
public class CategoryNode extends DefaultMutableTreeNode {
    private UICategory category;
    private WebLabel view = new WebLabel();

    public CategoryNode(UICategory category) {
        super();
        this.category = category;

        view.setMargin(5, 8, 5, 5);
    }

    public UICategory getCategory() {
        return category;
    }

    public void setCategory(UICategory category) {
        this.category = category;
    }

    /**
     * 这个方法特别频繁，一定要处理好
     *
     * @return
     */
    public WebLabel getView() {
        if(!view.getText().equals(category.getName())) {
            view.setText(category.getName() + "("+category.getOnline()+"/"+category.getTotal()+")");
        }
        return view;
    }
}
