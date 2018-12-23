// Christopher Wong (#111386693)
// CSE 214
public class Packet {
	static int packetCount = 0;
	int id, packetSize, timeArrive, timeToDest;
	/**
	 * Constructor for a packet, whenever a packet is made the class's count is increased and the packet made assumes the count as its ID.
	 */
	public Packet() {
		packetCount++;
		id = packetCount;
	}
	/**
	 * Creates a packet with parameters, whenever a packet is made the class's count is increased and the packet made assumes the count as its ID.
	 * @param timeArrive
	 * @param packetSize
	 * @param timeToDest
	 */
	public Packet(int timeArrive, int packetSize, int timeToDest) {
		packetCount++;
		id = packetCount;
		this.timeArrive = timeArrive;
		this.packetSize = packetSize;
		this.timeToDest = timeToDest;
	}
	/**
	 * @return the packetCount
	 */
	public static int getPacketCount() {
		return packetCount;
	}
	/**
	 * @param packetCount the packetCount to set
	 */
	public static void setPacketCount(int packetCount) {
		Packet.packetCount = packetCount;
	}
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the packetSize
	 */
	public int getPacketSize() {
		return packetSize;
	}
	/**
	 * @param packetSize the packetSize to set
	 */
	public void setPacketSize(int packetSize) {
		this.packetSize = packetSize;
	}
	/**
	 * @return the timeArrive
	 */
	public int getTimeArrive() {
		return timeArrive;
	}
	/**
	 * @param timeArrive the timeArrive to set
	 */
	public void setTimeArrive(int timeArrive) {
		this.timeArrive = timeArrive;
	}
	/**
	 * @return the timeToDest
	 */
	public int getTimeToDest() {
		return timeToDest;
	}
	/**
	 * @param timeToDest the timeToDest to set
	 */
	public void setTimeToDest(int timeToDest) {
		this.timeToDest = timeToDest;
	}
	public String toString() {
		return ("["+ id + ", " + timeArrive + ", " + timeToDest+"]");
	}
}
