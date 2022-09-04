package org.example;
import java.util.*;

public class GarbageCollectorImplementation implements GarbageCollector {

  @Override
  public List<ApplicationBean> collect(HeapInfo heap, StackInfo stack) {
    final Map<String, ApplicationBean> beans = heap.getBeans();
    final Deque<StackInfo.Frame> frames = stack.getStack();
    Set<ApplicationBean> beanSet = new HashSet<>();
    Set<ApplicationBean> aliveBeansSet = new HashSet<>();
    getBeans(beans, beanSet);
    getAliveBeans(frames, aliveBeansSet);
    return getGarbage(aliveBeansSet, beanSet);
  }

  private void getBeans(final Map<String, ApplicationBean> beans, Set<ApplicationBean> beanSet) {
    for (Map.Entry<String, ApplicationBean> beanEntry : beans.entrySet()) {
        beanSet.add(beanEntry.getValue());
    }
  }

  private void getAliveBeans(final Deque<StackInfo.Frame> frames, Set<ApplicationBean> aliveBeanSet) {
    for (StackInfo.Frame frame : frames) {
      for (ApplicationBean applicationBean : frame.getParameters()) {
        getChild(applicationBean, aliveBeanSet);
      }
    }
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

