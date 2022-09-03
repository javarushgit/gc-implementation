package org.example;

import java.util.*;


public class GarbageCollectorImplementation implements GarbageCollector {
  @Override
  public List<ApplicationBean> collect(HeapInfo heap, StackInfo stack) {
    Map<String, ApplicationBean> beans = heap.getBeans();
    Deque<StackInfo.Frame> frames = stack.getStack();
    return getGarbage(getStack(frames),getHeap(beans));
  }
  public Set<ApplicationBean> getStack(Deque<StackInfo.Frame> frames){
    Set<ApplicationBean> stackSet = new HashSet<>();
    for (StackInfo.Frame frame : frames) {
      for (ApplicationBean applicationBean : frame.getParameters()) {
        stackSet = getStackChild(applicationBean, stackSet);
      }
    }
    return stackSet;
  }
  public Set<ApplicationBean> getHeap( Map<String, ApplicationBean> beans){
    Set<ApplicationBean> heapSet = new HashSet<>();
    for (Map.Entry<String, ApplicationBean> beanEntry : beans.entrySet()) {
        heapSet.add(beanEntry.getValue());
    }
    return heapSet;
  }
  public List<ApplicationBean> getGarbage(Set<ApplicationBean> stack, Set<ApplicationBean> heap) {
    List<ApplicationBean> garbage = new ArrayList<>();
    for (ApplicationBean heapBean : heap) {
      if (!stack.contains(heapBean)) {
        garbage.add(heapBean);
      }
    }
    return garbage;
  }
  public Set<ApplicationBean> getStackChild(ApplicationBean bean, Set<ApplicationBean> stackSet){
    stackSet.add(bean);
      for (ApplicationBean applicationBean : bean.getFieldValues().values()) {
        if (!stackSet.contains(applicationBean))
          stackSet.addAll(getStackChild(applicationBean, stackSet));
      }
    return stackSet;
  }
}

