package no.auke.drone.ws;

import java.lang.reflect.Field;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumSet;
import java.util.Map;

import javax.servlet.DispatcherType;
import javax.ws.rs.Path;

import no.auke.drone.ws.support.CrossDomainFilter;
import no.auke.drone.ws.support.RestContextLoaderListener;
import no.auke.drone.ws.support.XmlRestApplicationContext;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

import com.auke.drone.ws.mapper.AccessDeniedMapper;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.bundles.AssetsBundle;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.json.Json;
import com.yammer.dropwizard.tasks.Task;
import com.yammer.metrics.core.HealthCheck;

public class AukeService extends Service<AukeUiConfiguration> {

    private final static String DATETIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";

    public static void main(String[] args) throws Exception {
        new AukeService().run(args);
    }

    private AukeService() {
        super("webui");
    }

    @Override
    protected void initialize(AukeUiConfiguration conf, Environment env) throws Exception {
        XmlRestApplicationContext appContext = new XmlRestApplicationContext();
        appContext.setConfigLocation("classpath*:/spring/*-context.xml");
        appContext.getEnvironment().setActiveProfiles(conf.getProfile());
        appContext.registerShutdownHook();

        final AssetsBundle assets = new AssetsBundle("/assets", 5);
        assets.initialize(env);

        // Filters 
        if(conf.getProfile().equalsIgnoreCase("dev")){
            env.addFilter(CrossDomainFilter.class, "/*");
        }
        // Servlet Listeners
        env.addServletListeners(new RestContextLoaderListener(appContext));

        // Mappers
        env.addProvider(new AccessDeniedMapper());

        appContext.refresh();
        Map<String, Object> resourceMap = appContext.getBeansWithAnnotation(Path.class);
        for (String beanName : resourceMap.keySet()) {
            env.addResource(resourceMap.get(beanName));
        }

        // Tasks
        Map<String, Task> taskMap = appContext.getBeansOfType(Task.class);
        for (String beanName : taskMap.keySet()) {
            env.addTask(taskMap.get(beanName));
        }

        // Health Checks
        Map<String, HealthCheck> healthcheckMap = appContext.getBeansOfType(HealthCheck.class);
        for (String beanName : healthcheckMap.keySet()) {
            env.addHealthCheck(healthcheckMap.get(beanName));
        }
    }

    @Override
    public Json getJson() {
        Json json = super.getJson();
        try {
            Field field = Json.class.getDeclaredField("mapper");
            field.setAccessible(true);
            ObjectMapper mapper = (ObjectMapper) field.get(json);

            // original SimpleDateFormat is not Thread-Safe
            DeserializationConfig config = mapper.getDeserializationConfig();
            config = config.withDateFormat(new SimpleDateFormat(DATETIME_PATTERN) {
                private static final long serialVersionUID = 1L;

                @Override
                public synchronized Date parse(String text, ParsePosition pos) {
                    return super.parse(text, pos);
                }

                @Override
                public synchronized StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition pos) {
                    return super.format(date, toAppendTo, pos);
                }
            });
            mapper.setDeserializationConfig(config);
        } catch (Exception ex) {
        }

        return json;
    }

}
