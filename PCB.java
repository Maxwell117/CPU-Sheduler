//Maxwell Large
public class PCB {
	private int pid;
	private MemoryFragment memoryFragment;
	private boolean realTime;
	
	
	public PCB(int pid, MemoryFragment memoryFragment, boolean realTime) {
		this.pid = pid;
		this.memoryFragment = memoryFragment;
		this.realTime = realTime;
	}


	public int getPid() {
		return pid;
	}


	public MemoryFragment getMemoryFragment() {
		return memoryFragment;
	}


	public boolean isRealTime() {
		return realTime;
	}


	@Override
	public String toString() {
		return String.format("PCB [pid=%s, memoryFragment=%s, realTime=%s]", pid, memoryFragment, realTime);
	}

}
