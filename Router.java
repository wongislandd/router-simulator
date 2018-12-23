// Christopher Wong (#111386693)
// CSE 214
import java.util.*;
public class Router {
	private static int maxBufferSize = 0;
	Queue<Packet> routerQ= new LinkedList<Packet>();
	/**
	 * Setter for maxBufferSize variable
	 * @param max
	 */
	public static void setMaxBufferSize(int max) {
		maxBufferSize = max;
	}
	/**
	 * Getter for maxBufferSize variable
	 * @return
	 */
	public static int getMaxBufferSize() {
		return (maxBufferSize);
	}
	/**
	 * Default constructor for router.
	 */
	public Router() {

	}
	/**
	 * Adds a packet to the back of the queue.
	 * @param p
	 */
	public void enqueue(Packet p) {
		routerQ.add(p);
	}
	/**
	 * removes the first packet in the router queue
	 * @returns the first packet in the router queue
	 */
	public Packet dequeue() {
		return (routerQ.remove());
	}
	/**
	 * returns but does not remove the first packet in the router queue.
	 * @return the first packet
	 */
	public Packet peek() {
		return(routerQ.peek());
	}
	/**
	 * 
	 * @returns the amount of packets in the router
	 */
	public int size() {
		return (routerQ.size());
	}
	/**
	 * Checks if the router is empty.
	 * @return true if no packets are in the router, false if at least 1 exists.
	 */
	public boolean isEmpty() {
		return (routerQ.isEmpty());
	}
	/**
	 * Pushes packet p to the front of the queue, used later when decrementing timeToDest later.
	 * @param p
	 */
	public void queueAtFront(Packet p) { // puts the packet at the front of the queue, used for later
		enqueue(p);
		for (int i=1;i<size();i++) {
			enqueue(dequeue());
		}
	}
	/**
	 * Returns a string representation of all the packets in the router in a neat format.
	 */
	public String toString() {
		String s = ("{" + routerQ + "}");
		return s;
	}
	/**
	 * Finds the most available (least packets) router and returns the index in the given ArrayList routerArray
	 * @param routerArray
	 * @return
	 * @throws FullBufferException
	 */
	public static int sendPacketTo(ArrayList<Router> routerArray) throws FullBufferException {
		int lowestSize = maxBufferSize;
		int lowestRouter = -1;
		for (int j=0;j<routerArray.size();j++) {
			if (routerArray.get(j).size() < lowestSize) { // finds the lowest buffer size
				lowestSize = routerArray.get(j).size();
			}
		}
		if (lowestSize == maxBufferSize) {
			throw new FullBufferException();
		}
		for (int j=0;lowestRouter == -1;j++) {
			if (routerArray.get(j).size() == lowestSize) { // finds the lowest router number
				lowestRouter = j;
			}
		}
		return lowestRouter;
		}	
}
class FullBufferException extends Exception{
	public FullBufferException() {
		System.out.print("Network is congested. ");
	}
}

