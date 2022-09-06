package org.example;




import java.util.*;


public class GarbageCollectorImplementation implements GarbageCollector {
  @Override
  public List<ApplicationBean> collect(HeapInfo heap, StackInfo stack) {
    Map<String, ApplicationBean> beans = heap.getBeans();
    Deque<StackInfo.Frame> frames = stack.getStack();
    Set<ApplicationBean> aliveBeans = getAliveBeans(frames);
    Set<ApplicationBean> aliveHeapBeans = getHeapBeans(beans);
    ArrayList<ApplicationBean> garbageList = new ArrayList<>();
    for (ApplicationBean bean : aliveHeapBeans) {
      if (!aliveBeans.contains(bean)){
        garbageList.add(bean);
      }
    }
    return garbageList;
  }

  public Set<ApplicationBean> getAliveBeans(Deque<StackInfo.Frame> frames) {
    Set<ApplicationBean> aliveBeans = new HashSet<>();
    for (StackInfo.Frame frame :
            frames) {
      for (ApplicationBean bean :
              frame.parameters) {
        aliveBeans.addAll(getRelation(bean, aliveBeans));
      }
    }
    return aliveBeans;
  }

  public Set<ApplicationBean> getHeapBeans(Map<String, ApplicationBean> beans) {
    Set<ApplicationBean> aliveBeans = new HashSet<>();
    for (Map.Entry<String, ApplicationBean> bean :
            beans.entrySet()) {
      ApplicationBean frame = bean.getValue();
        aliveBeans.addAll(getRelation(frame, aliveBeans));
    }
    return aliveBeans;
    
  }
  private Set<ApplicationBean> getRelation(ApplicationBean bean, Set<ApplicationBean> beanSet) {
    if (!beanSet.contains(bean)) {
      beanSet.add(bean);
      for(Map.Entry<String, ApplicationBean> beanEntry : bean.getFieldValues().entrySet()) {
        getRelation(beanEntry.getValue(), beanSet);
      }
    }
    return beanSet;
  }
}
