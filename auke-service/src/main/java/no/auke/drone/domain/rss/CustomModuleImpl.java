package no.auke.drone.domain.rss;

import com.sun.syndication.feed.module.ModuleImpl;

public class CustomModuleImpl extends ModuleImpl implements CustomModule {

    private static final long serialVersionUID = -409245416482630350L;
    private String rel;
    private String href;
    private String type;

    public CustomModuleImpl() {
        super(CustomModuleImpl.class, CustomModule.URI);
    }

    @Override
    public Class<CustomModuleImpl> getInterface() {
        return CustomModuleImpl.class;
    }

    @Override
    public void copyFrom(Object obj) {
        @SuppressWarnings("unused")
        CustomModule sm = (CustomModule) obj;
    }

    @Override
    public String getRel() {
        return this.rel;
    }

    @Override
    public void setRel(String rel) {
       this.rel = rel;
        
    }

    @Override
    public String getHref() {
        return href;
    }

    @Override
    public void setHref(String href) {
        this.href = href;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }
}
