package com.silkroad.core.manager;

import com.silkroad.core.bean.*;
import com.silkroad.core.bean.args.IMSocketResponse;
import com.silkroad.core.context.IMSession;
import com.silkroad.core.event.IMEventType;
import com.silkroad.core.event.IMSocketListener;
import com.silkroad.core.exception.IMException;
import com.silkroad.core.manager.listener.IMPacketListener;
import com.silkroad.core.socket.SmackConnFactory;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.proxy.ProxyInfo;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.offline.OfflineMessageManager;
import org.jivesoftware.smackx.search.ReportedData;
import org.jivesoftware.smackx.search.ReportedData.Row;
import org.jivesoftware.smackx.search.UserSearchManager;
import org.jivesoftware.smackx.xdata.Form;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by caoyong on 2014/7/7.
 */
public final class SmackManager extends SmackConnFactory {
    private static final Logger Log = LoggerFactory.getLogger(SmackManager.class);
    private static SmackManager instance = null;
    private ExecutorService executorService;

    private ProxyInfo proxy = null;

    public enum LoginType {
        LOGIN_SUCCESS(),
        LOGIN_AUTH_ERROR,
        LOGIN_IO_ERROR,
        OTHER_ERROR
    }

    private SmackManager() {
        executorService = Executors.newSingleThreadExecutor();
    }

    public static SmackManager getInstance() {
        if (null == instance) {
            instance = new SmackManager();
        }
        return instance;
    }

    public void setProxyInformation(String proxyType, String proxyHost, int proxyPost, String proxyUser, String proxyPassword) {
        proxy = new ProxyInfo(ProxyInfo.ProxyType.valueOf(proxyType), proxyHost, proxyPost, proxyUser, proxyPassword);
    }

    public Future<IMSocketResponse> initServer(final IMSession session, final IMEventType actionType, final IMSocketListener listener) {
        if (null == proxy) {
            super.setUseProxy(false);
        } else {
            super.setUseProxy(true);
        }
        //super.setDebuggerEnabled(true);
        return executorService.submit(new Runnable() {
            @Override
            public void run() {
                IMSocketResponse response = null;
                try {
                    if (getConnection() == null) {
                        initConnection(session.getHost(), session.getPort(), proxy);
                    }
                    session.setConnection(getConnection());
                    session.setConnectionId(getConnection().getConnectionID());
                    response = new IMSocketResponse(0, actionType, "connected");
                    listener.onSuccess(response);
                } catch (IOException e) {
                    listener.onFailed(new IMException(IMException.IMErrorCode.IO_ERROR));
                } catch (XMPPException e) {
                    listener.onFailed(new IMException(IMException.IMErrorCode.ERROR_SOCKET_STATUS));
                } catch (SmackException e) {
                    listener.onFailed(new IMException(IMException.IMErrorCode.NOT_CONNECTED));
                }
            }
        }, new IMSocketResponse());
    }

    public Future<IMSocketResponse> loginToServer(final IMAccount account, final IMEventType actionType, final IMSocketListener listener) {
        return executorService.submit(new Callable<IMSocketResponse>() {
            @Override
            public IMSocketResponse call() throws Exception {
                IMSocketResponse response = null;
                try {
//                  String uJid = account.getUsername()+"@"+session.getHost() + "/" + account.getClientRes();
//                  Log.debug(uJid);
                    getConnection().login(account.getUsername(), account.getPassword(), account.getClientRes());
                    String jid = getConnection().getUser();
                    /**
                     * TODO 如果登陆成功，直接获取个人信息
                     */
                    Roster rst = getConnection().getRoster();
                    //RosterEntry rosterEntry = rst.getEntry(getConnection().getUser());
                    Presence presence = rst.getPresence(getConnection().getUser());
                    account.setJid(jid);
                    //account.setNickName(rosterEntry.getName());
                    switch (presence.getType()) {
                        case unavailable:
                            account.setStatus(IMStatus.OFFLINE);
                            break;
                        default:
                            switch (presence.getMode()) {
                                case available:
                                    account.setStatus(IMStatus.ONLINE);
                                    break;
                                case chat:
                                    account.setStatus(IMStatus.CALLME);
                                    break;
                                case away:
                                    account.setStatus(IMStatus.HIDDEN);
                                    break;
                                case dnd:
                                    account.setStatus(IMStatus.BUSY);
                                    break;
                                case xa:
                                    account.setStatus(IMStatus.AWAY);
                                    break;
                            }
                            break;
                    }
                    response = new IMSocketResponse(0, actionType, account);
                    listener.onSuccess(response);
                } catch (XMPPException e) {
                    Log.error("XMPP_ERROR:", e);
                    if (e instanceof XMPPException.StreamErrorException) {
                        listener.onFailed(new IMException(IMException.IMErrorCode.IO_ERROR));
                    } else if (e instanceof XMPPException.XMPPErrorException) {
                        listener.onFailed(new IMException(IMException.IMErrorCode.INVALID_RESPONSE));
                    }
                } catch (SmackException e) {
                    Log.error("SMACK_ERROR:", e);
                    if (e instanceof SmackException.ConnectionException) {
                        listener.onFailed(new IMException(IMException.IMErrorCode.INVALID_CONNECTION));
                    } else if (e instanceof SmackException.NotConnectedException) {
                        listener.onFailed(new IMException(IMException.IMErrorCode.NOT_CONNECTED));
                    } else if (e instanceof SmackException.AlreadyLoggedInException) {
                        listener.onFailed(new IMException(IMException.IMErrorCode.USER_IS_LOGGEDIN));
                    } else if (e instanceof SmackException.NotLoggedInException) {
                        listener.onFailed(new IMException(IMException.IMErrorCode.USER_NOT_LOGGEDIN));
                    } else if (e instanceof SmackException.FeatureNotSupportedException) {
                        listener.onFailed(new IMException(IMException.IMErrorCode.FEATURE_NOT_SUPPORTED));
                    } else if (e instanceof SmackException.NoResponseException) {
                        listener.onFailed(new IMException(IMException.IMErrorCode.INVALID_RESPONSE));
                    } else if (e instanceof SmackException.SecurityRequiredException) {
                        listener.onFailed(new IMException(IMException.IMErrorCode.UNEXPECTED_RESPONSE));
                    } else if (e instanceof SmackException.IllegalStateChangeException) {
                        listener.onFailed(new IMException(IMException.IMErrorCode.INVALID_STATE_CHANGE));
                    } else if (e instanceof SmackException.ResourceBindingNotOfferedException) {
                        listener.onFailed(new IMException(IMException.IMErrorCode.UNKNOWN_RES_BINDING));
                    }
                } catch (IOException e) {
                    Log.error("IO_ERROR:", e);
                    listener.onFailed(new IMException(IMException.IMErrorCode.IO_ERROR));
                }
                return response;
            }
        });
    }

    public Future<IMSocketResponse> doGetBuddyList(final IMEventType actionType, final IMSocketListener listener) {
        return executorService.submit(new Runnable() {
            @Override
            public void run() {
                List<IMCategory> imCategories = new LinkedList<IMCategory>();
                Roster rst = getConnection().getRoster();
                Collection<RosterGroup> rstGroups = rst.getGroups();
                for (RosterGroup rstGroup : rstGroups) {
                    IMCategory imBuddyCategory = new IMCategory();
                    List<IMBuddy> imBuddies = new LinkedList<IMBuddy>();
                    for (RosterEntry rstEntry : rstGroup.getEntries()) {
                        IMBuddy imBuddy = new IMBuddy();
                        imBuddy.setJid(rstEntry.getUser());
                        imBuddy.setUsername(StringUtils.parseName(rstEntry.getUser()));
                        imBuddy.setNickName(rstEntry.getName() == null ? imBuddy.getUsername() : rstEntry.getName());
                        Presence presence = rst.getPresence(rstEntry.getUser());
                        switch (presence.getType()) {
                            case available:
                                imBuddy.setStatus(IMStatus.ONLINE);
                                break;
                            default:
                                imBuddy.setStatus(IMStatus.OFFLINE);
                                break;
                        }
                        imBuddies.add(imBuddy);
                    }
                    imBuddyCategory.setName(rstGroup.getName());
                    imBuddyCategory.setBuddyList(imBuddies);
                    imCategories.add(imBuddyCategory);
                }

                if (rst.getUnfiledEntryCount() > 0) {
                    Collection<RosterEntry> rstEntries = rst.getUnfiledEntries();
                    IMCategory imCategory = new IMCategory();
                    imCategory.setName("Unfiled");
                    List<IMBuddy> imBuddies = new LinkedList<IMBuddy>();
                    for (RosterEntry rosterEntry : rstEntries) {
                        IMBuddy imBuddy = new IMBuddy();
                        imBuddy.setJid(rosterEntry.getUser());
                        imBuddy.setUsername(StringUtils.parseName(rosterEntry.getUser()));
                        imBuddy.setNickName(rosterEntry.getName() == null ? imBuddy.getUsername() : rosterEntry.getName());
                        Presence presence = rst.getPresence(rosterEntry.getUser());
                        switch (presence.getType()) {
                            case available:
                                imBuddy.setStatus(IMStatus.ONLINE);
                                break;
                            default:
                                imBuddy.setStatus(IMStatus.OFFLINE);
                                break;
                        }
                        imBuddies.add(imBuddy);
                    }
                    imCategory.setBuddyList(imBuddies);
                    imCategories.add(imCategory);
                }

                IMSocketResponse response = new IMSocketResponse(0, actionType, imCategories);
                listener.onSuccess(response);
            }
        }, new IMSocketResponse());
    }


    public Future<IMSocketResponse> doPollMessage(final IMEventType actionType, final IMSocketListener listener) {
        return executorService.submit(new Runnable() {
            @Override
            public void run() {
                getConnection().addPacketListener(new IMPacketListener(actionType, listener), new PacketFilter() {
                    @Override
                    public boolean accept(Packet packet) {
                        return true;
                    }
                });
            }
        }, new IMSocketResponse());
    }

    public Future<IMSocketResponse> doGetOfflineMessage(final IMEventType actionType, final IMSocketListener listener) {
        return executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    OfflineMessageManager offlineMessageManager = new OfflineMessageManager(getConnection());
                    List<Message> offMessages = offlineMessageManager.getMessages();
                    List<IMMsg> imMsgs = new LinkedList<IMMsg>();
                    for (Message msg : offMessages) {
                        Log.debug(msg.getBody());
                    }
                    offlineMessageManager.deleteMessages();
                    Presence presence = new Presence(Presence.Type.available);
                    presence.setStatus("Online");
                    getConnection().sendPacket(presence);
                    IMSocketResponse response = new IMSocketResponse(0, actionType, imMsgs);
                    listener.onSuccess(response);
                } catch (XMPPException e) {
                    if (e instanceof XMPPException.XMPPErrorException) {
                        listener.onFailed(new IMException(IMException.IMErrorCode.INVALID_RESPONSE));
                    }
                    listener.onFailed(new IMException(IMException.IMErrorCode.UNKNOWN_ERROR));
                } catch (SmackException e) {
                    Log.error("SMACK_ERROR:", e);
                    if (e instanceof SmackException.NotConnectedException) {
                        listener.onFailed(new IMException(IMException.IMErrorCode.NOT_CONNECTED));
                    }
                    listener.onFailed(new IMException(IMException.IMErrorCode.UNKNOWN_ERROR));
                }
            }
        }, new IMSocketResponse());
    }

    public Future<IMSocketResponse> processChatMessage(final IMMsg imMsg, final IMEventType actionType, final IMSocketListener listener) {
        return executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    ChatManager chatManager = ChatManager.getInstanceFor(getConnection());
                    Chat chat = chatManager.getThreadChat(imMsg.getThread());
                    if (chat != null) {
                        chat.sendMessage(imMsg);
                    } else {
                        chat = chatManager.createChat(imMsg.getTo(), imMsg.getThread(), new MessageListener() {
                            @Override
                            public void processMessage(Chat chat, Message message) {
//                                try {
//                                    chat.sendMessage(message);
//                                } catch (SmackException.NotConnectedException e) {
//                                    e.printStackTrace();
//                                }
                            }
                        });
                        chat.sendMessage(imMsg);
                    }
                    listener.onSuccess(new IMSocketResponse(0, actionType, imMsg));
                } catch (SmackException e) {
                    listener.onSuccess(new IMSocketResponse(1, actionType, imMsg));
                    e.printStackTrace();
                } catch (Exception e) {
                    listener.onSuccess(new IMSocketResponse(1, actionType, imMsg));
                    e.printStackTrace();
                }
            }
        }, new IMSocketResponse());
    }

    public Future<IMSocketResponse> createAccount(final IMAccount account, final IMEventType actionType, final IMSocketListener listener) {
        return executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    AccountManager accountManager = AccountManager.getInstance(getConnection());
                    if (accountManager.supportsAccountCreation()) {
                        accountManager.createAccount(account.getUsername(), account.getPassword());
                        listener.onSuccess(new IMSocketResponse(0, actionType, "reg success"));
                    } else {
                        listener.onFailed(new IMException(IMException.IMErrorCode.FEATURE_NOT_SUPPORTED));
                    }
                } catch (SmackException.NoResponseException e) {
                    listener.onFailed(new IMException(IMException.IMErrorCode.INVALID_RESPONSE, e));
                } catch (XMPPException.XMPPErrorException e) {
                    listener.onFailed(new IMException(IMException.IMErrorCode.ERROR_SOCKET_STATUS, e));
                } catch (SmackException.NotConnectedException e) {
                    listener.onFailed(new IMException(IMException.IMErrorCode.NOT_CONNECTED, e));
                }
            }
        }, new IMSocketResponse());
    }

    public Future<IMSocketResponse> searchUser(final String username, final IMEventType actionType, final IMSocketListener listener) {
        return executorService.submit(new Runnable() {
            @Override
            public void run() {
                UserSearchManager userSearchManager = new UserSearchManager(getConnection());
                String serviceDomain = "search." + getConnection().getServiceName();
                try {
                    Form searchForm = userSearchManager.getSearchForm(serviceDomain);
                    Form answerForm = searchForm.createAnswerForm();
                    answerForm.setAnswer("Username", true);
                    answerForm.setAnswer("search", username);
                    ReportedData reportedData = userSearchManager.getSearchResults(answerForm, serviceDomain);
                    Iterator<Row> itRows = reportedData.getRows().iterator();
                    Row row = null;
                    IMUser imUser = null;
                    while (itRows.hasNext()) {
                        row = itRows.next();
                        imUser = new IMUser();
                        imUser.setJid(row.getValues("jid").get(0).toString());
                        imUser.setNickName(row.getValues("Name").get(0).toString());
                    }
                    listener.onSuccess(new IMSocketResponse(0, actionType, imUser));
                } catch (SmackException.NoResponseException e) {
                    listener.onFailed(new IMException(IMException.IMErrorCode.INVALID_RESPONSE, e));
                } catch (XMPPException.XMPPErrorException e) {
                    listener.onFailed(new IMException(IMException.IMErrorCode.ERROR_SOCKET_STATUS, e));
                } catch (SmackException.NotConnectedException e) {
                    listener.onFailed(new IMException(IMException.IMErrorCode.NOT_CONNECTED, e));
                }
            }
        }, new IMSocketResponse());
    }

    public Future<IMSocketResponse> addBuddy(final String jid, final String uname, final String groupName, final IMEventType actionType, final IMSocketListener listener) {
        return executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
//                    Presence presence = new Presence(Presence.Type.subscribe);
//                    presence.setFrom(getConnection().getUser());
//                    presence.setTo(jid);
//                    getConnection().sendPacket(presence);
                    Roster roster = getConnection().getRoster();
                    roster.createEntry(jid, uname, new String[]{groupName});
                    listener.onSuccess(new IMSocketResponse(0, actionType, "success"));
                } catch (SmackException.NotConnectedException e) {
                    listener.onFailed(new IMException(IMException.IMErrorCode.NOT_CONNECTED, e));
                }
                catch (SmackException.NotLoggedInException e) {
                    listener.onFailed(new IMException(IMException.IMErrorCode.USER_NOT_LOGGEDIN, e));
                } catch (XMPPException.XMPPErrorException e) {
                    listener.onFailed(new IMException(IMException.IMErrorCode.ERROR_SOCKET_STATUS, e));
                } catch (SmackException.NoResponseException e) {
                    listener.onFailed(new IMException(IMException.IMErrorCode.INVALID_RESPONSE, e));
                }
            }
        }, new IMSocketResponse());
    }

    public void addBuddy(String jid, String uname, String groupName) {
        try {
            Roster roster = getConnection().getRoster();
            roster.createEntry(jid, uname, new String[]{groupName});
        } catch (SmackException.NotLoggedInException e) {
            e.printStackTrace();
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }

    public Future<IMSocketResponse> addedBuddy(final String jid, final String uname, final String groupName, final IMEventType actionType, final IMSocketListener listener) {
        return executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
//                    Presence presence = new Presence(Presence.Type.subscribed);
//                    presence.setFrom(getConnection().getUser());
//                    presence.setTo(jid);
//                    getConnection().sendPacket(presence);
                    Roster roster = getConnection().getRoster();
                    RosterEntry rosterEntry = roster.getEntry(jid);
                    rosterEntry.setName(uname);
                    RosterGroup rosterGroup = roster.getGroup(groupName);
                    if (rosterGroup == null) {
                        rosterGroup = roster.createGroup(groupName);
                    }
                    rosterGroup.addEntry(rosterEntry);
                    listener.onSuccess(new IMSocketResponse(0, actionType, "success"));
                } catch (SmackException.NotConnectedException e) {
                    listener.onFailed(new IMException(IMException.IMErrorCode.NOT_CONNECTED, e));
                } catch (XMPPException.XMPPErrorException e) {
                    listener.onFailed(new IMException(IMException.IMErrorCode.ERROR_SOCKET_STATUS, e));
                } catch (SmackException.NoResponseException e) {
                    listener.onFailed(new IMException(IMException.IMErrorCode.INVALID_RESPONSE, e));
                }
            }
        }, new IMSocketResponse());
    }
}
