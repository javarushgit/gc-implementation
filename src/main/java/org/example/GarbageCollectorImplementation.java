package org.example;
import java.util.*;

public class GarbageCollectorImplementation implements GarbageCollector {

  @Override
  public List<ApplicationBean> collect(HeapInfo heap, StackInfo stack) {
    final Map<String, ApplicationBean> beans = heap.getBeans();
    final Deque<StackInfo.Frame> frames = stack.getStack();
    Set<ApplicationBean> beanSet;
    Set<ApplicationBean> aliveBeanSet;
    beanSet = getBeans(beans);
    aliveBeanSet = getAliveBeans(frames);
    return getGarbage(aliveBeanSet, beanSet);
  }

  private Set<ApplicationBean> getBeans(final Map<String, ApplicationBean> beans) {
    Set<ApplicationBean> beanSet = new HashSet<>();
    for (Map.Entry<String, ApplicationBean> beanEntry : beans.entrySet()) {
        beanSet.add(beanEntry.getValue());
    }
    return beanSet;
  }

  private Set <ApplicationBean> getAliveBeans(final Deque<StackInfo.Frame> frames) {
    Set<ApplicationBean> aliveBeanSet = new HashSet<>();
    for (StackInfo.Frame frame : frames) {
      for (ApplicationBean applicationBean : frame.getParameters()) {
        getChild(applicationBean, aliveBeanSet);
      }
    }
    return aliveBeanSet;
  }

  private Set<ApplicationBean> getChild(ApplicationBean bean, Set<ApplicationBean> aliveBeansSet) {
    aliveBeansSet.add(bean);
      for (ApplicationBean applicationBean : bean.getFieldValues().values()) {
        if (!aliveBeansSet.contains(applicationBean))
          aliveBeansSet.addAll(getChild(applicationBean, aliveBeansSet));
      }
    return aliveBeansSet;
  }

  private List<ApplicationBean> getGarbage(Set<ApplicationBean> aliveBeanSet, Set<ApplicationBean> beanSet) {
    beanSet.removeAll(aliveBeanSet);
    return new ArrayList<>(beanSet);
  }
}

