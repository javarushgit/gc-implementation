package org.example;


import java.util.*;


public class GarbageCollectorImplementation implements GarbageCollector {
  @Override
  public List<ApplicationBean> collect(HeapInfo heap, StackInfo stack) {
       Map<String, ApplicationBean> beans = heap.getBeans();
    Map<String, ApplicationBean> tmp=beans;
    Deque<StackInfo.Frame> frames = stack.getStack();
    List<String> links = getParametrsFrames(frames);
        for (String l: links) {
    if (beans.containsKey(l)){tmp.remove(l);}
     tmp=checkForChildLinks(tmp,l);
    }
      List<ApplicationBean> garbage=new ArrayList<>(  );
    Map<String, ApplicationBean> finalTmp = tmp;
     tmp.forEach((k, v)->garbage.add(finalTmp.get(k )));
            return garbage ;
  }


  private List<String> getParametrsFrames(Deque<StackInfo.Frame> frames){
    List<String> links =new ArrayList<>();
    while (!frames.isEmpty( )) {
      List<ApplicationBean> parametrs = frames.pollFirst().getParameters( );
      for (ApplicationBean parametr : parametrs) {
        Map<String, ApplicationBean> mark = parametr.getFieldValues();
      mark.forEach((k, v)->links.add(k));
        List<String> linksChildren = getLinksChildren(mark);
        linksChildren.forEach(x->links.add(x));
      }

    }
    return links;
  }

  private List<String> getLinksChildren(Map<String, ApplicationBean> mark) {
   Set<String> links =  mark.keySet();
    List<String> lCh=new ArrayList<>();
      for (String key: links) {
      Map<String, ApplicationBean> childs= mark.get(key).getFieldValues();
      Set<String> linksChild =  childs.keySet();
      for ( String child:linksChild) {
        if (!childs.get(child).equals(0)){ lCh.add(child); }
      }
    }
   return lCh;
  }


  private Map<String, ApplicationBean> checkForChildLinks(Map<String, ApplicationBean> tmpChild, String link){
    Set<String> links =  tmpChild.keySet();
       List<String> del=new ArrayList<>();
    for ( String l:links) {
      Map<String, ApplicationBean> childs= tmpChild.get(l).getFieldValues();
      if (childs.containsKey(link)){del.add(l);}
          }
    del.forEach(x->tmpChild.remove(x));
    return tmpChild;
  }



}


