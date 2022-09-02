package org.example;


import java.util.*;


public class GarbageCollectorImplementation implements GarbageCollector {
    @Override
    public List<ApplicationBean> collect(HeapInfo heap, StackInfo stack) {
        List<ApplicationBean> garbage = new ArrayList<>( );
        Map<String, ApplicationBean> beans = heap.getBeans( );
        Deque<StackInfo.Frame> frames = stack.getStack( );
        Set<ApplicationBean> steckBeans = getParametrsFrames(frames);
       Set<ApplicationBean> heapBeans = getBeansHeap(beans);
        for (ApplicationBean heapBean : heapBeans) {
            if (!checkLiveBeans(steckBeans, heapBean)) {
                garbage.add(heapBean);
            }
        }
        return garbage;
    }


    private Set<ApplicationBean> getParametrsFrames(Deque<StackInfo.Frame> frames) {
        Set<ApplicationBean> parametrs = new HashSet<>( );
        for (StackInfo.Frame frame : frames) {
            for (ApplicationBean bean : frame.parameters) {
                parametrs.addAll(getChildren(bean));
            }
        }
        return parametrs;
    }

    private Set<ApplicationBean> getBeansHeap(Map<String, ApplicationBean> beans) {
        Set<ApplicationBean> parametrs = new HashSet<>( );
        for (Map.Entry<String, ApplicationBean> entry : beans.entrySet( )) {
            parametrs.addAll(getChildren(entry.getValue( )));
        }        return parametrs;
    }

        private Set<ApplicationBean> getChildren(ApplicationBean bean) {
        Set<ApplicationBean> childrenBean = new HashSet<>();
          childrenBean.add(bean);
        if (bean.getFieldValues().isEmpty()){return childrenBean;}
            bean.getFieldValues()
                    .forEach(
                            (key, value) -> {
                              if (!childrenBean.contains(value)) {childrenBean.addAll(getChildren(value));}
                            });

        return childrenBean;
    }




    private boolean checkLiveBeans(Set<ApplicationBean> beans, ApplicationBean bean) {
        int country = 0;
        for (ApplicationBean be : beans) {
            if (be == bean) {
                country++;
            }
        }
        if (country > 0) {
            return true;
        }
        return false;
    }


}






