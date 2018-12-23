// Christopher Wong (#111386693)
// CSE 214
import java.util.*;
public class Simulator {
	Router dispatcher = new Router();
	Router destination = new Router();
	ArrayList<Router> routers = new ArrayList<Router>();
	int totalServiceTime, totalPacketsArrived, packetsDropped = 0, numIntRouters,
		maxBufferSize, minPacketSize, maxPacketSize, bandwidth, duration, destinationSpace;
	double arrivalProb;
	/**
	 * A constructor with all needed parameters to begin a simulation.
	 * @param numIntRouters
	 * @param arrivalProb
	 * @param maxBufferSize
	 * @param minPacketSize
	 * @param maxPacketSize
	 * @param bandwidth
	 * @param duration
	 */
	public Simulator(int numIntRouters, double arrivalProb, int maxBufferSize,
			int minPacketSize, int maxPacketSize, int bandwidth, int duration) {
		this.numIntRouters = numIntRouters;
		this.arrivalProb = arrivalProb;
		this.maxBufferSize = maxBufferSize;
		Router.setMaxBufferSize(maxBufferSize);
		this.minPacketSize = minPacketSize;
		this.maxPacketSize = maxPacketSize;
		this.bandwidth = bandwidth;
		this.duration = duration;
	}
	/**
	 * Helper method to generate a random number between two given values.
	 * @param minVal
	 * @param maxVal
	 * @return a random int between minVal and maxVal
	 */
	public int randInt(int minVal, int maxVal) {
		int difference = maxVal - minVal;
		return ((int)(Math.random()*difference + minVal +1));
	}
	/**
	 * simulation simulates a routing network where packets have a set chance (determined by the user) to arrive at a dispatcher then be dispersed to a network of routers
	 * where it is then processed and, once ready, is sent to the destination. This continues with given parameters.
	 * simulation tracks the process and prints out each step.
	 * @returns the average service time per packet
	 */
	public double simulation() {
		int savedSpot = 0;
		for	(int r=0;r<numIntRouters;r++) {
			routers.add(new Router()); // adds the intermediate routers
		}
		try {
		for (int i = 1;i<=duration;i++) { // the simulation time
			destinationSpace = bandwidth;
			System.out.println("Time: " + i);
			for (int j=0;j<3;j++) { // starting packets at the dispatcher (max of 3)
				int roll = randInt(0,10);
				if (roll <= arrivalProb*10) {
					int packetSize = randInt(minPacketSize, maxPacketSize);
					Packet newPacket = new Packet(i, packetSize, packetSize / 100);
					System.out.println("Packet "+ newPacket.getId()+ " arrives at dispatcher with size " + newPacket.getPacketSize() + ".");
					dispatcher.enqueue(newPacket);
				}
			}
				if (dispatcher.isEmpty()) {
					System.out.println("No packets arrived.");
				}
			while (!dispatcher.isEmpty()) {
				try {
				int routerNumber = (Router.sendPacketTo(routers)) +1;
				System.out.println("Packet " + dispatcher.peek().getId() + " sent to Router " + routerNumber + "."); // this could be bad bc it assumes it goes through to one of them
				routers.get(Router.sendPacketTo(routers)).enqueue(dispatcher.dequeue()); // sends packet from dispatcher to least full router
				}
				catch (FullBufferException o) {
					System.out.println("Packet " + dispatcher.peek().getId() + " is dropped.");
					dispatcher.dequeue();
					packetsDropped++;
					continue;
				}
			}
			for (int o=savedSpot;o<routers.size()+savedSpot;o++) {
				if (o == routers.size()) { // basically an exception for if the savedSpot is above bounds then it resets to 0
					savedSpot = 0;
					break;
				}
				if (!routers.get(o).isEmpty()) {
					Packet changingPacket = routers.get(o).dequeue();
					changingPacket.setTimeToDest(changingPacket.getTimeToDest() - 1);
					routers.get(o).queueAtFront(changingPacket); // decrements the timeToArrive of all packets currently being processed by the routers, then adds it back to the front.
						if (routers.get(o).peek().getTimeToDest() <= 0) {
							if (destinationSpace !=0) {
								Packet arrivedPacket = routers.get(o).dequeue();
								System.out.println("Packet " +arrivedPacket.getId() + " has successfully reached its destination: +" + (i-arrivedPacket.getTimeArrive()));
								totalServiceTime += (i-arrivedPacket.getTimeArrive());
								totalPacketsArrived++;
								destinationSpace--;
								savedSpot = o+1; // so it starts the search from the router after the one that just sent. "FAIRNESS"
							}
							else { // more fairness
								Packet delayedPacket = routers.get(o).dequeue();
								delayedPacket.setTimeToDest(delayedPacket.getTimeToDest() + 1);
								routers.get(o).queueAtFront(delayedPacket);
							}
						}
				}
			}
			for (int r=1; r<routers.size()+1;r++) { // prints out the routers status
				System.out.println("R" + r + ": " + routers.get(r-1).toString());
			}
			System.out.println("\n \n");
		}
		}
		catch(Exception ex) {
			System.out.println("Something went wrong!");
		}
		double avgServiceTime = 0;
		if (totalPacketsArrived != 0)
			avgServiceTime = (double)totalServiceTime/(double)totalPacketsArrived;
		else
			System.out.println("The total packets arrived is 0 and there is no average service time.");
		return avgServiceTime;
	}
	/**
	 * Test client for simulator.
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		int numIntRouters, maxBufferSize, minPacketSize, maxPacketSize, bandwidth, duration;
		double arrivalProb;
		boolean cont = true;
		System.out.println("Starting simulator. . . ");
		while (cont) {
			try {
			System.out.print("Enter the number of Intermediate routers: ");
			numIntRouters = input.nextInt();
			input.nextLine();
			System.out.print("\nEnter the arrival probability of a packet: ");
			arrivalProb = input.nextDouble();
			input.nextLine();
			System.out.print("\nEnter the maximum buffer size of a router: ");
			maxBufferSize = input.nextInt();
			input.nextLine();
			System.out.print("\nEnter the minimum size of a packet: ");
			minPacketSize = input.nextInt();
			input.nextLine();
			System.out.print("\nEnter the maximum size of a packet: ");
			maxPacketSize = input.nextInt();
			input.nextLine();
			System.out.print("\nEnter the bandwidth size: ");
			bandwidth = input.nextInt();
			input.nextLine();
			System.out.print("\nEnter the simulation duration: ");
			duration = input.nextInt();
			input.nextLine();
			Simulator mySim = new Simulator(numIntRouters, arrivalProb, maxBufferSize,
				minPacketSize, maxPacketSize, bandwidth, duration);
			//Simulator mySim = new Simulator(4,.5,10,500,1500,1,25);
			double avgServiceTime = mySim.simulation();
			System.out.println("Simulation ending. . .");
			System.out.println("Total service time: "+ mySim.totalServiceTime);
			System.out.println("Total packets served: " + mySim.totalPacketsArrived);
			System.out.println("Average service time per packet: " + avgServiceTime);
			System.out.println("Total packets dropped: " + mySim.packetsDropped);
			System.out.print("\nDo you want to try another simulation? (y/n): ");
			String answer = input.nextLine();
			if (answer.equals("n")) {
				cont = false;
				System.out.println("\nProgram terminating successfully. . .");
				input.close();
			}
			}
			catch(Exception ex) {
				System.out.println("That's an invalid input, please try again.");
				input.nextLine();
				continue;
			}
		}
	}
}
