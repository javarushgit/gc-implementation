package org.example;


import java.util.*;


public class GarbageCollectorImplementation implements GarbageCollector {
  @Override
  public List<ApplicationBean> collect(HeapInfo heap, StackInfo stack) {

    Set<ApplicationBean> heapApplicationBeans = new LinkedHashSet<>(heap.getBeans().values());
    Set<ApplicationBean> stackApplicationBeans = new LinkedHashSet<>();

    Deque<StackInfo.Frame> frames = stack.getStack();

    for (StackInfo.Frame frame: frames) {
      stackApplicationBeans.addAll(getFrameBeans(frame));
    }
    heapApplicationBeans.removeAll(stackApplicationBeans);
    return new ArrayList<>(heapApplicationBeans);
  }

  private Set<ApplicationBean> getFrameBeans(StackInfo.Frame frame){
    List<ApplicationBean> frameApplicationBeans = frame.getParameters();
    Set<ApplicationBean> allFrameApplicationBeans = new HashSet<>();
    Set<ApplicationBean> childrenBeans = new HashSet<>();
    for (ApplicationBean applicationBean:frameApplicationBeans) {
      allFrameApplicationBeans.addAll(getChildren(childrenBeans, applicationBean));
    }
    return allFrameApplicationBeans;
  }

  private Set<ApplicationBean> getChildren(Set<ApplicationBean> childrenBeans, ApplicationBean bean) {
    if (!childrenBeans.contains(bean)) {
        childrenBeans.add(bean);
        bean.getFieldValues()
            .forEach(
                    (key, value) -> {
                        childrenBeans.addAll(getChildren(childrenBeans, value));
                      }
                    );
    }
    return childrenBeans;
  }

}

