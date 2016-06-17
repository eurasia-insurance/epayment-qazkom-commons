package test.com.lapsa.kkb.services.impl;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.runner.RunWith;

import com.lapsa.kkb.services.impl.DefaultKKBMerchantSignatureService;

@RunWith(Arquillian.class)
public abstract class ArquillianBaseTestCase {

    @Deployment
    public static JavaArchive createDeployment() {
	JavaArchive jar = ShrinkWrap.create(JavaArchive.class);
	jar.addPackages(true, DefaultKKBMerchantSignatureService.class.getPackage());
	System.out.println(jar.toString(true));
	return jar;
    }
}
