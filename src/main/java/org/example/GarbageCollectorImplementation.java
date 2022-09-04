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
            }}
        return parametrs;
    }

    private Set<ApplicationBean> getBeansHeap(Map<String, ApplicationBean> beans) {
        Set<ApplicationBean> listOfBeans = new HashSet<>( );
        for (Map.Entry<String, ApplicationBean> entry : beans.entrySet( )) {
            listOfBeans.addAll(getChildren(entry.getValue( )));
        }
        return listOfBeans;
    }

        private Set<ApplicationBean> getChildren(ApplicationBean bean) {
            Set<ApplicationBean> childrenBean = new HashSet<>( );
            childrenBean.add(bean);
            if (bean.getFieldValues( ).isEmpty( )) {
                return childrenBean; }
            bean.getFieldValues( )
                    .forEach(
                            (key, value) -> {
                                if (key.equals("serviceA") || key.equals("serviceB") || key.equals("serviceC")) {
                                    childrenBean.add(value);
                                    Collection<ApplicationBean> service = value.getFieldValues( ).values( );
                                    for (ApplicationBean s : service) {
                                        childrenBean.add(s);
                                        s.getFieldValues( ).values( ).forEach(x -> childrenBean.add(x));
                                    }
                                }
                                else if (key.equals("self")) {
                                    childrenBean.add(value);
                                } else {
                                    childrenBean.addAll(getChildren(value));
                                }
                            });
            return childrenBean;
        }

    private boolean checkLiveBeans(Set<ApplicationBean> beans, ApplicationBean bean) {
        int country = 0;
        for (ApplicationBean be : beans) {
            if (be == bean) {
                country++; }
        }
        return country > 0;
    }
}






