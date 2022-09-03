package org.example;


import java.util.*;


public class GarbageCollectorImplementation implements GarbageCollector {
  @Override
  public List<ApplicationBean> collect(HeapInfo heap, StackInfo stack) {
    Map<String, ApplicationBean> beans = heap.getBeans();
    Deque<StackInfo.Frame> frames = stack.getStack();
    Set<ApplicationBean> aliveBeans = getAliveBeans(frames);
    List<ApplicationBean> deadBeans = getDeadBeans(beans, aliveBeans);

    return deadBeans;
  }

  public List<ApplicationBean> getDeadBeans(Map<String, ApplicationBean> beans, Set<ApplicationBean> aliveBeans){
    List<ApplicationBean> deadBeans = new ArrayList<>();
    Set<ApplicationBean> allBeans = new HashSet<>();
    for(Map.Entry<String, ApplicationBean> beanEntry : beans.entrySet()){
      allBeans.add(beanEntry.getValue());
    }
    allBeans.removeAll(aliveBeans);
    for(ApplicationBean bean : allBeans)
      deadBeans.add(bean);
    return deadBeans;
  }

  public Set<ApplicationBean> getAliveBeans(Deque<StackInfo.Frame> frames){
    Set<ApplicationBean> aliveBeans = new HashSet<>();
    for(StackInfo.Frame frame : frames){
      for (ApplicationBean bean : frame.parameters) {
        aliveBeans.addAll(getRelation(bean, aliveBeans));
      }
    }
    return aliveBeans;
  }

  private Set<ApplicationBean> getRelation(ApplicationBean bean, Set<ApplicationBean> beanSet) {
    if (!beanSet.contains(bean)){
      beanSet.add(bean);
      for(Map.Entry<String, ApplicationBean> beanEntry : bean.getFieldValues().entrySet()){
        getRelation(beanEntry.getValue(), beanSet);
      }
    }
    return beanSet;
  }
}

