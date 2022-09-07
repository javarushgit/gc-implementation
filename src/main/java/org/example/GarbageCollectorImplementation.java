package org.example;


import java.util.*;


public class GarbageCollectorImplementation implements GarbageCollector{

@Override
public List<ApplicationBean> collect(HeapInfo heap,StackInfo stack){
	List<ApplicationBean> garbage=new ArrayList<>();
	Map<String,ApplicationBean> beans=heap.getBeans();
	Deque<StackInfo.Frame> frames=stack.getStack();
	Set<ApplicationBean> stackBeans=getParametrsFrames(frames);
	Set<ApplicationBean> heapBeans=getBeansHeap(beans);
	for(ApplicationBean heapBean: heapBeans){
		if(!checkLiveBeans(stackBeans,heapBean)){
			garbage.add(heapBean);
		}
	}
	return garbage;
}

private Set<ApplicationBean> getParametrsFrames(Deque<StackInfo.Frame> frames){
	Set<ApplicationBean> parametrs=new HashSet<>();
	Set<ApplicationBean> tmp=new HashSet<>();
	for(StackInfo.Frame frame: frames){
		for(ApplicationBean bean: frame.parameters){
			parametrs.add(bean);
			parametrs.addAll(bean.getFieldValues().values());
		}
	}
	for(ApplicationBean bean: parametrs){
		tmp.addAll(bean.getFieldValues().values());
	}
	parametrs.addAll(tmp);
	for(ApplicationBean bean: parametrs){
		tmp.addAll(getChildren(parametrs,bean));
	}
	parametrs.addAll(tmp);
	return parametrs;
}

private Set<ApplicationBean> getBeansHeap(Map<String,ApplicationBean> beans){
	Set<ApplicationBean> listOfBeans=new HashSet<>();
	Set<ApplicationBean> tmp=new HashSet<>();
	for(Map.Entry<String,ApplicationBean> entry: beans.entrySet()){
		listOfBeans.add(entry.getValue());
	}
	for(ApplicationBean bean: listOfBeans){
		tmp.addAll(bean.getFieldValues().values());
	}
	listOfBeans.addAll(tmp);
	for(ApplicationBean bean: listOfBeans){
		tmp.addAll(getChildren(listOfBeans,bean));
	}
	listOfBeans.addAll(tmp);
	return listOfBeans;
}

private Set<ApplicationBean> getChildren(Set<ApplicationBean> listOfBeans,ApplicationBean bean){
	Set<ApplicationBean> childrenBean=new HashSet<>();
	if(bean.getFieldValues().isEmpty()){
		return childrenBean;
	}else if(checkLiveBeans(listOfBeans,bean)){
		childrenBean.addAll(bean.getFieldValues().values());
		return childrenBean;
	}
	bean.getFieldValues().forEach((key,value)->{
		childrenBean.addAll(getChildren(listOfBeans,value));
	});
	return childrenBean;
}

private boolean checkLiveBeans(Set<ApplicationBean> beans,ApplicationBean bean){
	for(ApplicationBean be: beans){
		if(be==bean){
			return true;
		}
	}
	return false;
}

}






