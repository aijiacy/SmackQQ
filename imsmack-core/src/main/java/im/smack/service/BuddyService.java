package im.smack.service;

import im.smack.IMSmkException;
import im.smack.bean.IMSmkBuddy;
import im.smack.bean.IMSmkCategory;
import im.smack.core.IMSmkSession;
import im.smack.core.IMSmkStore;
import im.smack.socket.IMSmkSocketListener;
import im.smack.socket.IMSmkSocketRequest;
import im.smack.socket.IMSmkSocketResponse;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 获取好友分组
 */
public class BuddyService extends AbstractService implements IMSmkSocketService {
    private static final Logger LOG = LoggerFactory.getLogger(BuddyService.class);
    private ExecutorService exec;


	@Override
	public Future<IMSmkSocketResponse> executeHttpRequest(IMSmkSocketRequest request, final IMSmkSocketListener listener)
			throws IMSmkException {
        // TODO 这里实现Smack的获取好友分组请求
        LOG.debug("执行服务器好友分组请求" + request.getType());
        exec = Executors.newSingleThreadExecutor();
        Future<IMSmkSocketResponse> futureResponse = null;
        switch (request.getType()){
            case BUDDY_LIST:
                futureResponse = (Future<IMSmkSocketResponse>) exec.submit(new Runnable() {
                    @Override
                    public void run() {
                        IMSmkSocketResponse response = new IMSmkSocketResponse(IMSmkSocketResponse.S_OK, "Success");
                        try {

                            IMSmkSession session = getContext().getSession();
                            Roster roster = session.getConnection().getRoster();

                            Collection<RosterGroup> rosterGroups = roster.getGroups();
                            Iterator<RosterGroup> rosterGroupIterator = rosterGroups.iterator();

                            IMSmkStore store = getContext().getStore();

                            while (rosterGroupIterator.hasNext()){
                                RosterGroup rosterGroup = rosterGroupIterator.next();
                                IMSmkCategory category= new IMSmkCategory();
                                category.setIndex(rosterGroup.hashCode());
                                category.setName(rosterGroup.getName());
                                LOG.debug("分组名称：" + rosterGroup.getName());
                                Collection<RosterEntry> rosterEntries = rosterGroup.getEntries();
                                for (RosterEntry entry : rosterEntries){

                                    IMSmkBuddy buddy = new IMSmkBuddy();
                                    buddy.setCategory(category);
                                    buddy.setUsername(entry.getUser());
                                    buddy.setNickName(entry.getName());
                                    category.getBuddyList().add(buddy);
                                }
                                store.addCategory(category);
                            }
                            listener.onSocketFinish(response);
                        }catch (Exception ex){
                            listener.onSocketError(new IMSmkException(IMSmkException.IMErrorCode.UNKNOWN_ERROR,"获取分组信息异常!"));
                            LOG.error("ERROR:", ex);
                        }

                    }
                });
                break;
        }
		return futureResponse;
	}

}
