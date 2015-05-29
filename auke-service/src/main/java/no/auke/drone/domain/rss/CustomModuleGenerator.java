package no.auke.drone.domain.rss;

import static org.apache.commons.lang.StringUtils.isBlank;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

import com.sun.syndication.feed.atom.Link;
import com.sun.syndication.feed.module.Module;
import com.sun.syndication.io.ModuleGenerator;

public class CustomModuleGenerator implements ModuleGenerator {

    private static final Namespace ATOM_NS = Namespace.getNamespace("atom", CustomModule.URI);

    @Override
    public String getNamespaceUri() {
        return CustomModule.URI;
    }

    private static final Set<Namespace> NAMESPACES;

    static {
        Set<Namespace> nss = new HashSet<Namespace>();
        nss.add(ATOM_NS);
        NAMESPACES = Collections.unmodifiableSet(nss);
    }

    @Override
    public Set<Namespace> getNamespaces() {
        return NAMESPACES;
    }

    @Override
    public void generate(Module module, Element element) {
        Element root = element;
        while (root.getParent() != null && root.getParent() instanceof Element) {
            root = (Element) element.getParent();
        }
        root.addNamespaceDeclaration(ATOM_NS);
        CustomModule m = (CustomModule) module;
        addLinks(element, m);

    }

    protected Element generateSimpleElement(String name, String value) {
        Element element = new Element(name, ATOM_NS);
        element.addContent(value);
        return element;
    }

    private void addLinks(final Element parent, CustomModule module) {
        Link link = new SyndicationLink().withRel(module.getRel()).withHref(module.getHref())
                .withType(module.getType()).getLink();
        final Element e = new Element("link", CustomModule.ATOM_NS);
        
        if (!isBlank(link.getHref()))
            e.setAttribute(new Attribute("href", link.getHref()));
        if (!isBlank(link.getRel()))
            e.setAttribute(new Attribute("rel", link.getRel()));
        if (!isBlank(link.getType()))
            e.setAttribute(new Attribute("type", link.getType()));
        if (!isBlank(link.getHreflang()))
            e.setAttribute(new Attribute("hreflang", link.getHreflang()));
        if (!isBlank(link.getTitle()))
            e.setAttribute(new Attribute("title", link.getTitle()));
        if (link.getLength() != 0)
            e.setAttribute(new Attribute("length", Long.toString(link.getLength())));
        parent.addContent(e);
    }

}
