package application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Step {
    public ArrayList jobs;
    public ArrayList memory;
    public ArrayList FinishedPCB;
    public int time;
    public String explanation;
    public Step(ArrayList jobs,ArrayList memory, ArrayList FinishedPCB, int time, String explanation) {
        this.jobs = jobs;
        makeMemoryReadyForUi(memory);
        this.FinishedPCB = FinishedPCB;
        this.time = time;
        this.explanation = explanation;
    }
    void makeMemoryReadyForUi(ArrayList<ProcessControlBlock> processes) {
    	Scanner scanner=new Scanner(System.in);
    	
    	
    	//asdasdas
    	if(false) {
    		memory=processes;
    	}
    	else {
    	memory=new ArrayList();
    	Space OS=new Space("OS");
    	OS.base=0;
    	OS.limit=511;
    	for(ProcessControlBlock p:processes) {
    		p.base+=512;
    		p.limit+=512;
    		memory.add(p);
    	}
    	addSpacesToMemory();
    	memory.add(0,OS);
    }
    }
    void addSpacesToMemory() {
    	ArrayList<ArrayList> copy=new ArrayList();
    	//copy.addAll(memory);

    	if(memory.isEmpty()) {
    		Space hole=new Space("Hole");
    		hole.base=512;
    		hole.limit=2048;
    		memory.add(hole);
    		return;
    	}
    	for(int i=0;i<memory.size();i++) {
    		ProcessControlBlock process=(ProcessControlBlock)memory.get(i);
    		//if we at point 0 then we check if it start on 0 or not
    		if(i==0 && process.base>512) {
        		Space space=new Space("Hole");
    			space.base=512;
    			space.limit=process.base-1;
    				copy.add(new ArrayList(Arrays.asList(process,space,0)));
    		}
    		//we will check the gap between the last element and the space 
    		if(i==memory.size()-1 && (2048-process.limit)>=1) {
        		Space space=new Space("Hole");
    			space.base=process.limit+1;
    			space.limit=2048;
				copy.add(new ArrayList(Arrays.asList(process,space,1)));
    		}
    		if(i==0)continue;
    		ProcessControlBlock prevProcess=(ProcessControlBlock)memory.get(i-1);
    		//we compare base with prev limit and if it exceed 0 then there is a gap and we will increase the counter
    		if((process.base-prevProcess.limit-1)>=1) {
        		Space space=new Space("Hole");
    			space.base=prevProcess.limit+1;
    			space.limit=process.base-1;
				copy.add(new ArrayList( Arrays.asList(process,space,0)));
    		}
    	}
    	for(ArrayList ar:copy) {
    		int index=memory.indexOf(ar.get(0));
    		if(((int)ar.get(2))==0) {
    			memory.add(index,ar.get(1));
    		}
    		else {
    			if(index==memory.size()-1) {
    				memory.add(ar.get(1));
    			}
    			else {
    				memory.add(index+1,ar.get(1));
    			}
    		}
    	}
    }
}
