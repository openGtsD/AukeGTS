package no.auke.drone.domain.rss;

import org.jdom.Namespace;

import com.sun.syndication.feed.CopyFrom;
import com.sun.syndication.feed.module.Module;

public interface CustomModule extends Module, CopyFrom {
    public static final String URI = "http://www.w3.org/2005/Atom";
    public final static Namespace ATOM_NS = Namespace.getNamespace("atom", URI);
    
    public String getRel();
    public void setRel(String rel);
    
    public String getHref();
    public void setHref(String href);
    
    public String getType();
    public void setType(String type);

}
