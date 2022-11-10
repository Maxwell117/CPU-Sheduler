//Maxwell Large
import java.util.Scanner;

public class Shell {
	Scheduler scheduler;

	public Shell(int memorySize, int diskCount) {
		this.scheduler = new Scheduler(memorySize, diskCount);
		runSimulation();
	}

	public void runSimulation() {
		Scanner in = new Scanner(System.in);
		System.out.println(">");
		while (in.hasNextLine()) {//get input without asking
			String command = in.nextLine();
			command = command.trim();//hand extra spaces
			try {
				if (command.startsWith("A ")) {
					String line = command.substring(2).trim();
					int size = Integer.parseInt(line);
					scheduler.createProcess(size);

				} else if (command.startsWith("AR ")) {
					String line = command.substring(3).trim();
					int size = Integer.parseInt(line);
					scheduler.createRealTimeProcess(size);
				} else if (command.startsWith("Q")) {
					scheduler.endTimeSlice();
				} else if (command.startsWith("t")) {
					scheduler.terminateRunningProcess();
				} else if (command.startsWith("d ")) {
					String line = command.substring(2).trim();
					int diskNumber = Integer.parseInt(line);
					scheduler.requestDisk(diskNumber);
				} else if (command.startsWith("D ")) {
					String line = command.substring(2).trim();
					int diskNumber = Integer.parseInt(line);
					scheduler.completeDiskActivity(diskNumber);
				} else if (command.startsWith("S r")) {
					scheduler.displayRunningStatus();
				} else if (command.startsWith("S i")) {
					scheduler.displayDiskStatus();
				} else if (command.startsWith("S m")) {
					scheduler.displayMemoryState();
				}
			} catch (Exception e) {//wrong command
				e.printStackTrace();
			}
			System.out.println(">");
		}

	}

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.println("How much RAM memory is there on the simulated computer?");
		int memorySize = in.nextInt();
		System.out.println("How many hard disks does the simulated computer have?");
		int diskCount = in.nextInt();
		new Shell(memorySize, diskCount);//runs simulations
	}

}
