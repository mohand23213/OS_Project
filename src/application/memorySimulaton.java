package application;
import java.util.ArrayList;

public class memorySimulaton {
    private ArrayList<ProcessControlBlock> memory;
    private ArrayList<ProcessControlBlock> jobs;
	int time=0;
    private ArrayList<Step> Steps=new ArrayList<>(); 
    private ArrayList<ProcessControlBlock> finishes = new ArrayList<>();
    //
    public memorySimulaton(ArrayList list) {
        this.memory =(ArrayList<ProcessControlBlock>) list.get(0);
        this.jobs =(ArrayList<ProcessControlBlock>)list.get(1);
    }
    void runSimulation() {
    	//set base and limit for process in memory
    	origizeBaseLimit();
    	///initial State
    	initialState();
    	///get the rest of the steps
    	while(!(memory.isEmpty() && jobs.isEmpty())) {
    		getJobsToMemory();
    		time++;
    		//as long as the job queue and memory is not empty the simulation should keep working
    		checkMemory(time);

    		checkIfHolesExceedTheLimit();
    		
    	}
    	new simulationPage(Steps);
    	
    }
    void initialState() {
		addNewStep("starting Point");
    }
    ArrayList changeArraysRefrenceAndItsContent() {
    	ArrayList x=new ArrayList<>();
    	x.add(changeArrayRefrenceAndItsContent(jobs));
    	x.add(changeArrayRefrenceAndItsContent(memory));
    	x.add(changeArrayRefrenceAndItsContent(finishes));
    	return x;
    	
    }
    ArrayList<ProcessControlBlock> changeArrayRefrenceAndItsContent(ArrayList<ProcessControlBlock> data) {
    	ArrayList<ProcessControlBlock> list=new ArrayList<ProcessControlBlock>();
    	for(ProcessControlBlock m:data) {
    		list.add(new ProcessControlBlock(m));
    	}
    	return list;
    }
    void checkMemory(int time) {
    	ArrayList<ProcessControlBlock> ids=new ArrayList<ProcessControlBlock>();
		String message="process with ids =";
    	for(int i=0;i<memory.size();i++) {
    		///increament the counter
    		memory.get(i).counter++;
    		///check if the process reach it's end
    		if(memory.get(i).counter==memory.get(i).timeInMemory) {
    			ids.add(memory.get(i));
    		}
    	}
    	///now we are going to see if the there is process finishes thier work and then we will remove it from memory
    	//and add it to finishes job
    	if(!ids.isEmpty()) {
    		//remove it from memroy
    		for(int i=0;i<ids.size();i++) {
    			memory.remove(ids.get(i));
    			finishes.add(ids.get(i));
    			message+=ids.get(i).processId+",";
    		}
    		addNewStep(message+" has finished their work!");
    	}
    }
    void origizeBaseLimit() {
    	int base=0;
    	for(int i=0;i<memory.size();i++) {
    		memory.get(i).base=base;
    		base=memory.get(i).limit=base+memory.get(i).size-1;
        	base+=1;
    	}
    }
    void getJobsToMemory() {
    	boolean isShifted=false;
    	String message="Items shiffted from job to memory queue ids=";
    	//which mean if there is a job in job queue that can fit in memory we will shift it
    	if(jobs.isEmpty())return;
    	//start from the first process if there is avaliable space if not check other
    	for(ProcessControlBlock x:jobs) {
    		setJobInAvaliablePlace(x);
    	}
    	for(ProcessControlBlock x:memory) {
    		if(jobs.contains(x)) {
    			jobs.remove(x);
    			isShifted=true;
    			message+=x.processId+",";
    			
    		}
    	}
    	//if any process goes from job to memory add new Step
    	if(isShifted) {
    		addNewStep(message);
        	
    	}
    	
    }
    void setJobInAvaliablePlace(ProcessControlBlock x) {
    	//which mean we will take a process and see if any avaliable place and we will but it
    	//check if memory is empty
    	if(memory.size()==0) {
			memory.add(x);
			x.base=0;
			x.limit=x.base+x.size-1;
			return;
    	}
    	for(int i=0;i<memory.size();i++) {
    		ProcessControlBlock process=memory.get(i);
    		if(i==0 && process.base>=x.size) {
    				memory.add(i,x);
    				x.base=0;
    				x.limit=x.base+x.size-1;
    				return;
    		}
    		if(i==memory.size()-1 && (1536-process.limit-1)>=x.size  ) {
				memory.add(x);
				x.base=process.limit+1;
				x.limit=x.base+x.size-1;
				return;
    		}
    		if(i==0)continue;
    		ProcessControlBlock prevProcess=memory.get(i-1);
    		if((process.base-prevProcess.limit-1)>=x.size) {
				memory.add(i,x);
				x.base=prevProcess.limit+1;
				x.limit=x.base+x.size-1;
				return;
    		}
    	}
    }
    void checkIfHolesExceedTheLimit() {
    	int counter=0;
    	for(int i=0;i<memory.size();i++) {
    		ProcessControlBlock process=memory.get(i);
    		//if we at point 0 then we check if it start on 0 or not
    		if(i==0 && process.base>=1) {
    				counter+=1;
    		}
    		//we will check the gap between the last element and the space 
    		if(i==memory.size()-1 && (1536-process.limit)>=1) {
				counter++;
    		}
    		if(i==0)continue;
    		ProcessControlBlock prevProcess=memory.get(i-1);
    		//we compare base with prev limit and if it exceed 0 then there is a gap and we will increase the counter
    		if((process.base-prevProcess.limit-1)>=1) {
    			counter+=1;
    		}
    	}
    	if(counter>3) {
    		//take a step before the compaction
    		addNewStep("Compaction Will happen!");
    		origizeBaseLimit();
    		//take a step after the compaction
        	addNewStep("After compaction");
    	}
    }
    void addNewStep(String message) {
    	ArrayList x=changeArraysRefrenceAndItsContent();
    	Steps.add(new Step((ArrayList<ProcessControlBlock>)x.get(0),(ArrayList<ProcessControlBlock>)x.get(1),(ArrayList<ProcessControlBlock>)x.get(2),time,message));
    }
}