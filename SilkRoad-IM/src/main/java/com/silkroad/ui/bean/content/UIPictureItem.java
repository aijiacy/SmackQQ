package com.silkroad.ui.bean.content;

import java.io.File;

/**
 * 聊天图片信息
 *
 * Project  : iqq-projects
 * Author   : 承∮诺 < 6208317@qq.com >
 * Created  : 14-5-15
 * License  : Apache License 2.0
 */
public class UIPictureItem implements UIContentItem {
    private File file;

    @Override
    public UIContentType getType() {
        return UIContentType.PIC;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
