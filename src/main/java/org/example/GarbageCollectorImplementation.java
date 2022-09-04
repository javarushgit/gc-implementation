package org.example;
import java.util.*;

public class GarbageCollectorImplementation implements GarbageCollector {

  @Override
  public List<ApplicationBean> collect(HeapInfo heap, StackInfo stack) {
    Map<String, ApplicationBean> beans = heap.getBeans();
    Deque<StackInfo.Frame> frames = stack.getStack();
    return getGarbage(getAliveBeans(frames),getBeans(beans));
  }

  public Set<ApplicationBean> getBeans( Map<String, ApplicationBean> beans) {
    Set<ApplicationBean> beansSet = new HashSet<>();
    for (Map.Entry<String, ApplicationBean> beanEntry : beans.entrySet()) {
        beansSet.add(beanEntry.getValue());
    }
    return beansSet;
  }

  public Set<ApplicationBean> getAliveBeans(Deque<StackInfo.Frame> frames) {
    Set<ApplicationBean> aliveBeansSet = new HashSet<>();
    for (StackInfo.Frame frame : frames) {
      for (ApplicationBean applicationBean : frame.getParameters()) {
        aliveBeansSet = getChild(applicationBean, aliveBeansSet);
      }
    }
    return aliveBeansSet;
  }

  public Set<ApplicationBean> getChild(ApplicationBean bean, Set<ApplicationBean> aliveBeansSet) {
    aliveBeansSet.add(bean);
      for (ApplicationBean applicationBean : bean.getFieldValues().values()) {
        if (!aliveBeansSet.contains(applicationBean))
          aliveBeansSet.addAll(getChild(applicationBean, aliveBeansSet));
      }
    return aliveBeansSet;
  }

  public List<ApplicationBean> getGarbage(Set<ApplicationBean> aliveBeans, Set<ApplicationBean> beans) {
    beans.removeAll(aliveBeans);
    return new ArrayList<>(beans);
  }
}

