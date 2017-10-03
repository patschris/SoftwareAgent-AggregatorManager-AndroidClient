package am.services.agent;

/**	
 * <code>AcceptBlocker</code> is used for blocking an incoming request
 * 	from an Agent/client that wishes to login to AggregatorManager until
 * 	user accept or deny this request.
 *	
 *	@author		C. Patsouras I. Venieris
 *	@version	1
 */
public class AcceptBlocker {

	/**
	 *	A flag used for blocking an incoming request
	 *	until user accept/deny the client.
	 */
	private boolean accept;

	/**	Sets the accept flag to the given value.
	 * 	Releases this object so answer is sent to Agent/client.
	 *	
	 *	@param	accept	boolean value for accept value.
	 */
	public void setAccept(boolean accept) {
		synchronized(this) {
			this.accept = accept;
			this.notify();
		}
	}

	/**	Waits on an object until user accepts or deny
	 * 	an incoming request.
	 *	
	 *	@return	value of the accept flag.
	 */
	public boolean getAccept() {
		synchronized(this){
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return accept;
	}
}