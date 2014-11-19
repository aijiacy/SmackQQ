package com.silkroad.ui.bean.content;

/**
 * 聊天文本信息
 *
 * Project  : iqq-projects
 * Author   : 承∮诺 < 6208317@qq.com >
 * Created  : 14-5-15
 * License  : Apache License 2.0
 */
public class UITextItem implements UIContentItem {
    private String text;

    public UITextItem(String text) {
        this.text = text;
    }

    @Override
    public UIContentType getType() {
        return UIContentType.TEXT;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
