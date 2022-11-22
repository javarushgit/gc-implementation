package org.example;


import java.util.*;


public class GarbageCollectorImplementation implements GarbageCollector {
    @Override
    public List<ApplicationBean> collect(HeapInfo heap, StackInfo stack) {
        Map<String, ApplicationBean> beans = heap.getBeans();
        Deque<StackInfo.Frame> frames = stack.getStack();

        List<ApplicationBean> stackBeans = stackInit (frames);
        Set<ApplicationBean> heapBeans = new HashSet<>();
        beans.forEach((key, value) -> heapBeans.add(value));

        for (ApplicationBean stackBean : stackBeans) {
            heapBeans.remove(stackBean);
        }
        return heapBeans.stream().toList();
    }

    private List<ApplicationBean> stackInit(Deque<StackInfo.Frame> frames) {
        List<ApplicationBean> stackBeans = new ArrayList<>();
        while (!frames.isEmpty()) {
            StackInfo.Frame frame = frames.pop();
            List<ApplicationBean> beanList = frame.getParameters();
            for (ApplicationBean bean : beanList) {
                addChildren(bean, stackBeans);
            }
        }
        return stackBeans;
    }

    private void addChildren(ApplicationBean bean, List<ApplicationBean> stack) {
        if (!stack.contains(bean)) {
            stack.add(bean);
            bean.getFieldValues()
                    .forEach((key, value) -> addChildren(value, stack));
        }
    }


}

