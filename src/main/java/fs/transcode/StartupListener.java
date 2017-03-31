package fs.transcode;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
    An ApplicationStartedEvent is sent at the start of a run, but before any processing except the registration of listeners and initializers.
    An ApplicationEnvironmentPreparedEvent is sent when the Environment to be used in the context is known, but before the context is created.
    An ApplicationPreparedEvent is sent just before the refresh is started, but after bean definitions have been loaded.
    An ApplicationReadyEvent is sent after the refresh and any related callbacks have been processed to indicate the application is ready to service requests.
    An ApplicationFailedEvent is sent if there is an exception on startup.
 */
@Component
public class StartupListener implements ApplicationListener<ApplicationReadyEvent>{

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

    }
}
