package test.services;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.runner.RunWith;

import com.lapsa.kkb.services.impl.DefaultKKBMerchantSignatureService;

import tech.lapsa.lapsa.arquillian.archive.ArchiveBuilderFactory;

@RunWith(Arquillian.class)
public abstract class ArquillianBaseTestCase {

    @Deployment
    public static Archive<?> createDeployment() {
	return ArchiveBuilderFactory.newEjbBuilder() //
		.withPackageOf(DefaultKKBMerchantSignatureService.class) //
		.build() //
		.dumpingTo(System.out::println)
		.asJavaArchive();
    }
}
