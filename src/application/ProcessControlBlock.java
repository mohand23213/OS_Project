package application;
public class ProcessControlBlock implements Comparable<ProcessControlBlock>{
    int processId;
    int base;
    int limit;
    int size;
    int timeInMemory;
    int counter=0;

    public ProcessControlBlock(String processInfo) {
    	super();
        // Split the input string by one or more spaces
        String[] parts = processInfo.trim().split("\\s+");
        // Assign values to the class fields
        this.processId = Integer.parseInt(parts[0]);
        this.size = Integer.parseInt(parts[1]);
        this.timeInMemory = Integer.parseInt(parts[2]);
        
        // Set counter to the value of timeInMemory
        
    }
    ProcessControlBlock(ProcessControlBlock copy){
    	this.processId=copy.processId;
    	this.size=copy.size;
    	this.timeInMemory=copy.timeInMemory;
    	this.counter=copy.counter;
    	this.base=copy.base;
    	this.limit=copy.limit;
    }
    
    @Override
    public int compareTo(ProcessControlBlock other) {
        return Integer.compare(this.base, other.base);
    }
}
