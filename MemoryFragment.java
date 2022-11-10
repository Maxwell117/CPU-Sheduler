//Maxwell Large
public class MemoryFragment {
	
	private int startingLocation;
	private int fragmentSize;
	
	public MemoryFragment(int startingLocation, int fragmentSize) {
		this.startingLocation = startingLocation;
		this.fragmentSize = fragmentSize;
	}

	public int getStartingLocation() {
		return startingLocation;
	}

	public int getFragmentSize() {
		return fragmentSize;
	}


	public void setStartingLocation(int startingLocation) {
		this.startingLocation = startingLocation;
	}

	public void setFragmentSize(int fragmentSize) {
		this.fragmentSize = fragmentSize;
	}

	@Override
	public String toString() {//print in this format
		return String.format("MemoryFragment [startingLocation=%s, fragmentSize=%s]", startingLocation, fragmentSize);
	}



}
