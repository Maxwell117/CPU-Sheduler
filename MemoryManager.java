//Maxwell Large
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class MemoryManager {

	private List<MemoryFragment> freeList = new LinkedList<>();

	public MemoryManager(int memorySize) {
		freeList.add(new MemoryFragment(0, memorySize));

	}

	
	/**
	 * Allocate a memory fragment from free list.
	 * @param size
	 * @return
	 */
	public MemoryFragment allocate(int size) {
		
		// Find first free memory fragment satisfying the size requirement
		MemoryFragment fragment = null;
		for(MemoryFragment f : freeList) {
			if (f.getFragmentSize() > size) {
				fragment = f;
				break;
			}
		}
		
		// If we did not find a fragment, just return
		if (fragment == null) {
			return null;
		}
		
		else if (fragment.getFragmentSize() == size) {
			// if the fragment is of exact size, remove it from free list and return
			freeList.remove(fragment);
			return fragment;
		} else {
			// selected fragment is bigger. 
			// Split it into two parts - 1st part from beginning which is allocated
			int startingLocation = fragment.getStartingLocation();
			MemoryFragment newFragment = new MemoryFragment(startingLocation, size);
			fragment.setStartingLocation(startingLocation + size);
			fragment.setFragmentSize(fragment.getFragmentSize() - size);
			return newFragment;
		}
		
	}

	public void release (MemoryFragment fragment) {
		int index = 0;
		int startingLocation = fragment.getStartingLocation();
		for(MemoryFragment f : freeList) {
			if (f.getStartingLocation() > startingLocation) {
				break;
			}
			index++;//increase index
		}
		freeList.add(index, fragment);
		
		
	}

}
