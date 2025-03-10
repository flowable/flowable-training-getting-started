package com.flowable.training.flyable.bootstrap;

import com.flowable.training.flyable.service.FlowableAdminService;
import com.flowable.training.flyable.util.FlowableUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.zip.ZipInputStream;

@Component
@ConditionalOnExpression("${flyable.deploy-demo-apps:false}")
public class FlyableDemoBootstrapper implements CommandLineRunner {

    private final FlowableAdminService adminRepositoryService;
    protected final Log logger = LogFactory.getLog(this.getClass());

    public FlyableDemoBootstrapper(FlowableAdminService adminRepositoryService) {
        this.adminRepositoryService = adminRepositoryService;
    }

    @Override
    public void run(String... args) throws Exception {
        // First check if Flowable is even running, if not, show an error message and skip
        if(!adminRepositoryService.flowableIsRunningAndHealthy()) {
            logger.error("Flowable is not running or not healthy, skipping deployment of demo apps.");
            return;
        }

        // Find all zip or bar files in the apps folder and deploy them if they don't exist yet in Work
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath:apps/*.{zip,bar}");

        for (Resource resource : resources) {
            try (InputStream is = resource.getInputStream();
                 ZipInputStream zis = new ZipInputStream(is)) {
                String appKey = FlowableUtil.getAppKeyFromAppZipInputStream(zis);
                var definitions = adminRepositoryService.getAppDefinitionsByKey(appKey);
                if(definitions.getSize() == 0) {
                    adminRepositoryService.deployApp(resource);
                } else {
                    logger.info("App with key " + appKey + " already exists, skipping deployment.");
                }
            }
        }
    }
}