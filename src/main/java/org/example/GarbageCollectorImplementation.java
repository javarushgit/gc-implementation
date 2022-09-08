package org.example;


import java.util.*;


public class GarbageCollectorImplementation implements GarbageCollector {
  @Override
  public List<ApplicationBean> collect(HeapInfo heap, StackInfo stack) {
    Map<String, ApplicationBean> beans = heap.getBeans();
    Deque<StackInfo.Frame> frames = stack.getStack();
    List<ApplicationBean> deadBeans = new ArrayList<>(beans.values());
    Set<ApplicationBean> aliveBeans = getAliveBeans(frames);
    deadBeans.removeAll(aliveBeans);
    return deadBeans;
  }

  private Set<ApplicationBean> getAliveBeans(Deque<StackInfo.Frame> frames) {
    Set<ApplicationBean> aliveBeans = new HashSet<>();
    for (StackInfo.Frame frame : frames) {
      for (ApplicationBean bean : frame.parameters) {
        aliveBeans.addAll(getRelations(bean, aliveBeans));
      }
    }
    return aliveBeans;
  }

  private Set<ApplicationBean> getRelations(ApplicationBean bean, Set<ApplicationBean> aliveSet) {
    if (!aliveSet.contains(bean)) {
      aliveSet.add(bean);
      for(Map.Entry<String, ApplicationBean> beanEntry : bean.getFieldValues().entrySet()) {
        getRelations(beanEntry.getValue(), aliveSet);
      }
    }
    return aliveSet;
  }
}

