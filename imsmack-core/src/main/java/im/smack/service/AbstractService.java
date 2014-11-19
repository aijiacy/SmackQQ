package im.smack.service;

import im.smack.IMSmkException;
import im.smack.core.IMSmkContext;
import im.smack.core.IMSmkService;

/**
 * 抽象的服务类，实现了部分接口，方便子类实现
 * @author CaoYong
 *
 */
public class AbstractService implements IMSmkService {

	private IMSmkContext context;

	@Override
	public void init(IMSmkContext context) throws IMSmkException {
		this.context = context;
	}

	@Override
	public void destroy() throws IMSmkException {
	}

	public IMSmkContext getContext() {
		return context;
	}
}
