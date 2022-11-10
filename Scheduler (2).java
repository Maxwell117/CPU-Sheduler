//Maxwell Large
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class Scheduler {
	private static int NEXT_PID = 1;
	int memorySize;
	int diskCount;

	private Map<Integer, PCB> activeProcesses = new LinkedHashMap<>();
	private Deque<PCB> commonReadyQueue = new LinkedList<PCB>();
	private Deque<PCB> rtReadyQueue = new LinkedList<PCB>();
	private Deque<PCB>[] ioQueues; // processes waiting on disk
	private PCB[] processUsingDisk; // process using disk
	private MemoryManager memory;
	private PCB runningProcess = null;

	public Scheduler(int memorySize, int diskCount) {
		super();
		this.memorySize = memorySize;
		this.diskCount = diskCount;
		this.memory = new MemoryManager(memorySize);
		this.processUsingDisk = new PCB[diskCount];
		this.ioQueues = new Deque[diskCount];
		for (int i = 0; i < diskCount; i++) {
			ioQueues[i] = new LinkedList<PCB>();
		}
	}

	/**
	 * Create common process.
	 * 
	 * @param size
	 */
	public void createProcess(int size) {
		int pid = NEXT_PID++;
		// Allocate memory block.
		MemoryFragment memoryFragment = memory.allocate(size);
		// create its PCB
		PCB process = new PCB(pid, memoryFragment, false);
		// place the process in the readyqueue
		commonReadyQueue.add(process);
		activeProcesses.put(pid, process);
		if (runningProcess == null) {
			scheduleNextProcess();
		}
	}

	/**
	 * Create realtime process
	 * 
	 * @param size
	 */
	public void createRealTimeProcess(int size) {
		int pid = NEXT_PID++;
		// Allocate memory block.
		MemoryFragment memoryFragment = memory.allocate(size);
		// create its PCB
		PCB process = new PCB(pid, memoryFragment, true);
		// place the process in the readyqueue
		rtReadyQueue.add(process);
		activeProcesses.put(pid, process);
		preempt();
	}

	/**
	 * Preempt the currently running common process
	 */
	public void preempt() {
		if (runningProcess != null && !runningProcess.isRealTime()) {
			commonReadyQueue.addFirst(runningProcess);
			runningProcess = null;
			scheduleNextProcess();
		}
	}

	/**
	 * The time slice has ended for the currently running process.
	 */
	public void endTimeSlice() {
		if (runningProcess != null) {
			if (runningProcess.isRealTime()) {
				rtReadyQueue.add(runningProcess);
			} else {
				commonReadyQueue.add(runningProcess);
			}
		}
		runningProcess = null;
		scheduleNextProcess();
	}

	/**
	 * Shows the state of memory. Show the range of memory addresses used by each
	 * process in the system.
	 */
	public void displayMemoryState() {
		System.out.println("Memory Status");
		System.out.println("PID\tFragment");
		for (PCB proc : activeProcesses.values()) {
			System.out.printf("%d\t%s\n", proc.getPid(), proc.getMemoryFragment());
		}
	}

	/**
	 * currently running process terminates
	 * 
	 */
	public void terminateRunningProcess() {
		if (runningProcess != null) {
			MemoryFragment fragment = runningProcess.getMemoryFragment();
			memory.release(fragment);
			activeProcesses.remove(runningProcess.getPid());
		}
		runningProcess = null;
		scheduleNextProcess();
	}

	/**
	 * The process that currently uses the CPU requests the hard disk #number
	 * 
	 * @param diskNumber
	 */
	public void requestDisk(int diskNumber) {
		if (runningProcess != null) {
			// Suspend the active process
			// Schedule the next process on cpu
			PCB process = runningProcess;
			runningProcess = null;
			scheduleNextProcess();
			// the existing process starts using the disk if free else gets into the disks
			// waiting queue
			if (processUsingDisk[diskNumber] == null) {
				processUsingDisk[diskNumber] = process;
			} else {
				ioQueues[diskNumber].add(process);
			}
		}
	}

	/**
	 * The hard disk #number has finished the work for one process.
	 * 
	 * @param diskNumber
	 */
	public void completeDiskActivity(int diskNumber) {
		if (processUsingDisk[diskNumber] == null) {
			// Add the process using disk to CPU queue
			PCB process = processUsingDisk[diskNumber];
			if (process.isRealTime()) {
				rtReadyQueue.add(process);
			} else {
				commonReadyQueue.add(process);
			}
			// Schedule next cpu process if cpu is idle
			if (runningProcess == null) {
				scheduleNextProcess();
			}
			// if processes are waiting for the disk, allocate the disk to the first one.
			if (!ioQueues[diskNumber].isEmpty()) {
				processUsingDisk[diskNumber] = ioQueues[diskNumber].remove();
			}
		}
	}

	/**
	 * Find the next eligible process and make it running. Run common processes only
	 * if there is no RTprocesses waiting. on each level you practice round-robin
	 */
	public void scheduleNextProcess() {
		if (!rtReadyQueue.isEmpty()) {
			runningProcess = rtReadyQueue.remove();
		} else if (!commonReadyQueue.isEmpty()) {
			runningProcess = commonReadyQueue.remove();
		} else {
			runningProcess = null;
		}
	}

	public void displayRunningStatus() {
		System.out.println("Running process: ");
		System.out.println(runningProcess);
		System.out.println("Realtime Processes on Ready-Queue: ");
		if (rtReadyQueue.isEmpty()) {
			System.out.println("Empty");
		} else {
			for (PCB process : rtReadyQueue) {
				System.out.println(process);
			}
		}
		System.out.println("Common Processes on  Ready-Queue: ");
		if (commonReadyQueue.isEmpty()) {
			System.out.println("Empty");
		} else {
			for (PCB process : commonReadyQueue) {
				System.out.println(process);
			}
		}
	}

	/**
	 * Shows what processes are currently using the hard disks and what processes
	 * are waiting to use them.
	 */
	public void displayDiskStatus() {
		System.out.println("Disk Status");
		for (int i = 0; i < diskCount; i++) {
			System.out.println("Disk " + i);
			PCB activeProcess = processUsingDisk[i];
			if (activeProcess == null) {
				System.out.println("\tDisk is idle");
			} else {
				System.out.println("\tUsed by " + activeProcess);
			}
			Deque<PCB> queue = ioQueues[i];
			if (!queue.isEmpty()) {
				System.out.println("Processes waiting for the disk");
				for (PCB process : queue) {
					System.out.println("\t" + process);
				}
			}
			System.out.println();
		}
	}

}