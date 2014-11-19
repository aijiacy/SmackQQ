package org.silkroad.im;

import com.silkroad.core.bean.IMBuddy;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caoyong on 2014/7/6.
 */
public class TestIOC {

    @Test
    public void testNutIoc() {
        //I18nService i18n = IMContext.me().getBean(I18nServiceImpl.class);
        //String directory = i18n.getI18nDirectory();

        //UIToAction uiToAction = IMContext.me().getBean(UIToAction.class);
        List<IMBuddy> imBuddies = new ArrayList<IMBuddy>();
        IMBuddy imBuddy = new IMBuddy();
        imBuddy.setUsername("11111111111111");
        imBuddies.add(imBuddy);

        IMBuddy chgBuddy = null;
        for (IMBuddy buddy : imBuddies){
            chgBuddy = buddy;
        }

        chgBuddy.setUsername("22222222222");

        System.out.println(chgBuddy.getUsername());
        for (IMBuddy buddy : imBuddies){
            System.out.println(buddy.getUsername());
        }

    }

}
