package no.auke.drone.utils;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class BlockingQueue<T> extends LinkedBlockingQueue<T> {

		private static final long serialVersionUID = -3206820010396658036L;
	
		private AtomicBoolean done = new AtomicBoolean();
        private Object sync=new Object();
        
        public BlockingQueue(int capacity) {  
        	super(capacity+1); 
        	done.set(false);
        }
        
        public void release() { 
        	synchronized (sync) {
        		sync.notify();	
        	} 
        }
        
        public void done() { 
        	done.set(true); 
        	synchronized (sync) {
        		sync.notify();	
        	}  
        }

        public boolean isDone() { 
        	return done.get(); 
        }

        public void resetDone() {
        	done.set(false);
        	synchronized (sync) {
        		sync.notify();	
        	}  
        }
       
        public T take() throws InterruptedException {

        	T el;
            while ((el = super.poll()) == null && !done.get()) {
                synchronized (sync) {
                	sync.wait();
                    return super.poll();
                }
            }
            return el;
        }
        
        public void put(T e) throws InterruptedException {

        	if(!done.get()) {
            	super.put(e);
            	synchronized (sync) {
            		sync.notify();	
    			}
        	}
        }
        
        public boolean offer(T e) {
        	
        	if(!done.get() && super.offer(e)) {
            	synchronized (sync) {
            		sync.notify();	
    			}
            	return true;
        	
        	} else {
        		
        		return false;
        	
        	}
        		
        };

        public boolean offer(T e, long timeout, java.util.concurrent.TimeUnit unit) throws InterruptedException {
        	
        	if(!done.get() && super.offer(e, timeout, unit)) {
        		
            	synchronized (sync) {
            		sync.notify();	
    			}

            	return true;
            	
        	} else {
        		
        		return false;
        	
        	}

        }

        
    }